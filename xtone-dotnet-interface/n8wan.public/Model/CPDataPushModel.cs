using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Model
{
    public class CPDataPushModel
    {
        static Dictionary<NameKey, string> _nkMap;
        public enum NameKey { mobile, msg, port, linkid, cpparam, price, servicecode, provinceId, virtualMobile, paycode, ordernum }

        static CPDataPushModel()
        {
            _nkMap = new Dictionary<NameKey, string>();
            _nkMap.Add(NameKey.mobile, "mobile");
            _nkMap.Add(NameKey.msg, "msg");
            _nkMap.Add(NameKey.port, "port");
            _nkMap.Add(NameKey.linkid, "linkid");
            _nkMap.Add(NameKey.cpparam, "cpparam");
            _nkMap.Add(NameKey.price, "price");
            _nkMap.Add(NameKey.servicecode, "servicecode");
            _nkMap.Add(NameKey.provinceId, "provinceId");
            _nkMap.Add(NameKey.virtualMobile, "virtualMobile");
            _nkMap.Add(NameKey.paycode, "paycode");
            _nkMap.Add(NameKey.ordernum, "ordernum");

            var cfg = System.Configuration.ConfigurationManager.AppSettings["CPDataPushMap"];
            if (string.IsNullOrEmpty(cfg))
                return;
            var fields = cfg.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
            foreach (var f in fields)
            {
                var d = f.Split(new char[] { ':' }, StringSplitOptions.RemoveEmptyEntries);
                if (d.Length != 2)
                    continue;
                int i;
                if (!int.TryParse(d[0], out i))
                    continue;
                _nkMap[(NameKey)i] = d[1];
            }

        }

        public CPDataPushModel()
        {
#pragma warning disable
            this.ProvinceId = 32;
#pragma warning restore
        }
        public string Url { get; set; }
        public int PayCode { get; set; }
        public int PoolId { get; set; }
        public int Price { get; set; }

        public string Mobile { get; set; }
        public string Msg { get; set; }
        public string Port { get; set; }
        public string Linkid { get; set; }
        public string Cpparam { get; set; }
        public string Servicecode { get; set; }
        public string VirtualMobile { get; set; }
        [Obsolete("建议渠道使用VirtualMobile替换")]
        public int ProvinceId { get; set; }
        public string OrderNum { get; set; }


        public override string ToString()
        {
            var data = new Dictionary<string, string>();
            data[_nkMap[NameKey.cpparam]] = this.Cpparam;
            data[_nkMap[NameKey.linkid]] = this.Linkid;
            data[_nkMap[NameKey.mobile]] = this.Mobile;
            data[_nkMap[NameKey.msg]] = this.Msg;
            data[_nkMap[NameKey.ordernum]] = this.OrderNum;
            data[_nkMap[NameKey.paycode]] = "1" + this.PayCode.ToString("00000");
            data[_nkMap[NameKey.port]] = this.Port;
            data[_nkMap[NameKey.price]] = this.Price.ToString();
#pragma warning disable
            data[_nkMap[NameKey.provinceId]] = this.ProvinceId.ToString();
#pragma warning restore


            data[_nkMap[NameKey.servicecode]] = this.Servicecode;
            data[_nkMap[NameKey.virtualMobile]] = this.VirtualMobile;

            if (PoolId != 0)
            {
                data[_nkMap[NameKey.paycode]] = "P" + this.PoolId.ToString("00000");
                data[_nkMap[NameKey.port]] = this.Price.ToString();
                data[_nkMap[NameKey.msg]] = "P" + this.PoolId.ToString("00000");
            }

            var sb = new StringBuilder(this.Url);
            if (sb.Length == 0)
                sb.Append("#");
            else if (Url.Contains("?"))
                sb.Append("&");
            else
                sb.Append("?");

            foreach (var kv in data)
            {
                if (string.IsNullOrEmpty(kv.Value))
                    continue;
                sb.AppendFormat("{0}={1}&", System.Web.HttpUtility.UrlEncode(kv.Key), System.Web.HttpUtility.UrlEncode(kv.Value));
            }
            sb.Length--;
            return sb.ToString();
        }

        public static CPDataPushModel CreateTestModel(string url)
        {
            var m = new CPDataPushModel();
            m.Mobile = "13800138000";
            m.VirtualMobile = m.Mobile;
            m.Linkid = "TEST" + DateTime.Now.Ticks.ToString();
            m.Cpparam = "max_16_bytes";
            m.OrderNum = string.Format("{0:yyyyMM}00000001TEST", DateTime.Today);
            m.Url = url;
            return m;
        }
        public static string GetFieldName(NameKey key)
        {
            return _nkMap[key];
        }
    }
}
