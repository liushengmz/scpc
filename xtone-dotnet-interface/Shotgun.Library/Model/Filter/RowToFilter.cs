using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Model.Data;
using Shotgun.Model.List;
using Shotgun.Model.Filter;
using Shotgun.Model.Logical;
using Shotgun.Database;
using Shotgun.Library;

namespace Shotgun.Model.Filter
{
    /// <summary>
    /// 将行对像转变为查询条件，字段间将用and连接
    /// </summary>
    public class RowToFilter : IDataFilter
    {

        private DataFilterCollections _andFilter, _orFilter;

        /// <summary>
        /// 作为条件的行集合
        /// </summary>
        public BaseDataRow FilterRow { get; set; }

        /// <summary>
        /// 设置FilterRow后才能使用
        /// </summary>
        public RowToFilter() { }

        /// <summary>
        /// 可通过FilterRow修改
        /// </summary>
        /// <param name="row">行对像</param>
        public RowToFilter(BaseDataRow row)
        {
            this.FilterRow = row;
        }

        public override string ToString()
        {
            return ToString(Shotgun.Database.DefaultBaseDataSpecial.MsSql());
        }

        public string ToString(IBaseDataSpecial cfg)
        {
            StringBuilder sql = new StringBuilder();

            IUpatedataInfo _row = this.FilterRow;
            foreach (string f in _row.GetUpateFields())
            {
                object v = _row.GetValueByName(f);
                if (v == null || v == DBNull.Value)
                {
                    switch (cfg.FieldMask)
                    {
                        case EM_Safe_Field_MASK.MySQLMode:
                            sql.AppendFormat(" and isNull(`{0}`)=1", f); continue;
                        case EM_Safe_Field_MASK.MsSQLMode:
                            sql.AppendFormat(" and [{0}] is null", f); continue;
                        default:
                            throw new Exception("未知的编码方式:" + cfg.FieldMask.ToString());
                    }
                }

                if (v is int)
                    sql.AppendFormat(" and {0}={1}", cfg.FieldEncode(f), v);
                else
                    sql.AppendFormat(" and {0}='{1}'", cfg.FieldEncode(f), cfg.SqlEncode(v.ToString()));
            }

            if (_andFilter != null && _andFilter.Count != 0)
                sql.AppendFormat(" and {0}", DataFilter.FiltersToString(_andFilter, "and", cfg));

            if (_orFilter != null && _orFilter.Count != 0)
            {
                sql.AppendFormat("\n or {0}", DataFilter.FiltersToString(_orFilter, "or", cfg));
            }
            if (sql.Length > 4)
            {
                string h = sql.ToString().Substring(0, 4);

                if (h == " and" || h == "\n or")
                    sql.Remove(0, 4);
            }
            return sql.ToString();
        }

        #region IDataFilter 成员

        public string FieldName
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public object Value
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public System.Data.DbType ValueType
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public DataFilterCollections AndFilters
        {
            get
            {
                if (_andFilter == null)
                    _andFilter = new DataFilterCollections();
                return _andFilter;
            }
        }

        public DataFilterCollections OrFilters
        {
            get
            {
                if (_orFilter == null)
                    _orFilter = new DataFilterCollections();
                return _orFilter;
            }
        }

        public EM_DataFiler_Operator Operator
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public object NullToValue
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }


        #endregion
    }
}
