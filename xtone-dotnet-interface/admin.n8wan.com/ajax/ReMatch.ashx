<%@ WebHandler Language="C#" Class="ReMatch" %>

using System;
using System.Web;
using LightDataModel;

public class ReMatch : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    System.Text.StringBuilder sb = new System.Text.StringBuilder();
    public override void BeginProcess()
    {
        Ajax = new Shotgun.Library.simpleAjaxResponser();
        int id = int.Parse(Request["id"]);

        var m = LightDataModel.tbl_mrItem.GetRowById(dBase, id);
        LightDataModel.tbl_troneItem trone = null;

        bool hasUpdate = false;
        if (!m.IsMatch)
        {
            trone = n8wan.Public.Logical.BaseSPCallback.FillToneId(dBase, m);
            if (trone == null)
            {
                Ajax.message = "匹对失败";
                return;
            }
            //m.trone_id = tid;
            m.IsMatch = true;
            dBase.SaveData(m);
            hasUpdate = true;
        }
        else
            trone = LightDataModel.tbl_troneItem.GetRowById(dBase, m.trone_id);

        try
        {
            hasUpdate |= DoCpPush(m, trone);
        }
        finally
        {
            if (hasUpdate)
            {
                var daily = LoadVRDaily(m);
                if (daily != null)
                {
                    m.CopyToDailyMr(daily);
                    dBase.SaveData(daily);
                }
            }
        }

        Ajax.message = sb.ToString();

        Ajax.state = Shotgun.Library.emAjaxResponseState.ok;
    }


    bool DoCpPush(tbl_mrItem mr, tbl_troneItem trone)
    {

        var apiPush = new n8wan.Public.Logical.HTAPIPusher()
        {
            dBase = dBase,
            Trone = trone,
            LogFile = Server.MapPath(string.Format("~/PushLog/{0:yyyyMMdd}.log", DateTime.Today)),
            TrackLog = sb
        };
        if (apiPush.LoadCPAPI())
        {
            apiPush.PushObject = mr;
            if (apiPush.DoPush())
            {
                Ajax.state = Shotgun.Library.emAjaxResponseState.ok;
                return true;
            }
        }

        var cp = new n8wan.Public.Logical.AutoMapPush();
        cp.dBase = dBase;
        cp.Trone = trone;
        cp.TrackLog = sb;
        //cp.UnionUserId = -1;
        cp.LogFile = Server.MapPath(string.Format("~/PushLog/{0:yyyyMMdd}.log", DateTime.Today));

        if (!cp.LoadCPAPI())
            return false;

        cp.PushObject = mr;

        return cp.DoPush();
    }

    tbl_mr_dailyItem LoadVRDaily(tbl_mrItem mr)
    {
        return tbl_mr_dailyItem.GetVRDaily(dBase, mr);
    }
}
 