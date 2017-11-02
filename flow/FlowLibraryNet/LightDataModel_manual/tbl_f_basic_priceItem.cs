using Shotgun.Model.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LightDataModel
{
    partial class tbl_f_basic_priceItem
    {
        static StaticCache<tbl_f_basic_priceItem, int> _cache = new StaticCache<tbl_f_basic_priceItem, int>() { Expired = new TimeSpan(1, 0, 0) };
        public enum FlowSizeUnitEnum
        {
            /// <summary>
            /// 自动模式,小于1G使用M
            /// </summary>
            Auto,
            MB,
            GB

        }
        public static tbl_f_basic_priceItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
        {
            var m = _cache.GetDataByIdx(id);
            if (m != null)
                return m;

            var q = GetQueries(dBase);
            //q.Fields = fields;
            q.Filter.AndFilters.Add(identifyField, id);
            m = q.GetRowByFilters();
            if (m != null)
                _cache.InsertItem(m);

            return m;
        }


        public string GetSizeName(FlowSizeUnitEnum unit)
        {
            var size = this.num;
            if (size < 1000 || unit == FlowSizeUnitEnum.MB)
                return string.Format("{0}M", size);
            return string.Format("{0}G", size / 1024);
        }

    }
}
