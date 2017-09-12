using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using Shotgun.Database;
using Shotgun.Model.Logical;

namespace FlowLibraryNet.Logical
{
    public abstract class FlowBaseHandler : Shotgun.PagePlus.SimpleHttpHandler, Shotgun.Model.Logical.ILogical
    {
        static Dictionary<string, object> logLocker = new Dictionary<string, object>();
        StringBuilder _sbLog;


        #region 日志服务


        /// <summary>
        /// 写入日志文件
        /// </summary>
        protected virtual void FlushLog()
        {
            if (_sbLog == null || _sbLog.Length == 0)
                return;
            string FileName = string.Format("~/splog/{0:yyyyMMdd}/{1}.log", DateTime.Now, LogName);
            var fi = new FileInfo(Request.MapPath(FileName));
            if (!fi.Directory.Exists)
                fi.Directory.Create();
            _sbLog.AppendLine();

            FlushLog(fi, _sbLog.ToString());

            _sbLog.Clear();
        }
        protected virtual void FlushLog(FileInfo logFile, string logContent)
        {
            object Locker = null;

            lock (logLocker)
            {//写入日志IO 操作相对较慢，多对像锁可减少对不同文件写入等待时间
                if (!logLocker.ContainsKey(logFile.Name))
                    logLocker[logFile.Name] = Locker = new object();
                else
                    Locker = logLocker[logFile.Name];
            }

            lock (Locker)
            {
                StreamWriter stm = null;
                try
                {
                    stm = new StreamWriter(logFile.FullName, true);
                    stm.WriteLine(_sbLog.ToString());
                }
                catch { }
                finally { if (stm != null) stm.Dispose(); }

            }

        }




        public void WriteLog(string msg, params object[] args)
        {
            WriteLog(string.Format(msg, args));
        }


        protected void WriteLog(string msg)
        {
            if (string.IsNullOrEmpty(msg))
                return;
            if (_sbLog == null)
                _sbLog = new StringBuilder();
            else if (_sbLog.Length != 0)
                _sbLog.AppendLine();

            if (msg.Length > 2048)
            {
                msg = msg.Substring(0, 2045) + "...";
            }
            _sbLog.AppendFormat("{0:HH:mm:ss.fff} {1}", DateTime.Now, msg);
        }


        protected virtual string LogName
        {
            get
            {
                var file = Request.Url.AbsolutePath.Replace('\\', '/');
                int i = file.LastIndexOf('/');
                if (i != -1)
                    file = file.Substring(i + 1);
                i = file.LastIndexOf(".");
                if (i != -1)
                    file = file.Substring(0, i);
                return file;
            }
        }

        #endregion


        int Shotgun.Model.Logical.ILogical.lastUpdateCount { get { throw new NotImplementedException(); } }

        Shotgun.Database.IBaseDataClass2 Shotgun.Model.Logical.ILogical.dBase { get { return this.dBase; } set { throw new NotImplementedException(); } }

        public string ErrorMesage { get; protected set; }

        public bool IsSuccess { get; protected set; }

        #region 远程HTML获取

        public CookieContainer Cookies { get; set; }


        /// <summary>
        /// 当发生错误时，将通过此方法,返回（错误）页面内容
        /// </summary>
        /// <param name="srcUrl"></param>
        /// <param name="postdata"></param>
        /// <param name="ex"></param>
        /// <returns></returns>
        protected virtual string OnWebException(string srcUrl, string postdata, WebException ex)
        {
            return null;
        }

        /// <summary>
        /// 下载远程代码,带日志
        /// </summary>
        /// <param name="url">目标网址</param>
        /// <param name="timeout">超时时间,单位为毫秒,默认3秒</param>
        /// <param name="encode">编码方式,utf8/gb2312等,默认utf8</param>
        /// <returns></returns>
        protected string GetHTML(string url, int timeout, string encode)
        {
            return DownloadHTML(url, null, timeout, encode, (IDictionary<string, string>)null);
        }

        /// <summary>
        /// 下载远程代码 utf8/3秒超时 ,带日志
        /// </summary>
        /// <param name="url">目标网址</param>
        /// <param name="timeout">超时时间,单位为毫秒,默认3秒</param>
        /// <param name="encode">编码方式,utf8/gb2312等,默认utf8</param>
        /// <returns></returns>
        protected string GetHTML(string url)
        {
            return DownloadHTML(url, null, 0, null, (IDictionary<string, string>)null);
        }

        protected string GetHTML(string url, IEnumerable<KeyValuePair<string, string>> keyValues, int timeout, string encode)
        {
            Encoding enc = null;
            if (string.IsNullOrEmpty(encode))
                enc = System.Text.ASCIIEncoding.UTF8;
            else
                enc = System.Text.ASCIIEncoding.GetEncoding(encode);
            var sb = new StringBuilder(url);
            if (url.Contains("?"))
                sb.Append("&");
            else
                sb.Append("?");

            foreach (var kv in keyValues)
            {
                sb.AppendFormat("{0}={1}&", System.Web.HttpUtility.UrlEncode(kv.Key, enc), System.Web.HttpUtility.UrlEncode(kv.Value, enc));
            }
            sb.Length--;

            return GetHTML(sb.ToString(), timeout, encode);
        }



        protected string PostHTML(string url, IEnumerable<KeyValuePair<string, string>> keyValues, int timeout, string encode)
        {
            Encoding enc = null;
            if (string.IsNullOrEmpty(encode))
                enc = System.Text.ASCIIEncoding.UTF8;
            else
                enc = System.Text.ASCIIEncoding.GetEncoding(encode);
            var sb = new StringBuilder();

            foreach (var kv in keyValues)
            {
                sb.AppendFormat("{0}={1}&", System.Web.HttpUtility.UrlEncode(kv.Key, enc), System.Web.HttpUtility.UrlEncode(kv.Value, enc));
            }
            sb.Length--;
            return PostHTML(url, sb.ToString(), timeout, encode);
        }

        /// <summary>
        /// 下载远程代码,带日志,contentType=application/x-www-form-urlencoded
        /// </summary>
        /// <param name="url">目标网址</param>
        /// <param name="data">传送的字符串</param>
        /// <param name="timeout">超时时间,单位为毫秒,默认3秒</param>
        /// <param name="encode">编码方式,utf8/gb2312等,默认utf8</param>
        /// <returns></returns>
        protected string PostHTML(string url, string data, int timeout, string encode)
        {
            if (string.IsNullOrEmpty(encode))
            {
                return DownloadHTML(url, data ?? string.Empty, timeout, encode, "application/x-www-form-urlencoded");
            }
            return DownloadHTML(url, data ?? string.Empty, timeout, encode, "application/x-www-form-urlencoded; charset=" + encode);
        }


        /// <summary>
        /// 下载远程代码 utf8/3秒超时,带日志
        ///</summary>
        protected string PostHTML(string url, string data)
        {
            return DownloadHTML(url, data ?? string.Empty, 0, null, "application/x-www-form-urlencoded; charset=UTF-8");
        }

        /// <summary>
        /// 下载远程代码,带日志
        /// </summary>
        /// <param name="url">目标网址</param>
        /// <param name="postdata">传送的字符串，传入null时，将采用GET方法访问url</param>
        /// <param name="timeout">超时时间,单位为毫秒,默认3000毫秒</param>
        /// <param name="encode">可为空，默认为utf8</param>
        /// <param name="contentType">HTTP报文头，可为空</param>
        /// <returns></returns>
        protected string DownloadHTML(string url, string postdata, int timeout, string encode, string contentType)
        {
            Dictionary<string, string> heads = null;
            if (!string.IsNullOrEmpty(contentType))
            {
                heads = new Dictionary<string, string>();
                heads.Add("Content-Type", contentType);
            }
            return DownloadHTML(url, postdata, timeout, encode, heads);
        }

        /// <summary>
        /// 下载远程代码,带日志
        /// </summary>
        /// <param name="url">目标网址</param>
        /// <param name="postdata">传送的字符串，传入null时，将采用GET方法访问url</param>
        /// <param name="timeout">超时时间,单位为毫秒,默认3000毫秒</param>
        /// <param name="encode">可为空，默认为utf8</param>
        /// <param name="Heads">HTTP报文头，可为空</param>
        /// <returns></returns>
        protected virtual string DownloadHTML(string url, string postdata, int timeout, string encode, IDictionary<string, string> Heads)
        {
            Stopwatch st = new Stopwatch();
            st.Start();
            string html = null;
            try
            {
                WriteLog(url);
                if (!String.IsNullOrEmpty(postdata))
                    WriteLog(postdata);
                html = n8wan.Public.Library.DownloadHTML(url, postdata, encode, e =>
                {
                    if (timeout > 0)
                    {
                        if (timeout < 100)
                            e.Timeout = timeout * 1000;
                        else
                            e.Timeout = timeout;
                    }
                    if (Cookies != null)
                        e.CookieContainer = Cookies;
                    if (Heads != null)
                    {
                        foreach (var head in Heads)
                        {
                            switch (head.Key.ToLower())
                            {
                                case "content-type": e.ContentType = head.Value; break;
                                case "content-length": break;
                                case "user-agent": e.UserAgent = head.Value; break;
                                default: e.Headers[head.Key] = head.Value; break;
                            }
                        }
                    }
                });
                return html;
            }
            catch (Exception ex)
            {
                WriteLog(ex.Message);
                if (ex is WebException)
                    return html = OnWebException(url, postdata, (WebException)ex);
                return null;
            }
            finally
            {
                st.Stop();
                WriteLog(string.Format("+{0}ms {1}", st.ElapsedMilliseconds, html));
            }

        }

        #endregion


    }
}
