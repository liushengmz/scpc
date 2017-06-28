using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;

public partial class tbl_mobile_white_list : Shotgun.PagePlus.ShotgunPage
{
    tbl_mobile_white_listItem Row;
    int id;
    protected void Page_Load(object sender, EventArgs e)
    {
        LoadDefault();
        if (IsPostBack)
        {
            var msg = SaveData();
            if (string.IsNullOrEmpty(msg))
            {
                msg = id == 0 ? "添加成功" : "修改成功";
                txtmobile.Text = null;
            }
            Static.alert(msg);
        }

        LoadList();
        this.DataBind();
    }

    private void LoadList()
    {
        var l = tbl_mobile_white_listItem.GetQueries(dBase);
        l.PageSize = 1000;
        l.SortKey.Add(tbl_mobile_white_listItem.Fields.mobile, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        rpList.DataSource = l.GetDataList();
    }

    private void LoadDefault()
    {
        var idStr = Request["id"];
        if (string.IsNullOrEmpty(idStr))
            return;

        if (int.TryParse(Request["id"], out id))
            Row = tbl_mobile_white_listItem.GetRowById(dBase, id);

        if (Row == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        if (IsPostBack)
            return;

        txtmobile.Text = Row.mobile;

    }

    private string SaveData()
    {
        bool isNew = Row == null;
        if (isNew)
        {
            Row = new tbl_mobile_white_listItem();
        }

        Row.mobile = txtmobile.Text.Trim();
        if (string.IsNullOrEmpty(Row.mobile))
            return "号码不能为空";
        Row.adddate = DateTime.Now;

        if (CheckExist(Row.mobile))
            return "已经存在";

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

    private bool CheckExist(string mobile)
    {
        var l = tbl_mobile_white_listItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_mobile_white_listItem.Fields.mobile, mobile);
        return l.TotalCount > 0;
    }
}