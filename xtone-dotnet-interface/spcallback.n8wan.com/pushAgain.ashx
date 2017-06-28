<%@ WebHandler Language="C#" Class="pushAgain" %>

using System;
using System.Web;
using n8wan.Public.Logical;
using System.Text.RegularExpressions;
using System.Linq;

public class pushAgain : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{

    public override void BeginProcess()
    {

        var m = Request["month"];
        var ids = Request["data"];
        AgainPusher ap;
        if (string.IsNullOrEmpty(m) || string.IsNullOrEmpty(ids))
        {
            ap = AgainPusher.getInstance();
            if (ap == null)
            {
                Response.Write(string.Format("已经完成进度{0}% ", ((AgainPusher.GetPercent() & 0xff) * 100) / 256f));
            }
            else
            {
                Response.Write("暂无任务/已经完成\n");
                var f = AgainPusher.GetLastLogFile();
                if (f != null)
                {
                    Response.Write("<a href='pushlog/" + f + "?" + DateTime.Now.Ticks + "' >查看最近补发日志:" + f + "</a>");
                }

            }
            return;
        }
        if (!Regex.IsMatch(m, @"20\d{4}"))
        {
            Response.Write("日期格式错误！");
            return;
        }
        if (!Regex.IsMatch(ids, @"^\d+(,\d+){0,}$"))
        {
            Response.Write("数据错误");
            return;
        }


        var mrDate = DateTime.Parse(Regex.Replace(m, @"(\d{4})(\d{2})", "$1-$2-01"));
        var ars = ids.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);

        int[] allId = new int[ars.Length];
        for (int i = 0; i < allId.Length; i++)
        {
            allId[i] = int.Parse(ars[i]);
        }

        ap = AgainPusher.getInstance();
        if (ap == null)
        {
            Response.Write(string.Format("上一个任务尚未完成！已经完成进度{0}% ", (AgainPusher.GetPercent() & 0xff) / 256f));
            return;
        }

        ap.MrIds = allId;
        ap.MRDate = mrDate;
        ap.DoAgain();

        //ap.DoAgain();
        Response.Redirect(string.Format("pushagain.html?{0}", allId.Length));
    }
}