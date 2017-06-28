using System;
using System.Collections.Generic;
using System.Text;
using System.Web.Caching;
using System.IO;
using System.Web;

namespace Shotgun.Library.Mail
{
    public class WebMail: BaseMailHandler
    {


        public WebMail()
        {
            Server = HttpContext.Current.Server;
            string ec= Request.QueryString["encode"];
            if (!string.IsNullOrEmpty(ec))
                Request.ContentEncoding = ASCIIEncoding.GetEncoding(ec);
        }

        protected override string MailTemplete
        {
            get { throw new NotImplementedException(); }
        }

        public override void ProcessRequest(System.Web.HttpContext context)
        {
            this.OutputFormnat = Request["outfmt"];

            string body = Request["body"];
            string title = Request["title"];
            string to = Request["to"];
            bool isHtml = Request["html"] == "1";
            
            if (SendMail(to, title, body, isHtml))
                base.MsgOutput("发送成功！", true);
            else
                base.MsgOutput("发送失败！",false);
            
        }
    }
}
