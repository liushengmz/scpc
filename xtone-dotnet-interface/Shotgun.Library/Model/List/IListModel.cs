using System;
using System.Collections.Generic;
using System.Text;
using System.Data;
using Shotgun.Model.Filter;

namespace Shotgun.Model.List
{
    public interface IListModel<T> where T : DataTable
    {
        string[] Fields { get; set; }
        int PageSize { get; set; }
        int CurrentPage { get; set; }
        int TotalCount { get; }
        EmptyFilter Filter { get; }
        Dictionary<string,Filter.EM_SortKeyWord> SortKey { get; }
        T GetDataList();
    }
}
