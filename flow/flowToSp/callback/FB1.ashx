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

        OrderInfo.SpStatus = Request["state"];
        OrderInfo.SpErrorMsg = Request["stateinfo"];

        switch (OrderInfo.SpStatus)
        {
            case "1": OrderInfo.StatusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.Success; break;
            case "0": OrderInfo.StatusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.Charging; break;
            case "-1": OrderInfo.StatusE = FlowLibraryNet.Logical.ChangeOrderStatusEnum.ChargFail; break;
        }
    }

}