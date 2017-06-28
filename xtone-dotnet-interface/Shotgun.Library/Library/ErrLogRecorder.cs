using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.IO;
using System.Reflection;
using System.Web;
using System.Data;
using System.Data.SqlClient;
using System.Data.Common;


namespace Shotgun.Library
{
    public class ErrLogRecorder
    {
        public static void SqlError(System.Data.Common.DbException ex)
        {
            SqlError(ex, string.Empty);
        }
        public static void SqlError(System.Data.Common.DbException ex, string TSql)
        {
            bool isLocal = false;
            using (EventLog log = new EventLog("WebException"))
            {

                string url, method;
                string extData;
                if (System.Web.HttpContext.Current != null)
                {
                    HttpRequest Request = System.Web.HttpContext.Current.Request;
                    url = Request.Url.ToString();
                    method = Request.HttpMethod;
                    isLocal = Request.IsLocal;
                    string rp;
                    if (Request.UrlReferrer == null)
                        rp = "none";
                    else
                    {
                        rp = Request.UrlReferrer.ToString();
                        if (rp.Length > 255)
                            rp = rp.Substring(0, 255) + "<...>";
                    }
                    extData = "\r\nReferrer:" + rp;
                    extData += "\r\nUserAgent:" + Request.UserAgent;
                    if (extData.Length > 512)
                        extData = extData.Substring(0, 512) + "<...>";
                }
                else
                {
                    url = string.Empty;
                    method = "n/a";
                    extData = string.Empty;
                }
                log.Source = "Sql";

                StringBuilder msg = new StringBuilder();
                msg.AppendFormat("url:{0} {1}", method, url);
                msg.AppendLine(extData);
                if (!string.IsNullOrEmpty(TSql))
                {
                    msg.AppendLine("Sql:");
                    msg.AppendLine(TSql);
                }

                //msg.AppendLine(ex.Message);
                if (ex is SqlException)
                {
                    SqlException msEx = (SqlException)ex;
                    for (int i = 0; i < msEx.Errors.Count; i++)
                    {
                        msg.AppendLine("Message: " + msEx.Errors[i].Message);
                        msg.AppendLine("LineNumber: " + msEx.Errors[i].LineNumber);
                        msg.AppendLine("Source: " + msEx.Errors[i].Source);
                        msg.AppendLine("Procedure: " + msEx.Errors[i].Procedure);
                    }
                }
                else
                {
                    msg.AppendLine("Message:" + ex.Message);
                    msg.AppendLine("ErrorCode:" + ex.ErrorCode);

                }



                StackTrace s = new StackTrace(true);
                for (int i = 1; i < s.FrameCount; i++)
                {
                    StackFrame t = s.GetFrame(i);
                    MethodBase mb = s.GetFrame(i).GetMethod();
                    System.Reflection.Module m = mb.Module;
                    int line = t.GetFileLineNumber();

                    if (mb.Name != "SqlError" && !m.ScopeName.StartsWith("System"))
                    {
                        msg.AppendLine(mb.Name + "() " + t.GetFileName() + "  行:" + t.GetFileLineNumber());
                    }
                }
                //msg.AppendLine(ex.ToString());

                byte[] bin = null;
                if (method == "POST")
                {
                    Stream stm = System.Web.HttpContext.Current.Request.InputStream;
                    if (stm.Length > 5120)
                        bin = System.Text.ASCIIEncoding.ASCII.GetBytes("*data length:" + stm.Length.ToString());
                    else
                    {
                        long p = stm.Position;
                        bin = new byte[stm.Length];
                        stm.Seek(0, SeekOrigin.Begin);
                        stm.Read(bin, 0, bin.Length);
                        stm.Position = p;
                    }
                }

                try
                {

                    if (bin != null)
                        log.WriteEntry(msg.ToString(), EventLogEntryType.Error, 0, 0, bin);
                    else
                        log.WriteEntry(msg.ToString(), EventLogEntryType.Error);
                }
                catch
                {
                    if (isLocal)
                        throw;
                }
                log.Close();
            }
        }
        public static void SqlError(System.Data.Common.DbException ex, string TSql, IDataParameterCollection Parameters)
        {
            if (Parameters == null || Parameters.Count == 0)
                SqlError(ex, TSql);
            string ptr = string.Empty;
            foreach (IDataParameter p in Parameters)
            {
                ptr += string.Format("declare {0} {2}; set {0} ='{1}'\r\n", p.ParameterName, p.Value, p.DbType);
            }
            ptr += "-----auto code------\r\n";
            ptr += TSql;
            SqlError(ex, ptr);

        }
        public static void SqlError(System.Data.Common.DbException ex, IDbCommand cmd)
        {
            SqlError(ex, cmd.CommandText, cmd.Parameters);
        }
        static void LogContextError(string logName, string errMsg)
        {


            if (!EventLog.SourceExists(logName))
                EventLog.CreateEventSource(logName, "WebSqlError");  //写入应用程序日志
            EventLog.WriteEntry(logName, errMsg);
        }

    }
}
