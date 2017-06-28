using n8wan.codepool.Model;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.codepool
{
    public abstract class BaseCodePool : Shotgun.Model.Logical.Logical
    {
        private ErrorCode _eCode;
        protected LightDataModel.tbl_api_orderItem _orderInfo;
        private APIModel _apiRequestModel;
        protected System.Diagnostics.Stopwatch timer;



        [Obsolete("禁止使用此方法")]
        protected new bool SetErrorMesage(string msg)
        {
            throw new NotSupportedException("不支持此方法");
        }

        protected bool SetErrorMesage(ErrorCode code, string msg)
        {
            this._eCode = code;
            return base.SetErrorMesage(msg);
        }
        protected override bool SetSuccess()
        {
            this._eCode = codepool.ErrorCode.OK;
            return base.SetSuccess();
        }

        public abstract bool Init(System.Web.HttpRequest request);

        public void QueryCmd()
        {
            InitAPIRequestModel();
            var pfxUrl = GetApiToSpUrl();
            if (string.IsNullOrEmpty(pfxUrl))
            {
                SetErrorMesage(ErrorCode.INNER_CONFIG_ERROR, "api_to_sp_url 未配置");
                return;
            }
            var url = string.Format(pfxUrl, _orderInfo.api_id);
            var data = this._apiRequestModel.ToString();
            string html = null;
            try
            {
                Shotgun.Library.SimpleLogRecord.WriteLog("codepool", url);
                dBase.ReleseConnection();
                if (timer != null) this.timer.Stop();
                html = n8wan.Public.Library.DownloadHTML(url, data, null, e => { e.Timeout = 40 * 1000; });
                if (timer != null) this.timer.Start();
                Shotgun.Library.SimpleLogRecord.WriteLog("codepool", html);
                ProcessCmdResult(html);
            }
            catch (System.Net.WebException ex)
            {
                if (timer != null) this.timer.Start();
                if (ex.Status == System.Net.WebExceptionStatus.Timeout)
                {
                    SetErrorMesage(ErrorCode.GATEWAY_TIMEOUT, "内部网关超时");
                    return;
                }
                var rsp = (System.Net.HttpWebResponse)ex.Response;

                if (rsp == null)
                {
                    SetErrorMesage(ErrorCode.GATEWAY_TIMEOUT, ex.Message);
                    return;
                }
                switch (rsp.StatusCode)
                {
                    case System.Net.HttpStatusCode.NotFound: SetErrorMesage(ErrorCode.INNER_CONFIG_ERROR, "api_to_sp未找到"); break;
                    case System.Net.HttpStatusCode.InternalServerError: SetErrorMesage(ErrorCode.INNER_CONFIG_ERROR, "api_to_sp错误"); break;
                    default: SetErrorMesage(ErrorCode.INNER_ERROR, string.Format("{0:D},{1}", rsp.StatusCode, rsp.StatusDescription)); break;
                }
                rsp.Close();
                return;
            }
#if !DEBUG
            catch (Exception ex)
            { 
                this.timer.Start();
                SetErrorMesage(ErrorCode.INNER_ERROR, ex.Message);
                Shotgun.Library.SimpleLogRecord.WriteLog("CodePool", ex.ToString());
            }
#endif
            finally
            {
                //SaveOrder();
            }

        }

        private void ProcessCmdResult(string html)
        {
            var rlt = Shotgun.Library.Static.JsonParser<ApiResultModel>(html);

            if (_apiRequestModel.spExField != rlt.Request.spExField)
                _orderInfo.sp_exField = rlt.Request.spExField;

            if (_apiRequestModel.spLinkId != rlt.Request.spLinkId)
                _orderInfo.sp_linkid = rlt.Request.spLinkId;


            if (_apiRequestModel.status != rlt.Request.status)
                _orderInfo.status = rlt.Request.status;

            if (_apiRequestModel.extraParams != rlt.Request.extraParams)
                _orderInfo.sp_linkid = rlt.Request.spLinkId;

            if (_apiRequestModel.apiExdata != rlt.Request.apiExdata)
                _orderInfo.api_exdata = rlt.Request.apiExdata;


            var jtk = JToken.Parse(html);
            var jcmd = jtk["Command"];
            var jobj = jcmd["description"];
            _orderInfo.description = jobj.Value<string>();
            SetErrorMesage((ErrorCode)_orderInfo.status, _orderInfo.description);
            //jobj.Parent.Remove();

            if (ECode != ErrorCode.OK)
                return;
            spResult = jcmd;
            jobj = jcmd["port"];
            if (jobj == null || string.IsNullOrEmpty(jobj.Value<string>()))
                return;
            _orderInfo.port = jobj.Value<string>();
            jobj = jcmd["msg"];
            if (jobj == null || string.IsNullOrEmpty(jobj.Value<string>()))
                return;
            _orderInfo.msg = jobj.Value<string>();

        }

        public virtual void SaveOrder()
        {
            if (_orderInfo == null)
                return;
            _orderInfo.description = ErrorMesage;
            _orderInfo.status = (int)this.ECode;
            var uInfo = (Shotgun.Database.IUpatedataInfo)_orderInfo;
            if (uInfo.GetUpateFields().Count == 0)
                return;

            _orderInfo.TruncationVarChar();
            if (timer != null)
                _orderInfo.duration = (int)timer.Elapsed.TotalMilliseconds;
            _orderInfo.SaveToDatabase(dBase);

        }

        /// <summary>
        /// api to sp的响应结果
        /// </summary>
        public JToken spResult { get; protected set; }

        public virtual ErrorCode ECode { get { return _eCode; } }

        public virtual string GetApiToSpUrl()
        {
            return System.Configuration.ConfigurationManager.AppSettings["api_to_sp_url"];
        }
        public string OrderNum
        {
            get
            {
                if (_orderInfo == null || _orderInfo.id == 0)
                    return null;
                return string.Format("{0:yyyyMM}{1}", _orderInfo.FirstDate, _orderInfo.id);
            }
        }


        protected bool InitAPIRequestModel()
        {
            if (_apiRequestModel != null)
                return true;

            if (_orderInfo.id <= 0)
                SaveOrder();

            _apiRequestModel = APIModel.CopyFrom(_orderInfo);
            return true;
        }
    }
}
