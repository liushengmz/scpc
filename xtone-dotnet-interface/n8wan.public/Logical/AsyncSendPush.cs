using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 用于异步的数据推送
    /// </summary>
    public class AsyncSendPush
    {
        private HttpWebRequest _web;

        public Uri url { get; set; }
        /// <summary>
        /// 日志文件，绝对地址
        /// </summary>
        public string logFile { set; get; }

        public void SendPushInfo()
        {
            _web = (HttpWebRequest)WebRequest.Create(url);
            _web.ReadWriteTimeout = 888;// *1000;//5秒超时

            _web.AllowAutoRedirect = false;
            _web.AutomaticDecompression = System.Net.DecompressionMethods.GZip;

            var rsp = (HttpWebResponse)_web.GetResponse();
            RspProc(rsp);
        }

        private void RspProc(HttpWebResponse rsp)
        {
            string msg = null;

            if (rsp == null)
            {
                WriteLog(0, msg);
                return;
            }
            var code = rsp.StatusCode;
            try
            {
                using (var stm = rsp.GetResponseStream())
                {
                    using (var rd = new System.IO.StreamReader(stm))
                        msg = rd.ReadLine();
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
            if (!string.IsNullOrEmpty(msg) && msg.Length > 512)
                msg = msg.Substring(0, 510) + "...";

            WriteLog((int)code, msg);
        }



        private void GetResponseProc(IAsyncResult ar)
        {
            Console.WriteLine("回调了");
            if (!ar.IsCompleted)
                return;
            System.Net.HttpWebResponse rsp = null;
            try
            {
                rsp = (System.Net.HttpWebResponse)_web.EndGetResponse(ar);
            }
            catch (System.Net.WebException ex)
            {
                rsp = (System.Net.HttpWebResponse)ex.Response;

            }
            RspProc(rsp);


        }

        private void WriteLog(int p, string msg)
        {
            Console.WriteLine("{0},{1}", p, msg);
        }
    }
}
