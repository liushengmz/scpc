using System;
using System.Collections.Generic;
using System.Web;
using System.Text.RegularExpressions;
using System.IO;
using System.Security.Cryptography;
using Shotgun.Library;
using System.Configuration;

namespace Shotgun.Library
{

    public enum FU_SaveMethod { normal, skip, overwrite, fail }

    /// <summary>
    ///FilesUpload 的摘要说明
    /// </summary>
    public class FilesUpload : IHttpHandler
    {

        protected HttpResponse Response;
        protected HttpRequest Request;
        protected HttpServerUtility Server;
        protected FUConfig config;
        /// <summary>
        /// 用户上传的文件
        /// </summary>
        protected HttpPostedFile uFile;
        public FilesUpload()
        {
            Response = HttpContext.Current.Response;
            Request = HttpContext.Current.Request;
            Server = HttpContext.Current.Server;
            config = new FUConfig(LoadXml());
        }

        protected virtual string LoadXml()
        {
            string path = ConfigurationManager.AppSettings["uploadConfig"];
            if (string.IsNullOrEmpty(path))
                path = "~/upload/default.xml";
            return Server.MapPath(path);
        }

        #region IHttpHandler 成员

        public bool IsReusable
        {
            get { return false; }
        }

        /// <summary>
        /// 与文件无关的检查代码（如身份验证）
        /// </summary>
        /// <returns></returns>
        protected virtual string PerCheck()
        {
            return null;
        }

        /// <summary>
        /// 上传成功后继处理(如记录数据)
        /// </summary>
        /// <returns></returns>
        protected virtual void OnSaved(string FileName)
        {
            return;
        }

        public void ProcessRequest(HttpContext context)
        {
            if (Request.Files.Count == 0)
            {
                ResultOutput(string.Empty, FU_SaveMethod.fail, "上传数据丢失！");
                return;
            }
            //验证身份等信息
            string ret = PerCheck();
            if (!string.IsNullOrEmpty(ret))
            {
                ResultOutput(string.Empty, FU_SaveMethod.fail, ret);
                return;
            }

            uFile = Request.Files[0];
            if (!config.VerifyFileType(uFile.FileName))
            {
                ResultOutput(string.Empty, FU_SaveMethod.fail, "不允许上传此文件类型！");
                return;
            }
            string uFileName = uFile.FileName;
            string fileExt = Regex.Match(uFile.FileName, @"\.[^\.]+$").Value;
            uFileName = uFileName.Substring(0, uFileName.Length - fileExt.Length);
            int i = uFileName.Replace('/', '\\').LastIndexOf('\\');
            if (i != -1)
                uFileName = uFileName.Substring(i + 1);

            if (!string.IsNullOrEmpty(fileExt)) fileExt = fileExt.Substring(1).ToLower();


            if (uFile.ContentLength > config.MaxSize)
            {
                ResultOutput(string.Empty, FU_SaveMethod.fail, string.Format("不允许上传大于{0}Kb的文件。上传的文件超出了{1}Byte",
                    config.MaxSize / 1024, uFile.ContentLength - config.MaxSize));
                return;
            }
            Stream filestream = uFile.InputStream;

            Stream tStm = null;
            try
            {
                tStm = LogoPrint(filestream, ref fileExt);
            }
            catch (Exception ex)
            {
                ResultOutput(string.Empty, FU_SaveMethod.fail, "添加水印出错！" + ex.Message);
                return;
            }
            if (tStm != filestream)
            {
                filestream.Close();
                filestream.Dispose();
                filestream = tStm;
            }

            tStm = null;

            string fileName = getStorePath(uFileName, fileExt);
            try
            {
                FU_SaveMethod SaveMethod = SaveToFile(filestream, fileName);
                OnSaved(fileName);
                ResultOutput(fileName, SaveMethod, "ok");
            }
            catch (Exception ex)
            {
                ResultOutput(string.Empty, FU_SaveMethod.fail, ex.Message);
            }

        }
        #endregion

        /// <summary>
        /// 输出结果
        /// </summary>
        /// <param name="url">上传之后文件的完整路径</param>
        /// <param name="SaveMethod">保存方法</param>
        /// <param name="errMsg">错误信息</param>
        protected virtual void ResultOutput(string url, FU_SaveMethod SaveMethod, string errMsg)
        {
            url = url.Replace("//", "/");
            url = config.PerUrl + url;
            string json = string.Format("{{iSuccess:{0},msg:\"{1}\",url:\"{2}\",method:\"{3}\"}}",
                    (SaveMethod == FU_SaveMethod.fail ? 0 : 1), jsEncode(errMsg), jsEncode(url), SaveMethod);
            string ret = config.Return;
            if (config.returnMode == FUC_RETURN.redirect)
            {
                ret = ret.Replace("{fileUrl}", Server.UrlEncode(url));
                ret = ret.Replace("{json}", Server.UrlEncode(json));
                ret = ret.Replace("{msg}", Server.UrlEncode(errMsg));
                ret = ret.Replace("{method}", Server.UrlEncode(SaveMethod.ToString()));
                Response.Redirect(ret);
            }
            else
            {
                ret = ret.Replace("{fileUrl}", url);
                ret = ret.Replace("{json}", json);
                ret = ret.Replace("{msg}", errMsg);
                ret = ret.Replace("{method}", SaveMethod.ToString());
                Response.Write(ret);
            }

        }

        protected string jsEncode(string str)
        {
            str = str.Replace("\\", "\\\\");
            str = str.Replace("/", "\\/");
            str = str.Replace("'", "\\'");
            str = str.Replace("\"", "\\\"");
            str = str.Replace("\n", "\\n");
            return str;
        }

        private FU_SaveMethod SaveToFile(Stream filestream, string fileName)
        {
            if (!fileName.StartsWith("/") && !fileName.StartsWith("\\"))
                fileName = "/" + fileName;
            FileInfo fi = new FileInfo(Server.MapPath("~/upfiles" + fileName));
            bool Exist = fi.Exists;
            if (Exist)
            {
                if (config.WhenExist == FUC_OVERWRITE_MODE.skip)
                    return FU_SaveMethod.skip;
            }
            else if (!fi.Directory.Exists)
                fi.Directory.Create();


            filestream.Position = 0;
            byte[] bin = new byte[512 * 1024];
            FileStream sStm = fi.Create();
            int c;
            c = filestream.Read(bin, 0, 512 * 1024);
            try
            {
                while (c != 0)
                {
                    sStm.Write(bin, 0, c);
                    c = filestream.Read(bin, 0, 512 * 1024);
                }
            }
            finally
            {
                sStm.Close();
                sStm.Dispose();
            }
            return Exist ? FU_SaveMethod.overwrite : FU_SaveMethod.normal;

        }

        /// <summary>
        /// 取得文件夹保存的完整路径
        /// </summary>
        /// <param name="uFileName"></param>
        /// <param name="fileExt"></param>
        /// <returns></returns>
        protected virtual string getStorePath(string uFileName, string fileExt)
        {
            var p = config.Store.Replace("{ext}", fileExt);
            p = p.Replace("{name}", uFileName);
            if (p.Contains("{idstr}"))
                p = p.Replace("{idstr}", getFileIdentify(uFile.InputStream));
            return p;
        }

        protected virtual string getFileIdentify(Stream fStream)
        {
            MD5CryptoServiceProvider Md5 = new MD5CryptoServiceProvider();
            byte[] md5Bin = Md5.ComputeHash(fStream);
            string md5String = string.Empty;
            for (int i = 0; i < md5Bin.Length; i++)
            {
                if (md5Bin[i] > 15)
                    md5String += string.Format("{0:x0}", md5Bin[i]);
                else
                    md5String += string.Format("0{0:x0}", md5Bin[i]);
            }
            return md5String;
        }

        protected virtual Stream LogoPrint(Stream sFile, ref string fileExt)
        {
            if (string.IsNullOrEmpty(config.Logo))
                return sFile;

            switch (fileExt)
            {
                case "jpg":
                case "bmp":
                case "jpeg":
                case "png":
                    break;
                default:
                    return sFile;
            }
            string logoFile = Server.MapPath(config.Logo);

            MemoryStream mStm = new MemoryStream();

            LogoMark.PrintLogo(sFile, mStm, logoFile);

            fileExt = "jpg";
            return mStm;
        }

    }
}