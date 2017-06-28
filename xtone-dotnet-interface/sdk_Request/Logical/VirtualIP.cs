using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace sdk_Request.Logical
{
    public class VirtualIP
    {
        public static string GetIpByProvinceId(int pId, string imsi)
        {
            string[] ips = null;
            switch (pId)
            {
                case 3: ips = new string[] { "211.138", "122.92", "211.141" }; break;//安徽
                case 1: ips = new string[] { "60.30", "60.194" }; break;//北京
                case 21: ips = new string[] { "218.66" }; break;//福建
                case 11: ips = new string[] { "61.178" }; break;//甘肃
                case 8: ips = new string[] { "219.137" }; break;//广东
                case 10: ips = new string[] { "219.159" }; break;//广西
                case 28: ips = new string[] { "222.86" }; break;//贵州
                case 22: ips = new string[] { "202.100" }; break;//海南
                case 17: ips = new string[] { "218.12" }; break;//河北
                case 26: ips = new string[] { "218.28" }; break;//河南
                case 16: ips = new string[] { "221.208" }; break;//黑龙江
                case 4: ips = new string[] { "59.172" }; break;//湖北
                case 25: ips = new string[] { "220.168" }; break;//湖南
                case 12: ips = new string[] { "222.168" }; break;//吉林
                case 2: ips = new string[] { "58.67" }; break;//江苏
                case 23: ips = new string[] { "117.136" }; break;//江西
                case 13: ips = new string[] { "60.18" }; break;//辽宁
                case 14: ips = new string[] { "218.21" }; break;//内蒙古
                case 29: ips = new string[] { "222.75" }; break;//宁夏
                case 27: ips = new string[] { "61.133" }; break;//青海
                case 6: ips = new string[] { "218.201" }; break;//山东
                case 24: ips = new string[] { "211.142" }; break;//山西
                case 20: ips = new string[] { "61.243" }; break;//陕西
                case 7: ips = new string[] { "114.80" }; break;//上海
                case 19: ips = new string[] { "218.88" }; break;//四川
                case 5: ips = new string[] { "221.239" }; break;//天津
                case 31: ips = new string[] { "220.182" }; break;//西藏
                case 15: ips = new string[] { "124.117" }; break;//新疆
                case 30: ips = new string[] { "61.138" }; break;//云南
                case 9: ips = new string[] { "124.160" }; break;//浙江
                case 18: ips = new string[] { "222.182" }; break;//重庆
            }
            if (ips == null)
                return null;
            int rnd;

            var mc = Regex.Match(imsi, ".+(\\d{5})");
            if (mc.Success)
                rnd = int.Parse(mc.Groups[1].Value);
            else
                rnd = DateTime.Now.Millisecond;

            string pfxIp = ips[rnd % ips.Length];
            pfxIp += "." + ((rnd / 256) % 255).ToString();
            pfxIp += "." + (rnd % 256).ToString();
            return pfxIp;

        }
    }
}
