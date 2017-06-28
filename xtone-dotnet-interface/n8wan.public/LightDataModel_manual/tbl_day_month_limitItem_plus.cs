using n8wan.Public.Logical;
using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_day_month_limitItem
    {
        static StaticCache<tbl_day_month_limitItem, int> cache = new StaticCache<tbl_day_month_limitItem, int>() { Expired = new TimeSpan(1, 0, 0), IsManualLoad = true };
        public const string SCHEMA = "daily_log";

        public tbl_day_month_limitItem()
        {
            Schema = SCHEMA;
        }


        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_day_month_limitItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_day_month_limitItem>(tableName, Fields.PrimaryKey, dBase, SCHEMA);

        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_day_month_limitItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_day_month_limitItem>(tableName, Fields.PrimaryKey, null, SCHEMA);

        }



        /// <summary>
        /// 从缓存中获取日月限制
        /// </summary>
        /// <param name="spTroneId"></param>
        /// <param name="cpId"></param>
        /// <param name="date"></param>
        /// <returns></returns>
        public static tbl_day_month_limitItem GetOrCreateItem(IBaseDataClass2 dBase, int spTroneId, int cpId, DateTime date)
        {
            var m = cache.FindFirstData(e => e.sp_trone_id == spTroneId && e.cp_id == cpId && e.fee_date.Date == date.Date);
            if (m != null)
                return m;

            var l = GetQueries(dBase);
            l.Filter.AndFilters.Add(Fields.sp_trone_id, spTroneId);
            l.Filter.AndFilters.Add(Fields.cp_id, cpId);
            l.Filter.AndFilters.Add(Fields.fee_date, date.Date);
            m = l.GetRowByFilters();
            if (m != null)
            {
                cache.InsertItem(m);
                return m;
            }

            m = new tbl_day_month_limitItem();
            m.fee_date = date.Date;
            m.cp_id = cpId;
            m.sp_trone_id = spTroneId;

            return m;
        }



        /// <summary>
        /// 从缓存中获取日月限制
        /// </summary>
        /// <param name="spTroneId"></param>
        /// <param name="cpId"></param>
        /// <param name="date"></param>
        /// <returns></returns>
        public static tbl_day_month_limitItem GetOrCreateItem(IBaseDataClass2 dBase, int spTroneId, int cpId)
        {
            return GetOrCreateItem(dBase, spTroneId, cpId, DateTime.Today);
        }

    }
}
