using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;

public partial class tbl_sp_Editor : Shotgun.PagePlus.ShotgunPage
{
    tbl_spItem Row;
    int id;
    protected void Page_Load(object sender, EventArgs e)
    {
        LoadDefault();
        if (IsPostBack)
        {
            var msg = SaveData();
            if (string.IsNullOrEmpty(msg))
                RedirectFromPage(id == 0 ? "添加成功" : "修改成功");
            Static.alert(msg);
        }
    }

    private void LoadDefault()
    {
        var idStr = Request["id"];
        if (string.IsNullOrEmpty(idStr))
            return;

        if (int.TryParse(Request["id"], out id))
            Row = tbl_spItem.GetRowById(dBase, id);

        if (Row == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        if (IsPostBack)
            return;

        txtfull_name.Text = Row.full_name;
        txtshort_name.Text = Row.short_name;
        txtcontract_person.Text = Row.contract_person;
        rblcommerce_user_id.Items.SelectByValue(Row.commerce_user_id.ToString());
        txtaddress.Text = Row.address;
        if (!Row.Iscontract_start_dateNull())
            txtcontract_start_date.Text = Row.contract_start_date.ToString("yyyy-MM-dd");
        if (!Row.Iscontract_end_dateNull())
            txtcontract_end_date.Text = Row.contract_end_date.ToString("yyyy-MM-dd");
        chkstatus.Checked = Row.status == 0;

    }

    private string SaveData()
    {
        bool isNew = Row == null;
        if (isNew)
            Row = new tbl_spItem();

        Row.full_name = txtfull_name.Text;
        Row.short_name = txtshort_name.Text;

        if (string.IsNullOrEmpty(Row.full_name))
        {
            if (string.IsNullOrEmpty(Row.short_name))
                return "公司名称不能为空";
            Row.full_name = Row.short_name;
        }
        else if (string.IsNullOrEmpty(Row.short_name))
            Row.short_name = Row.full_name;



        Row.contract_person = txtcontract_person.Text;
        int adminId;
        int.TryParse(rblcommerce_user_id.SelectedValue, out adminId);

        Row.commerce_user_id = adminId;

        Row.address = txtaddress.Text;
        DateTime date;
        if (DateTime.TryParse(txtcontract_start_date.Text, out date))
            Row.contract_start_date = date;
        else
            Row.Setcontract_start_dateNull();
        if (DateTime.TryParse(txtcontract_end_date.Text, out date))
            Row.contract_end_date = date;
        else
            Row.Setcontract_end_dateNull();

        Row.status = chkstatus.Checked ? 0 : 1;
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
}