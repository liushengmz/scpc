<%@ WebHandler Language="C#" Class="CpSyncTest" %>

using System;
using System.Web;
using System.Linq;

public class CpSyncTest : Shotgun.PagePlus.SimpleHttpHandler
{

    public override void BeginProcess()
    {
        int poolid;
        if (!int.TryParse(Request["id"], out poolid))
        {
            poolid = 3;
        }
        var sptrone = n8wan.codepool.Dao.PoolSet.QuerySptroneByPoolid(dBase, poolid);
        var sb = new System.Text.StringBuilder();
        var poolInfo = LightDataModel.tbl_cp_poolItem.GetRowById(dBase, poolid);
        sb.AppendFormat("业务名：{0} (P{1:00000})\r\n", poolInfo.name, poolid);
        sb.AppendFormat("资费：{0:c}\r\n", poolInfo.fee / 100f);
        var province = string.Join(",", sptrone.Select(p => p.provinces)).Split(new char[] { ',', ' ' }, StringSplitOptions.RemoveEmptyEntries).Distinct();
        var pq = LightDataModel.tbl_provinceItem.GetQueries(dBase);
        pq.PageSize = int.MaxValue;
        pq.Filter.AndFilters.Add(LightDataModel.tbl_provinceItem.Fields.id, province);
        var t = string.Join(",", pq.GetDataList().Select(p => p.name));
        sb.AppendFormat("省份：{0}\r\n", t);

        int min = int.MaxValue, max = int.MinValue;
        foreach (var sp in sptrone)
        {

            if (string.IsNullOrEmpty(sp.shield_start_hour) || string.IsNullOrEmpty(sp.shield_end_hour))
                continue;

            if (sp.shield_start_hour == sp.shield_end_hour)
                continue;

            DateTime time;
            if (!DateTime.TryParse(sp.shield_start_hour, out time))
                continue;
            int start = time.Hour * 60 + time.Minute;
            if (!DateTime.TryParse(sp.shield_end_hour, out time))
                continue;
            int endtime = time.Hour * 60 + time.Minute;
            if (start > endtime)
                start = start - 1440;
            if (min > start)
                min = start;
            if (max < endtime)
                max = endtime;
        }
        sb.Append("屏蔽时间：");
        if (max == int.MinValue)
            sb.AppendFormat("无（24小时放量）");
        else
        {
            if (min < 0)
                min += 1440;
            sb.AppendFormat("{0:00}:{1:00}", min / 60, min % 60);
            sb.AppendFormat(" - {0:00}:{1:00}", max / 60, max % 60);
        }
        sb.AppendLine();
        sb.Append("日月限量： -\r\n");

        var m = n8wan.Public.Model.CPDataPushModel.CreateTestModel(null);
        m.PoolId = poolid;
        m.Price = poolInfo.fee;
        m.Port = m.Price.ToString();
        m.Msg = string.Format("P{0:00000}", poolid);
        //sb.AppendFormat("无（24小时放量）");
        var sql = "select url from tbl_cp_push_url url left join tbl_trone_order tno on tno.push_url_id= url.id "
                + " left join  tbl_cp_pool_set cps on cps.trone_order_id= tno.id"
                + " where cp_pool_id=" + poolInfo.id + " limit 1";

        var url = dBase.ExecuteScalar(sql);
        if (url != null && !DBNull.Value.Equals(url))
            m.Url = (string)url;

        sb.AppendFormat("同步参数：{0}={1}\r\n", n8wan.Public.Model.CPDataPushModel.GetFieldName(n8wan.Public.Model.CPDataPushModel.NameKey.port), m.Port);
        sb.AppendFormat("同步参数：{0}={1}\r\n", n8wan.Public.Model.CPDataPushModel.GetFieldName(n8wan.Public.Model.CPDataPushModel.NameKey.msg), m.Msg);
        sb.AppendFormat("同步参数：{0}={1}\r\n", n8wan.Public.Model.CPDataPushModel.GetFieldName(n8wan.Public.Model.CPDataPushModel.NameKey.paycode), m.Msg);


        sb.AppendFormat("模似同步：{0} (使用浏览器打开此网址即可)\r\n", m);

        t = string.Join("\r\n-------------------\r\n", sptrone.Select(p => p.ramark));
        sb.AppendFormat("下行语：\r\n{0}", t);
        Response.ContentType = "application/octet-stream";
        Response.AddHeader("Content-Disposition", "attachment");
        Response.Write(sb.ToString());
    }
}
 
   

 