using Shotgun.Library;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 咪咕基地数据数据同步
    /// </summary>
    public class MiGopBaseSync : BaseSPCallback
    {
        XmlElement root;
        private string virLinkid;
        protected override bool OnInit()
        {
            if (Request.TotalBytes < 10)
                return false;
            var xml = new XmlDocument();
            try
            {
                xml.Load(Request.InputStream);
            }
            catch
            {
                return false;
            }
            root = xml.DocumentElement;
            virLinkid = Static.StrId(xml.OuterXml);
            return base.OnInit();
        }

        protected override void FillAreaInfo(LightDataModel.tbl_mrItem m)
        {
            base.FillAreaInfo(m);
            if (m.province_id != 32)
                return;
            int prvid;
            if (!int.TryParse(GetParamValue("provinceId"), out prvid))
                return;
            switch (prvid)
            {
                case 551: m.province_id = 3; break;//安徽
                case 100: m.province_id = 1; break;//北京
                case 591: m.province_id = 21; break;//福建
                case 931: m.province_id = 11; break;//甘肃
                case 200: m.province_id = 8; break;//广东
                case 771: m.province_id = 10; break;//广西
                case 851: m.province_id = 28; break;//贵州
                case 898: m.province_id = 22; break;//海南
                case 311: m.province_id = 17; break;//河北
                case 371: m.province_id = 26; break;//河南
                case 451: m.province_id = 16; break;//黑龙江
                case 270: m.province_id = 4; break;//湖北
                case 731: m.province_id = 25; break;//湖南
                case 431: m.province_id = 12; break;//吉林
                case 250: m.province_id = 2; break;//江苏
                case 791: m.province_id = 23; break;//江西
                case 240: m.province_id = 13; break;//辽宁
                case 471: m.province_id = 14; break;//内蒙古
                case 951: m.province_id = 29; break;//宁夏
                case 971: m.province_id = 27; break;//青海
                case 531: m.province_id = 6; break;//山东
                case 351: m.province_id = 24; break;//山西
                case 290: m.province_id = 20; break;//陕西
                case 210: m.province_id = 7; break;//上海
                case 280: m.province_id = 19; break;//四川
                case 220: m.province_id = 5; break;//天津
                case 891: m.province_id = 31; break;//西藏
                case 991: m.province_id = 15; break;//新疆
                case 871: m.province_id = 30; break;//云南
                case 571: m.province_id = 9; break;//浙江
                case 230: m.province_id = 18; break;//重庆
            }

        }


        public override string GetParamValue(string Field)
        {
            if (root == null)
                return string.Empty;
            if (Field.Equals("vLinkid", StringComparison.OrdinalIgnoreCase))
                return virLinkid;
            if (Field.Equals("status"))
            {//hret status
                var node = root.SelectSingleNode("hRet");
                if (node == null)
                    return "fail";
                var t = node.InnerText;
                node = root.SelectSingleNode("status");
                if (node == null)
                    return "fail";
                t += "-" + node.InnerText;

                return t.Equals("0-1800") ? "ok" : "fail";
            }

            foreach (XmlElement node in root)
            {
                if (node.NodeType != XmlNodeType.Element)
                    continue;
                if (node.Name.Equals(Field, StringComparison.OrdinalIgnoreCase))
                    return node.InnerText;
            }
            return base.GetParamValue(Field);
        }

        protected override void WriteSuccess()
        {
            WriteResult(0);
            Response.Write("<!--OK-->");
        }

        protected override void WriteError(string msg)
        {
            //425
            WriteResult(0);
            Response.Write("<!--" + msg + "-->");
        }

        void WriteResult(int code)
        {
            var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><packageid><hret>0</hret><message>Successful</message></packageid></response>";
            Response.ContentType = "text/xml";
            Response.Write(xml);
        }

    }
}
