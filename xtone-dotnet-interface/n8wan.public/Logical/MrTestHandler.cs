using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace n8wan.Public.Logical
{
    public class MrTestHandler : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
    {
        DateTime nightStart, nightEnd;
        public override void BeginProcess()
        {
            object obj;
            try
            {
                obj = dBase.ExecuteScalar(string.Format("select create_date from `daily_log`.`tbl_mr_{0:yyyyMM}` order by id desc limit 1", DateTime.Now));
            }
            catch (Exception ex)
            {
                throw new System.Web.HttpException(417, ex.Message);
            }
            DateTime lastDate = DateTime.Today;
            if (obj != null)
                lastDate = (DateTime)obj;
            int MaxTime;
            //night=23-8&nightspan=30&day=10
            //var t = Request["night"];
            if (IsNight())
            {
                if (!int.TryParse(Request["nightspan"], out MaxTime))
                    int.TryParse(Request["day"], out MaxTime);
            }
            else
                int.TryParse(Request["day"], out MaxTime);

            if (MaxTime < 5)
                MaxTime = 5;

            if ((DateTime.Now - lastDate).TotalMinutes > MaxTime)
                throw new System.Web.HttpException(504, "MR Timeout");
            Response.ContentType = "text/plain";
            Response.Write("last Time:");
            Response.Write(lastDate);
            Response.Write("\n-------setting-------\n");
            Response.Write(string.Format("current span:{0}min\n", MaxTime));
            Response.Write(string.Format("nigth time:{0:HH:mm:ss} - {1:HH:mm:ss}\n", nightStart, nightEnd));
            Response.Write(string.Format("nigth span:{0}min\n", (Request["nightspan"] ?? "N/A ")));
            Response.Write(string.Format("day span:{0}min\n", (Request["day"] ?? "N/A ")));
        }

        private bool IsNight()
        {//night=23-8
            var t = Request["night"];
            if (string.IsNullOrEmpty(t))
                return false;
            var ar = t.Split(new char[] { '-' });
            if (ar.Length != 2)
                return false;
            int s, e;
            int.TryParse(ar[0], out s);
            int.TryParse(ar[1], out e);

            if (s == e)
                return false;
            if (s > 12)
                nightStart = DateTime.Today.AddHours(s - 24);
            else
                nightStart = DateTime.Today.AddHours(s);
            nightEnd = DateTime.Today.AddHours(e);
            return DateTime.Now >= nightStart && DateTime.Now < nightEnd;
        }
    }
}
