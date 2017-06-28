using LightDataModel;
using NoSqlModel;
using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool.Dao
{
    public class PoolSet
    {
        static n8wan.Public.Logical.StaticCache<PoolSetModel, int> cache;
        static PoolSet()
        {
            cache = new Public.Logical.StaticCache<PoolSetModel, int>();
            cache.Expired = new TimeSpan(0, 10, 0);
            cache.IsManualLoad = true;
        }

        public static List<PoolSetModel> QueryPoolSetById(Shotgun.Database.IBaseDataClass2 dBase, int poolId)
        {
            var data = cache.GetCacheData(false);

            if (data == null)
            {
                lock (cache)
                {
                    data = cache.GetCacheData(false);
                    if (data == null)
                    {
                        if (LoadFromDbase(dBase) == 0)
                            return null;
                        data = cache.GetCacheData(false);
                    }
                }
            }
            if (poolId == 0)
                return null;
            if (data == null)
                return null;
            lock (cache.SyncRoot)
            { return data.Where(e => e.cp_pool_id == poolId).ToList(); }

        }

        private static int LoadFromDbase(Shotgun.Database.IBaseDataClass2 dBase)
        {
            var sql = "SELECT cpls.id,cpls.cp_pool_id,tn.sp_trone_id,tod.trone_id,trone_order_id,priority,"
                + " ( cpls.status=1 and stn.status=1 and tn.status=1 and tod.disable=0 and stn.is_on_api=1) status"
                + " FROM daily_config.tbl_cp_pool_set cpls "
                + "left join tbl_cp_pool cpl on cpl.id= cpls.cp_pool_id "
                + "left join tbl_trone_order tod on cpls.trone_order_id= tod.id "
                + "left join tbl_trone tn on tn.id= tod.trone_id "
                + "left join tbl_sp_trone stn on stn.id= tn.sp_trone_id "
                + "where  cpl.status=1 ";
            var cmd = dBase.Command();
            cmd.CommandText = sql;
            System.Data.IDataReader rd = null;
            int c = 0;
            try
            {
                rd = dBase.ExecuteReader(cmd);
                lock (cache.SyncRoot)
                {
                    cache.ClearCache();
                    while (rd.Read())
                    {
                        PoolSetModel pm = new PoolSetModel();
                        pm.id = rd.GetInt32(0);
                        pm.cp_pool_id = rd.GetInt32(1);
                        pm.sp_trone_id = rd.GetInt32(2);
                        pm.trone_id = rd.GetInt32(3);
                        pm.trone_order_id = rd.GetInt32(4);
                        pm.priority = rd.GetInt32(5);
                        pm.status = rd.GetInt32(6);
                        cache.InsertItem(pm);
                        c++;
                    }
                }
            }
            finally
            {
                if (rd != null)
                    rd.Dispose();
                cmd.Dispose();
            }
            return c;
        }

        public static PoolSetModel TroneOrderToPoolSetMode(IBaseDataClass2 dBase, int troneOrderId)
        {

            var data = cache.GetCacheData(false);
            PoolSetModel m = null;
            if (data == null)
            {//此处要保证正常poolset的加载
                QueryPoolSetById(dBase, 0);
            }
            else
            {
                lock (cache.SyncRoot)
                {
                    m = data.FirstOrDefault(e => e.trone_order_id == troneOrderId);
                }
            }
            if (m != null)
                return m;

            var sql = "select sp_trone_id,tod.trone_id  ,  ( stn.status=1 and tn.status=1 and tod.disable=0 and stn.is_on_api=1) status "
                 + " FROM tbl_trone_order tod "
                 + " left join tbl_trone tn on tn.id= tod.trone_id "
                 + " left join tbl_sp_trone stn on stn.id= tn.sp_trone_id "
                 + "  where tod.id= " + troneOrderId;
            var cmd = dBase.Command();
            cmd.CommandText = sql;
            IDataReader dr = null;
            m = new PoolSetModel();
            m.trone_order_id = troneOrderId;
            m.id = 100000 + troneOrderId;
            m.priority = 30;

            try
            {
                dr = dBase.ExecuteReader(cmd);
                if (!dr.Read())
                    m.status = 0;
                else
                {
                    m.sp_trone_id = dr.GetInt32(0);
                    m.trone_id = dr.GetInt32(1);
                    m.status = dr.GetInt32(2);
                }
            }
            finally
            {
                if (dr != null)
                    dr.Dispose();
                cmd.Dispose();
            }

            lock (cache)
                cache.InsertItem(m);

            return m;
        }


        public static List<PoolSetInfoModel> QueryPoolSetInfoById(Shotgun.Database.IBaseDataClass2 dBase, int poolId)
        {
            var sql = "select tno.id paycode, tno.trone_id,trn.trone_name,trn.price,trn.sp_trone_id,strn.name,cps.cp_pool_id,strn.sp_id,strn.provinces,"
                + " (strn.status=1 and trn.status=1 and tno.Disable=0 and cps.status=1 ) status ,priority,cps.id cp_pool_set_id from tbl_trone_order tno "
                + " left join tbl_trone trn on trn.id= tno.trone_id"
                + " left join tbl_sp_trone strn on strn.id= trn.sp_trone_id "
                + " left join tbl_cp_pool_set cps  on tno.id= cps.trone_order_id"
                + " where cp_pool_id=" + poolId.ToString() + " order by cp_pool_id desc ,status desc , paycode desc";

            var q = new Shotgun.Model.List.LightDataQueries<NoSqlModel.PoolSetInfoModel>("-");
            q.dBase = dBase;
            return q.GetDataListBySql(sql);

        }



        public static List<tbl_sp_troneItem> QuerySptroneByPoolid(Shotgun.Database.IBaseDataClass2 dBase, int poolId)
        {
            var sql = "select stn.* from  tbl_cp_pool_set cps "
                    + " left join tbl_trone_order tno on cps.trone_order_id = tno.id"
                    + " left join tbl_trone tn on tno.trone_id= tn.id"
                    + " left join tbl_sp_trone stn on tn.sp_trone_id =stn.id"
                    + " where cps.status=1 and stn.status=1 and tn.status=1 and tno.disable=0 and cps.cp_pool_id=" + poolId.ToString();
            var q = LightDataModel.tbl_sp_troneItem.GetQueries(dBase);
            return q.GetDataListBySql(sql);
        }

    }
}
