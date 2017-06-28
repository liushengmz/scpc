using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;

public partial class tbl_trone_Editor : Shotgun.PagePlus.ShotgunPage
{
    tbl_troneItem Row;
    int id;
    protected void Page_Load(object sender, EventArgs e)
    {
        LoadDefault();
        LoadSP();
        if (IsPostBack && !string.IsNullOrEmpty(Request["action"]))
        {
            var msg = SaveData();
            if (string.IsNullOrEmpty(msg))
                RedirectFromPage(id == 0 ? "添加成功" : "修改成功");
            Static.alert(msg);
        }
    }

    private void LoadSP()
    {
        if (IsPostBack)
            return;
        var l = tbl_spItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_spItem.Fields.status, 1);
        l.Fields = new string[] { tbl_spItem.Fields.id, tbl_spItem.Fields.full_name };
        l.SortKey.Add(tbl_spItem.Fields.full_name, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        l.PageSize = int.MaxValue;

        int spid;
        if (Row != null)
        {
            l.Filter.AndFilters.Add(tbl_spItem.Fields.id, Row.sp_id);
            ddlSpId.Enabled = false;
            spid = Row.sp_id;
        }
        else
            int.TryParse(Request["spid"], out spid);

        var dt = l.GetDataList();
        if (dt.Count == 0)
            return;
        ddlSpId.DataSource = dt;
        ddlSpId.DataBind();

        if (spid > 0)
            ddlSpId.Items.SelectByValue(spid.ToString());

        ddlSpId_SelectedIndexChanged(null, null);
    }

    private void LoadDefault()
    {
        var idStr = Request["id"];
        if (string.IsNullOrEmpty(idStr))
            return;

        if (int.TryParse(Request["id"], out id))
            Row = tbl_troneItem.GetRowById(dBase, id);

        if (Row == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        if (IsPostBack)
            return;
        ddlSpId.Items.SelectByValue(Row.sp_id.ToString());

        txttrone_num.Text = Row.trone_num;
        txtorders.Text = Row.orders;
        txttrone_name.Text = Row.trone_name;
        txtPrice.Text = Row.price.ToString("0.00");
        chkdymaic.Checked = Row.is_dynamic;
        chkmatch_price.Checked = Row.match_price;
        this.chkstatus.Checked = Row.status == 0;

    }

    private string SaveData()
    {
        bool isNew = Row == null;
        if (isNew)
        {
            Row = new tbl_troneItem();
            Row.sp_id = int.Parse(ddlSpId.SelectedValue);
            Row.sp_api_url_id = int.Parse(ddlUrlId.SelectedValue);
        }



        Row.trone_num = txttrone_num.Text;
        Row.orders = txtorders.Text;
        Row.trone_name = txttrone_name.Text;
        Row.status = chkstatus.Checked ? 0 : 1;

        Row.is_dynamic = chkdymaic.Checked;
        Row.match_price = chkmatch_price.Checked;

        decimal fee;
        if (!decimal.TryParse(txtPrice.Text, out fee))
            return "输入的价格无法识别";
        Row.price = fee;
        if (string.IsNullOrEmpty(Row.trone_name))
            return "业务名称不能为空";
        if (string.IsNullOrEmpty(Row.trone_num))
            return "通道号不能为空";

        try
        {
            Row.SaveToDatabase(dBase);
        }
        catch (Exception ex)
        {
            return "保存出错\n" + ex.Message;
        }
        return null;
    }

    protected void ddlSpId_SelectedIndexChanged(object sender, EventArgs e)
    {
        int spid = 0;
        int urlid = 0;
        if (IsPostBack)
            spid = int.Parse(ddlSpId.SelectedValue);
        else
        {
            if (Row != null)
                urlid = Row.sp_api_url_id;
            else
                int.TryParse(Request["urlId"], out urlid);
        }

        var l = tbl_sp_api_urlItem.GetQueries(dBase);
        if (spid != 0)
            l.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.sp_id, spid);
        else if (urlid != 0)
        {
            var tf = new Shotgun.Model.Filter.TableFilter(tbl_sp_api_urlItem.Fields.sp_id, tbl_sp_api_urlItem.tableName, tbl_sp_api_urlItem.Fields.sp_id);
            tf.TableFilters.AndFilters.Add(tbl_sp_api_urlItem.Fields.id, urlid);
            l.Filter.AndFilters.Add(tf);
        }
        else
            return;
        l.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.Disable, false);

        l.Fields = new string[] { tbl_sp_api_urlItem.Fields.id, tbl_sp_api_urlItem.Fields.name, tbl_sp_api_urlItem.Fields.sp_id };
        //if (Row != null && Row.sp_api_url_id != 0)
        //{
        //    l.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.id, Row.sp_api_url_id);
        //    ddlUrlId.Enabled = false;
        //}

        l.PageSize = int.MaxValue;
        var dt = l.GetDataList();
        ddlUrlId.DataSource = dt;
        ddlUrlId.DataBind();

        if ((urlid != 0 || Row != null) && !IsPostBack)
        {
            var m = dt.Find(item => item.id == urlid);
            if (m != null)
            {
                ddlUrlId.SelectedValue = m.id.ToString();
                var item = ddlSpId.Items.FindByValue(m.sp_id.ToString());
                if (item != null)
                    ddlSpId.SelectedValue = item.Value;
            }
        }

    }
}