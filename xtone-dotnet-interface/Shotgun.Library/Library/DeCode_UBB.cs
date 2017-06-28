using System;
using System.Collections.Generic;
using System.Web;
using System.Text.RegularExpressions;

namespace Shotgun.Library
{
    /// <summary>
    ///提供UBB转换html代码使用。
    /// </summary>
    public class UBBDecode
    {
        private UBBDecode()
        {
            //
            //TODO: 在此处添加构造函数逻辑
            //
        }
        public static string baseDecode(string ubbStr)
        {
            return baseDecode(ubbStr, true);
        }


        public static string baseDecode(string ubbStr, bool NoHtml)
        {
            Regex rx = null;// new Regex("\\[(|/)(I|B|U)\\]", RegexOptions.IgnoreCase);
            //rx = new Regex(@"\[(i)\]([ \S\t]*?)\[/i\]",RegexOptions.IgnoreCase);
            if (NoHtml)
            {
                ubbStr = ubbStr.Replace("<", "&lt;");
                ubbStr = ubbStr.Replace(">", "&gt;");
            }

            #region [i]xx[/i]
            rx = new Regex(@"\[(i)\]([　 \S\t\n]*?)\[/i\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr);
            #endregion

            #region [b]xx[/b]
            rx = new Regex(@"\[(b)\]([　 \S\t\n]*?)\[/b\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr);
            #endregion

            #region [u]xx[/u]
            rx = new Regex(@"\[(u)\]([　 \S\t\n]*?)\[/u\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr);
            #endregion

            #region [p]xx[/p]
            rx = new Regex(@"\[(b)\]([　 \S\t\n]*?)\[/b\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr);
            #endregion

            #region [sup]xx[/sup]
            rx = new Regex(@"\[(sup)\]([　 \S\t\n]*?)\[/sup\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr);
            #endregion

            #region [color=#rrggbb]xx[/color]
            rx = new Regex(@"\[color=(\#.{6})\]([　 \S\t\n]*?)\[\/color\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<span style=\"color:$1\">$2</span>");
            #endregion

            return ubbStr;
        }

        private static void rxReplace(Regex rx, ref string ubbStr)
        {
            while (rx.IsMatch(ubbStr))
            {
                ubbStr = rx.Replace(ubbStr, "<$1>$2</$1>");
            }
        }

        private static void rxReplace(Regex rx, ref string ubbStr, string newStr)
        {
            while (rx.IsMatch(ubbStr))
            {
                ubbStr = rx.Replace(ubbStr, newStr);
            }
        }


        public static string talkBase(string ubbStr)
        {
            ubbStr = baseDecode(ubbStr);
            Regex rx = new Regex("\\[img\\]([^\\[]+)\\[/img\\]", RegexOptions.IgnoreCase);
            ubbStr = rx.Replace(ubbStr, "<a href=\"$1\" target=\"_blank\" class=\"imgLnk\" ></a>");
            return ubbStr;
        }

        /// <summary>
        /// 手机专用UBB解码
        /// </summary>
        /// <param name="ubbStr"></param>
        /// <returns></returns>
        public static string DecodeMobile(string ubbStr)
        {
            Regex rx;

            ubbStr = Regex.Replace(ubbStr, " +$", "", RegexOptions.Multiline);
            ubbStr = ubbStr.Replace("\r", "");
            ubbStr = ubbStr.Replace("  ", " &nbsp;");
            ubbStr = Regex.Replace(ubbStr, @"\n+$", "");



            rx = new Regex(@"\[quote\]\[b\]以下是引用\[i\](.+)\[/i\]在\[i\](.+)\[/i\]的发言：\[/b\](.|\n)*\[/quote\]");
            rxReplace(rx, ref ubbStr, "<p>引用 <b>$1</b> 在 $2 发言<span class=\"quote\">【内容省略】</span></p>");

            rx = new Regex(@"\[quote\]\[b\]以下是引用\[i\](.+)在(.+)\[/i\]的发言：\[/b\](.|\n)*\[/quote\]");
            rxReplace(rx, ref ubbStr, "<p>引用 <b>$1</b> 在 $2 发言<span class=\"quote\">【内容省略】</span></p>");

            rx = new Regex(@"\[align=right\]\[color=#000066\]\[此贴子已经被.+于(.+)编辑过\]\[/color\]\[/align\]");
            rxReplace(rx, ref ubbStr, "<div class=\"edit\">最后修改：<b>$1</b></div>");


            ubbStr = baseDecode(ubbStr, false);

            rx = new Regex(@"\n{2,}");
            rxReplace(rx, ref ubbStr, "\n");

            ubbStr = Regex.Replace(ubbStr, "\n", "<br/>\r\n");



            #region [upload=jpg|gif|png|bmp]images[/upload]标记
            rx = new Regex(@"\[upload=(jpg|gif|png|bmp|jpeg)\]([ \S\t]*?)\[\/upload\](<br/>){0,}", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<p class=\"img\"><a title=\"查看大图\" href=\"http://filestools.gkong.com/thumbnail.ashx/480/$2\">"
                + "<img src=\"http://filestools.gkong.com/thumbnail.ashx/100/$2\" alt=\"图\"/><br/>查看大图</a></p>");
            #endregion

            #region [upload=filetype]file[/upload]
            rx = new Regex(@"\[upload=([^\]]+)]((?in)(/|))uploadImage(([^/]+)/\w+\.\w+(|(/([^\[]+))))\[/upload\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<p class=\"file\">附件:<b>$7</b> <a href=\"http://filestools.gkong.com/bbs/download.aspx/$3\">"
                + "点击下载&gt;&gt;</a></p>");
            #endregion
            #region [img]
            rx = new Regex(@"\[img\]([ \S\t#]*?)\[\/img\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<a href=\"$1\">$1</a>");
            #endregion

            facialImg(ref ubbStr);
            UrlDecode(ref ubbStr, false);

            #region 识别网址
            ///论坛网址转变
            rx = new Regex("http://bbs.gkong.com/((dispbbs.asp)|(archive.aspx))", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "http://m.gkong.com/bbs/archive.aspx");

            rx = new Regex(@"([^>\=""']|^)http://m.gkong.com/bbs/([A-Za-z0-9\./=\?%\-&_~`@':+!#]+)", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "$1<a href=\"$2\">http://m.gkong.com/bbs/$2</a>");
            #endregion

            return ubbStr.Replace("</p><br/>", "</p>");


        }

        /// <summary>
        /// 表情解码
        /// </summary>
        /// <param name="ubbStr"></param>
        static void facialImg(ref string ubbStr)
        {

            Regex rx = new Regex(@"\[((?in)((tusiji)|(qq)|(congtou)))(\d+)\]", RegexOptions.IgnoreCase);
            ubbStr = rx.Replace(ubbStr, "<img alt=\"表情\" src=\"http://static.gkong.com/bbs/files/pic/smilies/$1/$2.gif\"/>");

            rx = new Regex(@"\[(em\d+)\]", RegexOptions.IgnoreCase);
            ubbStr = rx.Replace(ubbStr, "<img alt=\"表情\" src=\"http://static.gkong.com/bbs/files/pic//$1.gif\"/>");

        }

        /// <summary>
        ///  处理[url=xxx]description[/url]标记
        /// </summary>
        /// <param name="ubbStr"></param>
        /// <returns></returns>
        static void UrlDecode(ref string ubbStr, bool isNewWindow)
        {
            Regex rx;
            #region 处理[url][/url]标记
            //处[url][/url]标记
            rx = new Regex(@"\[url\]([ \S\t#]*?)\[\/url\]", RegexOptions.IgnoreCase);
            if (isNewWindow)
                rxReplace(rx, ref ubbStr, "<a href=\"$1\" target=\"_blank\">$1</a>");
            else
                rxReplace(rx, ref ubbStr, "<a href=\"$1\">$1</a>");

            #endregion

            #region 处理[url=xxx]description[/url]标记
            rx = new Regex(@"\[url=([^\]]+)\]([^[]+)\[\/url\]", RegexOptions.IgnoreCase);
            if (isNewWindow)
                rxReplace(rx, ref ubbStr, "<a href=\"$1\" target=\"_blank=\" >$2</a>");
            else
                rxReplace(rx, ref ubbStr, "<a href=\"$1\">$2</a>");
            #endregion
        }


        public static string FullDeCode(string ubbStr)
        {
            Regex rx;
            ubbStr = ubbStr.Replace("\r", "");
            ubbStr = ubbStr.Replace("  ", " &nbsp;");

            ubbStr = baseDecode(ubbStr);

            ubbStr = Regex.Replace(ubbStr, "\n", "<br/>\r\n");

            rx = new Regex("\\[img\\]([^\\[]+)\\[/img\\]", RegexOptions.IgnoreCase);
            ubbStr = rx.Replace(ubbStr, "<img src=\"$1\" alt='图' onload=\"img_onload(this,{t:1})\" />");


            #region [size]
            rx = new Regex("\\[size=(\\d)]([　 \\S\\t\\n]*?)\\[/size\\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<span class=\"size$1\">$2</span>");
            #endregion

            #region [face]
            rx = new Regex("\\[face=([^\\]]+)]([　 \\S\\t\\n]*?)\\[/face\\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<span style=\"font-family:'$1';\">$2</span>");
            #endregion



            #region [upload=jpg|gif|png|bmp]images[/upload]标记
            rx = new Regex(@"\[upload=(jpg|gif|png|bmp|jpeg)\]([ \S\t]*?)\[\/upload\](<br/>){0,1}", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<div class=\"img\"><a title=\"新窗口打开此图\" target=\"_blank\" href=\"http://static.gkong.com/bbs/files/$2\">"
                + "<img src=\"http://static.gkong.com/bbs/files/$2\" alt=\"附件\" onload=\"img_onload(this,{t:1})\"/></a></div>");
            #endregion

            #region [upload=filetype]file[/upload]
            rx = new Regex(@"\[upload=([^\]]+)]((?in)(/|))uploadImage(([^/]+)/\w+\.\w+(|(/([^\[]+))))\[/upload\]", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<div class=\"file\"><div>附件:<b>$7</b></div>\n[<a name=\"tha\"  href=\"http://filestools.gkong.com/bbs/Download.aspx/$3\">"
                + "本地下载</a>]</div>");
            #endregion


            #region [flash] flv 视频标记
            rx = new Regex(@"\[flash=(\d*?),(\d*?)\]([\S\t]*?).flv\[\/flash\](<br/>){0,1}", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<div class=\"flash\">"
                    + "<span class=\"blue\">[全屏观看请双击视频]</span><br>"
                    + "<object codeBase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0 classid=clsid:D27CDB6E-AE6D-11cf-96B8-444553540000 "
                    + "width=\"$1\" height=\"$2\"><param name=\"movie\" value=\"face/flvplayer.swf\"/>\n"
                    + "<param name=\"quality\" value=\"high\"/>\n"
                    + "<param name=\"allowFullScreen\" value=\"true\" />\n"
                    + "<param name=\"FlashVars\" value=\"IsAutoPlay=0&vcastr_file=$3.flv\" /> "
                    + "<param name=\"menu\" value=\"false\"/>\n<embed src=\"face/flvplayer.swf\" quality=\"high\" menu=\"false\"\n"
                    + "flashvars=\"IsAutoPlay=0&vcastr_file=$3.flv\"\n"
                    + "allowfullscreen=\"true\"\n"
                    + "pluginspage=\"http://www.macromedia.com/go/getflashplayer type=application/x-shockwave-flash\" width=\"$1\" height=\"$2\"></embed></object></div>");
            #endregion

            #region [flash]标记
            rx = new Regex(@"\[flash=(\d*?),(\d*?)\]([\S\t]*?)\[\/flash\](<br/>){0,1}", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<div class=\"flash\"><a href=\"$3\" target=\"_blank\">[全屏欣赏]</a><br>\n"
                    + "<object codeBase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0 classid=clsid:D27CDB6E-AE6D-11cf-96B8-444553540000 "
                    + "width=\"$1\" height=\"$2\"><param name=\"movie\" value=\"$3\"/>\n<param name=\"quality\" value=\"high\"/>\n"
                    + "<param name=\"allowFullScreen\" value=\"true\" />\n"
                    + "<param name=\"menu\" value=\"false\"/>\n<embed src=\"$3\" quality=\"high\" menu=\"false\" "
                    + "allowfullscreen=\"true\"\n"
                    + "pluginspage=\"http://www.macromedia.com/go/getflashplayer type=application/x-shockwave-flash\" width=\"$1\" height=\"$2\"></embed></object></div>");
            #endregion


            #region [align=???]xx[/align]
            rx = new Regex(@"\[align=([^\]]+)\]([　 \S\t\n]*?)\[\/align\](<br/>){0,1}", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "<div style=\"text-align:$1\">$2</div>\n");
            #endregion


            #region [quote]xx[/quote]
            rx = new Regex(@"\[quote\]([ \S\s\t\n]*?)\[\/quote\](<br/>){0,1}", RegexOptions.IgnoreCase);

            rxReplace(rx, ref ubbStr, "<div class=\"quote\" >$1</div>\n");
            #endregion

            UrlDecode(ref ubbStr, true);//[URL]

            facialImg(ref ubbStr);//表情


            #region 识别网址
            rx = new Regex(@"([^>\=""']|^)((?in)(http|https|ftp|rtsp|mms):(\/\/|\\\\)[A-Za-z0-9\./=\?%\-&_~`@':+!#]+)", RegexOptions.IgnoreCase);
            rxReplace(rx, ref ubbStr, "$1<a href=\"$2\" target=\"_blank\">$2</a>");
            #endregion


            return ubbStr.Replace("\n", string.Empty);
        }

        /// <summary>
        /// UBB签名解码器
        /// </summary>
        /// <param name="_sign"></param>
        /// <returns></returns>
        public static string SignDecode(string _sign)
        {
            return SignDecode(_sign, false);
        }

        public static string SignDecode(string _sign, bool isVip)
        {
            if (string.IsNullOrEmpty(_sign))
                return string.Empty;
            Regex rx;

            _sign = _sign.Replace("\r", "");

            //去掉结尾的空格
            rx = new Regex(" +$", RegexOptions.Multiline);
            _sign = rx.Replace(_sign, "");

            //处理无效换行，以减少版面占用
            rx = new Regex("\\n{2,}");
            rxReplace(rx, ref  _sign, "\n");

            //去掉结尾的回车
            rx = new Regex("(^\\n)|(\\n$)");
            _sign = rx.Replace(_sign, "");


            if (!isVip)
            {
                //电话或QQ
                rx = new Regex("\\d{5,}");
                _sign = rx.Replace(_sign, "*****");
            }

            string html = FullDeCode(_sign);

            //控制图片大小
            html = html.Replace("onload=\"img_onload(this,{t:1})\"", "onload=\"img_onload(this,{t:2})\"");

            //屏蔽连接
            if (isVip)
                html = html.Replace(" href=", " href1=");
            else
                html = html.Replace(" href=", " style=\"display:none;\" href1=");
            //恢复被屏蔽的站内连接
            rx = new Regex("( style=\"display:none;\"){0,1} href1=\"(\\w+://\\w+.gkong.com/)", RegexOptions.IgnoreCase);
            html = rx.Replace(html, " href=\"$2");

            return html;
        }
        /// <summary>
        /// 用于一般的文件显示解码
        /// </summary>
        /// <param name="ubbCode"></param>
        /// <returns></returns>
        public static string TextDeCode(string ubbCode)
        {
            Regex rx = new Regex(@"\[quote\]\[b\]以下是引用\[i\](.+)\[/i\]在\[i\](.+)\[/i\]的发言：\[/b\](.|\n)*\[/quote\]");
            ubbCode = rx.Replace(ubbCode, "引用 <b>$1</b> 在 $2 的发言【内容省略】 ");

            rx = new Regex(@"\[quote\]\[b\]以下是引用\[i\](.+)在(.+)\[/i\]的发言：\[/b\](.|\n)*\[/quote\]");
            ubbCode = rx.Replace(ubbCode, "引用 <b>$1</b> 在 $2 的发言【内容省略】 ");

            ubbCode = Regex.Replace(ubbCode, "\\[img\\]([^\\[]+)\\[/img\\]", "<b>图片</b>", RegexOptions.IgnoreCase);
            ubbCode = Regex.Replace(ubbCode, @"\[upload=[^\]]+\]([ \S\t]*?)\[\/upload\]", "<b>附件</b>", RegexOptions.IgnoreCase);
            ubbCode = Regex.Replace(ubbCode, @"\[((?in)((tusiji)|(qq)|(congtou)))(\d+)\]", "<b>表情</b>", RegexOptions.IgnoreCase);
            return Regex.Replace(ubbCode, @"[(|/)\w{1,}[^\]]{0,}]", string.Empty);

        }

    }
}