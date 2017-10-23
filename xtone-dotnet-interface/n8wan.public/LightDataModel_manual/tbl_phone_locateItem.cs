using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading;

namespace LightDataModel
{
    partial class tbl_phone_locateItem
    {
        private static Dictionary<int, tbl_phone_locateItem> phones;
        private static object locker = new object();
        /// <summary>
        /// 缓存加载状态 0，未加载，1加载中，2加载完成
        /// </summary>
        private static int cacheStatus = 0;

        /// <summary>
        /// 根据手机前七位查出城市（持续缓存）
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="spNum"></param>
        /// <returns></returns>
        public static tbl_cityItem GetRowByMobile(Shotgun.Database.IBaseDataClass2 dBase, int spNum)
        {
            tbl_phone_locateItem m = null;

            if (phones != null && phones.ContainsKey(spNum))
                m = phones[spNum];
            else if (cacheStatus != 2)
            {//缓存未生成时
                if (cacheStatus == 0)
                {
                    lock (locker)
                    {//防止并发重复查询
                        if (cacheStatus == 0)
                        {
                            cacheStatus = 1;
                            ThreadPool.QueueUserWorkItem(LoadCache);
                        }
                    }
                }
                var q = GetQueries(dBase);
                q.Filter.AndFilters.Add(Fields.phone, spNum.ToString());
                m = q.GetRowByFilters();
            }
            if (m == null)
                return null;

            return tbl_cityItem.GetRowById(dBase, m.city_id);

        }

        private static void LoadCache(object s)
        {
            int minId = 0;
            if (phones == null)
                phones = new Dictionary<int, tbl_phone_locateItem>();
            else
                minId = phones.Values.Max(e => e.id);

            using (var dBase = new DBDriver().CreateDBase())
            {
                var q = GetQueries(dBase);
                q.Filter.AndFilters.Add(tbl_phone_locateItem.identifyField, minId, Shotgun.Model.Filter.EM_DataFiler_Operator.More);

                q.SortKey.Add(tbl_phone_locateItem.identifyField, Shotgun.Model.Filter.EM_SortKeyWord.asc);
                q.PageSize = 2000;

                var count = q.TotalCount;
                var pageCount = count / q.PageSize + ((count % q.PageSize) > 0 ? 1 : 0);
                bool iDone = true;
                for (int p = 1; p <= pageCount; p++)
                {
                    q.CurrentPage = p;
                    try
                    {
                        var itmes = q.GetDataList();
                        itmes.ForEach(e => phones[int.Parse(e.phone)] = e);
                    }
                    catch (System.Data.DataException)
                    {
                        iDone = false;
                        break;
                    }
                }
                cacheStatus = iDone ? 2 : 0;
            }
        }


    }
}
