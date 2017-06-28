<%@ WebHandler Language="C#" Class="test" %>

using System;
using System.Web;

public class test : IHttpHandler
{
    
    public void ProcessRequest (HttpContext context) {
        context.Response.ContentType = "text/plain";
        context.Response.Write(context.User.Identity.IsAuthenticated);
    }
 
    public bool IsReusable {
        get {
            return false;
        }
    }

}