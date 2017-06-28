using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;

public partial class tbl_user_Editor : Shotgun.PagePlus.ShotgunPage
{
    tbl_userItem Row;
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
            Row = tbl_userItem.GetRowById(dBase, id);

        if (Row == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        if (IsPostBack)
            return;

        txtname.Text = Row.name;
        //txtpwd.Text = Row.pwd;

        txtnick_name.Text = Row.nick_name;
        txtmail.Text = Row.mail;
        txtqq.Text = Row.qq;
        txtphone.Text = Row.phone;
        txtgroup_list.Text = Row.group_list;
        chkStatus.Checked = Row.status == 0;

    }

    private string SaveData()
    {
        bool isNew = Row == null;
        if (isNew)
            Row = new tbl_userItem();


        Row.name = txtname.Text;
        if (isNew || !string.IsNullOrEmpty(txtpwd.Text))
        {
            if (isNew)
            {
                if (checkExisted())
                {
                    return "用户已经存在";
                }
            }
            if (string.IsNullOrEmpty(txtpwd.Text))
                return "密码不能为空";
            Row.pwd = System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(txtpwd.Text, "md5");
        }

        Row.nick_name = txtnick_name.Text;
        Row.mail = txtmail.Text;
        Row.qq = txtqq.Text;
        Row.phone = txtphone.Text;
        Row.group_list = txtgroup_list.Text;
        Row.status = chkStatus.Checked ? 0 : 1;
        Row.create_date = DateTime.Now;
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

    private bool checkExisted()
    {
        var l = LightDataModel.tbl_userItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(LightDataModel.tbl_userItem.Fields.name, txtname.Text);
        var d = l.ExecuteScalar(LightDataModel.tbl_userItem.Fields.id);
        return d != null;
    }
}