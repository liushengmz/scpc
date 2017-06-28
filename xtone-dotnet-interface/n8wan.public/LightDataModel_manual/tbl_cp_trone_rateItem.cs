using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_cp_trone_rateItem
    {
        static StaticCache<tbl_cp_trone_rateItem, int> _cache = new StaticCache<tbl_cp_trone_rateItem, int>() { IsManualLoad = true, Expired = new TimeSpan(1, 0, 0) };



        public static tbl_cp_trone_rateItem QueryBySpTroneId(Shotgun.Database.IBaseDataClass2 dBase, int spTroneId, int cpId)
        {

            var m = _cache.FindFirstData(e => e.sp_trone_id == spTroneId && e.cp_id == cpId);
            if (m != null)
                return m;

            var q = GetQueries(dBase);
            q.Filter.AndFilters.Add(Fields.cp_id, cpId);
            q.Filter.AndFilters.Add(Fields.sp_trone_id, spTroneId);
            m = q.GetRowByFilters();
            if (m != null)
                _cache.InsertItem(m);
            else
                _cache.InsertItem(m = new tbl_cp_trone_rateItem() { cp_id = cpId, sp_trone_id = spTroneId });
            return m;

        }

        #region 静态方法

        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_cp_trone_rateItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
        {
            var m = _cache.GetDataByIdx(id);
            if (m != null)
                return m;

            var q = GetQueries(dBase);
            //q.Fields = fields;
            q.Filter.AndFilters.Add(identifyField, id);
            m = q.GetRowByFilters();
            _cache.InsertItem(m);
            return m;
        }
        #endregion
    }
}
