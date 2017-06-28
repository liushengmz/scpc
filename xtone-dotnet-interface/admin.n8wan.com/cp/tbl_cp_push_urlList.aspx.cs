using LightDataModel;
using Shotgun.Model.Filter;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Library;

public partial class tbl_cp_push_urlList : Shotgun.PagePlus.ShotgunPage
{
    protected int id;
    private List<tbl_cpItem> _cps;
    protected void Page_Load(object sender, EventArgs e)
    {


        var l = LightDataModel.tbl_cp_push_urlItem.GetQueries(dBase);
        l.PageSize = int.MaxValue;
        if (int.TryParse(Request["id"], out id))
            l.Filter.AndFilters.Add(tbl_cp_push_urlItem.Fields.cp_id, id);


        var dt = l.GetDataList();

        var cpids = dt.GetFieldValueArray<int>(tbl_cp_push_urlItem.Fields.cp_id);
        rpLst.DataSource = dt;
        _cps = tbl_cpItem.GetCPsByIds(dBase, cpids, new string[] { tbl_cpItem.Fields.id, tbl_cpItem.Fields.short_name });


        this.DataBind();
    }

    public string GetCP(int cpId)
    {
        if (cpId == 0)
            return "未指定";
        var item = _cps.Find(e => e.id == cpId);
        if (item == null)
            return cpId.ToString();
        return item.short_name;
    }
}