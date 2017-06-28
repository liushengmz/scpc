using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Model.Filter;

namespace Shotgun.Model.List
{
    /// <summary>
    /// 轻质量数据查询器
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public interface IDBQueries<T> where T : Logical.LightDataModel
    {
        string[] Fields { get; set; }
        int PageSize { get; set; }
        int CurrentPage { get; set; }
        int TotalCount { get; }
        EmptyFilter Filter { get; }
        Dictionary<string, Filter.EM_SortKeyWord> SortKey { get; }
        List<T> GetDataList();
    }

    public interface IDBSQLHelper
    {
        /// <summary>
        /// 指定数据库。可为空时
        /// </summary>
        string schema { get; }
        /// <summary>
        /// 指令数据表
        /// </summary>
        string table { get; }
        int TotalCount { get; }
        int PageSize { get; }
        int CurrentPage { get; }
        string IdentityField { get; }
        EmptyFilter Filter { get; }
        Dictionary<string, EM_SortKeyWord> SortKey { get; }

        string safeTableName { get; }
        

        string GetFieldsString();

        string GetOrderBy(bool p);

        string GetOrderField();

        string GetWhere();
    }

}
