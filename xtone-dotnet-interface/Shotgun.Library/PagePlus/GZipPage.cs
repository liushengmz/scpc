using System;
using System.Collections.Generic;
using System.Text;
using System.Web;
using System.IO.Compression;
using System.IO;
using System.Web.UI;

/*实现GZip压缩相关的方法*/

namespace Shotgun.PagePlus
{

    public partial class ShotgunPage : System.Web.UI.Page, IGZipPage
    {

        private int _support;
        private bool _allow;

        #region IGZipPage 成员

        /// <summary>
        /// 客户端是否支持GZip压缩
        /// </summary>
        public bool IsSupport
        {
            get
            {
                if (_support != 0)
                    return _support == 1;
                string h = Request.Headers["Accept-Encoding"];

                if (string.IsNullOrEmpty(h))
                    _support = -1;
                _support = h.ToLower().IndexOf("gzip") != -1 ? 1 : -1;

                return _support == 1;
            }
        }

        /// <summary>
        /// 允许压缩传送
        /// </summary>
        public bool AllowCompress
        {
            get
            {
                return _allow;
            }
            set
            {
                _allow = value;
            }
        }

        #endregion

        protected override void Render(System.Web.UI.HtmlTextWriter writer)
        {

            if (!AllowCompress)
            {
                base.Render(writer);
                return;
            }



            TextWriter old = writer.InnerWriter;
            StringWriter sw = new StringWriter();

            writer.InnerWriter = sw;

            //Html32TextWriter htw = new Html32TextWriter(sw);
            base.Render(writer);

            Response.ClearContent();
            string ct = Response.ContentType;
            if (string.IsNullOrEmpty(ct))
                Response.ContentType = "charset=" + old.Encoding.BodyName;
            else
            {
                if (ct.IndexOf("charset", StringComparison.OrdinalIgnoreCase) == -1)
                {
                    Response.ContentType += "; charset=" + old.Encoding.BodyName;
                }
            }



            HttpContext.Current.Response.AddHeader("Content-encoding", "gzip");
            byte[] bin = old.Encoding.GetBytes(sw.ToString());
            GZipStream GZip = new GZipStream(Response.OutputStream, CompressionMode.Compress);
            GZip.Write(bin, 0, bin.Length);
            GZip.Close();
            GZip.Dispose();
            sw.Close();
            sw.Dispose();

        }

    }
}
