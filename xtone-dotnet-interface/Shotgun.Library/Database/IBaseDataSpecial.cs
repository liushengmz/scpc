using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Database
{
    /// <summary>
    /// 为Model提拱正确的数据库安全规范
    /// </summary>
    public interface IBaseDataSpecial
    {
        /// <summary>
        /// 使用GenericListModel分页时采用何种分页语句
        /// </summary>
        Model.List.EM_PAGE_Mode PageMode { get; }

        /// <summary>
        /// 表名，字段名安全编码模式
        /// </summary>
        Model.Filter.EM_Safe_Field_MASK FieldMask { get; }

        /// <summary>
        /// 字段名安全转换,为避免跟关键字冲突出错
        /// </summary>
        /// <param name="vName"></param>
        /// <returns></returns>
        string FieldEncode(string vName);

        /// <summary>
        /// 字符串数字安全编码
        /// </summary>
        /// <param name="vData"></param>
        /// <returns></returns>
        string SqlEncode(string vData);

        /// <summary>
        /// 空值转换函数
        /// </summary>
        /// <param name="field">字段名</param>
        /// <param name="defaultValue">为空时转化值</param>
        /// <returns></returns>
        string funcNullToValue(string field, object defaultValue);

    }
}
