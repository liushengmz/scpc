using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;
using Shotgun.Library;

namespace Shotgun.Security
{
    public abstract class LoginHandler : IHttpHandler
    {
        public enum AuthorMode
        {
            /// <summary>
            /// TickForm
            /// </summary>
            Default = TickForm,
            /// <summary>
            /// 系统默认的验证票模式
            /// </summary>
            TickForm = 0,
            /// <summary>
            /// 自定义验证,需要结合Gobal.asx
            /// </summary>
            CusotmCookie = 1
        }

        public bool IsReusable
        {
            get { return false; }
        }

        protected abstract Shotgun.Database.BDClass CreateDBase();

        public void ProcessRequest(HttpContext context)
        {
            HttpRequest Request = context.Request;


            string userName = Request["userName"];
            string password = Request["password"];
            string pType = Request["pType"];
            if (string.IsNullOrEmpty(userName) || string.IsNullOrEmpty(password))
            {
                Static.ClientRedirect("用户名或密码为能为空！");
                return;
            }

            string userInfo;
            using (this.dBase = CreateDBase())
            {
                userInfo = DoLogin(userName, password, pType);
            }
            if (userInfo == null)
            {
                Static.ClientRedirect("用户名或密码错误！");
                return;
            }
            if (!Regex.IsMatch(userInfo, "^.+?\\|.+?\\|[^\\|]{0,}$"))
            {
                Static.ClientRedirect(userInfo);
                return;
            }
            if (Mode == AuthorMode.TickForm)
            {
                WriteTick(userInfo.Split(new char[] { '|' }));
            }
            else
            {
                HttpCookie ec = new HttpCookie(AuthorIIdentity.C_SESSION_NAME);
                if (!Request.IsLocal)
                    ec.Domain = Domain;

                ec.HttpOnly = true;
                ec.Value = AdminCookieEnCode.Encode(userInfo, Request.UserAgent);
                context.Response.Cookies.Add(ec);
            }
            string ret = Request["returnUrl"];
            if (!string.IsNullOrEmpty(ret))
                context.Response.Redirect(ret);
            else
            {
                context.Response.Redirect("~/");
            }
        }

        private void WriteTick(string[] p)
        {
            System.Web.Security.FormsAuthentication.RedirectFromLoginPage(p[1], false);
        }
        /// <summary>
        /// 用于验证用户登陆
        /// </summary>
        /// <param name="userName"></param>
        /// <param name="password"></param>
        /// <param name="pType"></param>
        /// <returns></returns>
        protected abstract string DoLogin(string userName, string password, string pType);

        protected virtual string Domain { get { return null; } }

        protected virtual AuthorMode Mode { get { return AuthorMode.Default; } }


        public Database.BDClass dBase { get; private set; }
    }
}
