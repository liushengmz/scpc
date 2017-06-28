using System;
using System.Collections.Generic;
using System.Web;
using System.Text.RegularExpressions;
using System.Xml;
using System.IO;
using System.Configuration;
namespace Shotgun.Library
{
    public enum FUC_RETURN
    {
        /// <summary>
        /// html输出
        /// </summary>
        html,
        /// <summary>
        /// 带参数的url跳转
        /// </summary>
        redirect
    }
    public enum FUC_OVERWRITE_MODE { skip, overwite }

    /// <summary>
    ///文件上传的配置读取器
    /// </summary>
    public class FUConfig
    {

        string _allow, _store, _return, _logo, _preurl;
        int _maxsize;
        FUC_OVERWRITE_MODE _exist;

        FUC_RETURN _returnMode;

        public FUConfig(string xmlFile)
        {
            XmlDocument xml = new XmlDocument();
            FileInfo fi = new FileInfo(xmlFile);
            if (fi.Name.ToLower() != "default.xml")
            {
                string path = ConfigurationManager.AppSettings["uploadConfig"];
                if (string.IsNullOrEmpty(path))
                    path = "~/upload/default.xml";
                xml.Load(HttpContext.Current.Server.MapPath(path));
                load(xml.DocumentElement);
            }
            xml.Load(xmlFile);
            load(xml.DocumentElement);
        }
        void load(XmlNode root)
        {

            foreach (XmlNode node in root.ChildNodes)
            {
                if (node.NodeType != XmlNodeType.Element)
                    continue;
                switch (node.Name)
                {
                    case "allow":
                        Allow = node.InnerText;
                        break;
                    case "store":
                        Store = node.InnerText;
                        break;
                    case "exist":
                        WhenExist = node.InnerText == "overwrite" ? FUC_OVERWRITE_MODE.overwite : FUC_OVERWRITE_MODE.skip;
                        break;
                    case "returnMode":
                        returnMode = node.InnerText == "url" ? FUC_RETURN.redirect : FUC_RETURN.html;
                        break;
                    case "return":
                        Return = node.InnerText;
                        break;
                    case "logo":
                        Logo = node.InnerText;
                        break;
                    case "maxsize":
                        _maxsize = int.Parse(node.InnerText) * 1024;
                        break;
                    case "preurl":
                        _preurl = node.InnerText;
                        break;
                    default:
                        continue;
                }
            }

        }

        /// <summary>
        /// 输出的url前缀
        /// </summary>
        public virtual string PerUrl { get { return _preurl; } protected set { _preurl = value; } }

        /// <summary>
        /// 验证文件是否为允许的格式
        /// </summary>
        /// <param name="fileName"></param>
        /// <returns></returns>
        public virtual bool VerifyFileType(string fileName)
        {
            string ext = Regex.Match(fileName, "\\.[^\\.]+$").Value;
            if (string.IsNullOrEmpty(ext))
                return false;
            ext = ext.Substring(1);
            string exts = "(" + Allow.Replace(",", ")|(") + ")";
            return exts.IndexOf("(" + ext + ")", StringComparison.OrdinalIgnoreCase) != -1;
        }


        /// <summary>
        /// 允许的文件类弄
        /// </summary>
        public virtual string Allow { get { return _allow; } protected set { _allow = value; } }

        /// <summary>
        /// 可上传最大文件大小
        /// </summary>
        public virtual int MaxSize { get { return _maxsize; } protected set { _maxsize = value; } }

        protected string getRnd(int len)
        {
            Random rnd = new Random();
            string r = string.Empty;
            for (int i = 0; i < len; i++)
            {
                r += rnd.Next(10).ToString();
            }
            return r;

        }

        /// <summary>
        /// 存储的文件完整路径
        /// </summary>
        public virtual string Store
        {
            get { return _store; }

            protected set
            {
                value = value.Replace("{year}", DateTime.Now.Year.ToString());
                value = value.Replace("{month}", DateTime.Now.Month.ToString("00"));
                value = value.Replace("{day}", DateTime.Now.Day.ToString("00"));
                MatchCollection mcs = Regex.Matches(value, "\\{rnd(\\d+)\\}");
                int len;
                Random rnd = new Random();
                foreach (Match mc in mcs)
                {
                    len = int.Parse(mc.Groups[1].Value);
                    value = value.Replace(mc.Value, getRnd(len));
                }
                _store = value;
            }
        }

        /// <summary>
        /// 当文件存时的处理方式
        /// </summary>
        public virtual FUC_OVERWRITE_MODE WhenExist { get { return _exist; } protected set { _exist = value; } }

        /// <summary>
        /// 结果反馈方式
        /// </summary>
        public virtual FUC_RETURN returnMode { get { return _returnMode; } protected set { _returnMode = value; } }

        /// <summary>
        /// 返回时输出的内容
        /// </summary>
        public virtual string Return { get { return _return; } protected set { _return = value; } }

        public virtual string Logo { get { return _logo; } protected set { _logo = value; } }
    }


}