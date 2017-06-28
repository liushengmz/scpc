<%@ WebHandler Language="C#" Class="status" %>

using System;
using System.Web;
using LightDataModel;
using n8wan.Public.Logical;
using System.Collections.Generic;
using System.Linq;
using System.Collections;

public class status : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{

    public override void BeginProcess()
    {
        var t = typeof(n8wan.Public.Logical.StaticCache);
        var field = t.GetField("_allCache", System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.NonPublic);
        List<StaticCache> ls = (List<StaticCache>)field.GetValue(null);
        Response.ContentType = "text/plain";

        foreach (var sc in ls)
        {
            Response.Write('\n');
            t = sc.GetType();
            field = t.GetField("_tabName", System.Reflection.BindingFlags.Instance | System.Reflection.BindingFlags.NonPublic);
            Response.Write(field.GetValue(sc));
            Response.Write('\t');

            field = t.GetField("_data", System.Reflection.BindingFlags.Instance | System.Reflection.BindingFlags.NonPublic);
            IDictionary dict = (IDictionary)field.GetValue(sc);
            if (dict == null)
                Response.Write("\tNull");
            else
                Response.Write("\t" + dict.Count.ToString());

            field = t.GetField("_satus", System.Reflection.BindingFlags.Instance | System.Reflection.BindingFlags.NonPublic);
            Response.Write("\t" + field.GetValue(sc));

            field = t.GetField("_expired", System.Reflection.BindingFlags.Instance | System.Reflection.BindingFlags.NonPublic);
            Response.Write(string.Format("\t{0}", field.GetValue(sc)));

        }

    }
}