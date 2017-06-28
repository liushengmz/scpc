using LightDataModel;
using Shotgun.Model.Filter;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class report_cp_report : Shotgun.PagePlus.ShotgunPage
{
    protected decimal totalPrice;
    protected int totalCount;
    protected void Page_Load(object sender, EventArgs e)
    {
        var sql = string.Format("select mr.*,tbl_cp.short_name,T.trone_name,mr.c * T.price as amount ,T.price,0 push,0 hold,0.0 rx"
            + " from( "
            + "SELECT trone_order_id, COUNT(0) c,min(cp_id) cp_id ,min(trone_id) trone_id FROM daily_log.tbl_mr_{0:yyyyMM} where cp_id!=34 and mr_date='{0:yyyy-MM-dd}'  GROUP BY trone_order_id"
            + " ) mr"
            + " left join tbl_cp on tbl_cp.id= mr.cp_id"
            + " left join tbl_trone T on T.id=mr.trone_id"
            + " order by tbl_cp.short_name asc",
                DateTime.Now);
        var dt = dBase.GetDataTable(sql);
        rpList.DataSource = dt;

        var troneIds = new List<int>();
        foreach (DataRow dr in dt.Rows)
        {
            totalCount += Convert.ToInt32(dr["c"]);
            totalPrice += (decimal)dr["amount"];
            var tid = (int)dr["trone_order_id"];
            if (!troneIds.Contains(tid))
                troneIds.Add(tid);
        }

        string idstr = string.Join(",", troneIds);
        sql = string.Format(" select trone_order_id,count(0) from daily_log.tbl_mr_{0:yyyyMM} where mr_date='{0:yyyy-MM-dd}' "
                    + " and syn_flag=1 and trone_order_id in({1}) group by trone_order_id", DateTime.Today, idstr);

        var pDt = dBase.GetDataTable(sql);
        dt.PrimaryKey = new DataColumn[] { dt.Columns["trone_order_id"] };


        var dv = dt.DefaultView;
        dv.Sort = "trone_order_id";
        foreach (DataRow dr in pDt.Rows)
        {
            var drvs = dv.FindRows(dr[0]);
            if (drvs == null && drvs.Length != 1)
                continue;
            var drv = drvs[0];
            // var rpRow=drv.
            var pushCount = (long)dr[1];
            drv["push"] = pushCount;
            drv["hold"] = (long)drv["c"] - pushCount;
            drv["rx"] = Convert.ToDecimal((long)drv["hold"] * 100f / (long)drv["c"]);
        }
        dv.Sort = "short_name";




    }


}