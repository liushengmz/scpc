using LightDataModel;
using Newtonsoft.Json.Linq;
using sdk_Request.Model;
using Shotgun.Model.Filter;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace sdk_Request.Logical
{
    public abstract class APIRequestGet : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>, Shotgun.Model.Logical.ILogical
    {
        private sdk_Request.Model.APIRquestModel _aqm;
        ///// <summary>
        ///// sp通道业务
        ///// </summary>
        //private LightDataModel.tbl_sp_troneItem _trone;
        private string _errMsg;
        private API_ERROR _errCode;
        private LightDataModel.tbl_trone_paycodeItem _paymodel;
        private StringBuilder _sbLog;

        internal protected int Step = 0;

        /// <summary>
        ///写日志的文件锁对像
        /// </summary>
        private static object logLocker = new object();

        public sealed override void BeginProcess()
        {
            Response.ContentType = "text/plain";
            //var ticks = new System.Diagnostics.Stopwatch();
            //ticks.Start();
            if (!OnInit())
            {
                WriteError();
                return;
            }

            Model.SP_RESULT result = null;
            String step = Request["step"];
            Func<Model.SP_RESULT> func = null;
            if (step == null || step != "2")
            {
                Step = 1;
                func = GetSpCmd;
            }
            else
            {
                Step = 2;
                func = GetSpCmdStep2;
            }

            try
            {
                result = func();
            }
            catch (WebException webex)
            {
                SetError(API_ERROR.INNER_ERROR, webex.Message);
                WriteLog(webex.ToString());
            }
#if !DEBUG
            catch (Exception ex)
            {
                WriteLog(ex.ToString());
                SetError(API_ERROR.INNER_ERROR, "内部错误");
            }
#endif
            finally { }

            if (result == null)
            {
                WriteError();
            }
            else
            {
                if (_aqm == null)
                {
                    Response.Write("{}");
                    return;
                }
                ResultStatusMap(result);
                var json = new Model.APIResponseModel(result, _aqm);
                _aqm.status = ResultStatusMap(result);

                Response.Write(json.ToJson());
            }
            FlushLog();


        }

        /// <summary>
        /// 错误码映射
        /// </summary>
        /// <param name="json"></param>
        private API_ERROR ResultStatusMap(SP_RESULT rlt)
        {
            if (rlt == null || Step < 2)
                return rlt.status;

            if (rlt.status == API_ERROR.STEP2_OK)
                return rlt.status;

            if (rlt.status == API_ERROR.OK)
                return API_ERROR.STEP2_OK;

            if ((int)rlt.status >= 2000)
                return rlt.status;

            return rlt.status + 1000;
        }

        //protected virtual Model.SP_RESULT GetSpCmd()
        //{
        //    if (PayModel == null)
        //    {
        //        SetError(API_ERROR.INNER_CONFIG_ERROR, "PayModel未配置");
        //        return null;
        //    }
        //    var url = "http://218.242.153.106:4000/mgdmv1a/cert?ppid=" + PayModel.paycode + "&imei=357513056367783&imsi=460028602777674";
        //    WriteLog(url);
        //    using (var wc = new WebClient())
        //    {
        //        wc.Encoding = ASCIIEncoding.UTF8;
        //        var html = wc.DownloadString(url);
        //        WriteLog(html);
        //        var ar = html.Split(new string[] { "##" }, StringSplitOptions.RemoveEmptyEntries);
        //        if (ar[0] != "100")
        //        {
        //            SetError(API_ERROR.UNKONW_ERROR, ar[0]);
        //            return null;
        //        }

        //        var result = new Model.SP_SMS_Result();
        //        result.port = "1065842230";
        //        result.msg = ar[1];
        //        result.SMSType = E_SMS_TYPE.Data;
        //        _aqm.spLinkId = ar[2];
        //        result.status = API_ERROR.OK;
        //        return result;
        //    }

        //}

        protected abstract Model.SP_RESULT GetSpCmd();

        bool OnInit()
        {
            if (!InitOrder())
                return SetError("初始化错误");
            return true;
        }

        /// <summary>
        /// 计费点
        /// </summary>
        protected LightDataModel.tbl_trone_paycodeItem PayModel
        {
            get
            {
                if (_paymodel != null)
                    return _paymodel;

                _paymodel = LightDataModel.tbl_trone_paycodeItem.QueryPayCodeByTroneId(dBase, OrderInfo.troneId);


                //var l = LightDataModel.tbl_trone_paycodeItem.GetQueries(dBase);
                //if (OrderInfo.troneId > 0)
                //    l.Filter.AndFilters.Add(LightDataModel.tbl_trone_paycodeItem.Fields.trone_id, OrderInfo.troneId);
                //else
                //{
                //    var tf = new TableFilter(tbl_trone_paycodeItem.Fields.trone_id, tbl_trone_orderItem.tableName, tbl_trone_orderItem.Fields.trone_id);
                //    l.Filter.AndFilters.Add(tf);
                //    tf.TableFilters.AndFilters.Add(tbl_trone_orderItem.Fields.id, _aqm.tbl_trone_order_id);
                //}
                //_paymodel = l.GetRowByFilters();

                if (_paymodel == null)
                {
                    //throw new Exception("paycode 获取失败，请检查paycode是否有配置");
                    WriteLog("paycode 获取失败，请检查paycode是否有配置");
                    //WriteLog(l.LastSqlExecute);
                }
                return _paymodel;
            }
        }

        /// <summary>
        /// 渠道的第二次请求
        /// </summary>
        /// <returns></returns>
        protected virtual Model.SP_RESULT GetSpCmdStep2()
        {
            SetError(API_ERROR.INNER_CONFIG_ERROR, "请重写“GetSpCmdStep2”");
            return null;
        }

        private bool InitOrder()
        {
#if DEBUG
            if (Request.HttpMethod.Equals("GET"))
            {
                _aqm = new Model.APIRquestModel()
                {
                    cid = 123,
                    imei = "866568022922909",
                    imsi = "460023192787105",
                    iccid = "898600161315F1003574",
                    lac = 456,
                    mobile = "13570830935",
                    clientIp = "113.113.113.113",
                    id = (0x40000000 | (int)((DateTime.Now.Ticks / 100000) & 0x7FffFFFF)) * -1
                };
                return true;
            }
#endif
            if (Request.TotalBytes < 10)
                return false;

            var bin = new byte[Request.TotalBytes];
            Request.InputStream.Read(bin, 0, Request.TotalBytes);
            WriteLog("Request:" + ASCIIEncoding.UTF8.GetString(bin));
            Request.InputStream.Seek(0, SeekOrigin.Begin);

            _aqm = Shotgun.Library.Static.JsonParser<sdk_Request.Model.APIRquestModel>(Request.InputStream);


            return true;
        }

        //bool CheckOrder()
        //{
        //    _trone = LightDataModel.tbl_sp_troneItem.GetRowById(dBase, _aqm.spTroneId);
        //    if (_trone == null)
        //        return SetError(API_ERROR.TRONE_NOT_FOUND);

        //    var fields = _trone.api_fields.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);

        //    string err = string.Empty;
        //    foreach (var f in fields)
        //    {
        //        #region 字段检查
        //        switch (f.ToLower())
        //        {
        //            case "imsi":
        //            case "imei":
        //            case "mobile":
        //            case "lac":
        //            case "cid":
        //            case "":
        //            default:
        //                break;
        //        }
        //        #endregion
        //    }
        //    if (string.IsNullOrEmpty(err))
        //    {
        //        SetError(API_ERROR.FIELD_MISS, err.Substring(1));
        //        return false;
        //    }


        //    return true;
        //}

        protected APIRquestModel OrderInfo { get { return _aqm; } }

        #region 基础服务
        public bool SetError(API_ERROR Error)
        {
            SetError(Error, Error.ToString());
            return false;
        }
        public bool SetError(string Msg)
        {
            SetError(API_ERROR.GET_CMD_FAIL, Msg);
            return false;
        }

        public bool SetError(JToken jtk)
        {
            SetError(API_ERROR.GET_CMD_FAIL, jtk);
            return false;
        }

        protected bool SetError(API_ERROR Error, string msg)
        {
            _errCode = Error;
            _errMsg = msg;
            return false;
        }

        protected bool SetError(API_ERROR Error, JToken jtk)
        {
            if (jtk == null)
                return SetError(Error, null);
            return SetError(Error, jtk.ToString());
        }
        internal protected API_ERROR GetError() { return _errCode; }

        private void WriteError()
        {
            var rlt = new Model.SP_RESULT() { status = _errCode, description = _errMsg };
            var sp = new Model.APIResponseModel(rlt, _aqm);
            if (_aqm != null)
                _aqm.status = ResultStatusMap(rlt);

            Response.Write(sp.ToJson());
        }

        public string ErrorMesage { get { return _errMsg; } }

        public bool IsSuccess { get; private set; }



        /// <summary>
        /// 写入日志文件
        /// </summary>
        internal protected virtual void FlushLog()
        {
            if (_sbLog == null || _sbLog.Length == 0)
                return;
            string FileName = string.Format("~/splog/{0:yyyyMMdd}/{1:4000}-{0:HH}.log", DateTime.Now, _aqm.tbl_sp_trone_api_id);
            var fi = new FileInfo(Request.MapPath(FileName));
            if (!fi.Directory.Exists)
                fi.Directory.Create();

            _sbLog.AppendLine();
            lock (logLocker)
            {
                StreamWriter stm = null;
                try
                {
                    stm = new StreamWriter(fi.FullName, true);
                    stm.WriteLine(_sbLog.ToString());
                }
                catch { }
                finally { if (stm != null) stm.Dispose(); }

            }
            _sbLog.Clear();
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


        int Shotgun.Model.Logical.ILogical.lastUpdateCount { get { throw new NotImplementedException(); } }

        Shotgun.Database.IBaseDataClass2 Shotgun.Model.Logical.ILogical.dBase { get { return this.dBase; } set { throw new NotImplementedException(); } }

        /// <summary>
        /// 通过IMSI获取城市和省份信息
        /// </summary>
        /// <param name="imsi"></param>
        /// <returns></returns>
        public tbl_cityItem getCityByImsi(string imsi)
        {
            var phone = n8wan.Public.Library.GetPhoneByImsi(imsi);
            if (string.IsNullOrEmpty(phone))
                return null;
            int p;
            int.TryParse(phone, out p);
            return tbl_phone_locateItem.GetRowByMobile(dBase, p);
        }


        #endregion

        #region 远程HTML获取

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

        public CookieContainer Cookies { get; set; }

        protected virtual string OnWebException(string srcUrl, string postdata, WebException ex)
        {
            return null;
        }
    }
}
