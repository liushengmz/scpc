using LightDataModel;
using n8wan.Public;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace n8wan.codepool
{
    public class TelcomMoCallback : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
    {


        private LightDataModel.tbl_request_logItem dblog;
        private LightDataModel.tbl_sp_api_urlItem api;
        private TelcomModel.notifySmsReceptionRequest moRequest;
        private void RecordRequest()
        {
            dblog = new LightDataModel.tbl_request_logItem();
            dblog.createTime = DateTime.Now;
            dblog.date = dblog.createTime;
            dblog.is_post = Request.HttpMethod == "POST";
            if (api != null)
                dblog.sp_api_url_id = api.id;
            dblog.url = Request.Url.PathAndQuery;
            if (dblog.is_post)
            {
                var bin = new byte[Request.TotalBytes];
                Request.InputStream.Read(bin, 0, bin.Length);
                Request.InputStream.Seek(0, System.IO.SeekOrigin.Begin);
                dblog.data = Request.ContentEncoding.GetString(bin);
            }
        }

        private bool LoadAPI()
        {
            var p = Request.Url.AbsolutePath;
            var mc = Regex.Match(p, @"/([^/]+)/([^/]+)\.ashx$", RegexOptions.IgnoreCase);
            if (!mc.Success)
                return false;
            if (Regex.IsMatch(mc.Groups[1].Value, "^\\d+$"))//指定了ID作为目录
            {
                int id = int.Parse(mc.Groups[1].Value);
                api = LightDataModel.tbl_sp_api_urlItem.QueryById(dBase, id);
                if (api.Disable)
                    api = null;
            }
            else
            {
                api = LightDataModel.tbl_sp_api_urlItem.QueryByVirtualPage(dBase, mc.Groups[2].Value);
            }

            return api != null;
        }
        public override void BeginProcess()
        {


            var ua = Request.UserAgent;
            if (!string.IsNullOrEmpty(ua))
            {
                if (ua.Contains("Alibaba.Security.Heimdall"))
                    throw new System.Web.HttpException(410, "Not Welcome");
            }

            try
            {
                Shotgun.Database.IBaseDataPerformance dfp = (Shotgun.Database.IBaseDataPerformance)dBase;

                if (!LoadAPI())
                    throw new System.Web.HttpException(404, "Page Not Found");

                RecordRequest();

                if (!OnInit())
                {
                    WriteError("init fail/invalid data!");
                    return;
                }

                ProcessUser();

                if (dblog != null)
                {
                    dblog.sp_api_url_id = api.id;
                    dblog.linkid = GetLinkId();
                }
            }
            finally
            {

                try
                {
                    if (dblog != null)
                        dBase.SaveData(dblog);
                }
                catch (System.Data.Common.DbException)
                {
                }
            }

        }


        string _lnk;
        private string GetLinkId()
        {
            if (!string.IsNullOrEmpty(_lnk))
                return _lnk;

            var lnk = moRequest.body.message.senderAddress + DateTime.Now.Ticks.ToString("x");


            if (lnk.Length < 20)
                _lnk = lnk.PadLeft(20, '0');
            else
                _lnk = lnk.Substring(lnk.Length - 20, 20);
            return _lnk;
        }

        private void WriteError(string msg)
        {
            Response.Write(msg);
        }

        private bool OnInit()
        {
            var json = Request["params"];
            if (string.IsNullOrEmpty(json) || json.Length < 10)
                return false;
            try
            {
                moRequest = Shotgun.Library.Static.JsonParser<TelcomModel.notifySmsReceptionRequest>(json);
            }
            catch
            {
                return false;
            }
            return moRequest != null;

        }


        private void ProcessUser()
        {
            var mo = new LightDataModel.tbl_moItem();
            mo.sp_api_url_id = api.id;

            mo.mobile = moRequest.body.message.senderAddress;
            mo.ori_trone = moRequest.body.message.smsServiceActivationNumber;
            mo.ori_order = moRequest.body.message.message;
            mo.sp_id = api.sp_id;
            mo.create_date = DateTime.Now;
            mo.mo_date = mo.create_date.Date;
            mo.ip = Shotgun.Library.Static.GetUserHostAddress();
            mo.linkid = GetLinkId();
            mo.cp_param = moRequest.header.linkId;
            //var cityInfo = LightDataModel.tbl_phone_locateItem.GetRowByMobile(dBase, spNum);
            //if (cityInfo == null)
            //    return;
            //m.city_id = cityInfo.id;
            //m.province_id = cityInfo.province_id;

            //mo.ip=
            //var sms = new TelcomModel.MoToISMS(mo);
            var city = Library.GetCityInfo(dBase, mo.mobile, mo.imsi);
            mo.city_id = city.id;
            mo.province_id = city.province_id;
            var trone = n8wan.Public.Logical.BaseSPCallback.FillToneId(dBase, mo);

            mo.SaveToDatabase(dBase);

            if (trone == null)
            {
                WriteError("trone no found");
                return;
            }
            var troneOrder = MatchTroneOrder(mo);

            var bp = new BasePool();
            bp.trone = trone;
            bp.troneOrder = troneOrder;
            bp.dBase = dBase;
            if (!bp.Init(mo))
            {
                WriteError("bp init fail" + bp.ErrorMesage);
                return;
            }
            bp.orderInfo.sp_exField = moRequest.header.transactionId;

            if (!bp.ProcesUserRequest())
                WriteError(bp.ErrorMesage);
            else
                WriteError("ok");
            return;


        }

        private tbl_trone_orderItem MatchTroneOrder(n8wan.Public.Logical.ISMS_DataItem sms)
        {
            var _allCfg = LightDataModel.tbl_trone_orderItem.QueryByTroneIdWithCache(dBase, sms.trone_id);
            if (_allCfg.Count() == 0)
                return null;
            tbl_trone_orderItem tOrder = null;
            var matchCount = 0;
            var msg = sms.ori_order;
            foreach (var m in _allCfg)
            {
                if (m.is_unknow)
                    continue;
                if (!IsMatch(m, msg))
                    continue;

                if (tOrder == null)
                    tOrder = m;
                matchCount++;
            }

            if (matchCount != 1)
                return null;
            return tOrder;

        }

        private bool IsMatch(LightDataModel.tbl_trone_orderItem m, string msg)
        {
            Regex rx;
            if (msg == null)
                msg = string.Empty;
            if (m.is_dynamic)
            {//CP可模糊的指令
                rx = Library.GetRegex(m.order_num);
                return rx.IsMatch(msg);
            }
            //CP精确指令
            return msg.Equals(m.order_num, StringComparison.OrdinalIgnoreCase);
        }



    }
}