using System;
using System.Collections.Generic;
using System.Text;
using System.Data;
using Shotgun.Database;

namespace Shotgun.Model.Filter
{
    public class DataFilter : IDataFilter
    {
        private EM_DataFiler_Operator _operator = EM_DataFiler_Operator.Equal;
        private DbType _valueType = DbType.String;
        private DataFilterCollections _andFilter, _orFilter;
        private string _field;
        private object _value;

        public DataFilter() { }

        public DataFilter(string Field, object value)
        {
            this.FieldName = Field;
            this.Value = value;
        }
        public DataFilter(string Field, object value, EM_DataFiler_Operator Operator)
        {
            this.FieldName = Field;
            this.Value = value;
            this.Operator = Operator;
        }


        #region IDataFilter 成员

        public string FieldName
        {
            get
            {
                return _field;
            }
            set
            {
                _field = value;
            }
        }

        public object Value
        {
            get
            {
                return _value;
            }
            set
            {
                if ((value is IEnumerable<int> || value is IEnumerable<string>) && this.Operator != EM_DataFiler_Operator.Not_In)
                    this.Operator = EM_DataFiler_Operator.In;
                _value = value;
            }
        }

        public DbType ValueType
        {
            get
            {
                return _valueType;
            }
            set
            {
                _valueType = value;
            }
        }

        /// <summary>
        /// 附加件条
        /// </summary>
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

        public virtual EM_DataFiler_Operator Operator
        {
            get
            {
                return _operator;
            }
            set
            {
                _operator = value;
            }
        }

        public object NullToValue
        {
            get;
            set;
        }


        #endregion


        public override string ToString()
        {
            return ToString(DefaultBaseDataSpecial.MsSql());
        }

        public virtual string ToString(IBaseDataSpecial cfg)
        {
            StringBuilder ret = new StringBuilder();
            string value;
            if (_value is int || _value is long || _value is short || _value is byte)
                value = _value.ToString();
            else if (_value is bool)
                value = (bool)_value ? "1" : "0";
            else if (_value is Enum)
                value = ((Enum)_value).ToString("D");

            else if (_value is DateTime)
                value = "'" + _value.ToString() + "'";
            else if (_value is IEnumerable<int>
                || _value is IEnumerable<string>)
            {
                StringBuilder t = new StringBuilder();
                int arrType = 0;
                if (_value is IEnumerable<Enum>)
                    arrType = 2;
                else if (_value is IEnumerable<int>)
                    arrType = 1;
                else if (_value is string[])
                    arrType = 3;

                foreach (object s in (System.Collections.IEnumerable)_value)
                {
                    switch (arrType)
                    {
                        case 0: t.AppendFormat(",'{0}'", s); break;//unkonw
                        case 1: t.AppendFormat(",{0}", s); break;//digi
                        case 2: t.AppendFormat(",{0}", (int)s); break;//enum
                        case 3: t.AppendFormat(",'{0}'", cfg.SqlEncode((string)s)); break;//string
                        default: throw new Exception("未知数据类别");
                    }

                }
                if (t.Length == 0)
                    value = string.Empty;
                else
                {
                    t.Remove(0, 1);
                    value = t.ToString();
                }
            }
            else
            {
                if (_value == null)
                    value = "''";
                else
                    value = "'" + cfg.SqlEncode(_value.ToString()) + "'";
            }

            string op;
            switch (_operator)
            {
                case EM_DataFiler_Operator.Equal: op = "="; break;
                case EM_DataFiler_Operator.In: op = "in"; break;
                case EM_DataFiler_Operator.Less: op = "<"; break;
                case EM_DataFiler_Operator.LessOrEqual: op = "<="; break;
                case EM_DataFiler_Operator.Like: op = "like"; break;
                case EM_DataFiler_Operator.More: op = ">"; break;
                case EM_DataFiler_Operator.MoreOrEqual: op = ">="; break;
                case EM_DataFiler_Operator.Not_In: op = "not in"; break;
                case EM_DataFiler_Operator.Not_Equal: op = "<>"; break;
                default:
                    throw new Exception("未知操作符！");
            }

            if (NullToValue != null)
            {
                if (_operator == EM_DataFiler_Operator.In || _operator == EM_DataFiler_Operator.Not_In)
                    ret.AppendFormat("{0} {1} ({2})", cfg.funcNullToValue(_field, NullToValue), op, value);
                else
                    ret.AppendFormat("{0} {1} {2}", cfg.funcNullToValue(_field, NullToValue), op, value);
            }
            else
            {
                if (_operator == EM_DataFiler_Operator.In || _operator == EM_DataFiler_Operator.Not_In)
                    ret.AppendFormat("{0} {1} ({2})", cfg.FieldEncode(_field), op, value);
                else
                    ret.AppendFormat("{0} {1} {2}", cfg.FieldEncode(_field), op, value);
            }


            if (_andFilter != null && _andFilter.Count != 0)
            {
                ret.Append("\n and ");
                ret.Append(FiltersToString(_andFilter, "and", cfg));
            }

            if (_orFilter != null && _orFilter.Count != 0)
            {

                ret.Append("\n or ");
                ret.Append(FiltersToString(_orFilter, "or", cfg));

                ret.Insert(0, "(");
                ret.Append(")");

            }

            return ret.ToString();


        }


        internal static string FiltersToString(List<IDataFilter> fs, string keyword, IBaseDataSpecial cfg)
        {
            if (fs == null || fs.Count == 0)
                return string.Empty;
            StringBuilder ret = new StringBuilder();
            foreach (IDataFilter f in fs)
            {
                ret.AppendFormat(" {0} {1}", keyword, f.ToString(cfg));
            }
            if (ret.Length == 0)
                return string.Empty;

            ret.Remove(0, keyword.Length + 1);
            return ret.ToString();
        }
    }
}
