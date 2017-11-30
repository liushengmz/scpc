<%@ WebHandler Language="C#" Class="Fc1" %>

using System;
using System.Web;
using System.Text;
using System.Linq;
using System.Security.Cryptography;
using System.Collections.Generic;

public class Fc1 : FlowLibraryNet.Logical.FlowChargeHandler
{



    const string url = "http://120.77.62.55:8080/api.aspx";
    const string account = "hudong";
    const string key = "0a3dfd4d245c476fa9aecb11829f01ee";

    protected override bool DoCharge()
    {
        var dict = new System.Collections.Generic.Dictionary<string, string>();
        dict["action"] = "charge";//命令
        dict["v"] = "1.1";//版本号
        dict["range"] = OrderInfo.Rang.ToString();//流量类型
        dict["outTradeNo"] = OrderInfo.OrderId;//商户订单号
        dict["account"] = account;//帐号 (签名)
        dict["mobile"] = OrderInfo.Mobile;//号码 (签名)
        dict["package"] = getPackage();//套餐 (签名)

        DoSign(dict);

        var html = GetHTML(url, dict, 10, null);
        if (string.IsNullOrEmpty(html))
            return SetError("网关超时");

        var rlt = Shotgun.Library.Static.JsonParser<PackagesResult>(html);

        UpdateSpResult(rlt.Code, rlt.Message);
        if (rlt.Code != "0")
            return SetError(rlt.Message, FlowLibraryNet.Logical.ChangeErrorEnum.ChargeFail);

        return SetSuccess();
    }

    string getPackage()
    {
        var dict = new System.Collections.Generic.Dictionary<string, string>();

        dict["action"] = "getPackage";//命令
        dict["v"] = "1.1";//版本号
        dict["account"] = account;//帐号(签名)
        dict["range"] = OrderInfo.Rang.ToString();//流量类型
        dict["type"] = (new string[] { "0", "2", "3", "1", "0" })[(int)OrderInfo.Operator];// (签名)0:不指定, 1:移动, 2:联通, 3:电信
        DoSign(dict);
        var html = GetHTML(url, dict, 10, null);
        if (string.IsNullOrEmpty(html))
            return null;
        var rlt = Shotgun.Library.Static.JsonParser<PackagesResult>(html);
        var pInfo = rlt.Packages.FirstOrDefault(e => e.Name == this.FlowSizeInfo.name);
        return pInfo != null ? pInfo.Package : null;

    }


    void DoSign(Dictionary<string, string> dict)
    {
        string[] keys = { "account", "mobile", "package", "type" };
        var data = string.Join("&", dict.Where(e => keys.Contains(e.Key, StringComparer.OrdinalIgnoreCase)).Select(e => string.Format("{0}={1}", e.Key, e.Value)));
        data += "&key=" + key;
        dict["sign"] = Shotgun.Library.SecurityUtil.MD5(data);
        //WriteLog("md5 src：{0}", data);
    }





    public class PackagesItem
    {
        /// <summary>
        /// 0:不指定, 1:移动, 2:联通, 3:电信
        /// </summary>
        public string Type { get; set; }
        public string Package { get; set; }
        public string Name { get; set; }
        public string Price { get; set; }
    }

    public class PackagesResult
    {
        public string Code { get; set; }
        public string Message { get; set; }

        public List<PackagesItem> Packages { get; set; }
    }



}