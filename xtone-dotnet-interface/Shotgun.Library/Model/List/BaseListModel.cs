using System;
using System.Collections.Generic;
using System.Text;
using Shotgun.Model.Filter;
using System.Data;
using Shotgun.Database;
using Shotgun.Model.Data;

namespace Shotgun.Model.List
{
#if !DEBUG
    [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
#endif
    public abstract class BaseListModel<T> : IListModel<T> where T : DataTable, new()
    {

        int _pageSize = 10, _cPage;
        string[] _fields;
        protected Dictionary<string, EM_SortKeyWord> _sort;
        protected EmptyFilter _filter;
        Database.IBaseDataClass2 _dBase;
        string _lastSql;

        IBaseDataSpecial dbCfg;


        /// <summary>
        /// 数据支持
        /// </summary>
        public Database.IBaseDataClass2 dBase
        {
            get { return _dBase; }
            set
            {
                _dBase = value;
                if (value is IBaseDataSpecial)
                    dbCfg = (IBaseDataSpecial)value;
                else
                    dbCfg = DefaultBaseDataSpecial.MsSql();
            }
        }

        #region IListModel 成员
        /// <summary>
        /// 建议使用Filter属性
        /// </summary>

        /// <summary>
        /// 返回第一个Filter.无Filter集合时，自动添加EmptyFilter，并返回该对象
        /// </summary>
        public EmptyFilter Filter
        {
            get
            {
                if (_filter == null)
                    _filter = new EmptyFilter();
                return _filter;
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

        public int PageSize
        {
            get
            {
                return _pageSize;
            }
            set
            {
                _pageSize = value;
            }
        }

        public int CurrentPage
        {
            get
            {
                return _cPage;
            }
            set
            {
                _cPage = value;
            }
        }

        public Dictionary<string, EM_SortKeyWord> SortKey
        {
            get
            {
                if (_sort == null)
                    _sort = new Dictionary<string, EM_SortKeyWord>();
                return _sort;

            }
        }

        public virtual T GetDataList()
        {
            if (dBase == null)
                throw new NullReferenceException("数据库对像不能空");

            string sql = string.Empty;
            string sqlWhere;
            int start = 0, SelCount = 0;
            //正序排法
            string sqlOrder = string.Empty;
            //反序排法
            string sqlOrderRev = string.Empty;
            sqlWhere = getWhereString();

            string safeTableName = dbCfg.FieldEncode(this.TableName);


            if (CurrentPage <= 1)
            {//调首页
                if (this.PageSize == int.MaxValue)
                {
                    sql = string.Format("select {0} from {1} {2} {3}",
                       getFieldsString(), safeTableName, sqlWhere, getOrderBy(false));

                }
                else
                {
                    switch (dbCfg.PageMode)
                    {
                        case EM_PAGE_Mode.ByLimit:
                            sql = string.Format("select {0} from {1} {2} {3} limit {4}",
                                 getFieldsString(), safeTableName, sqlWhere, getOrderBy(false), this.PageSize);
                            break;
                        case EM_PAGE_Mode.ByTop:
                            sql = string.Format("select top {4} {0} from {1} {2} {3}",
                                getFieldsString(), safeTableName, sqlWhere, getOrderBy(false), this.PageSize);
                            break;
                        default:
                            throw new Exception("未知的分页方式:" + dbCfg.PageMode.ToString());
                    }
                }

            }
            else
            {//分页调取(初始化）
                start = (CurrentPage - 1) * PageSize;
                SelCount = TotalCount - start;
                if (SelCount <= 0)
                {
                    sql = string.Format("select {0} from {1} where 1=0 ", getFieldsString(), safeTableName);
                }
                if (SelCount > PageSize)
                    SelCount = PageSize;
                sqlWhere = this.getWhereString();
                sqlOrder = this.getOrderBy(false);
                sqlOrderRev = this.getOrderBy(true);
            }


            if (string.IsNullOrEmpty(sql))
            {//分面调取（实现）
                switch (dbCfg.PageMode)
                {
                    case EM_PAGE_Mode.ByLimit:
                        sql = string.Format("select {0} from {1} {2} {3} limit {4},{5}",
                            getFieldsString(), safeTableName, sqlWhere, getOrderBy(false), start, this.PageSize);
                        break;

                    case EM_PAGE_Mode.ByTop:
                        sql = "select " + getFieldsString() + " from " + safeTableName + " where [" + this.IdentityField + "] in (\n" +
                               "select  top " + SelCount.ToString() + " " + dbCfg.FieldEncode(IdentityField) + " from (\n" +
                               "select top " + (start + SelCount).ToString() + " " + this.getOrderField() +
                               " from   " + dbCfg.FieldEncode(this.TableName) + " \n" + sqlWhere + "\n " + sqlOrder + " ) as t \n" +
                                sqlOrderRev + ") \n" + sqlOrder;
                        break;

                }
            }

            T dt = new T();
            if (dt.Columns.Count != 0 && _fields != null && _fields.Length != 0)
                ClearEmptyColume(dt);

            _lastSql = sql;
            _dBase.TableFill(sql, dt);
            return dt;

        }


        #endregion

        protected void ClearEmptyColume(DataTable dt)
        {
            if (_fields == null || _fields.Length == 0)
                return;
            for (int i = dt.Columns.Count - 1; i >= 0; i--)
            {
                string c = dt.Columns[i].ColumnName.ToLower();
                foreach (string f in _fields)
                {
                    if (f.ToLower() == c)
                    {
                        c = null;
                        break;
                    }
                }
                if (c != null)
                {
                    if (dt.PrimaryKey != null)
                    {
                        if (dt.PrimaryKey.Length > 1)
                            throw new TableException("暂不支持主键不唯一的表", dt);
                        if (dt.PrimaryKey[0] == dt.Columns[i])
                            dt.PrimaryKey = null;
                    }
                    dt.Columns.Remove(c);
                }
            }
        }

        /// <summary>
        /// 数据库表名
        /// </summary>
        protected abstract string TableName { get; }

        /// <summary>
        /// 唯一标识字段（默认的列表方法时需要用到）
        /// </summary>
        protected abstract string IdentityField { get; }

        /// <summary>
        /// 根据当前条件，返回首行首列的数据
        /// </summary>
        /// <param name="field">字段名，或SQL函数</param>
        /// <returns></returns>
        public object ExecuteScalar(string field)
        {
            if (dBase == null)
                throw new NullReferenceException("数据库对像不能空");

            return dBase.ExecuteScalar(string.Format("select {0} from {1} {2} {3}",
                    field, dbCfg.FieldEncode(this.TableName), this.getWhereString(), this.getOrderBy(false)));

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
                if (dBase == null)
                    throw new NullReferenceException("数据库对像不能空");

                string where = this.getWhereString();
                string idstring = Library.Static.StrId(where); ;
                if (!string.IsNullOrEmpty(_LastTotalString))
                {
                    if (idstring == _LastTotalString)
                        return _LastTotal;
                }
                object t = dBase.ExecuteScalar(string.Format("select Count(0) from `{0}` {1}", this.TableName, where));
                if (t is long)
                    _LastTotal = (int)(long)t;
                else
                    _LastTotal = (int)t;

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
            string ret = string.Empty;
            EM_SortKeyWord s;
            if (_sort.Keys.Count == 0)
                return string.Empty;
            foreach (string key in _sort.Keys)
            {
                s = _sort[key];
                if (s == EM_SortKeyWord.asc && isReverse ||
                   s == EM_SortKeyWord.desc & !isReverse)
                {
                    ret += string.Format(",{0} desc", dbCfg.FieldEncode(key));
                }
                else
                    ret += string.Format(",{0} asc", dbCfg.FieldEncode(key));
            }
            return "order by " + ret.Substring(1);
        }

        /// <summary>
        /// 用于排序的字段
        /// </summary>
        /// <param name="extField">附加的字段</param>
        /// <returns></returns>
        protected string getOrderField()
        {
            string ret = string.Empty;
            if (!string.IsNullOrEmpty(this.IdentityField))
                ret = string.Format(",{0}", dbCfg.FieldEncode(this.IdentityField));

            if (_sort == null || _sort.Keys.Count == 0)
            {
                if (string.IsNullOrEmpty(ret))
                    return string.Empty;
                return ret.Substring(1);
            }

            string iStr = this.IdentityField.ToLower();
            foreach (string key in _sort.Keys)
            {
                if (key.ToLower() == iStr)
                    continue;
                ret += string.Format(",{0}", dbCfg.FieldEncode(key));
            }
            return ret.Substring(1);
        }

        protected string getFieldsString()
        {
            if (_fields == null || _fields.Length == 0)
                return "*";
            string ret = string.Empty;
            foreach (string s in _fields)
            {
                if (s.Contains(" "))
                    ret += "," + s; //不能对字段名加括号处理，这将不能对字段重命名
                else
                    ret += string.Format(",{0}", dbCfg.FieldEncode(s));
            }
            return ret.Substring(1);
        }

        /// <summary>
        /// 取得查询条件
        /// </summary>
        /// <returns>含关键字where</returns>
        protected string getWhereString()
        {
            if (_filter == null || !_filter.HasFilter)
                return string.Empty;
            string ret = _filter.ToString(dbCfg);

            if (string.IsNullOrEmpty(ret))
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

        /// <summary>
        /// 根据id读取指定行。该方法不受其它参数限定
        /// 销毁对象时，执行obj.Table.Dispose()
        /// </summary>
        /// <param name="id"></param>
        /// <returns>T实例行。可根据T类型进行类型转换</returns>
        public virtual DataRow GetRowById(int id)
        {
            if (dBase == null)
                throw new NullReferenceException("数据库对像不能空");
            T dt = new T();
            if (string.IsNullOrEmpty(dt.TableName))
            {
                dt.Dispose();
                throw new Exception("不支持的DataTable类型，缺少表名!");
            }
            if (dt.PrimaryKey.Length != 1)
            {
                using (dt)
                    throw new Exception("表[" + dt.TableName + "] 主键数量不等于1");
            }
            string sql;

            sql = string.Format("select * from {0} where {1}={2}",
                    dbCfg.FieldEncode(dt.TableName), dbCfg.FieldEncode(dt.PrimaryKey[0].ColumnName), id);


            LastSqlExecute = sql;
            try
            {
                dBase.TableFill(sql, dt);
            }
            catch
            {
                dt.Dispose();
                throw;
            }
            if (dt.Rows.Count == 0)
            {
                dt.Dispose();
                return null;
            }

            return dt.Rows[0];
        }

        /// <summary>
        /// 与GetDataList()方法类似，执行 top 1 语句
        /// 销毁对象时，执行obj.Table.Dispose()
        /// </summary>
        /// <returns>T实例行。可根据T类型进行类型转换</returns>
        public virtual DataRow GetRowByFilters()
        {
            if (dBase == null)
                throw new NullReferenceException("数据库对像不能空");
            string sql = string.Empty;
            string sqlWhere;
            sqlWhere = getWhereString();

            switch (dbCfg.PageMode)
            {
                case EM_PAGE_Mode.ByTop:
                    sql = string.Format("select top 1 {0} from {1} {2} {3}",
                        getFieldsString(), dbCfg.FieldEncode(TableName), sqlWhere, getOrderBy(false), this.PageSize);
                    break;
                case EM_PAGE_Mode.ByLimit:
                    sql = string.Format("select {0} from {1} {2} {3} limit 1",
                        getFieldsString(), dbCfg.FieldEncode(TableName), sqlWhere, getOrderBy(false), this.PageSize);
                    break;
                default:
                    throw new Exception("未知分页模式:" + this.dbCfg.PageMode.ToString());
            }

            _lastSql = sql;
            T dt = new T();
            if (dt.Columns.Count != 0 && _fields != null && _fields.Length != 0)
                ClearEmptyColume(dt);
            try
            {
                _dBase.TableFill(sql, dt);
            }
            catch { dt.Dispose(); throw; }
            if (dt.Rows.Count == 0)
            {
                dt.Dispose();
                return null;
            }
            return dt.Rows[0];


        }
    }
}
