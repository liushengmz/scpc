<%@ WebHandler Language="C#" Class="getPoolProvices" %>

using System;
using System.Web;
using System.Linq;

public class getPoolProvices : Shotgun.PagePlus.SimpleHttpHandler
{

    public override void BeginProcess()
    {
        var ids = Request["ids"];
        if (string.IsNullOrEmpty(ids))
        {
            Response.Write("[]");
            return;
        }
        var ss = string.Join(",", ids.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries).Select(e => int.Parse(e)).Distinct());

        var sql = "SELECT cp_pool_id,group_concat(provinces) FROM daily_config.tbl_cp_pool_set cps "
                + " left join tbl_trone_order tno on tno.id=cps.trone_order_id"
                + " left join tbl_trone tn on tno.trone_id= tn.id"
                + " left join tbl_sp_trone stn on stn.id=tn.sp_trone_id"
                + " where cp_pool_id in( " + ss + ") and cps.status=1 and tno.disable=0 and tn.status=1 and stn.status=1  group by cp_pool_id ;";

        var dt = dBase.GetDataTable(sql);
        var jobj = new Newtonsoft.Json.Linq.JObject();
        var sb = new System.Text.StringBuilder();

        foreach (System.Data.DataRow dr in dt.Rows)
        {


            string s;
            if (dr.IsNull(1))
                s = string.Empty;
            else
                s = (string)dr[1];
            var arr = s.Split(new char[] { ',' }).Select(e => int.Parse(e)).Distinct();

            jobj[dr[0].ToString()] = new Newtonsoft.Json.Linq.JArray(arr);
            // jarr.Add(jobj);

        }

        Response.Write(jobj.ToString(Newtonsoft.Json.Formatting.None, null));

    }
}