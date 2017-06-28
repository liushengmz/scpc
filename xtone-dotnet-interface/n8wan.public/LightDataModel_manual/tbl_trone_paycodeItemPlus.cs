using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_trone_paycodeItem
    {
        static StaticCache<tbl_trone_paycodeItem, int> cache = new StaticCache<tbl_trone_paycodeItem, int>() { Expired = new TimeSpan(0, 15, 0) };

        public static tbl_trone_paycodeItem QueryPayCodeByTroneId(Shotgun.Database.IBaseDataClass2 dBase, int troneId)
        {
            var data = cache.FindFirstData(e => e.trone_id == troneId);
            if (data != null)
                return data;

            var q = GetQueries(dBase);
            q.Filter.AndFilters.Add(Fields.trone_id, troneId);
            data = q.GetRowByFilters();
            if (data != null)
                cache.InsertItem(data);
            return data;
        }

    }
}
