<%@ WebHandler Language="C#" Class="List" %>

using System;
using System.Web;
using LightDataModel;
using n8wan.Public.Logical;
using System.Collections.Generic;
using System.Linq;
using System.Collections;

public class List : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{

    public override void BeginProcess()
    {

        tbl_cp_push_urlItem.GetRowByIdWithCache(dBase, 47);

        var t = typeof(n8wan.Public.Logical.StaticCache);
        var field = t.GetField("_allCache", System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.NonPublic);
        List<StaticCache> ls = (List<StaticCache>)field.GetValue(null);

        t = typeof(tbl_trone_orderItem);
        field = t.GetField("cache", System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.NonPublic);
        var obj = field.GetValue(null);
        Response.ContentType = "text/plain";
        Console.SetOut(Response.Output);
        foreach (var sc in ls)
        {
            t = sc.GetType();
            var p = t.GetProperty("TableName");
            var value = p.GetValue(sc, null);
            Console.WriteLine(value);
        }
        // return;

        t = obj.GetType();
        field = t.GetField("_expired", System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Instance);
        Response.Write(string.Format("_expired Date:{0}\n", field.GetValue(obj)));

        field = t.GetField("_data", System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Instance);

        IDictionary dic = (IDictionary)field.GetValue(obj);
        if (dic == null)
        {
            Response.Write("NO cache data!");
            return;
        }
        var today = DateTime.Today;
        Response.Write(string.Format("count:{0}\n", dic.Count));

        var sb = new System.Text.StringBuilder();




        Response.Write("id\tCustom\thold_start\tpush_count\tamount\thold_CycCount\tcpt.hold_percent");
        foreach (LightDataModel.tbl_trone_orderItem cpt in dic.Values)
        {
            //if (!cpt.hold_is_Custom || cpt.lastDate != today || cpt.hold_percent <= 0 || cpt.disable)
            //    continue;
            Response.Write(string.Format("\n{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}", cpt.id, cpt.hold_is_Custom, cpt.hold_start, cpt.push_count, cpt.amount, cpt.hold_CycCount, cpt.hold_percent));
        }

    }
}