using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace n8wan.Public
{
    public static class Library
    {
        /// <summary>
        /// 通配符转正则，默认不区分大小写
        /// </summary>
        /// <param name="mStr"></param>
        /// <returns></returns>
        public static Regex GetRegex(string mStr)
        {
            mStr = Regex.Replace(mStr, @"([\.\[\]\|\^\$\<\>\+])", @"\$1");

            if (mStr.Contains('?'))
                mStr = Regex.Replace(mStr, @"\?{1,}", e => string.Format(".{{0,{0}}}", e.Value.Length));

            if (mStr.Contains('*'))
                mStr = Regex.Replace(mStr, @"\*{1,}", ".{0,}");
            mStr = "^" + mStr + "$";
            return new Regex(mStr, RegexOptions.IgnoreCase);
        }


        /// <summary>
        ///反编译IMSI获得手机前7位 判断区域
        /// </summary>
        ///<param name="imsi">Imsi</param>
        public static string GetPhoneByImsi(string imsi)
        {

            if (imsi == null || imsi.Length < 10)
                return string.Empty;

            int pfxId;
            string pfxNum = null, h0, h1, h2, h3;
            if (imsi.StartsWith("46000"))
            {
                if (!int.TryParse(imsi.Substring(8, 1), out pfxId))
                    return string.Empty;

                h0 = imsi.Substring(9, 1);
                h1 = imsi.Substring(5, 1);
                h2 = imsi.Substring(6, 1);
                h3 = imsi.Substring(7, 1);

                switch (pfxId)
                {
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9: return "13" + pfxId.ToString() + "0" + h1 + h2 + h3;
                }
                return "13" + (pfxId + 5).ToString() + h0 + h1 + h2 + h3;
            }

            if (imsi.StartsWith("46002"))
            {
                if (!int.TryParse(imsi.Substring(5, 1), out pfxId))
                    return string.Empty;
                h0 = imsi.Substring(6, 1);
                h1 = imsi.Substring(7, 1);
                h2 = imsi.Substring(8, 1);
                h3 = imsi.Substring(9, 1);

                switch (pfxId)
                {
                    case 0: pfxNum = "134"; break;
                    case 1: pfxNum = "151"; break;
                    case 2: pfxNum = "152"; break;
                    case 3: pfxNum = "150"; break;
                    case 4: pfxNum = "184"; break;
                    case 5: pfxNum = "183"; break;
                    case 6: pfxNum = "182"; break;
                    case 7: pfxNum = "187"; break;
                    case 8: pfxNum = "158"; break;
                    case 9: pfxNum = "159"; break;
                }
                return pfxNum + h0 + h1 + h2 + h3;
            }

            if (imsi.StartsWith("46007"))
            {
                if (!int.TryParse(imsi.Substring(5, 1), out pfxId))
                    return string.Empty;
                h0 = imsi.Substring(6, 1);
                h1 = imsi.Substring(7, 1);
                h2 = imsi.Substring(8, 1);
                h3 = imsi.Substring(9, 1);
                switch (pfxId)
                {
                    case 0: pfxNum = "170"; break;
                    case 1:
                    case 2:
                    case 3:
                    case 4: break;
                    case 5: pfxNum = "178"; break;
                    case 6: break;
                    case 7: pfxNum = "157"; break;
                    case 8: pfxNum = "188"; break;
                    case 9: pfxNum = "147"; break;
                }

                if (pfxNum != null)
                    return pfxNum + h0 + h1 + h2 + h3;

                return string.Empty;

            }

            if (imsi.StartsWith("46001"))
            {
                //中国联通，只有46001这一个IMSI号码段
                h1 = imsi.Substring(5, 1);
                h2 = imsi.Substring(6, 1);
                h3 = imsi.Substring(7, 1);
                h0 = imsi.Substring(8, 1);

                if (!int.TryParse(imsi.Substring(9, 1), out pfxId))
                    return string.Empty;

                switch (pfxId)
                {
                    case 0:
                    case 1: pfxNum = "130"; break;
                    case 2: pfxNum = "132"; break;
                    case 3: pfxNum = "156"; break;
                    case 4: pfxNum = "155"; break;
                    case 5: pfxNum = "185"; break;
                    case 6: pfxNum = "186"; break;
                    case 7: pfxNum = "145"; break;
                    case 8: pfxNum = "170"; break;
                    case 9: pfxNum = "131"; break;
                }
                return pfxNum + h0 + h1 + h2 + h3;
            }

            if (imsi.StartsWith("46003"))
            { //电信IMSI段

            }

            return string.Empty;
        }
        /// <summary>
        /// 下载远程代码(不带ContentType值)
        /// </summary>
        /// <param name="url">目标网址</param>
        /// <param name="postdata">post数据,NULL时为GET</param>
        /// <param name="timeout">超时时间,单位为毫秒,默认:3秒</param>
        /// <param name="encode">编码方式,默认utf8</param>
        /// <returns></returns>
        public static string DownloadHTML(string url, string postdata, int timeout, string encode)
        {
            return DownloadHTML(url, postdata, timeout, encode, null, null);
        }

        /// <summary>
        /// 下载远程代码
        /// </summary>
        /// <param name="url">目标网址</param>
        /// <param name="postdata">post数据,NULL时为GET</param>
        /// <param name="timeout">超时时间,单位为毫秒,默认:3秒</param>
        /// <param name="encode">编码方式,默认utf8</param>
        /// <param name="ContentType">默认为空</param>
        /// <returns></returns>
        public static string DownloadHTML(string url, string postdata, int timeout, string encode, string ContentType, System.Net.CookieContainer cookies)
        {
            return DownloadHTML(url, postdata, encode, e =>
            {
                if (timeout > 0)
                {
                    if (timeout < 30)//小于30ms视为设置错误
                        timeout *= 1000;
                    else if (timeout > 90 * 1000)//大于90秒，限定为90秒
                        throw new ArgumentOutOfRangeException("此方法不设置超时时间大于90秒");
                    e.Timeout = timeout;
                }

                if (string.IsNullOrEmpty(ContentType))
                    e.ContentType = ContentType;
                if (cookies != null)
                    e.CookieContainer = cookies;
            });
        }
        public static string DownloadHTML(string url, string postdata, string encode, Action<System.Net.HttpWebRequest> OnRequestInit)
        {

            Encoding ec = null;
            if (string.IsNullOrEmpty(encode))
                ec = ASCIIEncoding.UTF8;
            else
                ec = ASCIIEncoding.GetEncoding(encode);

            System.Net.HttpWebRequest web = null;
            Stream stm = null;
            web = (System.Net.HttpWebRequest)System.Net.WebRequest.Create(url);
            web.AutomaticDecompression = System.Net.DecompressionMethods.GZip;
            web.ServicePoint.UseNagleAlgorithm = false;
            web.Timeout = 2888;
            web.ServicePoint.Expect100Continue = false;
            if (OnRequestInit != null)
            {
                OnRequestInit(web);
            }

            //web.AllowAutoRedirect = false;
            if (postdata != null)
            {
                web.Method = System.Net.WebRequestMethods.Http.Post;
                var bin = ec.GetBytes(postdata);
                using (stm = web.GetRequestStream())
                {
                    stm.Write(bin, 0, bin.Length);
                }
                stm = null;
            }

            StreamReader reader = null;
            System.Net.WebResponse rsp = web.GetResponse();
            try
            {
                stm = rsp.GetResponseStream();
                {
                    using (var rd = new System.IO.StreamReader(stm, ec))
                        return rd.ReadToEnd();
                }
            }
            finally
            {
                if (reader != null)
                    reader.Dispose();
                if (stm != null)
                    stm.Dispose();
                if (rsp != null)
                {
                    try
                    {
                        rsp.Close();
                    }
                    catch { }
                }
            }

        }

        static string syncUrlFix;
        public static string syncUrlPerfix()
        {
            if (syncUrlFix != null)
                return syncUrlFix;
            syncUrlFix = System.Configuration.ConfigurationManager.AppSettings["syncUrlPerfix"];
            if (syncUrlFix == null)
                syncUrlFix = string.Empty;
            return syncUrlFix;
        }

        /// <summary>
        /// 根据传手机或IMSI查得归属地，优先手机号
        /// </summary>
        /// <param name="dBase"></param>
        /// <returns>始终不为空</returns>
        public static LightDataModel.tbl_cityItem GetCityInfo(IBaseDataClass2 dBase, string phone, string imsi)
        {

            if (string.IsNullOrEmpty(phone) && string.IsNullOrEmpty(imsi))
                return new LightDataModel.tbl_cityItem() { id = 416, province_id = 32 };

            if (phone == null)
                phone = string.Empty;

            int spNum = 0;
            if (phone.Length == 11 && phone.StartsWith("1"))//传统手机
                int.TryParse(phone.Substring(0, 7), out spNum);
            else if (phone.Length != 15) //非手机号 非IMSI
            {
                phone = imsi;
                if (string.IsNullOrEmpty(phone) || phone.Length != 15)
                    return new LightDataModel.tbl_cityItem() { id = 416, province_id = 32 };
            } //else 长为15

            if (spNum == 0)
            {
                if (phone.Length == 15 && phone.StartsWith("460"))//IMSI
                {
                    var t = GetPhoneByImsi(phone);
                    if (string.IsNullOrEmpty(t) || t.Length != 7)
                        return new LightDataModel.tbl_cityItem() { id = 416, province_id = 32 };
                    spNum = int.Parse(t);
                }
                else
                    return new LightDataModel.tbl_cityItem() { id = 416, province_id = 32 };
            }



            var cityInfo = LightDataModel.tbl_phone_locateItem.GetRowByMobile(dBase, spNum);
            if (cityInfo == null)
                return new LightDataModel.tbl_cityItem() { id = 416, province_id = 32 };

            return cityInfo;

        }


    }
}
