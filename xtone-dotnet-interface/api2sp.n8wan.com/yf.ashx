<%@ WebHandler Language="C#" Class="yf" %>

using System;
using System.Web;
/// <summary>
/// 临时使用
/// </summary>
public class yf : IHttpHandler {
    
    public void ProcessRequest (HttpContext context) {
        context.Response.ContentType = "text/plain";
        context.Response.Write("A01D438626#SZYF"+DateTime.Now.ToString("MMddHHmmssff"));
    }
 
    public bool IsReusable {
        get {
            return false;
        }
    }

}