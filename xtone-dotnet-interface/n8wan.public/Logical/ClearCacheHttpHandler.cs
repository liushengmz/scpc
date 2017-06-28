using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    public class ClearCacheHandler : System.Web.IHttpHandler
    {
        public bool IsReusable
        {
            get { return false; }
        }

        public void ProcessRequest(System.Web.HttpContext context)
        {
            StaticCache.ClearAllCache();
            context.Response.Write("清除成功");
        }
    }
}
