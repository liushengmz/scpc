<%@ WebHandler Language="C#" Class="GetNames" %>

using System;
using System.Web;
using System.Linq;
using System.Collections.Generic;
using System.Text;

public class GetNames : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{

    public override void BeginProcess()
    {

        Func<System.Collections.Generic.IEnumerable<string[]>> fun = null;

        switch (Request["method"])
        {
            case "spname": fun = GetSpname; break;
            case "cpname": fun = GetCpname; break;
        }

        if (fun == null)
        {
            Response.Write("{'error':0}");
            return;
        }

        var result = fun();

        Dictionary<string, StringBuilder> data = new Dictionary<string, StringBuilder>();

        foreach (var item in result)
        {
            var py = Shotgun.Library.PinYinConverter.GetFirst(item[1].ToCharArray()[0]);
            StringBuilder sb;
            if (data.ContainsKey(py))
            {
                sb = data[py];
                sb.Append(",");
            }
            else
                data[py] = sb = new StringBuilder();

            sb.AppendFormat("{{'id':{0},'name':'{1}'}}", item[0], item[1]);
        }

        var t = data.OrderBy(e => e.Key);
        var json = new StringBuilder();
        foreach (var k in t)
        {
            json.AppendFormat(",'{0}':[{1}]", k.Key, k.Value.ToString());
        }
        json.Remove(0, 1);
        Response.Write("{");
        Response.Write(json.ToString());
        Response.Write("}");

    }


    public System.Collections.Generic.IEnumerable<string[]> GetSpname()
    {
        var l = LightDataModel.tbl_spItem.GetQueries(dBase);
        l.Fields = new string[] { LightDataModel.tbl_spItem.Fields.id, LightDataModel.tbl_spItem.Fields.short_name };
        l.Filter.AndFilters.Add(LightDataModel.tbl_spItem.Fields.status, 1);
        l.PageSize = int.MaxValue;
        var sp = l.GetDataList();
        var result = from s in sp orderby s.short_name select new string[] { s.id.ToString(), s.short_name };
        return result;
    }

    public System.Collections.Generic.IEnumerable<string[]> GetCpname()
    {
        var l = LightDataModel.tbl_cpItem.GetQueries(dBase);
        l.Fields = new string[] { LightDataModel.tbl_cpItem.Fields.id, LightDataModel.tbl_cpItem.Fields.short_name };
        l.Filter.AndFilters.Add(LightDataModel.tbl_cpItem.Fields.status, 1);
        l.PageSize = int.MaxValue;
        var sp = l.GetDataList();
        var result = from s in sp orderby s.short_name select new string[] { s.id.ToString(), s.short_name };
        return result;

    }

}