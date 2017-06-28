using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Database
{

    /// <summary>
    /// 用于收集，需要更新字段信息
    /// </summary>
    public interface IUpatedataInfo
    {
        /// <summary>
        /// 返回需要更新的字段值名
        /// 需要跟数据库字段名一致
        /// </summary>
        /// <returns></returns>
        List<string> GetUpateFields();
        /// <summary>
        /// 取值
        /// </summary>
        /// <param name="filds"></param>
        /// <returns></returns>
        object GetValueByName(string filds);
        /// <summary>
        /// 返回标识字段名
        /// </summary>
        string IdentifyField { get; }
        /// <summary>
        /// 数据保存成功后，调用方法
        /// </summary>
        /// <param name="IdentifyValue">当使用Insert时，会传入标识值</param>
        void SetUpdated(object IdentifyValue);
        /// <summary>
        /// 表名
        /// </summary>
        string TableName { get; }

        /// <summary>
        /// 数据库库名
        /// </summary>
        string Schema { get; }

    }
}
