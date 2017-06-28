<%@ WebHandler Language="C#" Class="data" %>

using System;
using System.Web;

public class data : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    public override void BeginProcess()
    {
        dBase.Connection.ChangeDatabase("game_log");

        var t = dBase.ExecuteScalar("select id from tbl_zyl_detail where  userid='" + dBase.SqlEncode(Request["userid"]) + "' and  create_Time > date( now() ) ");
        if (t != null)
        {
            Response.Write("Ok");
            return;
        }

        var m = new LightDataModel.tbl_zyl_detailItem();
        m.appkey = Request["appkey"];
        m.channel = "bsfb-" + Request["channel"]; //channel:bsfb+对方传过来的数据
        m.userid = Request["userid"]; //userid:玩家ID
        m.useracount = Request["useracount"];//useracount:玩家帐号
        m.nickname = Request["nickname"];        //nickname:昵称
        short s;
        short.TryParse(Request["status"], out s);

        m.status = s; //status:状态（0停用，1正常）
        try
        {
            m.amount = decimal.Parse(Request["amount"]);  //amount:总充值金额
            m.amount_times = int.Parse(Request["amount_times"]);        //amount_times:总充值笔数
            m.regist_time = DateTime.Parse(Request["regist_time"]); //regist_time:注册时间 yyyy-MM-dd hh:mm:ss
            m.last_login_time = DateTime.Parse(Request["last_login_time"]); //last_login_time:最后登录时间 yyyy-MM-dd hh:mm:ss
        }
        //catch
        //{
        //    return;
        //}
        finally { }
        dBase.SaveData(m);
        Response.Write("Ok");
    }
}