<%@ WebHandler Language="C#" Class="ivr_copy" %>

using System;
using System.Web;
/// <summary>
/// 用于解决渠道多个渠道，同时同步的问题
/// </summary>
public class ivr_copy : IHttpHandler
{

    public void ProcessRequest(HttpContext context)
    {
        context.Response.ContentType = "text/plain";
        var R = context.Request;
        var url = GetSyncUrl(R["caller"]);
        if (url == null)
        {
            context.Response.Write("unkonw province");
            return;
        }

        var qs = R.Url.Query;
        byte[] bin = null;
        if (R.HttpMethod == "POST")
        {
            R.InputStream.Seek(0, System.IO.SeekOrigin.Begin);
            bin = new byte[R.InputStream.Length];
            R.InputStream.Read(bin, 0, bin.Length);
        }

        context.Response.Write(string.Format("target:{0}{1}\r\n", url, qs));
        Shotgun.Library.AsyncRemoteRequest.RequestOnly(string.Format("{0}{1}", url, qs), bin);

    }

    string GetSyncUrl(string mobile)
    {
        if (string.IsNullOrEmpty(mobile) || mobile.Length != 11)
            return null;
        int pid;
        int.TryParse(mobile.Substring(0, 7), out pid);
        LightDataModel.tbl_cityItem m = null;
        using (var dBase = new Shotgun.Database.MySqlDBClass())
        {
            m = LightDataModel.tbl_phone_locateItem.GetRowByMobile(dBase, pid);
        }
        if (m == null)
            return null;

        if (m.province_id == 18)//18	重庆
            return "http://callback.n8wan.com:2109/sp/285/bjtltd_ivrtb.ashx";
        return "http://vooaa.cn:9093/inter/bjxyivr.jsp";
    }

    public bool IsReusable
    {
        get
        {
            return true;
        }
    }

}