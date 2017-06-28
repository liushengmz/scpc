using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using LightDataModel;
using Shotgun.Library;

public partial class trone_order_batchEditor : Shotgun.PagePlus.ShotgunPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            LoadTrones();
            LoadCPs();
        }
        else
        {
            dBase.BeginTransaction();
            try
            {
                SaveData();
                dBase.Commit();
                RedirectFromPage("保存成功");
            }
            catch (System.Threading.ThreadAbortException)
            {
            }
            catch (System.Data.Common.DbException)
            {
                dBase.Rollback();
                Static.ClientRedirect("保存出错");
            }
        }

    }

    private void SaveData()
    {
        var ids = Request["troneId"].Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
        var cp_id = int.Parse(rblCpId.SelectedValue);
        var url_id = int.Parse(Request["url_id"]);
        foreach (var id in ids)
        {
            var m = new tbl_trone_orderItem();
            m.cp_id = cp_id;
            m.push_url_id = url_id;
            m.create_date = DateTime.Now;
            m.is_dynamic = Request["dynamic_" + id] == "1";
            m.order_num = Request["CPTroneOrder_" + id];
            m.order_trone_name = Request["CPTroneName_" + id];
            m.trone_id = int.Parse(id);
            m.SaveToDatabase(dBase);
        }


    }

    private void LoadTrones()
    {
        var ids = Request["troneId"];
        ids = Regex.Replace(ids, @"[^\d,]", string.Empty);
        var l = LightDataModel.tbl_troneItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_troneItem.Fields.id, ids.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries));
        l.Filter.AndFilters.Add(tbl_troneItem.Fields.status, 1);
        l.SortKey.Add(tbl_troneItem.Fields.price, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        l.PageSize = int.MaxValue;
        var dt = l.GetDataList();
        rpTrone.DataSource = dt;

        if (dt.Count == 0)
            throw new HttpException(404, "Not Trone");
    }

    private void LoadCPs()
    {
        var l = LightDataModel.tbl_cpItem.GetQueries(dBase);
        l.Fields = new string[] { LightDataModel.tbl_cpItem.Fields.id, LightDataModel.tbl_cpItem.Fields.short_name };
        l.Filter.AndFilters.Add(LightDataModel.tbl_cpItem.Fields.status, 1);
        l.PageSize = int.MaxValue;

        var dt = l.GetDataList();
        rblCpId.DataSource = dt;
        rblCpId.DataTextField = LightDataModel.tbl_cpItem.Fields.short_name;
        rblCpId.DataValueField = LightDataModel.tbl_cpItem.Fields.id;
    }
}