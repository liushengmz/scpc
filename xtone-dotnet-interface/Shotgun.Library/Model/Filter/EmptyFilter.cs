using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Database;

namespace Shotgun.Model.Filter
{
    /// <summary>
    /// 空白条件过滤。用于andFilter, orFilter条件收集
    /// </summary>
    public class EmptyFilter : IDataFilter
    {
        private DataFilterCollections _andFilter, _orFilter;

        #region IDataFilter 成员

        string IDataFilter.FieldName
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

        object IDataFilter.Value
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

        System.Data.DbType IDataFilter.ValueType
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

        EM_DataFiler_Operator IDataFilter.Operator
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

        object IDataFilter.NullToValue
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
        public bool HasFilter
        {
            get
            {
                if (_andFilter != null && _andFilter.Count > 0)
                    return true;
                if (_orFilter != null && _orFilter.Count > 0)
                    return true;
                return false;
            }
        }

        /// <summary>
        /// 清除所有查询条件
        /// </summary>
        public void Clear()
        {
            _orFilter = null;
            _andFilter = null;
        }

        public override string ToString()
        {
            return ToString(DefaultBaseDataSpecial.MsSql());
        }

        public string ToString(IBaseDataSpecial cfg)
        {

            string ret = string.Empty;

            if (_andFilter != null && _andFilter.Count != 0)
                ret = DataFilter.FiltersToString(_andFilter, "and", cfg);

            if (_orFilter != null && _orFilter.Count != 0)
            {
                if (!string.IsNullOrEmpty(ret))
                    ret += "\n or ";
                ret += DataFilter.FiltersToString(_orFilter, "or", cfg);
            }

            return ret;
        }

    }
}
