using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_sp_troneItem
    {
        private int[] _pro;

        int closeTime_start = -1, closeTime_end = -1;

        static StaticCache<tbl_sp_troneItem, int> _cache = new StaticCache<tbl_sp_troneItem, int>() { Expired = new TimeSpan(1, 0, 0) };

        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_sp_troneItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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

        public static ICollection<tbl_sp_troneItem> GetRowsById(Shotgun.Database.IBaseDataClass2 dBase, IEnumerable<int> ids)
        {
            if (ids.Count() == 1)
            {
                var m = GetRowById(dBase, ids.First());
                if (m == null)
                    return null;
                return new tbl_sp_troneItem[] { m };
            }

            var data = _cache.GetCacheData(true);
            if (data != null)
            {
                lock (_cache.SyncRoot)
                {
                    return (from item in data where ids.Contains(item.id) select item).ToArray();
                }
            }

            var q = LightDataModel.tbl_sp_troneItem.GetQueries(dBase);
            q.Filter.AndFilters.Add(tbl_sp_troneItem.Fields.PrimaryKey, ids);
            q.PageSize = int.MaxValue;
            var rlt = q.GetDataList();
            foreach (var item in rlt)
            {
                _cache.InsertItem(item);
            }
            return rlt;
        }

        /// <summary>
        /// 根据provinces 转换成int数据组，结果始终不为null
        /// </summary>
        public int[] ProvinceId
        {
            get
            {
                if (_pro != null)
                    return _pro;
                if (string.IsNullOrEmpty(_provinces))
                    return _pro = new int[] { };
                var data = provinces.Split(new char[] { ',', ' ' }, StringSplitOptions.RemoveEmptyEntries);
                var pids = new List<int>();
                int i;
                foreach (var id in data)
                {
                    if (!int.TryParse(id, out i))
                        continue;
                    pids.Add(i);
                }
                return _pro = pids.ToArray();
            }
        }

        /// <summary>
        /// 通过属性shield_start_hour，shield_end_hour 判定是否为上量时间
        /// 如果其中有一个时间无效，则认为24小时放量（总是返回true）
        /// </summary>
        /// <param name="todaySeconds">当天已经过的秒数</param>
        /// <returns></returns>
        public bool IsOpenTime(int todaySeconds)
        {
            if (closeTime_start == -1)
            {
                DateTime time;
                if (!DateTime.TryParse(this.shield_start_hour, out time))
                {
                    closeTime_start = 0;
                    closeTime_end = 0;
                    return true;
                }
                closeTime_start = (time.Hour * 60 + time.Minute) * 60 + time.Second;
                if (!DateTime.TryParse(this.shield_end_hour, out time))
                {
                    closeTime_start = 0;
                    closeTime_end = 0;
                    return true;
                }
                closeTime_end = (time.Hour * 60 + time.Minute) * 60 + time.Second;
            }

            if (closeTime_end == closeTime_start)
                return true;//时间相等，就不限了

            if (closeTime_start > closeTime_end)//跨天限制，如 22：00 - 7：00
                return todaySeconds < closeTime_start && todaySeconds > closeTime_end;
            else//未跨天 如 00：00 -7：00
                return todaySeconds < closeTime_start || todaySeconds > closeTime_end;
        }

        public string GetCustomId(string mobile, string imsi)
        {
            return GetCustomId(this.up_data_type, mobile, imsi);
        }

        public static string GetCustomId(int up_data_type, string mobile, string imsi)
        {
            return up_data_type == 2 || up_data_type == 3 ? mobile : imsi;
        }

    }
}
