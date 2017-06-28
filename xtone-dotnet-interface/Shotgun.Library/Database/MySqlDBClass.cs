using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;
using Shotgun.Library;

namespace Shotgun.Database
{
    [System.Diagnostics.DebuggerNonUserCode]
    public class MySqlDBClass : Shotgun.Database.BDClass, Shotgun.Database.IBaseDataSpecial
    {

        public MySqlDBClass()
            : base(false, "mySqlConStr")
        { }
        public MySqlDBClass(bool AutoClose)
            : base(AutoClose, "mySqlConStr")
        { }

        public MySqlDBClass(bool AutoClose, string constrName)
            : base(AutoClose, constrName)
        { }

        protected override void OnDisposing()
        {
            if (_tran != null)
                _tran.Dispose();
            _tran = null;
            if (_conn != null)
                _conn.Dispose();
            _conn = null;
        }

        MySqlTransaction _tran;
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
                _tran = (MySqlTransaction)value;
            }
        }

        MySqlConnection _conn;
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

        private MySqlConnection CreateNewConnection()
        {
            var conn = new MySqlConnection();
            conn.ConnectionString = this.connectionStrings;
            conn.Open();
            return conn;
        }

        protected override System.Data.IDbCommand Command()
        {
            MySqlConnection _conn = (MySqlConnection)Conn;
            var cmd = new MySqlCommand(string.Empty, _conn, _tran);
            return cmd;
        }

        protected override bool SaveData(Shotgun.Database.IUpatedataInfo DataRow)
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


                MySqlParameter ptr;
                string sql1 = string.Empty, sql2 = string.Empty;

                foreach (string f in fields)
                {
                    if (f == idStr)
                        continue;
                    ptr = new MySqlParameter("?" + f, DataRow.GetValueByName(f));
                    cmd.Parameters.Add(ptr);
                    if (isNew)
                    {
                        sql1 += ",`" + f + "`";
                        sql2 += ",?" + f;
                    }
                    else
                    {
                        sql1 += ",`" + f + "`=?" + f;
                    }

                }
                if (!string.IsNullOrEmpty(sql1))
                    sql1 = sql1.Substring(1);
                string sql;
                if (isNew)
                {
                    sql = "insert into `" + table + "`(" + sql1 + ") values(" + sql2.Substring(1) + ")";
                }
                else
                {
                    sql = "update `" + table + "` set " + sql1 + " where `" + idStr + "`=?" + idStr;
                    ptr = new MySqlParameter("?" + idStr, DataRow.GetValueByName(idStr));
                    cmd.Parameters.Add(ptr);
                }

                cmd.CommandText = sql;
                TimerStart();
                try
                {
                    cmd.ExecuteNonQuery();
                }
                catch (System.Data.Common.DbException ex)
                {
                    Shotgun.Library.ErrLogRecorder.SqlError(ex, sql, cmd.Parameters);
                    throw;
                }
                TimerEnd(sql);
                if (isNew)
                {
                    cmd.CommandText = "select last_insert_id() ";

                    var s = cmd.ExecuteScalar().ToString();
                    int id;
                    int.TryParse(s, out id);
                    t = id;

                }
                else
                    t = null;

                DataRow.SetUpdated(t);
            }
            return true;
        }

        protected override void TableFill(string sql, System.Data.DataTable table)
        {

            var cmd = (MySqlCommand)Command();
            cmd.CommandText = sql;

            var da = new MySqlDataAdapter(cmd);


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
                TimerEnd(sql);
                da.Dispose();
                cmd.Dispose();
            }
        }

        public override string SqlEncode(string vData, bool AddQut)
        {
            if (!string.IsNullOrEmpty(vData))
            {
                vData = vData.Replace("'", "''").Replace(@"\", @"\\");
            }
            else if (vData == null)
                vData = string.Empty;
            if (AddQut)
                return "'" + vData + "'";
            return vData;

        }

        public override string FieldEncode(string name)
        {
            if (name.Contains(' '))
                return name;
            return string.Format("`{0}`", name);
        }

        protected override bool DeleteData(IUpatedataInfo DataRow)
        {
            throw new NotImplementedException();
        }

        protected override string GetPageTSql(Model.List.IDBSQLHelper list)
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
                    sql = string.Format("select {0} from `{1}` {2} {3}",
                       list.GetFieldsString(), list.table, sqlWhere, list.GetOrderBy(false));
                }
                else
                {
                    sql = string.Format("select {0} from `{1}` {2} {3} limit {4}",
                       list.GetFieldsString(), list.table, sqlWhere, list.GetOrderBy(false), list.PageSize);

                }
                return sql;
            }

            start = (list.CurrentPage - 1) * list.PageSize;

            if (SelCount < 0)
            {
                return string.Format("select {0} from `{1}` limit 0,0 ", list.GetFieldsString(), list.table);
            }

            return string.Format("select {0} from `{1}` {2} {3} limit {4},{5}"
                        , list.GetFieldsString(), list.table, list.GetWhere(), list.GetOrderBy(false), start, list.PageSize);

        }

        public Model.List.EM_PAGE_Mode PageMode
        {
            get { return Model.List.EM_PAGE_Mode.ByLimit; }
        }

        public Model.Filter.EM_Safe_Field_MASK FieldMask
        {
            get { return Model.Filter.EM_Safe_Field_MASK.MySQLMode; }
        }

        public string funcNullToValue(string field, object defaultValue)
        {
            return string.Format("ifnull(`{0}`,{1})", defaultValue);
        }

        public override System.Data.IDataReader ExecuteReader(System.Data.IDbCommand cmd)
        {

            var rd = base.ExecuteReader(cmd);
            if (rd == null)
                rd = base.ExecuteReader(cmd);
            return rd;
        }
    }

}
