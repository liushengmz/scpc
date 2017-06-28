using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace n8wan.Public.Logical
{
    public class AppUserFixSync : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
    {

        public override void BeginProcess()
        {
            var appItem = new tbl_xy_userItem();
            if (appItem == null)
                return;

            appItem.appkey = Request["AppKey"];
            appItem.channelkey = Request["ChannelKey"];
            appItem.imsi = Request["Imsi"];
            appItem.imei = Request["Imei"];
            appItem.addTime = DateTime.Parse(Regex.Replace(Request["AddTime"], @"(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})", "$1/$2/$3 $4:$5:$6"));

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



        private void WriteError(string p)
        {
            Response.Write("Error " + p);
        }
    }
}
