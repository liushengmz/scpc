<%@ WebHandler Language="C#" Class="getCpPinYin" %>

using System;
using System.Web;
using Newtonsoft.Json.Linq;
using System.Linq;

public class getCpPinYin : Shotgun.PagePlus.SimpleHttpHandler
{


    public override void BeginProcess()
    {
        //var userName = HttpContext.Current.User.Identity.Name;
        //if (userMode == UserMode.Cp)
        //    sql += "\n left join daily_config.tbl_user user on cp.commerce_user_id=user.id where user.name='" + dBase.SqlEncode(userName) + "'";
        //else if (userMode == UserMode.Sp)
        //    sql += "\n left join daily_config.tbl_user user on sp.commerce_user_id=user.id where user.name='" + dBase.SqlEncode(userName) + "'";

        //if(Request.Cookies[""])

        var cookie = Request.Cookies["viewmodel"];
        var isCpMode = "cp".Equals(cookie == null ? null : cookie.Value, StringComparison.OrdinalIgnoreCase);


        var q = LightDataModel.tbl_cpItem.GetQueries(dBase);
        q.Fields = new string[] { LightDataModel.tbl_cpItem.Fields.id, LightDataModel.tbl_cpItem.Fields.short_name, LightDataModel.tbl_cpItem.Fields.full_name };
        q.Filter.AndFilters.Add(LightDataModel.tbl_cpItem.Fields.status, 1);
        if (isCpMode)
        {
            var tf2 = new Shotgun.Model.Filter.TableFilter(LightDataModel.tbl_cpItem.Fields.commerce_user_id, LightDataModel.tbl_userItem.tableName, LightDataModel.tbl_userItem.Fields.PrimaryKey);
            tf2.TableFilters.AndFilters.Add(LightDataModel.tbl_userItem.Fields.name, HttpContext.Current.User.Identity.Name);
            q.Filter.AndFilters.Add(tf2);
        }

        q.PageSize = int.MaxValue;
        q.SortKey.Add(LightDataModel.tbl_cpItem.Fields.short_name, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        var items = q.GetDataList();
        var jobjs = new JObject[items.Count];
        int i = 0;
        foreach (var m in items)
        {
            var jobj = new JObject();
            jobj["id"] = m.id;
            jobj["key"] = Shotgun.Library.PinYinConverter.GetFirst(m.short_name);
            jobj["name"] = m.short_name;
            jobjs[i++] = jobj;
        }

        JArray jarr = new JArray(jobjs.OrderBy(e => e["key"]));
        Response.ContentType = "text/plain";
        Response.Write(jarr.ToString(Newtonsoft.Json.Formatting.None, null));



    }
}