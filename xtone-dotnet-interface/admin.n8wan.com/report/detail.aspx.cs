using LightDataModel;
using Shotgun.Library;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class report_detail : Shotgun.PagePlus.ShotgunPage
{
    protected PageSpliter PS;
    protected void Page_Load(object sender, EventArgs e)
    {
        var l = tbl_mrItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_mrItem.Fields.trone_id, 0, Shotgun.Model.Filter.EM_DataFiler_Operator.Less);
        l.PageSize = 200;
        l.Fields = new string[] { tbl_mrItem.Fields.id, tbl_mrItem.Fields.ori_order, tbl_mrItem.Fields.ori_trone, 
                tbl_mrItem.Fields.sp_api_url_id, tbl_mrItem.Fields.sp_id, tbl_mrItem.Fields.create_date };
        l.SortKey.Add(tbl_mrItem.Fields.id, Shotgun.Model.Filter.EM_SortKeyWord.desc);
        var urlids = l.GetColumnList<int>("distinct sp_api_url_id");
        var url_list = LightDataModel.tbl_sp_api_urlItem.GetQueries(dBase);
        url_list.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.id, urlids);
        url_list.SortKey.Add(tbl_sp_api_urlItem.Fields.name, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        url_list.PageSize = int.MaxValue;
        rpSel.DataSource = url_list.GetDataList();

        int uid;
        if (int.TryParse(Request["urlId"], out uid))
            l.Filter.AndFilters.Add(tbl_mrItem.Fields.sp_api_url_id, uid);

        PS = new PageSpliter();
        PS.NoTotalPage = false;
        if (!PS.IsFromURL)
            PS.CalcPageCount(l.TotalCount, l.PageSize);
        l.CurrentPage = l.CurrentPage;

        rpList.DataSource = l.GetDataList();
    }
}