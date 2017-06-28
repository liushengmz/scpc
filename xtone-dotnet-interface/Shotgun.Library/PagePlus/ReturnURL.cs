using System;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;


namespace Shotgun.PagePlus
{
    /// <summary>
    /// 在URL尾部加上returnUrl参数
    /// </summary>
    public class ReturnURL : IHttpHandler
    {
        #region IHttpHandler 成员

        public bool IsReusable
        {
            get { return false; }
        }

        public void ProcessRequest(HttpContext context)
        {
            HttpRequest Request = context.Request;

            Uri refUrl = context.Request.UrlReferrer;

            if (Request["returnURL"] == null && refUrl != null)
            {
                string ret = "returnURL=" + context.Server.UrlEncode(refUrl.PathAndQuery);
                if (Request.Url.Query.Length == 0)
                    context.Response.Redirect(Request.Url.ToString() + "?" + ret);
                else
                    context.Response.Redirect(Request.Url.ToString() + "&" + ret);
                return;

            }
            NormalPage(context);
        }

        #endregion

        void NormalPage(HttpContext context)
        {
            IHttpHandler handler = PageParser.GetCompiledPageInstance(
                    context.Request.Path,
                    context.Request.PhysicalPath, context);
            context.Handler = handler;
            try
            {
                handler.ProcessRequest(context);
            }
            catch (System.Threading.ThreadAbortException) { }
            finally
            {
                if (handler is IDisposable)
                    ((IDisposable)handler).Dispose();
            }
        }
    }
}