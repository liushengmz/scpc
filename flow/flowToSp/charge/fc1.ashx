<%@ WebHandler Language="C#" Class="Fc1" %>

using System;
using System.Web;
using System.Text;
using System.Linq;
using System.Security.Cryptography;

public class Fc1 : FlowLibraryNet.Logical.FlowChargeHandler
{
    string IV = "JwFDWXRG8JGVaiIV";
    string key = "TRDABvX5eWjDqKEY";
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
        dict["userid"] = username;
        dict["userpwd"] = Enparameter(pwd);
        dict["phone"] = Enparameter(OrderInfo.Mobile);
        dict["orderid"] = Enparameter(OrderInfo.SpOrderId);

        dict["flowvalue"] = Enparameter(FlowSizeInfo.GetSizeName(LightDataModel.tbl_f_basic_priceItem.FlowSizeUnitEnum.Auto));//流量ID

        var md5Src = string.Join(string.Empty, dict.Values) + key;
        dict["md5str"] = System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(md5Src, "MD5");

        dict["returl"] = FlowLibraryNet.Logical.FlowCallbackHandler.GetCallbackUrl(OrderInfo.sp_api_id);


        //encMD5(username + Enparameter(pwd) + Enparameter(phone) + Enparameter(order) + Enparameter(M) + key
        //return true;
        var url = "http://103.47.139.66:8088/flowInterface/API/Aflowrecharge.ashx";
        var html = GetHTML(url, dict, 0, null);
        //CallAPI(url);
        var rlt = Shotgun.Library.Static.JsonParser<Result>(html);
        string msg = null;
        switch (rlt.err_msg)
        {
            case "-30": msg = "-30,账户余额不足"; break;
            case "-31": msg = "-31,商品价格错误"; break;
            case "-32": msg = "-32,手机号码错误"; break;
            case "-33": msg = "-33,账户错误或密码错误"; break;
            case "-34": msg = "-34,商品错误"; break;
            case "-35": msg = "-35,签名错误"; break;
            case "-36": msg = "-36,订单重复提交"; break;
            case "-37": msg = "-37,商品不存在"; break;
            case "-38": msg = "-38,接口数据异常"; break;
            case "-39": msg = "-39,提交失败"; break;
            case "-40": msg = "-40,ip鉴权不通过"; break;
            case "-41": msg = "-41,当月充值次数受限"; break;
            case "-42": msg = "-42,订购异常"; break;
            case "-43": msg = "-43,号段判断失败"; break;
            case "-44": msg = "-44,账号已禁用"; break;
            case "-45": msg = "-45,订单不存在"; break;
            case "-46": msg = "-46,充值受限"; break;
            case "-47": msg = "-47,请求方式错误"; break;
            case "0": msg = "0,订单创建成功"; break;
            default: msg = rlt.err_msg + ",Unkonw msg"; break;
        }

        UpdateSpResult(rlt.retcode.ToString(), msg);

        if (rlt.retcode == 1)
            return SetSuccess();
        return SetError(msg, FlowLibraryNet.Logical.ChangeErrorEnum.ChargeFail);
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
        catch (Exception ex)
        {

            return null;
        }
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
        if (string.IsNullOrEmpty(str))
            return str;
        string retstr = str.Replace("=", "."); //把“=”替换成“.”       （点）
        retstr = retstr.Replace("/", "-");   //把 “/” 替换成 “-”    （减号）
        retstr = retstr.Replace("+", "*");  //把 “+” 替换成  “*”    （星号）
        return retstr;
    }

}