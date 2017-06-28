using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;

namespace n8wan.Public.Logical
{
    public class XmlBaseSync : BaseSPCallback
    {
        protected XmlDocument doc;
        protected XmlNode _curEL;


        protected override bool OnInit()
        {
            if (Request.TotalBytes < 10)
                return false;//初始化失败

            doc = new XmlDocument();
            try
            {
                doc.Load(Request.InputStream);
            }
            catch
            {
                doc = null;
                return false;
            }
            _curEL = doc;
            return base.OnInit();
        }

        public override string GetParamValue(string Field)
        {
            if (_curEL == null)
                return base.GetParamValue(Field);
            var node = _curEL.SelectSingleNode(Field);
            if (node == null)
                return base.GetParamValue(Field);
            switch (node.NodeType)
            {
                case XmlNodeType.Attribute:
                    return ((XmlAttribute)node).Value;
                case XmlNodeType.Element:
                default:
                    return node.InnerText;
            }
        }

    }
}
