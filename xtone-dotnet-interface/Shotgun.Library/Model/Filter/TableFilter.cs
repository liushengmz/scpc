using System;
using System.Collections.Generic;
using System.Text;
using Shotgun.Database;

namespace Shotgun.Model.Filter
{
    /// <summary>
    /// 表过滤器，将后成 [FieldName] in (select top [MaxRecord] [IdentityField] from [TableName])查询条件
    /// </summary>
    public class TableFilter : DataFilter
    {
        private int _max = 0;
        string _tableName;
        string _idField;
        EmptyFilter _tableFilters;
        Dictionary<string, EM_SortKeyWord> _sort;


        public TableFilter()
        {
            this.Operator = EM_DataFiler_Operator.In;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="field">与子表关连的字段</param>
        /// <param name="tableName">子表表名</param>
        /// <param name="tableField">与主表关连的字段</param>
        public TableFilter(string field, string tableName, string tableField)
        {
            this.FieldName = field;
            this.TableName = tableName;
            this.IdentityField = tableField;
            this.Operator = EM_DataFiler_Operator.In;
        }

        public int MaxRecord
        {
            get { return _max; }
            set
            {
                _max = value;
                if (value == 1)
                    this.Operator = EM_DataFiler_Operator.Equal;
                else
                    this.Operator = EM_DataFiler_Operator.In;
            }
        }

        public string TableName { get { return _tableName; } set { _tableName = value; } }

        public string IdentityField { get { return _idField; } set { _idField = value; } }

        public EmptyFilter TableFilters
        {
            get
            {
                if (_tableFilters == null)
                    _tableFilters = new EmptyFilter();
                return _tableFilters;
            }
        }

        /// <summary>
        /// 过滤表的排序(指定MaxRecord时有效)
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


        public override EM_DataFiler_Operator Operator
        {
            set
            {
                if (value != EM_DataFiler_Operator.In &&
                    value != EM_DataFiler_Operator.Not_In &&
                    value != EM_DataFiler_Operator.Equal)
                    throw new Exception("仅支持 Equal,In,Not_In操作符!");
                base.Operator = value;
                if (value == EM_DataFiler_Operator.Equal)
                    this._max = 1;
            }
        }
        public override string ToString(IBaseDataSpecial cfg)
        {
            string sql;
            if (_max > 0 && cfg.PageMode == List.EM_PAGE_Mode.ByTop)
                sql = string.Format("select top {2} {0} from {1}",
                        cfg.FieldEncode(_idField), cfg.FieldEncode(_tableName), _max);
            else
                sql = string.Format("select {0} from {1}",
                     cfg.FieldEncode(_idField), cfg.FieldEncode(_tableName));


            if (_tableFilters != null && _tableFilters.HasFilter)
            {
                sql += " where " + _tableFilters.ToString(cfg);
            }
            if (_max > 0 && _sort != null && _sort.Count > 0)
            {
                sql += " order by " + getOrderBy(cfg);
            }
            string ret;
            if (Operator == EM_DataFiler_Operator.Not_In)
            {
                ret = "{0} not in ({1})";
            }
            else if (Operator == EM_DataFiler_Operator.Equal)
            {
                ret = "{0} = ({1})";
            }
            else
                ret = "{0} in ({1})";

            ret = string.Format(ret, cfg.FieldEncode(this.FieldName), sql);

            if (this.AndFilters.Count != 0)
                ret += "\n and " + FiltersToString(this.AndFilters, "and", cfg);
            if (this.OrFilters.Count != 0)
                ret = "(" + ret + "\n or " + FiltersToString(this.OrFilters, "or", cfg) + ")";

            return ret;
        }
        public override string ToString()
        {
            return ToString(DefaultBaseDataSpecial.MsSql());
        }


        private string getOrderBy(IBaseDataSpecial cfg)
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
                if (s == EM_SortKeyWord.asc)
                {
                    ret += string.Format(",{0} desc", cfg.FieldEncode(key));
                }
                else
                    ret += string.Format(",{0} asc", cfg.FieldEncode(key));
            }
            return ret.Substring(1);
        }
    }
}
