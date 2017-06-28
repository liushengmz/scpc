using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Library;
using System.Data.OleDb;
using System.Web;
using System.IO;

namespace Shotgun.Database
{
    /// <summary>
    /// Excel读写方式
    /// </summary>
    public enum Excel_IMEX_Mode
    {
        /// <summary>
        /// 写模式
        /// </summary>
        ExportMode = 0,
        /// <summary>
        /// 读模工
        /// </summary>
        ImportMode = 1,
        /// <summary>
        /// 读写模式
        /// </summary>
        LinkedMode = 2

    }

    public class AccessDBClass : BDClass, IBaseDataSpecial
    {

        public AccessDBClass()
            : base(false, "Access")
        { this.Init(); }
        public AccessDBClass(bool AutoClose)
            : base(AutoClose, "Access")
        { this.Init(); }

        public AccessDBClass(bool AutoClose, string cfgName)
            : base(AutoClose, cfgName)
        { this.Init(); }

        public AccessDBClass(FileInfo dbFile)
            : base(false, null)
        {
            this.dbFile = dbFile;
            this.Init();
        }

        void Init()
        {
            this.ExcelOpenMode = Excel_IMEX_Mode.ImportMode;
        }

        public FileInfo dbFile { get; private set; }

        protected override void OnDisposing()
        {
            if (_tran != null)
                _tran.Dispose();
            _tran = null;
            if (_conn != null)
                _conn.Dispose();
            _conn = null;
        }

        OleDbTransaction _tran;
        protected override System.Data.IDbTransaction Tran
        {
            get
            {
                if (this.Disposed)
                    throw new ObjectDisposedException(this.GetType().Name);
                return _tran;
            }
            set
            {
                if (this.Disposed)
                    throw new ObjectDisposedException(this.GetType().Name);
                _tran = (OleDbTransaction)value;
            }
        }

        OleDbConnection _conn;
        protected override System.Data.IDbConnection Conn
        {
            get
            {
                if (this.Disposed)
                    throw new ObjectDisposedException(this.GetType().Name);
                if (_conn == null)
                    _conn = CreateNewConnection();
                return _conn;
            }
        }

        private OleDbConnection CreateNewConnection()
        {
            var conn = new OleDbConnection();
            var csb = new OleDbConnectionStringBuilder();
            if (!string.IsNullOrEmpty(dbConfigName))
            {
                csb.ConnectionString = this.connectionStrings;
                if (csb.DataSource.StartsWith("~/"))
                    csb.DataSource = HttpContext.Current.Server.MapPath(csb.DataSource);

            }
            else
            {
                if (dbFile.Extension == ".xls")
                    csb.ConnectionString = string.Format("Provider=Microsoft.Jet.OLEDB.4.0; Extended Properties='Excel 8.0;HDR=YES;IMEX={0}'", this.ExcelOpenMode.ToString("d"));
                else if (dbFile.Extension == ".xlsx")
                    csb.ConnectionString = string.Format("Provider=Microsoft.ACE.OLEDB.12.0;Extended Properties='Excel 12.0;HDR=YES;IMEX={0}'", this.ExcelOpenMode.ToString("d"));
                else
                    csb.ConnectionString = "Provider=Microsoft.Jet.OLEDB.4.0;";
                csb.DataSource = dbFile.FullName;
            }

            conn.ConnectionString = csb.ToString();
            conn.Open();
            return conn;
        }

        protected override System.Data.IDbCommand Command()
        {
            OleDbConnection _conn = (OleDbConnection)Conn;
            var cmd = new OleDbCommand(string.Empty, _conn, _tran);
            return cmd;
        }

        protected override void TableFill(string sql, System.Data.DataTable table)
        {
            OleDbConnection _conn = (OleDbConnection)Conn;
            var cmd = (OleDbCommand)Command();
            cmd.CommandText = sql;

            var da = new OleDbDataAdapter(cmd);


            TimerStart();
            try
            {
                da.Fill(table);
            }
            catch (System.Data.Common.DbException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                da.Dispose();
                TimerEnd(sql);
            }
        }

        protected override bool SaveData(IUpatedataInfo DataRow)
        {
            throw new NotImplementedException();
        }

        protected override bool DeleteData(IUpatedataInfo DataRow)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// Excel连接串的IMEX参数,默认:读模式(仅Execl有效)
        /// </summary>
        public Excel_IMEX_Mode ExcelOpenMode { get; set; }

        #region IBaseDataClass4 成员

        Shotgun.Model.List.EM_PAGE_Mode IBaseDataSpecial.PageMode
        {
            get { return Shotgun.Model.List.EM_PAGE_Mode.ByTop; }
        }

        Shotgun.Model.Filter.EM_Safe_Field_MASK IBaseDataSpecial.FieldMask
        {
            get { return Shotgun.Model.Filter.EM_Safe_Field_MASK.MsSQLMode; }
        }

        string IBaseDataSpecial.FieldEncode(string vName)
        {
            return "[" + vName + "]";
        }
        string IBaseDataSpecial.funcNullToValue(string field, object defaultValue)
        {
            if (defaultValue == null)
                return "[" + field + "]";
            bool isDigi = defaultValue is int || defaultValue is long || defaultValue is short || defaultValue is byte;
            if (isDigi)
                return string.Format("isNull([{0}],{1})", field, defaultValue);
            return string.Format("isNull([{0}],'{1}')", field, defaultValue);
        }
        #endregion

        public override string FieldEncode(string filed)
        {
            if (filed.Contains(' '))
                return filed;
            return string.Format("[{0}]", filed);
        }

        protected override string GetPageTSql(Shotgun.Model.List.IDBSQLHelper list)
        {
            string sql = string.Empty;
            string sqlWhere;
            int start = 0, SelCount = 0;
            //正序排法
            string sqlOrder = string.Empty;
            //反序排法
            string sqlOrderRev = string.Empty;
            sqlWhere = list.GetWhere();

            if (list.CurrentPage <= 1)
            {
                if (list.PageSize == int.MaxValue)
                {
                    sql = string.Format("select {0} from [{1}] {2} {3}",
                       list.GetFieldsString(), list.table, sqlWhere, list.GetOrderBy(false));
                }
                else
                {
                    sql = string.Format("select top {4} {0} from [{1}] {2} {3}",
                       list.GetFieldsString(), list.table, sqlWhere, list.GetOrderBy(false), list.PageSize);

                }
                return sql;
            }

            if (list.SortKey.Count == 0)
                throw new ArgumentException("分页调取数据时，需要添加排序字段");

            start = (list.CurrentPage - 1) * list.PageSize;

            SelCount = list.TotalCount - start;
            if (SelCount < 0)
            {
                sql = string.Format("select top 0 {0} from {1} ", list.GetFieldsString(), list.table);
            }
            if (SelCount > list.PageSize)
                SelCount = list.PageSize;
            sqlWhere = list.GetWhere();
            sqlOrder = list.GetOrderBy(false);
            sqlOrderRev = list.GetOrderBy(true);



            if (string.IsNullOrEmpty(sql))
            {

                sql = "select " + list.GetFieldsString() + " from [" + list.table + "] where [" + list.IdentityField + "] in (\n" +
                    "select  top " + SelCount.ToString() + " " + list.IdentityField + "  from (\n" +
                    "select top " + (start + SelCount).ToString() + " " + list.GetOrderField() +
                    " from   [" + list.table + "] \n" + sqlWhere + "\n " + sqlOrder + " ) t \n" +
                    sqlOrderRev + ") \n" + sqlOrder;

            }
            return sql;
        }

    }
}
