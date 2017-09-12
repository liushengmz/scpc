<%@ WebHandler Language="C#" Class="Fc1" %>

using System;
using System.Web;
using System.Text;
using System.Linq;
using System.Security.Cryptography;

public class Fc1 : FlowLibraryNet.Logical.FlowChargeHandler
{
    string IV = "JwFDWXRG8JGVaiIV";
    string key = "TRDABvX5eWjDqKEY ";
    string username = "lezhuohdong";
    string pwd = "lz578426hd112";
    //string url = "URL地址";
    //string phone = "手机号 ";
    //string M = "流量大小"; // 如 10M 1G...
    //string order = "订单号";
    //static int serail;
    //static object serailLocker = new object();

    protected override bool DoCharge()
    {
        //var order = "";
        var dict = new System.Collections.Generic.Dictionary<string, string>();
        dict["username"] = username;
        dict["userpwd"] = Enparameter(pwd);
        dict["phone"] = Enparameter(OrderInfo.mobile);
        dict["orderid"] = Enparameter(OrderInfo.sp_custom_order);

        dict["flowvalue"] = Enparameter(OrderInfo.flowsize.ToString());//流量ID

        var md5Src = string.Join(string.Empty, dict.Values) + key;

        dict["md5str"] = System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(md5Src, "MD5");

        dict["returl"] = FlowLibraryNet.Logical.FlowCallbackHandler.GetCallbackUrl(OrderInfo.trone_id);


        //encMD5(username + Enparameter(pwd) + Enparameter(phone) + Enparameter(order) + Enparameter(M) + key

        var url = "http://103.47.139.66:8088/flowInterface/API/Aflowrecharge.ashx";
        var html = GetHTML(url, dict, 0, null);
        //CallAPI(url);
        var rlt = Shotgun.Library.Static.JsonParser<Result>(html);
        UpdateSpResult(rlt.retcode.ToString(), rlt.err_msg);

        return rlt.retcode == 1;
    }

    public class Result
    {

        public int retcode { get; set; }

        public string err_msg { get; set; }

        public string orderid { get; set; }
    }



    private string Enparameter(string str)
    {
        return ReplaceStr(encPassword(str, key, IV));
    }

    /// <summary>
    /// 加密
    /// </summary>
    public string encPassword(string toEncrypt, string AESKEY, string AESIV)
    {
        try
        {
            byte[] keyArray = UTF8Encoding.UTF8.GetBytes(AESKEY);
            byte[] ivArray = UTF8Encoding.UTF8.GetBytes(AESIV);
            byte[] toEncryptArray = UTF8Encoding.UTF8.GetBytes(toEncrypt);
            RijndaelManaged rDel = new RijndaelManaged();
            rDel.Key = keyArray;
            rDel.IV = ivArray;
            rDel.Mode = CipherMode.CBC;
            rDel.Padding = PaddingMode.Zeros;
            ICryptoTransform cTransform = rDel.CreateEncryptor();
            byte[] resultArray = cTransform.TransformFinalBlock(toEncryptArray, 0, toEncryptArray.Length);
            return Convert.ToBase64String(resultArray, 0, resultArray.Length);
        }
        catch
        { return null; }
    }
    /// <summary>
    /// 解密
    /// </summary>
    /// <param name="toDecrypt"></param>
    /// <returns></returns>
    public string decPassword(string toDecrypt, string AESKEY, string AESIV)
    {
        try
        {
            byte[] keyArray = UTF8Encoding.UTF8.GetBytes(AESKEY);
            byte[] ivArray = UTF8Encoding.UTF8.GetBytes(AESIV);
            byte[] toEncryptArray = Convert.FromBase64String(toDecrypt);
            RijndaelManaged rDel = new RijndaelManaged();
            rDel.Key = keyArray;
            rDel.IV = ivArray;
            rDel.Mode = CipherMode.CBC;
            rDel.Padding = PaddingMode.Zeros;
            ICryptoTransform cTransform = rDel.CreateDecryptor();
            byte[] resultArray = cTransform.TransformFinalBlock(toEncryptArray, 0, toEncryptArray.Length);
            return UTF8Encoding.UTF8.GetString(resultArray);
        }
        catch
        { return null; }
    }
    //替换特殊字符 (依次进行替换)
    private string ReplaceStr(string str)
    {
        string retstr = str.Replace("=", "."); //把“=”替换成“.”       （点）
        retstr = retstr.Replace("/", "-");   //把 “/” 替换成 “-”    （减号）
        retstr = retstr.Replace("+", "*");  //把 “+” 替换成  “*”    （星号）
        return retstr;
    }

}