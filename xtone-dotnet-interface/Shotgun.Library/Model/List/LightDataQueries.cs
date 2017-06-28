using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Model.Filter;

namespace Shotgun.Model.List
{

#if !DEBUG
    [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
#endif
    public class LightDataQueries<T> : IDBQueries<T>, IDBSQLHelper where T : Model.Logical.LightDataModel, new()
    {
        #region 初始化
        protected LightDataQueries() { this.PageSize = 10; }

        public LightDataQueries(string TableName) : this(TableName, "id", null) { }

        public LightDataQueries(string TableName, string IdentityField) : this(TableName, IdentityField, null) { }

        public LightDataQueries(string TableName, string IdentityField, Database.IBaseDataClass2 dBase)
        {
            this.TableName = TableName;
            this.IdentityField = IdentityField;
            this.dBase = dBase;
            this.PageSize = 10;
        }
        public LightDataQueries(string TableName, string IdentityField, Database.IBaseDataClass2 dBase, string schema)
        {
            this.TableName = TableName;
            this.IdentityField = IdentityField;
            this.dBase = dBase;
            this.PageSize = 10;
            this.Schema = schema;
        }


        #endregion


        string[] _fields;
        protected Dictionary<string, EM_SortKeyWord> _sort;
        /// <summary>
        /// 查询条件集
        /// </summary>
        EmptyFilter _rootFilter;
        string _lastSql;

        /// <summary>
        /// 数据支持
        /// </summary>
        public Database.IBaseDataClass2 dBase { get; set; }

        #region IDBQueries<T> 成员

        /// <summary>
        /// 返回第一个Filter.无Filter集合时，自动添加EmptyFilter，并返回该对象
        /// </summary>
        public EmptyFilter Filter
        {
            get
            {
                if (_rootFilter == null)
                    _rootFilter = new EmptyFilter();
                return _rootFilter;
            }
        }

        /// <summary>
        /// 要查询的字段，特殊字段需要预先处理。支持SQL字段重命名
        /// </summary>
        public string[] Fields
        {
            get
            {
                return _fields;
            }
            set
            {
                _fields = value;
            }
        }

        public int PageSize { get; set; }

        public int CurrentPage { get; set; }

        /// <summary>
        /// 查询排序
        /// 排序字段名以空格开头，将不进行安全编码
        /// </summary>
        public Dictionary<string, EM_SortKeyWord> SortKey
        {
            get
            {
                if (_sort == null)
                    _sort = new Dictionary<string, EM_SortKeyWord>();
                return _sort;

            }
        }

        public virtual List<T> GetDataList()
        {
            var sql = dBase.GetPageTSql(this);
            return GetDataListBySql(sql);
        }

        /// <summary>
        /// 通过SQL语句直接读取Model对像组
        /// </summary>
        /// <param name="sql"></param>
        /// <returns></returns>
        public virtual List<T> GetDataListBySql(string sql)
        {
            //var Response = System.Web.HttpContext.Current.Response;
            //var st = new System.Diagnostics.Stopwatch();

            //st.Start();
            var cmd = dBase.Command();
            cmd.CommandText = sql;
            _lastSql = sql;
            System.Data.IDataReader reader = null;
            try
            {
                reader = dBase.ExecuteReader(cmd);
                //Response.Write(st.ElapsedMilliseconds.ToString() + "ms -read list\n");
                //Response.Write(sql);
                //Response.Write("\n");

                //st.Reset();
                return Reader2List(reader);
            }
            finally
            {
                try
                {
                    cmd.Cancel();//可快速关闭reader
                }
                catch { }
                if (reader != null)
                    reader.Dispose();
                cmd.Dispose();
                //Response.Write(st.ElapsedMilliseconds.ToString() + "ms-end list\n");
            }
        }

        #endregion

        /// <summary>
        /// 取得指定列的数据，不支持分页
        /// </summary>
        /// <typeparam name="I">值类型</typeparam>
        /// <param name="column">列名,注意未做SQL安全处理</param>
        /// <returns></returns>
        public List<I> GetColumnList<I>(string column)
        {
            return GetColumnList<I>(column, default(I));
        }

        /// <summary>
        /// 取得指定列的数据，不支持分页
        /// </summary>
        /// <typeparam name="I">值类型</typeparam>
        /// <param name="column">列名,注意未做SQL安全处理</param>
        /// <param name="Null">数据库空值</param>
        /// <returns></returns>
        public virtual List<I> GetColumnList<I>(string column, I Null)
        {
            if (dBase == null)
                throw new ArgumentNullException("dBase未设置");
            var cmd = dBase.Command();
            cmd.CommandText = string.Format("select {0} from {1} {2} {3} ",
                        dBase.FieldEncode(column), ((IDBSQLHelper)this).safeTableName,
                        this.getWhereString(), this.getOrderBy(false));

            System.Data.IDataReader reader = null;
            var ret = new List<I>();
            try
            {
                reader = dBase.ExecuteReader(cmd);
                while (reader.Read())
                {
                    var val = reader[0];
                    if (val is DBNull)
                        ret.Add(Null);
                    else
                        ret.Add((I)val);
                }
            }
            finally
            {
                cmd.Cancel();//可快速关闭reader
                if (reader != null)
                    reader.Dispose();
                cmd.Dispose();
            }
            return ret;


        }


        private List<T> Reader2List(System.Data.IDataReader reader)
        {
            string[] fields = new string[reader.FieldCount];
            for (var i = 0; i < reader.FieldCount; i++)
            {
                fields[i] = reader.GetName(i);
            }
            var list = new List<T>();
            while (reader.Read())
            {
                var row = new T();
                row.DataFilling = true;
                for (var i = 0; i < reader.FieldCount; i++)
                {
                    row[fields[i]] = reader.GetValue(i);
                }
                row.DataFilling = false;
                list.Add(row);
            }
            return list;
        }

        private T Reader2Item(System.Data.IDataReader reader)
        {
            if (!reader.Read())
                return null;
            var row = new T();
            row.DataFilling = true;
            for (var i = 0; i < reader.FieldCount; i++)
            {
                row[reader.GetName(i)] = reader.GetValue(i);
            }
            row.DataFilling = false;
            return row;
        }


        /// <summary>
        /// 数据库表名
        /// </summary>
        protected string TableName { get; set; }

        /// <summary>
        /// 唯一标识字段（默认的列表方法时需要用到）
        /// </summary>
        protected string IdentityField { get; set; }

        /// <summary>
        /// 根据当前条件，返回首行首列的数据
        /// </summary>
        /// <param name="field">字段名，或SQL函数</param>
        /// <returns></returns>
        public object ExecuteScalar(string field)
        {
            return dBase.ExecuteScalar(string.Format("select {0} from {1}  {2} {3} ",
                   dBase.FieldEncode(field), ((IDBSQLHelper)this).safeTableName, this.getWhereString(), this.getOrderBy(false)));
        }

        private int _LastTotal;
        private string _LastTotalString;
        /// <summary>
        /// 符合当前条件的总记录数
        /// </summary>
        public virtual int TotalCount
        {
            get
            {
                string where = this.getWhereString();
                string idstring = Library.Static.StrId(where); ;
                if (!string.IsNullOrEmpty(_LastTotalString))
                {
                    if (idstring == _LastTotalString)
                        return _LastTotal;
                }
                var d = dBase.ExecuteScalar(string.Format("select Count(0) from {0} {1}", ((IDBSQLHelper)this).safeTableName, where));
                if (d is long)
                    _LastTotal = (int)(long)d;
                else
                    _LastTotal = (int)d;
                _LastTotalString = idstring;
                return _LastTotal;
            }
        }

        /// <summary>
        /// 取得sql排序条件
        /// </summary>
        /// <param name="isReverse">反序（与参数相反）</param>
        /// <returns>含关键字Order by</returns>
        protected string getOrderBy(bool isReverse)
        {
            if (_sort == null)
                return string.Empty;
            //string ret = string.Empty;
            if (_sort.Keys.Count == 0)
                return string.Empty;
            var ret = new StringBuilder("order by ");

            foreach (var item in _sort)
            {

                ret.Append(dBase.FieldEncode(item.Key));


                if (item.Value == EM_SortKeyWord.asc && isReverse ||
                   item.Value == EM_SortKeyWord.desc && !isReverse)
                {
                    ret.Append(" desc,");
                }
                else
                    ret.Append(" asc,");
            }
            ret.Length--;
            return ret.ToString();
        }

        /// <summary>
        /// 用于排序的字段
        /// </summary>
        /// <param name="extField">附加的字段</param>
        /// <returns></returns>
        protected string getOrderField()
        {
            var ret = new StringBuilder();
            if (!string.IsNullOrEmpty(this.IdentityField))
                ret.AppendFormat("{0},", dBase.FieldEncode(this.IdentityField));

            if (_sort == null || _sort.Keys.Count == 0)
            {
                ret.Length--;
                return ret.ToString();
            }
            string iStr = this.IdentityField.ToLower();
            foreach (string key in _sort.Keys)
            {
                if (key.ToLower() == iStr)
                    continue;
                ret.AppendFormat("{0},", dBase.FieldEncode(key));
            }
            ret.Length--;
            return ret.ToString();
        }

        protected string getFieldsString()
        {
            if (_fields == null || _fields.Length == 0)
                return "*";
            var sb = new StringBuilder(100);
            foreach (string s in _fields)
            {
                sb.AppendFormat("{0},", dBase.FieldEncode(s));
            }
            sb.Length--;
            return sb.ToString();
        }

        /// <summary>
        /// 取得查询条件
        /// </summary>
        /// <returns>含关键字where</returns>
        protected string getWhereString()
        {
            if (_rootFilter == null || !_rootFilter.HasFilter)
                return string.Empty;

            string ret = _rootFilter.ToString((Shotgun.Database.IBaseDataSpecial)dBase);

            if (string.IsNullOrEmpty(ret) || ret.Length < 5)
                return string.Empty;

            return " where " + ret;
        }

        /// <summary>
        /// For Debug
        /// 最后一次执行的SQL语句
        /// </summary>
        public string LastSqlExecute
        {
            get { return _lastSql; }
            protected set { _lastSql = value; }
        }

        //T ConvertTableT( DataTable table)
        //{
        //    return new T(table);
        //}

        /// <summary>
        /// 根据id读取指定行。该方法不受其它参数限定
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public virtual T GetRowById(int id)
        {
            T dt = new T();

            if (string.IsNullOrEmpty(dt.IdentifyField))
            {
                throw new Exception("表[" + dt.TableName + "] 无主键数");
            }
            string sql = string.Format("select * from {0} where {1}={2}",
                ((IDBSQLHelper)this).safeTableName, dBase.FieldEncode(dt.IdentifyField), id);
            LastSqlExecute = sql;
            var cmd = dBase.Command();
            cmd.CommandText = sql;
            System.Data.IDataReader reader = null;
            try
            {
                reader = dBase.ExecuteReader(cmd);
                return Reader2Item(reader);
            }
            finally
            {
                cmd.Cancel();//可快速关闭reader
                if (reader != null)
                    reader.Dispose();
                cmd.Dispose();
            }
        }


        /// <summary>
        /// 与GetDataList()方法类似，执行 top 1 语句
        /// </summary>
        /// <returns>T实例行。可根据T类型进行类型转换</returns>
        public virtual T GetRowByFilters()
        {
            string sql = string.Empty;
            string sqlWhere;
            sqlWhere = getWhereString();

            sql = string.Format("select {0} from {1} {2} {3}",

                getFieldsString(), ((IDBSQLHelper)this).safeTableName, sqlWhere, getOrderBy(false), this.PageSize);
            _lastSql = sql;

            //var Response = System.Web.HttpContext.Current.Response;
            //var st = new System.Diagnostics.Stopwatch();

            var cmd = dBase.Command();
            cmd.CommandText = sql;
            System.Data.IDataReader reader = null;

            try
            {
                //st.Start();

                reader = dBase.ExecuteReader(cmd);
                //Response.Write(st.ElapsedMilliseconds.ToString() + "ms -read");
                //Response.Write(sql);
                //Response.Write("\n");
                //st.Reset();
                return Reader2Item(reader);
            }
            finally
            {

                //cmd.Cancel();//可快速关闭reader
                if (reader != null)
                    reader.Dispose();
                cmd.Dispose();
                //Response.Write(st.ElapsedMilliseconds.ToString() + "ms-end\n");
            }
        }



        string IDBSQLHelper.table
        {
            get { return this.TableName; }
        }

        string IDBSQLHelper.IdentityField
        {
            get { return this.IdentityField; }
        }

        string IDBSQLHelper.schema
        {
            get { return this.Schema; }
        }

        string IDBSQLHelper.GetFieldsString()
        {
            return this.getFieldsString();
        }

        string IDBSQLHelper.GetOrderBy(bool p)
        {
            return this.getOrderBy(p);
        }

        string IDBSQLHelper.GetOrderField()
        {
            return this.getOrderField();
        }

        string IDBSQLHelper.GetWhere()
        {
            return this.getWhereString();
        }

        string IDBSQLHelper.safeTableName
        {
            get
            {
                if (string.IsNullOrEmpty(Schema))
                    return dBase.FieldEncode(TableName);

                return dBase.FieldEncode(Schema) + "." + dBase.FieldEncode(TableName);

            }
        }


        /// <summary>
        /// 需要执行跨库操时，需要设置
        /// </summary>
        public virtual string Schema { get; set; }
    }
}
