<%@ WebHandler Language="C#" Class="FB1" %>

using System;
using System.Web;

public class FB1 : FlowLibraryNet.Logical.FlowCallbackHandler
{
    string _linkid;
    protected override string GetLinkId()
    {
        if (_linkid != null)
            return _linkid;
        return _linkid = Request["orderid"];
    }

    protected override void UpdateOrderInfo()
    {

        OrderInfo.sp_status = Request["state"];
        OrderInfo.sp_error_msg = Request["stateinfo"];

        switch (OrderInfo.sp_status)
        {
            case "1": OrderInfo.statusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.Success; break;
            case "0": OrderInfo.statusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.Charging; break;
            case "-1": OrderInfo.statusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.ChargFail; break;
        }
    }

}