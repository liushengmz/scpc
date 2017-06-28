using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class MasterPage : System.Web.UI.MasterPage
{
    bool isBined = false;
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!isBined)
            this.DataBind(false);
        Shotgun.PagePlus.PageTitleFixer.AppendTitle("浩天垫付平台");
    }


    protected override void OnDataBinding(EventArgs e)
    {
        isBined = true;
        base.OnDataBinding(e);
    }
}
