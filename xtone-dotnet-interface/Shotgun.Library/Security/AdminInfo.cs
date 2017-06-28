using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;

namespace Shotgun.Security
{
    public class AdminInfo
    {
        /// <summary>
        /// 管理员用户名
        /// </summary>
        public readonly string AdminName;
        /// <summary>
        /// 权限串
        /// </summary>
        public readonly string RightString;
        /// <summary>
        /// 管理员ID号
        /// </summary>
        public readonly int AdminId;

        public AdminInfo()
        {
            HttpCookie Cookie = HttpContext.Current.Request.Cookies[AuthorIIdentity.C_SESSION_NAME];

            if (Cookie == null || string.IsNullOrEmpty(Cookie.Value))
                return;
            string str = HttpUtility.HtmlDecode(Cookie.Value);

            string ui = AdminCookieEnCode.Decode(str, HttpContext.Current.Request.UserAgent);
            if (string.IsNullOrEmpty(ui))
                return;
            string[] ds = ui.Split('|');
            if (ds.Length != 3)
                return;
            int.TryParse(ds[0], out AdminId);
            if (AdminId == 0)
                return;

            AdminName = ds[1];
            RightString = ds[2];
            return;
        }
    }
}
