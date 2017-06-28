<%@ WebHandler Language="C#" Class="CpSyncTest" %>

using System;
using System.Web;

public class CpSyncTest : IHttpHandler
{

    public void ProcessRequest(HttpContext context)
    {

        int id;

        if (!int.TryParse(context.Request["id"], out id))
        {
            context.Response.Write("参数无效！");
            return;
        }


        var sql = "SELECT cps.cp_pool_id,url.url,tn.price from tbl_cp_pool_set cps "
            + " left join tbl_trone_order tno on tno.id= cps.trone_order_id"
            + " left join tbl_cp_push_url url on url.id= tno.push_url_id"
            + " left join tbl_trone tn on tn.id=tno.trone_id"
            + " where cp_pool_id=" + id.ToString() + " limit 1";
        var m = n8wan.Public.Model.CPDataPushModel.CreateTestModel(null);

        using (var db = new Shotgun.Database.MySqlDBClass())
        {
            using (var cmd = ((Shotgun.Database.IBaseDataClass2)db).Command())
            {
                cmd.CommandText = sql;
                using (var rd = db.ExecuteReader(cmd))
                {
                    if (rd.Read())
                    {
                        m.PoolId = rd.GetInt32(0);
                        m.Url = rd.GetString(1);
                        m.Price = decimal.ToInt32(rd.GetDecimal(2) * 100);
                    }
                }
            }
        }

        context.Response.Write("<html><head><title>CP callback test</title>");
        context.Response.Write("</head><body> url:");
        var url = m.ToString();

        context.Response.Write(url);

        context.Response.Write("<br/><a href='" + url + "'>立即同步</a></body></html>");


    }

    public bool IsReusable
    {
        get
        {
            return false;
        }
    }

}