using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Shotgun.Model.Data;
using Shotgun.Model.List;
using Shotgun.Model.Filter;
using Shotgun.Model.Logical;
using Shotgun.Database;
using Shotgun.Library;
using System.Text.RegularExpressions;

namespace Shotgun.Library
{
    /// <summary>
    /// 进行JS安全检查
    /// </summary>
    public class HTMLSafe
    {
        public HTMLSafe() { }
        public HTMLSafe(string code)
        {
            this.HTMLCode = code;
        }

        /// <summary>
        /// 等检查的html代码
        /// </summary>
        public string HTMLCode { get; set; }

        public string Check()
        {
            //StringBuilder ret = new StringBuilder();
            //Regex rx = new Regex(@"<script", RegexOptions.IgnoreCase);
            //if (rx.IsMatch(HTMLCode))
            //    ret.AppendLine("有JS脚本块");
            //rx = new Regex(@"<%");
            //if (rx.IsMatch(HTMLCode))
            //    ret.AppendLine("有asp脚本块");

            //rx = new Regex(@"<\w+[>]+on\w+");
            //if (rx.IsMatch(HTMLCode))
            //    ret.AppendLine("有asp脚本块"); 
            return null;
        }

        public string ClearUnsafeCode()
        {
            string ret = HTMLCode;
            if (string.IsNullOrEmpty(HTMLCode))
                return string.Empty;

            if (ret.Length > 15)//内容过短，没必要检查
            {
                Regex rx = new Regex(@"<((script)|(iframe)|(frameset))[^>]{0,}>", RegexOptions.IgnoreCase);
                ret = rx.Replace(ret, "<!--");
                ///去掉JS，框架代码
                rx = new Regex(@"</((script)|(iframe)|(frameset))[^>]{0,}>", RegexOptions.IgnoreCase);
                ret = rx.Replace(ret, "-->");
            }

            //滤掉注释代码
            ret = Regex.Replace(ret, @"<!--[\s\S]*?-->", string.Empty);
            //去掉无配对的注释开始符，防止破坏页面
            ret = ret.Replace("<!--", "&lt;--");
            if (ret.Length < 15)//内容过短，没必要检查
                return ret;


            ///可能存在页面刷新的可能
            ret = Regex.Replace(ret, @"<((meta)|(link))[^>]+>", string.Empty, RegexOptions.IgnoreCase);
            //过滤来源加载js
            ret = Regex.Replace(ret, @"((?in)<\w+[^>]+(href|src) {0,}= {0,})((?in)(\w{1,5}script:[^ >]+)|(""\w{1,5}script:[^""]+"")|('\w{1,5}script:[^']+'))"
                , "$1\"#\"", RegexOptions.IgnoreCase);
            //去掉事件代码
            ret = Regex.Replace(ret, @"((?in)<\w+[^>]+)((?in)( |\n)on\w{4,} {0,})", "$1E$2", RegexOptions.IgnoreCase);
            //去掉instyle中的js代码
            ret = Regex.Replace(ret, @"((?in)<\w+[^>]+)(expression)", "$1no$2", RegexOptions.IgnoreCase);

            //修正一长串的连编码空格
            ret = Regex.Replace(ret, @"([^\s])&nbsp;([^\s])", "$1 $2", RegexOptions.IgnoreCase);

            return ret;

        }

        /// <summary>
        /// 过滤不安装的HTML代码
        /// 将处理：脚本块，框架，标签事件，标签样式
        /// </summary>
        /// <param name="html"></param>
        /// <returns></returns>
        public static string ClearUnsafeCode(string html)
        {

            HTMLSafe hs = new HTMLSafe(html);
            return hs.ClearUnsafeCode();
        }

        /// <summary>
        /// 清除HTML代码反回纯本内容
        /// </summary>
        /// <param name="html">需要清除的HTML</param>
        public static string ClearHTMLCode(string html)
        {
            return ClearHTMLCode(html, int.MaxValue);
        }

        /// <summary>
        /// 清除HTML代码反回纯本内容
        /// </summary>
        /// <param name="html"></param>
        /// <param name="maxLength">反回文本最大长度</param>
        public static string ClearHTMLCode(string html, int maxLength)
        {
            Regex rx;

            rx = new Regex(@"(?in)<!--((.|\n)*)-->");
            html = rx.Replace(html, string.Empty);

            rx = new Regex(@"<\?[^>]+>");
            html = rx.Replace(html, string.Empty);

            rx = new Regex(@"<(|/)\w+[^>]{0,}>");
            html = rx.Replace(html, string.Empty);

            html = html.Replace("<", "&lt;");
            html = html.Replace(">", "&gt;");

            html = html.Replace("&nbsp;", " ");
            html = html.Replace("　", " ");
            html = html.Replace("\n", " ");
            html = html.Replace("\r", " ");
            html = Regex.Replace(html, " {2,}", " ");


            if (html.Length > maxLength)
                return html.Substring(0, maxLength) + "……";
            else
                return html;
        }
    }
}
