using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;
using Shotgun.Library;

namespace Shotgun.Database
{
    public class MsSqlDBClass : BDClass, IBaseDataSpecial
    {

        public MsSqlDBClass() : base(false, "MsSql") { }
        public MsSqlDBClass(bool AutoClose) : base(AutoClose, "MsSql") { }

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


        #endregion


        SqlTransaction _tran;
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
                _tran = (SqlTransaction)value;
            }
        }

        SqlConnection _conn;
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

        private SqlConnection CreateNewConnection()
        {
            SqlConnection con = new SqlConnection();
            con.ConnectionString = this.connectionStrings;
            con.Open();
            return con;
        }


        protected override void OnDisposing()
        {
            if (_tran != null)
                _tran.Dispose();
            _tran = null;
            if (_conn != null)
                _conn.Dispose();
            _conn = null;

        }



        protected override System.Data.IDbCommand Command()
        {
            var _c = Conn;
            return new SqlCommand(string.Empty, (SqlConnection)_c, _tran);
        }

        protected override bool SaveData(IUpatedataInfo DataRow)
        {
            string table = DataRow.TableName;
            if (string.IsNullOrEmpty(table))
                return false;
            string idStr = DataRow.IdentifyField;
            if (string.IsNullOrEmpty(idStr))
                return false;
            List<string> fields = DataRow.GetUpateFields();
            if (fields.Count == 0)
                return false;

            using (var cmd = Command())
            {

                bool isNew;
                object t = DataRow.GetValueByName(idStr);
                isNew = t == null || t == DBNull.Value || (int)t <= 0;

                SqlParameter ptr;
                string sql1 = string.Empty, sql2 = string.Empty;

                foreach (string f in fields)
                {
                    if (f == idStr)
                        continue;
                    var val = DataRow.GetValueByName(f);
                    if (val == null)
                        val = DBNull.Value;
                    ptr = new SqlParameter("@" + f, val);
                    cmd.Parameters.Add(ptr);
                    if (isNew)
                    {
                        sql1 += ",[" + f + "]";
                        sql2 += ",@" + f;
                    }
                    else
                    {
                        sql1 += ",[" + f + "]=@" + f;
                    }

                }
                if (!string.IsNullOrEmpty(sql1))
                    sql1 = sql1.Substring(1);
                string sql;
                if (isNew)
                {
                    sql = "insert into [" + table + "](" + sql1 + ") values(" + sql2.Substring(1) + ")";
                }
                else
                {
                    sql = "update [" + table + "] set " + sql1 + " where [" + idStr + "]=@" + idStr;
                    ptr = new SqlParameter("@" + idStr, DataRow.GetValueByName(idStr));
                    cmd.Parameters.Add(ptr);
                }

                cmd.CommandText = sql;
                TimerStart();
                try
                {
                    cmd.ExecuteNonQuery();
                }
                catch (System.Data.SqlClient.SqlException ex)
                {
                    Shotgun.Library.ErrLogRecorder.SqlError(ex, sql, cmd.Parameters);
                    cmd.Dispose();
                    throw;
                }
                finally
                {
                    TimerEnd(sql);
                }

                if (isNew)
                {
                    cmd.CommandText = "select @@identity";
                    t = cmd.ExecuteScalar();
                }
                else
                    t = null;
                cmd.Dispose();

                DataRow.SetUpdated(t);
            }
            return true;
        }

        protected override bool DeleteData(IUpatedataInfo DataRow)
        {
            throw new NotImplementedException();
        }

        protected override void TableFill(string sql, System.Data.DataTable table)
        {
            var cmd = (SqlCommand)this.Command();
            cmd.CommandText = sql;
            SqlDataAdapter da = new SqlDataAdapter(cmd);

            TimerStart();
            try
            {
                da.Fill(table);
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                TimerEnd(sql);
                da.Dispose();
            }
        }

        public override string FieldEncode(string filed)
        {
            if (filed.Contains(' '))
                return filed;
            return string.Format("[{0}]", filed);
        }
    }
}
