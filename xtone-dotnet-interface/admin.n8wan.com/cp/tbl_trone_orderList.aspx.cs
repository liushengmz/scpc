using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using LightDataModel;
using Shotgun.Model.Filter;
using Shotgun.Library;

public partial class cp_tbl_trone_orderList : Shotgun.PagePlus.ShotgunPage
{
    private List<tbl_cp_push_urlItem> Urls;
    protected void Page_Load(object sender, EventArgs e)
    {
        var l = tbl_trone_orderItem.GetQueries(dBase);
        l.PageSize = int.MaxValue;

        int i;
        if (int.TryParse(Request["cpid"], out i))
            l.Filter.AndFilters.Add(tbl_trone_orderItem.Fields.cp_id, i);
        l.SortKey.Add(tbl_trone_orderItem.Fields.id, EM_SortKeyWord.desc);
        InitFrom();

        var dt = l.GetDataList();

        var urlIds = dt.GetFieldValueArray<int>(tbl_trone_orderItem.Fields.push_url_id);
        this.Urls = LoadUrls(urlIds);
        rpLst.DataSource = dt;
        this.DataBind();
    }

    private List<tbl_cp_push_urlItem> LoadUrls(int[] urlIds)
    {
        var l = tbl_cp_push_urlItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_cp_push_urlItem.Fields.id, urlIds);
        l.PageSize = int.MaxValue;
        return l.GetDataList();
    }

    private void InitFrom()
    {
        var l = tbl_cpItem.GetQueries(dBase);
        var tf = new TableFilter(tbl_cpItem.identifyField, tbl_trone_orderItem.tableName, tbl_trone_orderItem.Fields.cp_id);
        tf.MaxRecord = int.MaxValue;
        l.Filter.AndFilters.Add(tf);
        l.PageSize = int.MaxValue;
        l.SortKey.Add(tbl_cpItem.Fields.short_name, EM_SortKeyWord.asc);
        rpCPLst.DataSource = l.GetDataList();

    }

    protected string GetCPName(int userId)
    {
        if (userId == 0)
            return null;
        var l = tbl_cpItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_cpItem.Fields.id, userId);
        var t = (string)l.ExecuteScalar(tbl_cpItem.Fields.short_name);
        return t;

    }

    protected string GetPushURL(int pid)
    {
        var m = Urls.Find(e => e.id == pid);
        if (m != null)
            return m.url;
        return "[丢失]";

    }

    protected string GetHoldInfo(object data)
    {
        n8wan.Public.Logical.IHold_DataItem hold = null;

        tbl_trone_orderItem t = (tbl_trone_orderItem)data;
        string html = string.Empty;
        if (t.hold_is_Custom)
        {
            hold = t;
            html = "<span class='red'>[自定义]</span>";
        }
        else
        {
            hold = Urls.Find(e => e.id == t.push_url_id);
            if (hold == null)
                return "[丢失]";
        }
        html += string.Format("{0}%", hold.hold_percent);
        if (hold.hold_amount > 0)
            html += string.Format(",限额{0}", hold.hold_amount);

        return html;
    }
}