using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool.Handler
{
    public class UpdateUserLimitHandler : System.Web.IHttpHandler
    {
        public bool IsReusable
        {
            get { return true; }
        }

        public void ProcessRequest(System.Web.HttpContext context)
        {
            //troneId={0}&mrDate={1:yyyy-MM-dd}&city_id={2}&customId={3}
            var troneId = int.Parse(context.Request["troneId"]);
            var mrDate = DateTime.Parse(context.Request["mrDate"]);
            var cityId = int.Parse(context.Request["city_id"]);
            var customId = context.Request["customId"];
            using (var db = new Shotgun.Database.MySqlDBClass())
            {
                Dao.CustomFee.UpdateLimit(db, troneId, customId, mrDate);
            }
        }
    }
}
