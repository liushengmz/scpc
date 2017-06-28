using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_cp_poolItem
    {
        static StaticCache<tbl_cp_poolItem, int> _cache = new StaticCache<tbl_cp_poolItem, int>() { IsManualLoad = true, Expired = new TimeSpan(2, 0, 0, 0) };

        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_cp_poolItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
        {
            var m = _cache.GetDataByIdx(id);
            if (m != null)
                return m;

            var q = GetQueries(dBase);
            q.Filter.AndFilters.Add(identifyField, id);
            m = q.GetRowByFilters();
            if (m != null)
                _cache.InsertItem(m);
            return m;
        }

    }
}
