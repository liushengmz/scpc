using n8wan.Public.Logical;
using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace LightDataModel
{
    partial class tbl_troneItem
    {
        static StaticCache<tbl_troneItem, int> cache = new StaticCache<tbl_troneItem, int>() { Expired = new TimeSpan(0, 15, 0) };

        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_troneItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
        {
            tbl_troneItem m = null;
            lock (cache.SyncRoot)
            {
                m = cache.GetDataByIdx(id);
                if (m != null)
                    return m;
            }

            var q = GetQueries(dBase);
            m = q.GetRowById(id);
            cache.InsertItem(m);
            return m;
        }

        /// <summary>
        /// 根据 sp_api_url_id 和端口号查找所有通道 (缓存15分钟)
        /// </summary>
        /// <param name="apiId"></param>
        /// <param name="port"></param>
        /// <returns></returns>
        public static IEnumerable<tbl_troneItem> QueryTronesByPort(Shotgun.Database.IBaseDataClass2 dBase, int apiId, string port)
        {
            IEnumerable<tbl_troneItem> cmds;
            var tTrones = cache.GetCacheData(true);
            if (tTrones != null)
            {
                lock (cache.SyncRoot)
                {
                    cmds = from t in tTrones where t.sp_api_url_id == apiId && t.trone_num == port && t.status == 1 select t;
                    if (cmds != null)
                        return cmds.ToArray();
                    return null;
                }
            }
            var csl = GetQueries(dBase);
            csl.Filter.AndFilters.Add(Fields.sp_api_url_id, apiId);
            csl.Filter.AndFilters.Add(Fields.trone_num, port);
            csl.Filter.AndFilters.Add(Fields.status, 1);

            //csl.Fields = new string[] { Fields.id, Fields.trone_num, Fields.orders, Fields.is_dynamic, Fields.price, Fields.match_price, Fields.sp_trone_id };

            csl.PageSize = int.MaxValue;

            cmds = csl.GetDataList();
            if (cmds != null)
            {
                foreach (var cmd in cmds)
                {
                    cache.InsertItem(cmd);
                }
            }
            return cmds;
        }


        /// <summary>
        /// 一些特殊的ID的含义描述
        /// </summary>
        /// <param name="errTroneId"></param>
        /// <returns></returns>
        public static string GetTroneErrMsg(int errTroneId)
        {
            switch (errTroneId)
            {
                case -1: return "没有匹配端口";
                case -2: return "没有匹配指令";
                case -3: return "发现同步状态";
                case -6: return "指令可匹配多个通首";
                case 0: return "未指定";
            }
            if (errTroneId > 0)
                return "匹配成功";
            return string.Format("未知状态({0})", errTroneId.ToString());
        }

        /// <summary>
        /// 根据sp业务id 查找sp通道 (缓存)
        /// </summary>
        /// <param name="dBase">缓存未建议立时，使用数据库进行查询</param>
        /// <param name="spTroneId"></param>
        /// <returns></returns>
        public static IEnumerable<tbl_troneItem> GetTroneIdsBySptroneId(Shotgun.Database.IBaseDataClass2 dBase, int spTroneId)
        {
            IEnumerable<tbl_troneItem> cmds;
            var tTrones = cache.GetCacheData(true);
            if (tTrones != null)
            {
                lock (cache.SyncRoot)
                {
                    cmds = from t in tTrones where t.sp_trone_id == spTroneId select t;
                    if (cmds != null)
                        return cmds.ToArray();
                    return null;
                }
            }
            var csl = GetQueries(dBase);
            csl.Filter.AndFilters.Add(Fields.sp_trone_id, spTroneId);
            csl.PageSize = int.MaxValue;

            cmds = csl.GetDataList();
            if (cmds != null)
            {
                foreach (var cmd in cmds)
                {
                    cache.InsertItem(cmd);
                }
            }
            return cmds;
        }




    }
}
