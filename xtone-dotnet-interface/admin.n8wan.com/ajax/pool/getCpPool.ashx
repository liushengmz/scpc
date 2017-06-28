<%@ WebHandler Language="C#" Class="getCpPool" %>

using System;
using System.Web;

public class getCpPool : Shotgun.PagePlus.SimpleHttpHandler
{
    bool isCpMode = false;
    public override void BeginProcess()
    {
        var cookie = Request.Cookies["viewmodel"];
        isCpMode = "cp".Equals(cookie == null ? null : cookie.Value, StringComparison.OrdinalIgnoreCase);

        var q = LightDataModel.tbl_cp_poolItem.GetQueries(dBase);
        q.PageSize = int.MaxValue;
        q.SortKey.Add(LightDataModel.tbl_cp_poolItem.Fields.status, Shotgun.Model.Filter.EM_SortKeyWord.desc);
        q.SortKey.Add(LightDataModel.tbl_cp_poolItem.Fields.PrimaryKey, Shotgun.Model.Filter.EM_SortKeyWord.desc);
        if (isCpMode)
        {
            var cptf = new Shotgun.Model.Filter.TableFilter(LightDataModel.tbl_cp_poolItem.Fields.cp_id, LightDataModel.tbl_cpItem.tableName, LightDataModel.tbl_cpItem.Fields.PrimaryKey);
            q.Filter.AndFilters.Add(cptf);

            var tf2 = new Shotgun.Model.Filter.TableFilter(LightDataModel.tbl_cpItem.Fields.commerce_user_id, LightDataModel.tbl_userItem.tableName, LightDataModel.tbl_userItem.Fields.PrimaryKey);

            tf2.TableFilters.AndFilters.Add(LightDataModel.tbl_userItem.Fields.name, HttpContext.Current.User.Identity.Name);
            cptf.TableFilters.AndFilters.Add(tf2);
        }

        var ms = q.GetDataList();
        Response.Write(Newtonsoft.Json.JsonConvert.SerializeObject(ms));

    }
}