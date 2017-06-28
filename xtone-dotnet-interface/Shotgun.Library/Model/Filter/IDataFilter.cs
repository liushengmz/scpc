using System;
using System.Collections.Generic;
using System.Text;
using System.Data;
using Shotgun.Database;

namespace Shotgun.Model.Filter
{
    public enum EM_DataFiler_Operator
    {
        /// <summary>
        /// 等于
        /// </summary>
        Equal,
        /// <summary>
        /// in 查询
        /// </summary>
        In,
        /// <summary>
        /// not in 查询
        /// </summary>
        Not_In,
        /// <summary>
        /// like查询
        /// </summary>
        Like,
        /// <summary>
        /// 大于
        /// </summary>
        More,
        /// <summary>
        /// 大于等于
        /// </summary>
        MoreOrEqual,
        /// <summary>
        /// 小于
        /// </summary>
        Less,
        /// <summary>
        /// 小于等于
        /// </summary>
        LessOrEqual,
        /// <summary>
        /// 不等于
        /// </summary>
        Not_Equal,
        /// <summary>
        /// 当前节点仅作条件容器，（忽略Key/Value值）
        /// </summary>
        EmptyFilter
    }

    public interface IDataFilter
    {
        string FieldName { get; set; }
        object Value { get; set; }
        DbType ValueType{get;set;}
        /// <summary>
        /// and 连接条件集合
        /// </summary>
        DataFilterCollections AndFilters { get; }
        /// <summary>
        /// or 连接的条件集合
        /// </summary>
        DataFilterCollections OrFilters { get; }
        EM_DataFiler_Operator Operator { get; set; }
        /// <summary>
        /// Sql转换效果:IsNull(FieldName,NullToValue) 
        /// </summary>
        object NullToValue { get; set; }
        /// <summary>
        /// 转化为T-SQL条件
        /// </summary>
        /// <param name="mask"></param>
        /// <returns></returns>
        string ToString(IBaseDataSpecial cfg);
    }
}
