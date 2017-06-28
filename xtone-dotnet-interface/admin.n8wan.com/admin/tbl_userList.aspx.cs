using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class admin_tbl_userList : Shotgun.PagePlus.ShotgunPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        var l = LightDataModel.tbl_userItem.GetQueries(dBase);
        var dt = l.GetDataList();
        
    }
}