using Shotgun.Library;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class cp_CreatePoolSet : Shotgun.PagePlus.ShotgunPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (Request.HttpMethod != "POST")
            return;
        var model = new LightDataModel.tbl_cp_poolItem();

        var s = Request["name"];
        model.name = s;
        int i;
        int.TryParse(Request["price"], out i);
        model.fee = i * 100;

        s = Request["cpid"];
        i = s.IndexOf(',');
        if (i != -1)
            s = s.Substring(0, i);
        int.TryParse(s, out i);
        model.cp_id = i;
        model.status = true;


        if (model.cp_id == 0 || model.fee == 0 || string.IsNullOrEmpty(model.name))
        {
            Static.alert("所有功均为必填项");
            return;
        }

        model.SaveToDatabase(dBase);
        Static.ClientRedirect("PoolSetEditor.aspx?returnUrl=./poolsetlist.aspx&id=" + model.id, "创建成功");


    }
}