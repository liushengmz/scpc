<%@ WebHandler Language="C#" Class="PoolProvince" %>

using System;
using System.Web;
using System.Linq;

public class PoolProvince : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{

    public override void BeginProcess()
    {
        int poolId;
        if (!int.TryParse(Request["id"], out poolId))
            poolId = 1;
        Response.ContentType = "text/plain";
        var ps = n8wan.codepool.Dao.PoolSet.QueryPoolSetById(dBase, poolId);
        var ids = ps.Where(e => e.status == 1).Select(e => e.sp_trone_id).Distinct();
        var q = LightDataModel.tbl_sp_troneItem.GetQueries(dBase);
        q.PageSize = int.MaxValue;
        q.Filter.AndFilters.Add(LightDataModel.tbl_sp_troneItem.Fields.PrimaryKey, ids);
        //q.Fields = new string[] { LightDataModel.tbl_sp_troneItem.Fields.id, LightDataModel.tbl_sp_troneItem.Fields.name, LightDataModel.tbl_sp_troneItem.Fields.provinces };
        var data = q.GetDataList();
        var dict = new System.Collections.Generic.Dictionary<int, int>();
        foreach (var m in data)
        {
            foreach (var pid in m.ProvinceId)
            {
                if (dict.ContainsKey(pid))
                    dict[pid]++;
                else
                    dict[pid] = 1;
            }
        }

        var pq = LightDataModel.tbl_provinceItem.GetQueries(dBase);
        pq.Filter.AndFilters.Add(LightDataModel.tbl_provinceItem.Fields.PrimaryKey, dict.Keys);
        pq.PageSize = int.MaxValue;
        var pInfo = pq.GetDataList();

        foreach (var kv in dict)
        {
            var pi = pInfo.Find(e => e.id == kv.Key);
            if (pi == null)
            {
                Response.Write(string.Format("{0}\t未知", kv.Key));
            }
            else
                Response.Write(string.Format("{1}\t{0}", pi.name, pi.id));

            Response.Write(string.Format("\t{0}\n", kv.Value));

        }


    }
}