using System;
using System.Collections.Generic;
using System.Text;

namespace Shotgun.PagePlus
{
    public partial class ShotgunPage
    {
        DateTime _requestTime;
        public ShotgunPage()
            : base()
        {
            _requestTime = DateTime.Now;
        }

        /// <summary>
        /// 客户请求时间
        /// 可用于计算页面执行时间
        /// </summary>
        public DateTime RequestTime { get { return _requestTime; } }

        public void SetTitle(string title)
        {
            PageTitleFixer.AppendTitle(title);
        }

        /// <summary>
        /// 跳转至来源页面
        /// </summary>
        public void RedirectFromPage()
        {
            RedirectFromPage(null, null);
        }

        /// <summary>
        /// 跳转至来源页面
        /// </summary>
        /// <param name="msg">返回前提示信息，可为null</param>
        public void RedirectFromPage(string msg)
        {
            RedirectFromPage(msg, null);
        }

        /// <summary>
        /// 跳转至来源页面
        /// </summary>
        /// <param name="msg">返回前提示信息，可为null</param>
        /// <param name="defualReturnUrl">当前URL没有returnURL参数时的跳转地址。可为null，为null时首选跳转至来源页，其实是当前目录默认首页</param>
        public void RedirectFromPage(string msg, string defualReturnUrl)
        {
            var url = Request["returnURL"];
            if (string.IsNullOrEmpty(url))
                url = defualReturnUrl;

            if (string.IsNullOrEmpty(url))
            {
                var rUrl = Request.UrlReferrer;
                if (rUrl != null && rUrl.Host == Request.Url.Host)
                {
                    if (!Request.Url.PathAndQuery.Equals(rUrl.PathAndQuery, StringComparison.OrdinalIgnoreCase))
                        url = rUrl.ToString();//来源页与当前页网址不相同时，则回到来源页
                }
            }
            if (string.IsNullOrEmpty(url))
                url = ".";//在缺少参数情况下，跳转到当前目录默认页

            if (string.IsNullOrEmpty(msg))
                Response.Redirect(url);
            else
                Library.Static.ClientRedirect(url, msg);
        }

    }
}
