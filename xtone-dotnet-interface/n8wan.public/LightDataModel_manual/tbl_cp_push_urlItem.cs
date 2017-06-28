using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_cp_push_urlItem : n8wan.Public.Logical.IHold_DataItem
    {
        private static StaticCache<tbl_cp_push_urlItem, int> cache;

        static tbl_cp_push_urlItem()
        {
            cache = new StaticCache<tbl_cp_push_urlItem, int>();
            cache.Expired = new TimeSpan(0, 5, 3);
            cache.SetExpriedProc(OnCacheExpried);
        }

        public static tbl_cp_push_urlItem GetRowByIdWithCache(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            var m = cache.GetDataByIdx(id);
            if (m != null)
                return m;
            m = GetRowById(dBase, id, null);
            if (m != null)
                cache.InsertItem(m);
            return m;
        }

        private static void OnCacheExpried(IEnumerable<tbl_cp_push_urlItem> obj)
        {
            var dBase = new Shotgun.Database.DBDriver().CreateDBase();
            int c = 0;
            try
            {
                foreach (var item in obj)
                {
                    var iUpdate = item.SaveToDatabase(dBase);
                    if (iUpdate)
                    {
                        c++;
                        Shotgun.Library.SimpleLogRecord.WriteLog("Cache2Sql", string.Format("item:{0} , key:{1} updated", tableName, item.id));
                    }
                }
            }
            catch (Exception ex)
            {
                Shotgun.Library.SimpleLogRecord.WriteLog("Cache2Sql", string.Format("item:{0}Error:\n{1}", tableName, ex.ToString()));
            }
            finally
            {
                if (dBase == null)
                    dBase.Dispose();
                dBase = null;
            }
            Shotgun.Library.SimpleLogRecord.WriteLog("Cache2Sql", string.Format("item:{0} ,count:{1}", tableName, c));
        }
    }
}
