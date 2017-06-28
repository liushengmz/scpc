using System;
using System.Collections.Generic;
using System.Text;
using System.Web;
using System.Data;
using System.Security.Principal;
using Shotgun.Database;
using System.Xml;

namespace Shotgun.Security
{
    public class AuthorInfo : IPrincipal
    {
        AuthorIIdentity _info;

        #region IPrincipal 成员

        public IIdentity Identity
        {
            get
            {
                if (_info == null)
                    _info = new AuthorIIdentity();
                return _info;
            }
        }

        public bool IsInRole(string role)
        {
            if (_info == null)
                _info = new AuthorIIdentity();
            switch (role.ToLower())
            {
                case "admin":
                    return _info.IsAdministrator;
                case "user":
                    return _info.IsAuthenticated;
                default:
                    return false;
            }
        }

        #endregion
    }

    public class AuthorIIdentity : IIdentity
    {
        /// <summary>
        /// 存入客户端的cookie名
        /// </summary>
        internal const string C_SESSION_NAME = "sgsession";
        bool iExist, Inited;
        int _userId;
        string _userName;
        string _right;

        HttpRequest Request;
        HttpResponse Response;

        public AuthorIIdentity()
        {
            Request = HttpContext.Current.Request;
            Response = HttpContext.Current.Response;
            SessionInit();
        }

        #region IIdentity 成员

        public string AuthenticationType
        {
            get { return "custom"; }
        }

        public bool IsAuthenticated
        {
            get
            {
                if (!iExist)
                    return false;
                if (!string.IsNullOrEmpty(_userName))
                    return true;
                //if (!string.IsNullOrEmpty((string)this["adminname"]) || Request.IsLocal)
                //    return true;
                return false;
            }
        }

        public string Name
        {
            get
            {
                if (!iExist)
                    return "@guest";
                if (Request.IsLocal && string.IsNullOrEmpty(_userName))
                    return "@LocalAdmin";
                return _userName;
            }
        }

        #endregion

        /// <summary>
        /// 是否网站管理员
        /// </summary>
        public bool IsAdministrator
        {
            get
            {
                return this.IsAuthenticated;
            }
        }



        #region 会话实现
        /// <summary>
        /// 初始化实例类
        /// </summary>
        private void SessionInit()
        {
            if (Inited) return;
            Inited = true;
            HttpCookie Cookie = Request.Cookies[C_SESSION_NAME];
            if (Cookie == null || string.IsNullOrEmpty(Cookie.Value))
            {
                iExist = false;
                return;
            }

            string ui = Security.AdminCookieEnCode.Decode(Cookie.Value, Request.UserAgent);
            iExist = !string.IsNullOrEmpty(ui);
            if (!iExist)
                return;
            string[] ds = ui.Split('|');
            iExist = ds.Length == 3;

            if (!iExist)
                return;
            int.TryParse(ds[0], out _userId);
            iExist = _userId != 0;
            if (!iExist)
                return;
            _userName = ds[1];
            _right = ds[2];
            return;
        }

        //private void CreateSession()
        //{
        //    if (iExist)//已经创建了会话
        //        return;

        //    string sql;
        //    DataTable dt = null;
        //    //int i;
        //    do
        //    {
        //        if (dt != null) dt.Dispose();
        //        SSID = CreateID(10);
        //        sql = "select count(0) from SessionPool where SessionId='" + SSID + "'";
        //    }
        //    while ((int)ExecuteNonQuery(sql) == 0);
        //    HttpCookie cookie = new HttpCookie("ShotgunSession");
        //    cookie.Value = SSID;
        //    if (!Request.IsLocal)
        //        cookie.Domain = "jcpeixun.com";

        //    Response.Cookies.Add(cookie);

        //    ExecuteNonQuery("Insert into SessionPool(SessionId) values('" + SSID + "')");

        //    _xml = new XmlDocument();
        //    _xml.LoadXml(@"<ShotgunSession version=""1.0"" date=""2010-10-18"" SessionTime=""" + DateTime.Now.ToString() + @"""  />");
        //    iExist = true;
        //}

        //private string xmlItem(string vName)
        //{
        //    if (_xml == null)
        //        return null;

        //    XmlElement root;
        //    //XmlNode node;
        //    XmlAttribute attr;

        //    vName = vName.ToLower();
        //    root = _xml.DocumentElement;

        //    foreach (XmlNode node in root.ChildNodes)
        //    {
        //        if (node.NodeType != XmlNodeType.Element)
        //            continue;
        //        attr = node.Attributes["name"];

        //        if (attr == null)
        //            continue;
        //        if (attr.Value != vName)
        //            continue;
        //        attr = node.Attributes["value"];
        //        if (attr == null)
        //            return node.InnerXml;
        //        return attr.Value;
        //    }
        //    return null;
        //}

        //private void setXmlValue(string vName, string vData)
        //{
        //    if (_xml == null)
        //    {
        //        _xml = new XmlDocument();
        //        _xml.LoadXml("<ShotgunSession/>");
        //    }
        //    XmlElement root, item = null;
        //    XmlAttribute attr;
        //    vName = vName.ToLower();
        //    root = _xml.DocumentElement;

        //    foreach (XmlNode node in root.ChildNodes)
        //    {
        //        if (node.NodeType == XmlNodeType.Element)
        //        {
        //            attr = node.Attributes["name"];
        //            if (attr != null && attr.Value == vName)
        //            {
        //                item = (XmlElement)node;
        //                break;
        //            }
        //        }
        //    }

        //    if (item == null)
        //    {

        //        item = _xml.CreateElement("item");
        //        root.AppendChild(item);
        //        attr = _xml.CreateAttribute("name");
        //        item.Attributes.Append(attr);
        //        attr.Value = vName;
        //        attr = null;
        //    }
        //    else
        //        attr = item.Attributes["value"];



        //    if (attr == null)
        //    {
        //        attr = _xml.CreateAttribute("value");
        //        item.Attributes.Append(attr);
        //    }
        //    attr.Value = vData;
        //}

        //private string CreateID(int length)
        //{
        //    Random rnd = new Random();
        //    int i, t;
        //    string tSSID = string.Empty;
        //    for (i = 0; i < length; i++)
        //    {
        //        t = rnd.Next(62);
        //        if (t < 10)
        //            tSSID = tSSID + (char)(48 + t);
        //        else if (t < 10 + 26)
        //            tSSID = tSSID + (char)(65 - 10 + t);
        //        else
        //            tSSID = tSSID + (char)(97 - 10 - 26 + t);

        //    }
        //    return tSSID;
        //}

        #endregion

        /// <summary>
        /// 重新从sql里加载会话信息
        /// </summary>
        public void Refresh()
        {
            Inited = false;
            SessionInit();
        }

        /// <summary>
        /// 取得ASP Session
        /// </summary>
        /// <param name="name"></param>
        /// <returns></returns>
        [System.Runtime.CompilerServices.IndexerNameAttribute("Items")]
        public object this[string name]
        {
            get
            {
                if (!Inited)
                    SessionInit();
                if (!iExist) //无会话
                {
                    if (name.ToLower() == "userid")
                        return 0;
                    return null;
                }
                switch (name.ToLower())
                {
                    case "userid":
                        return _userId;
                    case "username":
                        return _userName;
                    case "right":
                        return _right;
                }
                return null;
            }
            //set
            //{
            //    if (!Inited)
            //        SessionInit();
            //    if (!iExist)
            //        CreateSession();
            //    string sql;
            //    switch (name.ToLower())
            //    {
            //        case "userid":
            //            UserId = (int)value;
            //            sql = "userId=" + UserId.ToString();
            //            break;
            //        case "username":
            //            UserName = (string)value;
            //            sql = "userName=" + SqlEncode(UserName, true);
            //            break;
            //        case "truename":
            //            TrueName = (string)value;
            //            sql = "trueName=" + SqlEncode(TrueName, true);
            //            break;
            //        default:
            //            setXmlValue(name, value.ToString());
            //            sql = "ExtrXML=" + SqlEncode(_xml.OuterXml, true);
            //            break;
            //    }
            //    sql = "update SessionPool Set LastUpdate=getDate()," + sql + " where SessionId=" + SqlEncode(SSID, true);
            //    ExecuteNonQuery(sql);
            //}
        }

        public static AuthorIIdentity CurrentIdentity()
        {
            IIdentity iid = HttpContext.Current.User.Identity;
            if (iid is AuthorIIdentity)
                return (AuthorIIdentity)iid;
            return new AuthorIIdentity();
        }

    }


}
