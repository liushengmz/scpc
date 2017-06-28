using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Library;

public partial class sp_tbl_sp_api_urlList : Shotgun.PagePlus.ShotgunPage
{
    private List<tbl_spItem> _SpNames;
    protected void Page_Load(object sender, EventArgs e)
    {
        var l = tbl_sp_api_urlItem.GetQueries(dBase);
        l.Fields = new string[] { tbl_sp_api_urlItem.Fields.id, tbl_sp_api_urlItem.Fields.name, tbl_sp_api_urlItem.Fields.sp_id,
                tbl_sp_api_urlItem.Fields.urlPath,tbl_sp_api_urlItem.Fields.phy_file,tbl_sp_api_urlItem.Fields.Disable };

        int spid;
        if (int.TryParse(Request["spid"], out spid))
            l.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.sp_id, spid);

        

        l.SortKey.Add(tbl_sp_api_urlItem.Fields.Disable, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        l.SortKey.Add(tbl_sp_api_urlItem.Fields.id, Shotgun.Model.Filter.EM_SortKeyWord.desc);
        l.PageSize = int.MaxValue;
        var dt = l.GetDataList();

        if (dt.Count == 0)
            return;
        var spids = dt.GetFieldValueArray<int>(tbl_sp_api_urlItem.Fields.sp_id).Distinct();

        LoadSpName(spids);


        var spl = tbl_spItem.GetQueries(dBase);
        spl.Filter.AndFilters.Add(tbl_spItem.Fields.status, 1);
        spl.Fields = new string[] { tbl_spItem.Fields.id, tbl_spItem.Fields.short_name };
        spl.PageSize = int.MaxValue;
        spl.SortKey.Add(tbl_spItem.Fields.short_name, Shotgun.Model.Filter.EM_SortKeyWord.asc);

        rpSp.DataSource = spl.GetDataList();

        rpLst.DataSource = dt;
        rpLst.DataBind();
    }

    private void LoadSpName(IEnumerable<int> spids)
    {
        var l = tbl_spItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_spItem.Fields.id, spids);
        l.Fields = new string[] { tbl_spItem.Fields.id, tbl_spItem.Fields.short_name };
        l.PageSize = int.MaxValue;
        _SpNames = l.GetDataList();
    }

    public string GetSpName(int spId)
    {
        var sp = _SpNames.Find(e => e.id == spId);
        if (sp == null)
        {
            return string.Format("未知<{0}>", spId);
        }
        return sp.short_name;
    }



}