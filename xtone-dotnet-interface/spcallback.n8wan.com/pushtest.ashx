<%@ WebHandler Language="C#" Class="pushtest" %>

using System;
using System.Web;

public class pushtest : IHttpHandler
{

    public void ProcessRequest(HttpContext context)
    {
        //=13911600001&=api_1010&port=9800&linkid=TEST20170809103427&cpparam=103427&paycode=107435&ordernum=201708091034027 
        var m = new n8wan.Public.Model.CPDataPushModel();
        int i;
        int.TryParse(context.Request["paycode"], out i);

        m.PayCode = i % 100000;
        m.Linkid = context.Request["linkid"];
        m.Mobile = context.Request["mobile"];
        m.Msg = context.Request["msg"];
        m.Port = context.Request["port"];
        m.Cpparam = context.Request["cpparam"];
        m.OrderNum = context.Request["ordernum"];
        m.Url = context.Request["synurl"];

        context.Response.ContentType = "text/plain";
        var url = m.ToString();
        context.Response.Write("目标地址：" + url);
        context.Response.Write("\r\n");
        if (!url.StartsWith("http", StringComparison.OrdinalIgnoreCase))
        {
            context.Response.Write("非标准同步接口，无法直接模似");
            return;
        }
        var stw = new System.Diagnostics.Stopwatch();
        stw.Start();
        string html = null;
        try
        {
            html = n8wan.Public.Library.DownloadHTML(url, null, 1000, null);
        }
        catch (System.Net.WebException ex)
        {
            html = string.Format("Http Status:{0} - {1}", ex.Status, ex.Message);
            if (ex.Response != null)
            {
                using (var stm = new System.IO.StreamReader(ex.Response.GetResponseStream()))
                {
                    html += "\r\n" + stm.ReadToEnd();
                }
            }

        }

        stw.Stop();
        context.Response.Write("接口耗时：" + stw.ElapsedMilliseconds + "ms\r\n");

        context.Response.Write("模似结果：" + html);



    }

    public bool IsReusable
    {
        get
        {
            return false;
        }
    }

}