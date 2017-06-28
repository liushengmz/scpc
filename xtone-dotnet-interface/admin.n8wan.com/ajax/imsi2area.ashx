<%@ WebHandler Language="C#" Class="imsi2area" %>

using System;
using System.Web;

public class imsi2area : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    public override void BeginProcess()
    {
        Ajax = new Shotgun.Library.simpleAjaxResponser();
        string imsi = Request["imsi"];
        var Num = n8wan.Public.Library.GetPhoneByImsi(imsi);
        if (string.IsNullOrEmpty(Num))
        {
            Ajax.message = "未知IMSI";
            return;
        }
        int spNum = int.Parse(Num);
        var cityInfo = LightDataModel.tbl_phone_locateItem.GetRowByMobile(dBase, spNum);
        Ajax.message = string.Format("{0},{1},{2}", spNum, cityInfo.province_id, cityInfo.id);
    }
}