<%@ WebHandler Language="C#" Class="yh_3rd_stdcall" %>

using System;
using System.Web;
using System.Text;
public class yh_3rd_stdcall : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    string orderId, userId, gatewayCode, productCode, code, paymentTime, province, fee, sign;


    protected override Shotgun.Database.IBaseDataClass2 CreateDBase()
    {

        Shotgun.Database.IBaseDataClass2 db = new Shotgun.Database.MySqlDBClass(false, "129Mysql");
        return db;
    }

    public override void BeginProcess()
    {
        string err = Init();
        if (!string.IsNullOrEmpty(err))
        {
            Response.Write("error 参数不能为空");
            return;
        }


        var sb = new StringBuilder("Insert into any_log.tbl_yh_log(order_id, user_id, gateway_code, product_code, code, payment_time, province, fee,  sender_ip) Values(");
        sb.AppendFormat("'{0}',", dBase.SqlEncode(orderId));
        sb.AppendFormat("'{0}',", dBase.SqlEncode(userId));
        sb.AppendFormat("'{0}',", dBase.SqlEncode(gatewayCode));
        sb.AppendFormat("'{0}',", dBase.SqlEncode(productCode));
        sb.AppendFormat("'{0}',", dBase.SqlEncode(code));
        sb.AppendFormat("'{0}',", dBase.SqlEncode(paymentTime));
        sb.AppendFormat("'{0}',", dBase.SqlEncode(province));
        sb.AppendFormat("'{0}',", dBase.SqlEncode(fee));
        sb.AppendFormat("'{0}')", Request.UserHostAddress);
        sb.Append(" ON DUPLICATE KEY UPDATE fee=fee;");
        dBase.ExecuteNonQuery(sb.ToString());

        Response.Write("ok");
    }

    string Init()
    {
        orderId = Request["orderId"]; if (string.IsNullOrEmpty(orderId)) return "订单ID(流水号，唯一)";
        userId = Request["userId"]; if (string.IsNullOrEmpty(userId)) return "用户ID(用户ID信息)";
        gatewayCode = Request["gatewayCode"]; if (string.IsNullOrEmpty(gatewayCode)) return "资费编码(平台分配)";
        productCode = Request["productCode"]; if (string.IsNullOrEmpty(productCode)) return "产品编码(平台分配)";
        code = Request["code"]; if (string.IsNullOrEmpty(code)) return "公司编码(平台分配)";
        paymentTime = Request["paymentTime"]; if (string.IsNullOrEmpty(paymentTime)) return "时间(YYYYMMddHHmmss)";
        province = Request["province"]; if (string.IsNullOrEmpty(province)) return "省份(参照下方省份名称,不确定省份标注未知)";
        fee = Request["fee"]; if (string.IsNullOrEmpty(fee)) return "资费";
        sign = Request["sign"]; if (string.IsNullOrEmpty(sign)) return "签名(加密key)";
        return null;
    }
}