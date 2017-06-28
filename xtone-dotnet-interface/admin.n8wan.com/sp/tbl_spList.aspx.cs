using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class sp_tbl_spList : Shotgun.PagePlus.ShotgunPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        var l = LightDataModel.tbl_spItem.GetQueries(dBase);
        l.PageSize = int.MaxValue;
        var dt = l.GetDataList();
        rpLst.DataSource = dt;
        rpLst.DataBind();

    }
}