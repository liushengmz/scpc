using Shotgun.Library;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class cp_PoolSetEditor : Shotgun.PagePlus.ShotgunPage
{
    int poolid;
    protected void Page_Load(object sender, EventArgs e)
    {
        int.TryParse(Request["id"], out poolid);

        if (Request.HttpMethod == "POST")
        {
            SaveData();
            return;
        }

        var m = LightDataModel.tbl_cp_poolItem.GetRowById(dBase, poolid);
        if (m == null)
        {
            RedirectFromPage("参数无效");
            return;
        }
        var sql = "select tno.id paycode, tno.trone_id,trn.trone_name,trn.price,trn.sp_trone_id,strn.name,cps.cp_pool_id,strn.sp_id,strn.provinces,"
                + " (strn.status=1 and trn.status=1 and tno.Disable=0 ) status  from tbl_trone_order tno "
                + " left join tbl_trone trn on trn.id= tno.trone_id"
                + " left join tbl_sp_trone strn on strn.id= trn.sp_trone_id "
                + " left join (select * from  tbl_cp_pool_set where cp_pool_id=" + poolid + " ) cps  on tno.id= cps.trone_order_id"
                + " where cp_id=" + m.cp_id.ToString() + " and trn.price=" + (m.fee / 100).ToString() + " order by cp_pool_id desc ,status desc , paycode desc";
        //Response.Write(sql);
        using (var dt = dBase.GetDataTable(sql))
        {
            rplst.DataSource = dt;
            rplst.DataBind();
        }
    }

    private void SaveData()
    {
        var str = Request["troneOrderId"];
        if (string.IsNullOrEmpty(str))
        {
            Static.ClientRedirect("PoolSetPriorityEditor.aspx?returnURL=" + Server.UrlEncode(Request["returnURL"]) + "&id=" + poolid.ToString(), "无添加操作");
            return;
        }
        var ids = str.Split(new char[] { ',' });
        int i;
        var sb = new System.Text.StringBuilder();
        sb.Append("INSERT INTO `daily_config`.`tbl_cp_pool_set` (`cp_pool_id`, `trone_order_id`, `priority`, `status`) VALUES");
        bool iHas = false;
        foreach (var t in ids)
        {
            if (!int.TryParse(t, out i))
                continue;
            if (iHas)
                sb.Append(",");
            sb.AppendFormat("({0}, {1},30, 1)", poolid, i);
            iHas = true;
        }
        if (iHas)
        {
            dBase.ExecuteNonQuery(sb.ToString());
        }
        Static.ClientRedirect("PoolSetPriorityEditor.aspx?returnURL=" + Server.UrlEncode(Request["returnURL"]) + "&id=" + poolid.ToString(), "操作成功");
    }
}