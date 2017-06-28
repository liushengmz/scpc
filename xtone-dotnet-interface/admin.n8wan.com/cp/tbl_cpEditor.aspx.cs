using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;

public partial class tbl_cp_Editor : Shotgun.PagePlus.ShotgunPage
{
    tbl_cpItem extRow;
    tbl_userItem userRow;
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
        {
            userRow = tbl_userItem.GetRowById(dBase, id);
            extRow = tbl_cpItem.GetRowByUserId(dBase, id);
        }

        if (extRow == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        if (IsPostBack)
            return;

        txtname.Text = userRow.name;
        txtnick_name.Text = userRow.nick_name;


        if (extRow == null)
            return;

        txtfull_name.Text = extRow.full_name;
        txtshort_name.Text = extRow.short_name;
        txtcontact_person.Text = extRow.contract_person;

        this.rblcommerce_user_id.Items.SelectByValue(extRow.commerce_user_id.ToString());
        txtdefault_hold_percent.Text = extRow.default_hold_percent.ToString();

        txtaddress.Text = extRow.address;

        chkstatus.Checked = !extRow.status;


    }

    private string SaveData()
    {
        bool isNew = extRow == null;
        if (isNew)
        {
            extRow = new tbl_cpItem();
            userRow = new tbl_userItem();
        }
        userRow.name = txtname.Text;
        userRow.nick_name = txtnick_name.Text;

        extRow.full_name = txtfull_name.Text;
        extRow.short_name = txtshort_name.Text;
        extRow.contract_person = txtcontact_person.Text;

        if (rblcommerce_user_id.SelectedIndex != -1)
            extRow.commerce_user_id = int.Parse(rblcommerce_user_id.SelectedValue);

        int i;
        if (!int.TryParse(txtdefault_hold_percent.Text, out i))
            return "输入的默认扣量值错误";
        extRow.default_hold_percent = 1;
        extRow.address = txtaddress.Text;

        //DateTime date;
        //extRow.contract_start_date = txtcontract_start_date.Text;
        //extRow.contract_end_date = txtcontract_end_date.Text;

        extRow.status = !chkstatus.Checked;
        extRow.create_date = DateTime.Now;

        try
        {
            userRow.SaveToDatabase(dBase);
            if (isNew)
                extRow.user_id = userRow.id;
            extRow.SaveToDatabase(dBase);
        }
        catch (Exception ex)
        {
            return "保存出错\n" + ex.Message;
        }
        return null;
    }
}