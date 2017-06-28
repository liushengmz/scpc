using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class report_Default : Shotgun.PagePlus.ShotgunPage
{
    protected int totalCount;
    protected decimal totalPrice;
    protected void Page_Load(object sender, EventArgs e)
    {
        var sql = "SELECT \n"
                + "  mr.trone_id  ,  t.sp_id,tbl_sp.short_name,t.orders ,t.trone_name,t.price, t.price * mr.c AS total, mr.c,\n"
                + "0 hold,0 push,0.0 rx FROM\n"
                + "    tbl_trone t\n"
                + "    left join tbl_sp\n"
                + "    on tbl_sp.id=t.sp_id\n"
                + "        RIGHT JOIN\n"
                + "    (SELECT \n"
                + "        trone_id, COUNT(0) c\n"
                + "    FROM\n"
                + "        daily_log.tbl_mr_{0:yyyyMM}\n"
                + "    WHERE\n"
                + "        trone_id > 0 AND mr_date = '{0:yyyy-MM-dd}'\n"
                + "    GROUP BY trone_id) mr ON mr.trone_id = t.id\n"
                + "ORDER BY sp_id, trone_num,price";

        var dt = dBase.GetDataTable(string.Format(sql, DateTime.Now));
        rpList.DataSource = dt;
        var troneIds = new List<int>();
        foreach (DataRow dr in dt.Rows)
        {
            totalCount += Convert.ToInt32(dr["c"]);
            totalPrice += (decimal)dr["total"];
            var tid = (int)dr["trone_id"];
            if (!troneIds.Contains(tid))
                troneIds.Add(tid);
        }
        string idstr = string.Join(",", troneIds);
        sql = string.Format(" select trone_id,count(0) from daily_log.tbl_mr_{0:yyyyMM} where mr_date='{0:yyyy-MM-dd}' "
                    + " and syn_flag=1 and trone_id in({1}) group by trone_id", DateTime.Today, idstr);

        var pDt = dBase.GetDataTable(sql);
        dt.PrimaryKey = new DataColumn[] { dt.Columns["trone_id"] };


        var dv = dt.DefaultView;
        dv.Sort = "trone_id";
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



    }
}