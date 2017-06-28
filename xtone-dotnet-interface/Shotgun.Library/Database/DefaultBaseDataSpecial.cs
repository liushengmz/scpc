using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Model.Filter;
using Shotgun.Model.List;

namespace Shotgun.Database
{
    public class DefaultBaseDataSpecial : IBaseDataSpecial
    {
        public DefaultBaseDataSpecial(EM_PAGE_Mode pageMode, EM_Safe_Field_MASK encodeMode)
        {
            this.PageMode = pageMode;
            this.FieldMask = encodeMode;
        }

        #region IBaseDataSpecial 成员

        public EM_PAGE_Mode PageMode
        {
            get;
            set;
        }

        public EM_Safe_Field_MASK FieldMask
        {
            get;
            set;
        }

        public string FieldEncode(string vName)
        {
            switch (this.FieldMask)
            {
                case EM_Safe_Field_MASK.MsSQLMode: return "[" + vName + "]";
                case EM_Safe_Field_MASK.MySQLMode: return "[" + vName + "]";
                default: throw new Exception("未知编码方法:" + this.FieldMask.ToString());
            }

        }

        public string SqlEncode(string vData)
        {
            if (vData == null)
                return "null";

            return vData.Replace("'", "''");
        }

        public string funcNullToValue(string field, object defaultValue)
        {
            if (defaultValue == null)
                return FieldEncode(field);
            bool isDigi = defaultValue is int || defaultValue is long || defaultValue is short || defaultValue is byte;
            string funName;
            switch (this.PageMode)
            {
                case EM_PAGE_Mode.ByTop:
                    funName = "isNull"; break;
                case EM_PAGE_Mode.ByLimit:
                    funName = "ifNull"; break;
                default:
                    throw new Exception("未知的空值转换方式:" + this.PageMode.ToString());

            }
            if (this.PageMode == EM_PAGE_Mode.ByTop)
                funName = "isNull";
            else if (this.PageMode == EM_PAGE_Mode.ByTop)
                if (isDigi)
                    return string.Format("{2}({0},{1})", field, defaultValue, funName);
            return string.Format("{2}([{0}],'{1}')", field, defaultValue, funName);
        }
        #endregion

        public static DefaultBaseDataSpecial MsSql()
        {
            return new DefaultBaseDataSpecial(EM_PAGE_Mode.ByTop, EM_Safe_Field_MASK.MsSQLMode);
        }

    }
}
