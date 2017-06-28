namespace Shotgun.Library
{

    using System.IO;
    using System.Text.RegularExpressions;
    using System;
    using System.Data;
    using System.Data.SqlClient;
    using System.Configuration;
    using System.Web;
    using System.Web.UI;
    using System.Net;
    using System.Xml;

    public static partial class Static
    {
        const string SQLConnString = "msSql";


        /// <summary>
        /// 对于直接生成SQL语句里的数据进行安全编码
        /// </summary>
        /// <param name="vData"></param>
        /// <returns></returns>
        public static string SqlEncode(string vData)
        {
            if (string.IsNullOrEmpty(vData))
            { return string.Empty; }

            return vData.Replace("'", "''");
        }


        public static string SqlEncode(string vData, bool AddQut)
        {
            if (string.IsNullOrEmpty(vData))
            { return "''"; }

            return "'" + vData.Replace("'", "''") + "'";

        }

        public static string JsEncode(string vData)
        {
            if (string.IsNullOrEmpty(vData))
                return string.Empty;
            vData = vData.Replace("\\", "\\\\");
            vData = vData.Replace("\'", "\\'");
            vData = vData.Replace("\"", "\\\"");
            vData = vData.Replace("\n", "\\n");
            vData = vData.Replace("\r", "");
            return vData;
        }

        /// <summary>
        /// 指定Web.config中的连接串名称
        /// </summary>
        /// <param name="KeynName"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static SqlConnection GetSqlCon(string KeynName)
        {
            return new SqlConnection(ConfigurationManager.ConnectionStrings[KeynName].ConnectionString);
        }


        /// <summary>
        /// 取得SqlConnection实例
        /// </summary>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static SqlConnection GetSqlCon()
        {
            return new SqlConnection(ConfigurationManager.ConnectionStrings[SQLConnString].ConnectionString);
        }
        /// <summary>
        /// 取得SqlConnection实例
        /// </summary>
        /// <param name="iOpen">是否打开连接</param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static SqlConnection GetSqlCon(bool iOpen)
        {
            SqlConnection con = new SqlConnection(ConfigurationManager.ConnectionStrings[SQLConnString].ConnectionString);
            if (iOpen)
                con.Open();
            return con;
        }

        /// <summary>
        /// 取得Sql语句对就的DataSet实例
        /// </summary>
        /// <param name="sql"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static DataSet GetDataSet(string sql)
        {
            //SqlConnection con = GetSqlCon();
            string ConStr = ConfigurationManager.ConnectionStrings[SQLConnString].ConnectionString;
            SqlDataAdapter da = new SqlDataAdapter(sql, ConStr);
            DataSet ds = new DataSet();
            try
            {
                da.Fill(ds);
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                da.Dispose();
                ds.Dispose();
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            return ds;
        }
        /// <summary>
        /// 取得Sql语句对就的DataSet实例
        /// </summary>
        /// <param name="sql"></param>
        /// <param name="con"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static DataSet GetDataSet(string sql, SqlConnection con)
        {
            DataSet ds = new DataSet();
            SqlDataAdapter Da = new SqlDataAdapter(sql, con);
            try
            {
                Da.Fill(ds);
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ds.Dispose();
                Da.Dispose();
                ErrLogRecorder.SqlError(ex, sql);
                throw ex;
            }

            Da.Dispose();
            return ds;
        }

        /// <summary>
        /// 取得Sql语句对应的DataTable实例
        /// </summary>
        /// <param name="sql"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static DataTable GetDataTable(string sql)
        {
            //SqlConnection con = GetSqlCon();
            string ConStr = ConfigurationManager.ConnectionStrings[SQLConnString].ConnectionString;
            SqlDataAdapter da = new SqlDataAdapter(sql, ConStr);
            DataTable dt = new DataTable();
            try
            {
                da.Fill(dt);
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                dt.Dispose();
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally { da.Dispose(); }
            return dt;

        }

        /// <summary>
        /// 取得Sql语句对应的DataTable实例
        /// </summary>
        /// <param name="sql"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static DataTable GetDataTable(string sql, SqlConnection con)
        {
            DataTable dt = new DataTable();
            SqlDataAdapter Da = new SqlDataAdapter(sql, con);
            try
            {
                Da.Fill(dt);
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                dt.Dispose();
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                Da.Dispose();
            }
            return dt;
        }

        [Obsolete("从Database.DBClass实现")]
        public static DataTable GetDataTable(string sql, SqlTransaction trans)
        {
            DataTable dt = new DataTable();

            SqlDataAdapter Da = new SqlDataAdapter(sql, trans.Connection);
            Da.SelectCommand.Transaction = trans;
            try
            {
                Da.Fill(dt);
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                dt.Dispose();
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                Da.Dispose();
            }
            return dt;
        }


        /// <summary>
        /// 执行SQL语句，返回语句影响的条数
        /// </summary>
        /// <param name="sql"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static int ExecuteNonQuery(string sql)
        {
            SqlConnection con = GetSqlCon(true);
            SqlCommand cmd = new SqlCommand(sql, con);
            int i;
            try
            {
                i = cmd.ExecuteNonQuery();
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                cmd.Dispose();
                con.Close();
                con.Dispose();
            }
            return i;
        }
        /// <summary>
        /// 执行SQL语句，反回语句影响的条数
        /// </summary>
        /// <param name="sql"></param>
        /// <param name="con"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static int ExecuteNonQuery(string sql, SqlConnection con)
        {
            ConnectionState cs = con.State;
            SqlCommand cmd = new SqlCommand(sql, con);
            int i;
            try
            {
                if (cs != ConnectionState.Open)
                    con.Open();
                i = cmd.ExecuteNonQuery();
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                cmd.Dispose();
                if (cs == ConnectionState.Closed)
                    con.Close();
            }
            return i;

        }

        /// <summary>
        ///  执行SQL语句，反回语句影响的条数
        /// </summary>
        /// <param name="uSql"></param>
        /// <param name="Tran"></param>
        [Obsolete("从Database.DBClass实现")]
        public static int ExecuteNonQuery(string sql, SqlTransaction Tran)
        {
            SqlConnection con = Tran.Connection;
            ConnectionState cs = con.State;
            SqlCommand cmd = new SqlCommand(sql, con);
            cmd.Transaction = Tran;
            int i;
            try
            {
                if (cs != ConnectionState.Open)
                    con.Open();
                i = cmd.ExecuteNonQuery();
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                cmd.Dispose();
                if (cs == ConnectionState.Closed)
                    con.Close();
            }
            return i;
        }

        /// <summary>
        /// 执行SQL语句，返回首行首列的数据。
        /// </summary>
        /// <param name="sql"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static object ExecuteScalar(string sql)
        {
            SqlConnection con = GetSqlCon(true);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                return cmd.ExecuteScalar();
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                cmd.Dispose();
                con.Close();
                con.Dispose();
            }
        }
        /// <summary>
        /// 执行SQL语句，返回首行首列的数据。
        /// </summary>
        /// <param name="sql"></param>
        /// <param name="con"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static object ExecuteScalar(string sql, SqlConnection con)
        {
            ConnectionState cs = con.State;
            SqlCommand cmd = new SqlCommand(sql, con);

            try
            {
                if (cs != ConnectionState.Open)
                    con.Open();
                return cmd.ExecuteScalar();
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                cmd.Dispose();
                if (cs == ConnectionState.Closed)
                    con.Close();
            }

        }

        /// <summary>
        /// 执行SQL语句，返回首行首列的数据。
        /// </summary>
        /// <param name="sql"></param>
        /// <param name="con"></param>
        /// <returns></returns>
        [Obsolete("从Database.DBClass实现")]
        public static object ExecuteScalar(string sql, SqlTransaction tran)
        {
            SqlCommand cmd = new SqlCommand(sql, tran.Connection, tran);
            try
            {
                return cmd.ExecuteScalar();
            }
            catch (System.Data.SqlClient.SqlException ex)
            {
                ErrLogRecorder.SqlError(ex, sql);
                throw;
            }
            finally
            {
                cmd.Dispose();
            }
        }

        public static string URLEncodeByGB2312(string url)
        {
            return HttpUtility.UrlEncode(url, System.Text.Encoding.GetEncoding("GB2312"));
        }


        /// <summary>
        /// 向客户端发送alert()脚本
        /// </summary>
        /// <param name="msg"></param>
        public static void alert(string msg)
        {

            msg = JsEncode(msg);

            //System.Web.UI.Page
            System.Web.UI.Page page = (System.Web.UI.Page)HttpContext.Current.CurrentHandler;

            page.ClientScript.RegisterClientScriptBlock(page.GetType(),
                "SGAlert", "alert('" + msg + "');", true);

        }


        /// <summary>
        /// 在客户端显示提示消息后，跳转 
        /// 注意，带Response.End()
        /// </summary>
        /// <param name="url"></param>
        /// <param name="msg"></param>
        public static void ClientRedirect(string url, string msg)
        {
            ClientRedirect(url, msg, true);
        }

        /// <summary>
        /// 在客户端显示提示消息后，跳转 
        /// 注意：此函数将清掉输出缓存。
        /// </summary>
        /// <param name="url"></param>
        /// <param name="EndResponse">是否结束响应</param>
        public static void ClientRedirect(string url, string msg, bool EndResponse)
        {
            string jsmsg = JsEncode(msg);
            HttpResponse Response = HttpContext.Current.Response;
            Response.Cache.SetCacheability(HttpCacheability.NoCache);

            string html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "<meta http-equiv=\"nocache\" content=\"no-cache\">\n" +
                    "<title>{0}</title>\n" +
                    "<meta http-equiv=\"Refresh\" content=\"1;URL={2}\" />\n" +
                    "<script type=\"text/javascript\">\n" +
                    "	function page_load(){{\n" +
                    "		alert('{1}');\n" +
                    "		window.location='{2}';\n" +
                    "	}}\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body onload=\"page_load()\" >\n" +
                    "<h1>{0}</h1>\n" +
                    "</body>\n" +
                    "</html>";
            html = string.Format(html, msg, jsmsg, url);
            Response.ClearContent();
            Response.Write(html);

            if (EndResponse)
                Response.End();
            return;
        }

        /// <summary>
        /// 有客户端提示消息后，使浏览器后退。如果没有后退，则关闭浏览器
        /// </summary>
        /// <param name="msg"></param>
        public static void ClientRedirect(string msg)
        {
            ClientRedirect("javascript:void(history.go(-1),setTimeout(new Function(\\'window.close()\\'),100))", msg, true);
        }

        public static string FormatFileSize(object t)
        {
            int size;
            if (t is int)
                size = (int)t;
            else
                int.TryParse(t.ToString(), out size);

            double s = size / (1024 * 1024.0);
            return string.Format("{0:#,##0.00}MB", s);

        }
        /// <summary>
        /// 将字符串经运算得出一个16位标识字符串
        /// </summary>
        /// <param name="sName"></param>
        /// <returns></returns>
        public static string StrId(string sName)
        {
            //byte[] bin=System.Text.ASCIIEncoding.Default.GetBytes(sName);
            //int sum=0;
            //foreach(byte b in bin){
            //    sum += b;
            //    sum &= 0x7FffFFff;
            //}
            return System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(sName, "md5").Substring(8, 16);

            ///return string.Format("{0:x1}{1:x8}", sName.Length, sum);

        }

        /// <summary>
        /// 取得客户端IP地址，能识别CDN访问
        /// </summary>
        /// <returns></returns>
        public static string GetUserHostAddress()
        {
            string _ip = HttpContext.Current.Request.ServerVariables["HTTP_CDN_SRC_IP"];
            if (string.IsNullOrEmpty(_ip))
                _ip = HttpContext.Current.Request.UserHostAddress;
            return _ip;
        }

        /// <summary>
        /// 将可能为DBNull的数据，转为Int32
        /// </summary>
        /// <param name="d"></param>
        /// <returns></returns>
        public static int DBToInt32(object d)
        {
            if (d == null || d == DBNull.Value)
                return 0;
            if (d is int)
                return (int)d;
            if (d is long)
                return (int)(long)d;
            if (d is string)
            {
                int s;
                int.TryParse((string)d, out s);
                return s;
            }
            return Convert.ToInt32(d);
        }
    }

    public class WebHtmlDownloader
    {
        string _url;
        int _timeOut;
        string _postdata;
        System.Text.Encoding _Encoder;


        #region 构造函数
        public WebHtmlDownloader()
        {
            _url = string.Empty;
            _timeOut = 60000;
        }

        public WebHtmlDownloader(string url)
        {
            this.url = url;
            _timeOut = 60000;
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="url"></param>
        /// <param name="Timeout">单位:秒</param>
        public WebHtmlDownloader(string url, int Timeout)
        {

            this.url = url;
            _timeOut = Timeout * 1000;
        }

        #endregion

        public string url
        {
            get { return _url; }
            set
            {
                if (value.StartsWith("http:", StringComparison.OrdinalIgnoreCase))
                {
                    _url = value;
                    return;
                }
                string server;
                HttpRequest Request = HttpContext.Current.Request;
                server = @"http://" + Request.ServerVariables["SERVER_NAME"] + ":" + Request.ServerVariables["SERVER_PORT"];

                string offset = string.Empty;
                if (value.Substring(0, 1) != @"/")
                {
                    offset = Request.Path;
                    int i = offset.LastIndexOf("/");
                    offset = offset.Substring(0, i + 1);
                }
                _url = server + offset + value;

            }
        }

        /// <summary>
        /// 请求超时(单位:秒)
        /// </summary>
        public int TimeOut
        {
            get { return _timeOut / 1000; }
            set { _timeOut = value * 1000; }
        }

        /// <summary>
        ///将要post到服务器的数据
        /// 值为null时，用get方法
        /// </summary>
        public string PostData
        {
            get { return _postdata; }
            set { _postdata = value; }
        }

        /// <summary>
        /// 指定post数据的编码方式
        /// </summary>
        public System.Text.Encoding Encode
        {
            get { return _Encoder; }
            set { _Encoder = value; }
        }


        protected byte[] DownloadCore()
        {
            WebRequest Web;
            WebResponse Rsp;
            System.IO.Stream Stm;

            Web = WebRequest.Create(_url);

            HttpRequest Request = HttpContext.Current.Request;

            string cookies = Request.Headers["Cookie"];
            Web.Headers.Add(HttpRequestHeader.Cookie, cookies);
            ((HttpWebRequest)Web).UserAgent = @"Mozilla/4.0 (compatible; Shotgun Webdownloader 1.0; Windows NT 5.1; )";

            if (_postdata != null)
            {//以post方式提交
                Web.Method = "POST";
                if (_Encoder == null)
                    _Encoder = System.Text.ASCIIEncoding.UTF8;
                ((HttpWebRequest)Web).ServicePoint.Expect100Continue = false;
                //Web.Headers[HttpRequestHeader.ContentLength] = _Encoder.EncodingName;
                Web.Headers[HttpRequestHeader.ContentEncoding] = _Encoder.WebName;
                ((HttpWebRequest)Web).ContentType = @"application/x-www-form-urlencoded";
                StreamWriter pdStm = new StreamWriter(Web.GetRequestStream(), _Encoder);
                pdStm.Write(_postdata);
                pdStm.Close();
                pdStm.Dispose();
            }

            //Web.Timeout = _timeOut;

            try
            {
                Rsp = Web.GetResponse();
            }
            catch (Exception ex)
            {
                if (Request.IsLocal)
                    throw ex;
                return null;
            }

            int MaxLength, RecvicedLength, r;
            MaxLength = (int)Rsp.ContentLength;
            RecvicedLength = 0;
            byte[] bin = new byte[MaxLength];

            Stm = Rsp.GetResponseStream();

            do
            {
                r = Stm.Read(bin, RecvicedLength, MaxLength - RecvicedLength);
                RecvicedLength += r;
            }
            while (RecvicedLength < MaxLength);
            Stm.Close();
            Stm.Dispose();
            Rsp.Close();
            return bin;
        }

        #region 对外方法

        public string Download(System.Text.Encoding ec)
        {
            byte[] bin = DownloadCore();
            return ec.GetString(bin, 0, bin.Length);
        }

        public string Download()
        {
            return System.Text.ASCIIEncoding.Default.GetString(DownloadCore());
        }

        public System.Xml.XmlDocument DownloadXml()
        {
            byte[] bin = DownloadCore();

            System.IO.MemoryStream mStm = new System.IO.MemoryStream();
            mStm.Write(bin, 0, bin.Length);
            bin = null;
            mStm.Position = 0;
            System.Xml.XmlDocument xml = new System.Xml.XmlDocument();
            try
            {
                xml.Load(mStm);
            }
            catch (Exception ex)
            {
                mStm.Close();
                mStm.Dispose();
                if (HttpContext.Current.Request.IsLocal)
                    throw ex;
                return xml;
            }

            mStm.Close();
            mStm.Dispose();
            return xml;
        }

        public void DownloadToFile(string File)
        {
            byte[] bin = DownloadCore();
            System.IO.FileStream fStm = new System.IO.FileStream(File, System.IO.FileMode.OpenOrCreate, System.IO.FileAccess.Write);
            fStm.SetLength(0);
            fStm.Write(bin, 0, bin.Length);
            bin = null;
            fStm.Close();
            fStm.Dispose();
        }

        #endregion
    }

    /// <summary>
    ///  Ajax响应状态
    /// </summary>
    public enum emAjaxResponseState
    {
        /// <summary>
        /// 标记是成功的响应
        /// </summary>
        ok,
        /// <summary>
        /// 标记是错误的响应
        /// </summary>
        error,
        /// <summary>
        /// 未知指令
        /// </summary>
        unknow,
        /// <summary>
        /// 处理失败
        /// </summary>
        fail,
        /// <summary>
        /// 自定义的状态,通过CustomState设置
        /// </summary>
        custom

    }


    /// <summary>
    /// 简单的ajax响应类
    /// 适用于simpleXMlParse()解释执行
    /// </summary>
    public class simpleAjaxResponser
    {
        HttpResponse Response = (HttpResponse)HttpContext.Current.Response;
        //Page page = (Page)HttpContext.Current.Handler;

        XmlDocument xml;
        emAjaxResponseState _state;
        string _customState = null;


        /// <summary>

        XmlElement _ExtInfo = null;


        //protected string[] _datArray = null;

        public simpleAjaxResponser()
        {
            xml = new XmlDocument();
            xml.LoadXml("<shotgun version=\"v1.0 2008.12.30\" name=\"simpleAjaxResponser\"/>");
            _stateEL = (XmlElement)xml.CreateElement("state");
            xml.DocumentElement.AppendChild(_stateEL);
            _msgEl = xml.CreateElement("message");
            xml.DocumentElement.AppendChild(_msgEl);
            this.state = emAjaxResponseState.ok;
        }

        XmlElement _stateEL;
        public emAjaxResponseState state
        {
            get { return _state; }
            set
            {
                _state = value;
                if (value != emAjaxResponseState.custom)
                    _stateEL.InnerText = value.ToString();//非自定义状态
            }
        }

        public XmlElement ExtInfo()
        {
            if (_ExtInfo == null)
            {
                _ExtInfo = xml.CreateElement("ExtInfo");
                xml.DocumentElement.AppendChild(_ExtInfo);
            }
            return _ExtInfo;
        }

        XmlElement _msgEl;
        /// <summary>
        /// 传到客户端的消息主体
        /// </summary>
        public string message
        {
            get { return _msgEl.InnerText; }
            set { _msgEl.InnerText = value; }
        }

        public string CustomState
        {
            get
            {
                if (string.IsNullOrEmpty(_customState))
                    return _state.ToString();
                else
                    return _customState;
            }
            set
            {
                _customState = value;
                if (string.IsNullOrEmpty(value))
                    this.state = emAjaxResponseState.ok;
                else
                {
                    _state = emAjaxResponseState.custom;
                    _stateEL.InnerText = value;
                }

            }
        }

        /// <summary>
        /// 发送ajax请的数据到客户端，并停止Response
        /// </summary>
        public void SendResponse()
        {
            SendResponse(true);
        }

        /// <summary>
        /// 发送ajax请的数据到客户端
        /// </summary>
        public void SendResponse(bool isEnd)
        {
            Response.ClearContent();
            Response.ContentType = "text/xml";
            xml.Save(Response.OutputStream);
            if (isEnd)
                Response.End();
        }


        XmlElement _strsEl;
        /// <summary>
        /// 要传到客户端的string 一维数组
        /// </summary>
        public string[] StringArray
        {
            //get { return _datArray; }
            set
            {
                if (_strsEl == null)
                {
                    if (value == null || value.Length == 0)
                        return;
                    _strsEl = xml.CreateElement("DataArray");
                    xml.DocumentElement.AppendChild(_strsEl);
                }
                else if (value == null || value.Length == 0)
                {
                    _strsEl.ParentNode.RemoveChild(_strsEl);
                    _strsEl = null;
                    return;
                }


                XmlElement el;

                int i;
                for (i = 0; i < value.Length; i++)
                {
                    el = xml.CreateElement("String");
                    el.InnerText = value[i];
                    _strsEl.AppendChild(el);
                }
                XmlAttribute attr = _strsEl.Attributes["count"];
                if (attr == null)
                {
                    attr = xml.CreateAttribute("count");
                    _strsEl.Attributes.Append(attr);
                }
                attr.Value = i.ToString();

            }
        }

        public override string ToString()
        {
            return xml.OuterXml;
        }
    }







}