using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    public class AppFixSync : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
    {

        public override void BeginProcess()
        {
            var appItem = GetExistOrderId();
            if (appItem == null)
                return;

            appItem.appkey = Request["appkey"];
            appItem.amount = Request["amount"];
            appItem.orderid = Request["orderid"];
            appItem.imsi = Request["imsi"];
            appItem.imei = Request["imei"];
            appItem.appname = Request["appname"];
            appItem.appsubject = Request["appsubject"];
            appItem.channelid = Request["channelid"];
            appItem.createdate = DateTime.Parse(Request["createdate"]);
            appItem.oprator = Request["oprator"];
            appItem.mobileno = Request["mobileno"];
            appItem.userorderid = Request["userorderid"];
            appItem.status = Request["status"];
            try
            {
                dBase.SaveData(appItem);
            }
            catch
            {
                WriteError("database busy");
                return;
            }
            Response.Write("ok");
        }

        private LightDataModel.tbl_xypayItem GetExistOrderId()
        {
            var orderId = Request["orderId"];
            if (string.IsNullOrEmpty(orderId))
            {
                WriteError("orderId not found");
                return null;
            }
            var l = tbl_xypayItem.GetQueries(dBase);
            l.Filter.AndFilters.Add(tbl_xypayItem.Fields.orderid, orderId);
            l.Fields = new string[] { tbl_xypayItem.Fields.id };
            var m = l.GetRowByFilters();
            if (m == null)
            {
                m = new tbl_xypayItem();
                m.orderid = orderId;
                return m;
            }
            WriteError(orderId + " existed");
            return null;
        }

        private void WriteError(string p)
        {
            Response.Write("Error " + p);
        }
    }
}
