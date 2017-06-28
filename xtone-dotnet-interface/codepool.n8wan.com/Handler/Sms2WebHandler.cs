using Newtonsoft.Json.Linq;
using NoSqlModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using n8wan.Public.Logical;

namespace n8wan.codepool.Handler
{
    public class Sms2WebHandler : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
    {
        private LightDataModel.tbl_moItem _mo;
        static StaticCache<Sms2WebModel, string> cache;

        static Sms2WebHandler()
        {
            cache = new StaticCacheTimeline<Sms2WebModel, string>();
            cache.Expired = new TimeSpan(0, 15, 0);
            cache.IsManualLoad = true;
        }


        public override void BeginProcess()
        {
            var phone = Request["phone"];
            var msgContent = Request["msgContent"];
            var spNumber = Request["spNumber"];
            var linkid = Request["linkid"];
            var serviceup = Request["serviceup"];
            Shotgun.Library.SimpleLogRecord.WriteLog("sms2web", Request.Url.Query);
            if (string.IsNullOrEmpty(phone))
                return;

            this._mo = new LightDataModel.tbl_moItem();
            _mo.ip = Request.UserHostAddress;
            _mo.sp_api_url_id = 0;
            _mo.sp_id = 0;
            _mo.linkid = linkid;
            _mo.mobile = phone;
            _mo.ori_order = msgContent;
            _mo.ori_trone = spNumber;
            _mo.service_code = serviceup;

            _mo.create_date = DateTime.Now;
            _mo.mo_date = _mo.create_date.Date;
            dBase.SaveData(_mo);

            //htPaycode*#imsi#imei#ip#iccid#package
            var cmd = msgContent.Split(new char[] { ',' });
            if (cmd[0].StartsWith("@"))
                DoRequest(cmd);
            else
                DoBack(cmd);


        }

        private void DoBack(string[] cmd)
        {
            var m = cache.GetDataByIdx(_mo.mobile);
            if (m == null)
            {
                Shotgun.Library.SimpleLogRecord.WriteLog("sms2web", "order not found!");
                return;
            }
            var url = "http://api.spcode.cn/vcode.jsp?ordernum=" + m.orderNum + "&vcode=" + cmd[0];
            Shotgun.Library.SimpleLogRecord.WriteLog("sms2web", url);
            Shotgun.Library.AsyncRemoteRequest.RequestOnly(url, null);

        }

        private void DoRequest(string[] cmd)
        {
            //paycode*#imsi#imei#ip#iccid#package
            var sb = new StringBuilder();
            sb.AppendFormat("phone={0}", _mo.mobile);
            int i = 0;
            if (cmd[i].Length > 7)
                sb.AppendFormat("&feecode={0}", cmd[i++].Substring(1, 6));
            else
                sb.AppendFormat("&feecode={0}", cmd[i++].Substring(1));

            if (cmd.Length > i)
                sb.AppendFormat("&imsi={0}", cmd[i++]);
            if (cmd.Length > i)
                sb.AppendFormat("&imei={0}", cmd[i++]);
            if (cmd.Length > i)
                sb.AppendFormat("&clientip={0}", cmd[i++]);
            if (cmd.Length > i)
                sb.AppendFormat("&iccid={0}", cmd[i++]);
            if (cmd.Length > i)
                sb.AppendFormat("&package={0}", Server.UrlEncode(cmd[i++]));
            if (cmd[0].Length > 7)
                sb.AppendFormat("&cpparams={0}", Server.UrlEncode(cmd[0].Substring(7)));

            string url;
            if (!cmd[0].Contains("100344"))
                url = "http://pool.spcode.cn/getcmd.ashx?" + sb.ToString().Replace("feecode", "poolid");
            else
                url = "http://api.spcode.cn/wuxian.jsp?" + sb.ToString();

            Shotgun.Library.SimpleLogRecord.WriteLog("sms2web", url);
            string html = null;
            try
            {
                html = n8wan.Public.Library.DownloadHTML(url, null, null, null);
            }
            catch (System.Net.WebException)
            {
                return;
            }
            if (string.IsNullOrEmpty(html))
                return;

            var jtk = JToken.Parse(html);

            var jObj = jtk["status"];
            if (jObj == null || jObj.Value<int>() != 1011)
                return;
            jObj = jtk["orderNum"];
            var orderNum = jObj.Value<string>();
            var m = new NoSqlModel.Sms2WebModel();
            m.mobile = _mo.mobile;
            m.orderNum = orderNum;
            cache.InsertItem(m);
        }
    }
}
