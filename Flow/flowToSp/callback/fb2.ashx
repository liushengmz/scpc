<%@ WebHandler Language="C#" Class="fb2" %>

using System;
using System.Web;

public class fb2 : FlowLibraryNet.Logical.FlowCallbackHandler
{

    string _linkid;
    protected override string GetLinkId()
    {
        if (_linkid != null)
            return _linkid;
        return _linkid = Request["OutTradeNo"];
    }

    protected override void UpdateOrderInfo()
    {
        OrderInfo.SpStatus = Request["Status"];
        OrderInfo.SpErrorMsg = Request["ReportCode"];

        switch (OrderInfo.SpStatus)
        {
            case "4": OrderInfo.StatusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.Success; break;
            default: OrderInfo.StatusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.ChargFail; break;
        }
    }

}