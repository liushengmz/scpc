<%@ WebHandler Language="C#" Class="test" %>

using System;
using System.Web;
using System.Collections.Generic;

public class test : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    class AnObject
    {
        public int count { get; set; }
    }

    static Dictionary<int, AnObject> dict;

    public override void BeginProcess()
    {
        if (dict == null)
        {
            Response.Write("new object");
            dict = new Dictionary<int, AnObject>();
            var obj = new AnObject();
            obj.count = 123;
            dict[1] = obj;

        }

        var obj_b = dict[1];
        obj_b.count++;



        var obj_c = dict[1];

        Response.Write(obj_c.count);

    }
}