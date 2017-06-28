using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class sp_tbl_troneList : Shotgun.PagePlus.ShotgunPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        var l = LightDataModel.tbl_troneItem.GetQueries(dBase);
        int id;
        if (int.TryParse(Request["urlId"], out id))
            l.Filter.AndFilters.Add(LightDataModel.tbl_troneItem.Fields.sp_api_url_id, id);
        else if (int.TryParse(Request["spid"], out id))
            l.Filter.AndFilters.Add(LightDataModel.tbl_troneItem.Fields.sp_id, id);
        l.PageSize = int.MaxValue;

        l.SortKey.Add(LightDataModel.tbl_troneItem.Fields.status, Shotgun.Model.Filter.EM_SortKeyWord.desc);
        rpLst.DataSource = l.GetDataList();
        rpLst.ItemDataBound += rpLst_ItemDataBound;
        this.DataBind();

    }

    void rpLst_ItemDataBound(object sender, RepeaterItemEventArgs e)
    {
        if (e.Item.ItemType != ListItemType.Item && e.Item.ItemType != ListItemType.AlternatingItem)
            return;
        var trone = (tbl_troneItem)e.Item.DataItem;
        var l = tbl_trone_orderItem.GetQueries(dBase);
        l.Fields = new string[] { tbl_trone_orderItem.Fields.cp_id, tbl_trone_orderItem.Fields.order_num, tbl_trone_orderItem.Fields.disable,
            tbl_trone_orderItem.Fields.id, tbl_trone_orderItem.Fields.is_dynamic };
        l.Filter.AndFilters.Add(tbl_trone_orderItem.Fields.trone_id, trone.id);
        l.Filter.AndFilters.Add(tbl_trone_orderItem.Fields.is_unknow, false);
        l.SortKey.Add(tbl_trone_orderItem.Fields.disable, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        l.SortKey.Add(tbl_trone_orderItem.Fields.id, Shotgun.Model.Filter.EM_SortKeyWord.desc);

        l.PageSize = int.MaxValue;
        var dt = l.GetDataList();
        if (dt.Count == 0)
            return;

        var c = (Repeater)e.Item.FindControl("rpUsing");
        c.DataSource = dt;
        c.DataBind();
    }

    protected string GetCpName(int userId)
    {
        return LightDataModel.tbl_cpItem.GetCPNameById(dBase, userId, true);
    }

}