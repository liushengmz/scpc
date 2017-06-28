<%@ WebHandler Language="C#" Class="detail" %>

using System;
using System.Web;
using System.Reflection;
using System.Collections;
using System.Collections.Generic;
using n8wan.Public.Logical;
using System.Linq;


public class detail : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    string dstTable;
    public override void BeginProcess()
    {
        dstTable = Request["name"];

        var stw = new System.Diagnostics.Stopwatch();
        //LightDataModel.tbl_cp_push_urlItem.GetRowByIdWithCache(dBase, 47);
        //n8wan.codepool.Dao.CustomFee.QueryLimit(dBase, 1, null);

        stw.Start();

        var t = typeof(n8wan.Public.Logical.StaticCache);
        var field = t.GetField("_allCache", System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.NonPublic);
        List<StaticCache> ls = (List<StaticCache>)field.GetValue(null);

        //t = typeof(LightDataModel.tbl_trone_orderItem);
        //field = t.GetField("cache", System.Reflection.BindingFlags.Static | System.Reflection.BindingFlags.NonPublic);
        //var obj = field.GetValue(null);
        Response.ContentType = "text/plain";
        Console.SetOut(Response.Output);


        Response.Output.WriteLine("---------");
        foreach (var sc in ls)
        {
            ModelCacheToStrem(sc, Response.Output);
            Response.Output.WriteLine();
        }


        stw.Stop();
        Console.WriteLine("{0}", stw.Elapsed);
    }


    void ModelCacheToStrem(StaticCache cache, System.IO.TextWriter stm)
    {
        var cType = cache.GetType();
        var tabName = cType.GetProperty("TableName");
        if (tabName == null)
        {
            stm.WriteLine("unkonw support type:{0}", cType.FullName);
            return;
        }

        var value = tabName.GetValue(cache, null);
        if (!string.IsNullOrEmpty(dstTable) && !dstTable.Equals((string)value, StringComparison.OrdinalIgnoreCase))
            return;
        stm.WriteLine("table name:{0}({1})", value, cType.FullName);

        var expdate = cType.GetField("_expired", BindingFlags.NonPublic | BindingFlags.Instance);
        if (expdate != null)
        {
            value = expdate.GetValue(cache);
            var ts = ((DateTime)value - DateTime.Now);
            stm.WriteLine("Expired on :{0}({1})", value, ts);
        }
        else
        {
            stm.WriteLine("Expired Data :unkonw!");
        }

        var getCacheMethod = cType.GetMethod("GetCacheData");
        if (getCacheMethod == null)
        {
            stm.WriteLine("unkonw support type:{0}, method not found:GetCacheData()", cType.FullName);
            return;
        }

        var caches = (IEnumerable)getCacheMethod.Invoke(cache, new object[] { false });
        if (caches == null)
        {
            stm.WriteLine("no cache");
            return;
        }

        IEnumerator ie = null;
        object cacheItem = null;
        try
        {
            ie = caches.GetEnumerator();
            if (ie != null)
            {
                ie.Reset();
                ie.MoveNext();
                cacheItem = ie.Current;
            }
        }
        catch (Exception ex)
        {
            stm.WriteLine("dump catch fail:{0}", ex);
            return;
        }

        if (cacheItem == null)
        {
            stm.WriteLine("no cache");
            return;
        }

        var mType = cacheItem.GetType();
        var fieldsName = mType.FullName + "+Fields";
        var types = mType.Assembly.GetTypes();

        var fType = types.FirstOrDefault(e => e.FullName == fieldsName);
        MemberInfo[] fields = null;

        if (fType != null)
        {
            fields = fType.GetFields(BindingFlags.Static | BindingFlags.Public);
        }
        else
        {
            stm.WriteLine("InnerClass not Found:{0}", fieldsName);
            fields = mType.GetProperties(BindingFlags.Instance | BindingFlags.Public | BindingFlags.GetProperty | BindingFlags.DeclaredOnly);
        }




        List<PropertyInfo> pros = new List<PropertyInfo>();

        foreach (var f in fields)
        {
            if (f.Name == "PrimaryKey" || f.Name == "IdentifyField" || f.Name == "TableName")
                continue;
            var p = mType.GetProperty(f.Name);
            if (p == null)
                continue;
            pros.Add(p);
            stm.Write("{0}\t", f.Name);
        }
        stm.WriteLine();


        foreach (var item in caches)
        {
            foreach (var p in pros)
            {
                stm.Write("{0}\t", p.GetValue(item, null));
            }
            stm.WriteLine();
        }

    }
}