<%@ WebHandler Language="C#" Class="GetCpPushURL" %>

using System;
using System.Web;

public class GetCpPushURL : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{

    public override void BeginProcess()
    {
        int id;
        if (!int.TryParse(Request["id"], out id))
            int.TryParse(Request["cpId"], out id);

        var l = LightDataModel.tbl_cp_push_urlItem.GetQueries(dBase);
        l.Fields = new string[] { LightDataModel.tbl_cp_push_urlItem.Fields.id,
            LightDataModel.tbl_cp_push_urlItem.Fields.url , LightDataModel.tbl_cp_push_urlItem.Fields.name};

        l.Filter.AndFilters.Add(LightDataModel.tbl_cp_push_urlItem.Fields.cp_id, id);
        l.PageSize = int.MaxValue;

        var dt = l.GetDataList();

        if (string.IsNullOrEmpty(Request["callback"]))
            Response.Write("cp_push_url_callback");
        else
            Response.Write(Request["callback"]);
        Response.Write("([");
        int i = 0;
        dt.ForEach(e =>
        {
            Response.Write(string.Format("[{0},\"{1}\",\"{2}\"]",
                e.id,
                Shotgun.Library.Static.JsEncode(e.name),
                Shotgun.Library.Static.JsEncode(e.url)));
            if (++i != dt.Count)
                Response.Write(',');
        });

        Response.Write("]);");

    }
}