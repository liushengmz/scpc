using System;
using System.Collections.Generic;
using System.Text;
using System.Web;
using System.Text.RegularExpressions;

namespace Shotgun.Library
{
    public class PageSpliter
    {
        protected int _CurrentPage = 1, _TotalPage;
        protected HttpRequest Request { get; private set; }
        protected HttpServerUtility Server { get; private set; }

        protected bool _PageNumberURL = false;
        bool _noTotal = true;

        public PageSpliter()
        {
            this.Request = HttpContext.Current.Request;
            this.Server = HttpContext.Current.Server;

            string p = Request["Page"];
            if (string.IsNullOrEmpty(p))
                return;
            string[] ps = p.Split(new char[] { '/' }, 2);
            if (ps.Length == 0)
                return;
            int.TryParse(ps[0], out this._CurrentPage);

            if (ps.Length != 2)
                return;
            int.TryParse(ps[1], out this._TotalPage);
            _noTotal = false;
            if (this._CurrentPage < 1)
                this._CurrentPage = 1;
            if (this._TotalPage < 0)
                this._TotalPage = 0;
            _PageNumberURL = !_noTotal;
        }
        /// <summary>
        /// 是否生成带总页数的分页样式
        /// </summary>
        public bool NoTotalPage { get { return _noTotal; } set { _noTotal = value; } }

        /// <summary>
        /// 取得分页html代码
        /// </summary>
        /// <returns></returns>
        /// <summary>
        /// 取得分页html代码
        /// </summary>
        /// <returns></returns>
        public virtual string GetPageSpliterHtml(bool AlawysShow)
        {
            if (_TotalPage < 2)
            {
                if (AlawysShow)
                    return addContainer("div", null, "[上一页] <b>[1]</b> [下一页]", false);
                else
                    return string.Empty;
            }

            string pStr = "";
            string Qs = string.Empty;
            bool noQS = !string.IsNullOrEmpty(NoQueryString);

            foreach (string key in Request.QueryString)
            {
                if (string.IsNullOrEmpty(key) || key.ToLower() == "page")
                    continue;
                if (noQS)
                {
                    if (NoQueryString.Contains(string.Format("[{0}]", key.ToLower())))
                        continue;
                }
                Qs += key + "=" + Server.UrlEncode(Request.QueryString[key]) + "&";
            }


            if (_CurrentPage > 1)
                pStr = "<a href='?" + Qs + "page=" + (_CurrentPage - 1).ToString() + "/{0}'>[上一页]</a> ";
            else
                pStr = "[上一页] ";

            int s = _CurrentPage - (_CurrentPage % 10);
            int e = s + 9;
            if (e > _TotalPage)
                e = _TotalPage;

            if (_CurrentPage >= 10)
            {
                pStr += " <a href='?" + Qs + "page=1/{0}'>[1]</a> <a href='?" + Qs + "page=" + (s - 1).ToString() + "/{0}'>...</a> ";
            }
            if (s == 0)
                s = 1;

            for (; s <= e; s++)
            {
                if (s == _CurrentPage)
                    pStr += string.Format(" <b>[{0}]</b> ", s);
                else
                    pStr += string.Format(" <a href='?{2}page={1}/{0}'>[{1}]</a> ", _TotalPage, s, Qs);

            }

            if (e < _TotalPage)
                pStr += string.Format(" <a href='?{1}page={2}/{0}'>...</a> <a href='?{1}page={0}/{0}'>[{0}]</a> ", _TotalPage, Qs, e + 1);

            if (_CurrentPage < _TotalPage)
                pStr += "<a href='?" + Qs + "page=" + (_CurrentPage + 1).ToString() + "/{0}'>[下一页]</a> ";
            else
                pStr += "[下一页] ";

            if (NoTotalPage)
            {
                pStr = Regex.Replace(pStr, @"(page=[^/]+)/((\{0\})|\d+)", "$1");
            }

            pStr = string.Format(pStr, _TotalPage);

            if (!string.IsNullOrEmpty(baseUrl))
            {//使用绝对路径的url分页变换
                pStr = Regex.Replace(pStr, @"\<a href\='\?", "<a href='" + baseUrl + "?");
            }

            if (!string.IsNullOrEmpty(CSS) && !string.IsNullOrEmpty(ClientId))
            {
                string cnt = "<div";
                if (!string.IsNullOrEmpty(CSS))
                    cnt += " class=\"" + CSS + "\"";
                if (!string.IsNullOrEmpty(ClientId))
                    cnt += " id=\"" + ClientId + "\"";
                return cnt + ">" + pStr + "</div>";
            }
            return addContainer("div", null, pStr, false);
        }

        /// <summary>
        /// 页码是否来自网址
        /// </summary>
        public bool IsFromURL
        {
            get { return _PageNumberURL; }
        }

        public int CurrentPage
        {
            get { return _CurrentPage; }
        }
        public int TotalPage
        {
            get { return _TotalPage; }
            set
            {
                _TotalPage = value;
                if (_TotalPage < 0)
                    _TotalPage = 0;
                _PageNumberURL = false;
            }
        }


        /// <summary>
        /// 基准的URL前缀，即?前的URL地址
        /// </summary>
        public string baseUrl { get; set; }

        public void CalcPageCount(int RecordCount, int PageSize)
        {
            int m = RecordCount % PageSize;
            if (m > 0)
                m = 1;
            m += RecordCount / PageSize;
            this.TotalPage = m;
        }


        /// <summary>
        /// 需要排除的查询字段,注意要小写
        /// 格式:[qs1][qs2]...
        /// </summary>
        public string NoQueryString { get; set; }

        /// <summary>
        /// 分页最外层容器使用的CSS名称
        /// </summary>
        public string CSS { get; set; }
        /// <summary>
        /// 分页最外层容器ID
        /// </summary>
        public string ClientId { get; set; }

        /// <summary>
        /// HTML容器添加代码
        /// </summary>
        /// <param name="TagName">容器标签名</param>
        /// <param name="Property">已经有属性代码</param>
        /// <param name="html">写入容器HTML代码</param>
        /// <param name="alwaysAdd">如果CSS和clientID为空时，是否添加容器</param>
        /// <returns></returns>
        protected string addContainer(string TagName, string Property, string html, bool alwaysAdd)
        {
            if (!alwaysAdd)
            {
                if (string.IsNullOrEmpty(CSS) && string.IsNullOrEmpty(ClientId))
                    return html;
            }
            var cnt = "<" + TagName;
            if (string.IsNullOrEmpty(Property))
                cnt += " " + Property;

            if (!string.IsNullOrEmpty(CSS))
                cnt += " class=\"" + CSS + "\"";
            if (!string.IsNullOrEmpty(ClientId))
                cnt += " class=\"" + ClientId + "\"";
            cnt += ">" + html;
            cnt += "</" + TagName + ">";
            return cnt;
        }


    }

    /// <summary>
    /// 分页样式，表单+上下页
    /// </summary>
    public class PageSpliterForm : PageSpliter
    {
        public override string GetPageSpliterHtml(bool AlawysShow)
        {
            if (_TotalPage < 2)
            {
                if (AlawysShow)
                {
                    return addContainer("form", "onsubmit=\"return pageForm_onsubmit(this,{{max:1,page:1}});\"", "<input name=\"page\" placeholder=\"1/1页\"/><input type=\"submit\" class=\"button\" value=\"跳转\" />"
                        + "<p><a class=\"nolink\">上一页</a><a class=\"nolink\">下一页</a></p>", true);
                }
                else
                    return string.Empty;
            }

            string pStr = string.Empty;
            StringBuilder Qs = new StringBuilder();
            StringBuilder fields = new StringBuilder();

            bool noQS = !string.IsNullOrEmpty(NoQueryString);

            foreach (string key in Request.QueryString)
            {
                if (string.IsNullOrEmpty(key) || key.ToLower() == "page")
                    continue;
                if (noQS)
                {
                    if (NoQueryString.Contains(string.Format("[{0}]", key.ToLower())))
                        continue;
                }
                string value = Request.QueryString[key];
                Qs.AppendFormat("{0}={1}&amp;", key, Server.UrlEncode(value));
                fields.AppendFormat("<input type=\"hidden\" name=\"{0}\" value=\"{1}\"/>", key, Server.HtmlEncode(value));
            }


            if (_CurrentPage > 1)
                pStr = "<a href='?" + Qs + "page=" + (_CurrentPage - 1).ToString() + "/{0}'>上一页</a> ";
            else
                pStr = "<a class=\"nolink\">上一页</a>";

            if (_CurrentPage < _TotalPage)
                pStr += "<a href='?" + Qs + "page=" + (_CurrentPage + 1).ToString() + "/{0}'>下一页</a> ";
            else
                pStr += "<a class=\"nolink\">上一页</a>";

            if (NoTotalPage)
            {
                pStr = Regex.Replace(pStr, @"(page=[^/]+)/((\{0\})|\d+)", "$1");
            }

            pStr = string.Format(pStr, _TotalPage);

            if (!string.IsNullOrEmpty(baseUrl))
            {//使用绝对路径的url分页变换
                pStr = Regex.Replace(pStr, @"\<a href\='\?", "<a href='" + baseUrl + "?");
            }

            StringBuilder sb = new StringBuilder();

            sb.AppendFormat("<form action=\"{0}?\" onsubmit=\"return pageForm_onsubmit(this,{{max:{1},page:{2}}});\" method=\"get\""
                , baseUrl, this.TotalPage, this.CurrentPage);
            if (!string.IsNullOrEmpty(CSS))
                sb.Append(" class=\"" + CSS + "\"");
            if (!string.IsNullOrEmpty(ClientId))
                sb.Append(" id=\"" + ClientId + "\"");
            sb.Append(">");
            sb.AppendLine(fields.ToString());
            sb.AppendFormat("<input name=\"page\" placeholder=\"{0}/{1}页\" max=\"{1}\"/>", this.CurrentPage, this.TotalPage);
            sb.AppendFormat("<input type=\"submit\" value=\"跳转\"/><p>", this.CurrentPage, this.TotalPage);
            sb.Append(pStr);
            sb.Append("</p><div class=\"c\"></div></form>");

            return sb.ToString();
        }
    }
}

