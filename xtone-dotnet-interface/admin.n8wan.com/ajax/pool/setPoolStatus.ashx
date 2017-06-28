<%@ WebHandler Language="C#" Class="setPoolStatus" %>

using System;
using System.Web;

public class setPoolStatus : Shotgun.PagePlus.SimpleHttpHandler
{

    public override void BeginProcess()
    {
        //context.Response.ContentType = "text/plain";
        int s;
        if (!int.TryParse(Request["status"], out s))
            return;

        var m = new LightDataModel.tbl_cp_poolItem();
        m.status = s == 1;
        if (!int.TryParse(Request["id"], out s))
            return;
        m.id = s;
        dBase.SaveData(m);
        Response.Write("{\"status\":" + Request["status"] + "}");
    }
}