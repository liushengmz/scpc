using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class imsi2area : Shotgun.PagePlus.ShotgunPage
{
    protected void Page_Load(object sender, EventArgs e)
    {

        var pl = LightDataModel.tbl_provinceItem.GetQueries(dBase);
        pl.PageSize = int.MaxValue;
        pl.Fields = new string[] { LightDataModel.tbl_provinceItem.Fields.id, LightDataModel.tbl_provinceItem.Fields.name };

        var pms = pl.GetDataList();
        var sb = new StringBuilder(250);
        foreach (var m in pms)
        {
            sb.AppendFormat("provices[{0}]='{1}';", m.id, m.name);
        }

        var cl = LightDataModel.tbl_cityItem.GetQueries(dBase);
        cl.PageSize = int.MaxValue;
        cl.Fields = new string[] { LightDataModel.tbl_cityItem.Fields.id, LightDataModel.tbl_cityItem.Fields.name };

        var cms = cl.GetDataList();
        foreach (var m in cms)
        {
            sb.AppendFormat("citys[{0}]='{1}';", m.id, m.name);
        }
        var sc = new System.Web.UI.HtmlControls.HtmlGenericControl("script");
        sc.Attributes["type"] = "text/javascript";
        sc.InnerHtml = sb.ToString();
        Header.Controls.Add(sc);


    }
}