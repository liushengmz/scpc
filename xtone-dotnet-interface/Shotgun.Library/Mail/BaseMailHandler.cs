using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Web;
using System.Net.Mail;
using System.Data;
using System.Text.RegularExpressions;

namespace Shotgun.Library.Mail
{
    public abstract class BaseMailHandler : IHttpHandler
    {

        delegate string InsertAd(string body);

        string _outFmt = "js";

        protected HttpRequest Request = null;
        protected HttpResponse Response = null;
        protected HttpServerUtility Server;

        private bool _adAllow = true;

        public BaseMailHandler()
        {
            Request = System.Web.HttpContext.Current.Request;
            Response = System.Web.HttpContext.Current.Response;
            Server = System.Web.HttpContext.Current.Server;
        }

        protected abstract string MailTemplete { get; }

        /// <summary>
        /// 简单的加载文件内容
        /// </summary>
        /// <returns></returns>
        protected virtual string LoadHtml()
        {
            using (StreamReader stm = new StreamReader(this.MailTemplete))
                return stm.ReadToEnd();
        }

        /// <summary>
        /// 从简单加载的数据中，替换{XXXX}标签
        /// </summary>
        /// <param name="vRow">数据行</param>
        /// <param name="NullString">如果是空数据，将显示此值</param>
        /// <returns></returns>
        protected virtual string LoadHtml(DataRow vRow, string NullString)
        {
            string html = LoadHtml();
            Regex rx = new Regex(@"\{(\w+)\}");

            MatchCollection mcs = rx.Matches(html);
            string data;
            ///已经替换
            List<String> rdone = new List<string>();
            string key;
            foreach (Match mc in mcs)
            {
                key = mc.Groups[1].Value.ToLower();
                if (!vRow.Table.Columns.Contains(key))
                    continue;//无该项数据

                if (rdone.Contains(key))
                    continue;//已经替换
                if (vRow.IsNull(key))
                    data = NullString;
                else
                    data = vRow[key].ToString();
                rdone.Add(key);
                html = Regex.Replace(html, @"\{" + key + @"\}", data, RegexOptions.IgnoreCase);
            }
            return html;

        }

        protected string InsertMailTxtAd(string body)
        {
            string ad;
            FileInfo fi = new FileInfo(Server.MapPath("~/Templete/mail_ad.txt"));
            if (!fi.Exists)
                return body;
            try
            {
                StreamReader stm = fi.OpenText();
                ad = stm.ReadToEnd();
                stm.Dispose();
            }
            catch
            {
                return body;
            }
            return body + ad;
        }
        protected string InsertMailHtmlAd(string body)
        {
            string ad;

            FileInfo fi = new FileInfo(Server.MapPath("~/Templete/mail_ad.htm"));
            if (!fi.Exists)
                return body;
            try
            {
                StreamReader stm = fi.OpenText();
                ad = stm.ReadToEnd();
                stm.Dispose();
            }
            catch
            {
                return body;
            }

            int i = body.LastIndexOf("</body", StringComparison.OrdinalIgnoreCase);
            if (i == -1)
                i = body.LastIndexOf("</html", StringComparison.OrdinalIgnoreCase);
            if (i == -1)
                return body + ad;

            string p = body.Substring(0, i);
            return p + ad + body.Substring(i);
        }


        protected virtual string OutputFormnat
        {
            get { return _outFmt; }
            set { _outFmt = value; }
        }

        /// <summary>
        /// 是否允许在发送的邮件中插入广告
        /// </summary>
        protected bool AdAllow
        {
            get { return _adAllow; }
            set { _adAllow = value; }
        }

        /// <summary>
        /// 使用js对话框方式输入邮件发送结果。
        /// </summary>
        /// <param name="msg"></param>
        /// <param name="IsSuccess"></param>
        protected virtual void MsgOutput(string msg, bool IsSuccess)
        {
            if (_outFmt == "xml")
            {
                simpleAjaxResponser ajax = new simpleAjaxResponser();
                ajax.state = IsSuccess ? emAjaxResponseState.ok : emAjaxResponseState.error;
                ajax.message = msg;
                ajax.SendResponse(false);
                ajax = null;
                return;
            }


            Response.Write(string.Format("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                "</head><body><script>alert('{0}\\n{1}')</script></body></html>", (IsSuccess ? "成功" : "失败"), Static.JsEncode(msg)));
        }

        protected bool SendMail(string to, string title, string body, bool isHtml)
        {
            InsertAd pInsert;

            ///插入广告代码
            if (!isHtml)
                pInsert = InsertMailTxtAd;
            else
                pInsert = InsertMailHtmlAd;

            if (AdAllow)
            {
                try
                {
                    string ad = pInsert(body);
                    body = ad;
                }
                catch { }
            }

            return Static.SendSmtpMail(to, title, body, isHtml, ConfigName);
        }

        /// <summary>
        /// 采用用的SMTP配制名称
        /// </summary>
        protected virtual string ConfigName { get { return "SmtpMail"; } }


        #region IHttpHandler 成员

        public virtual bool IsReusable { get { return false; } }

        public abstract void ProcessRequest(HttpContext context);

        #endregion
    }
}
