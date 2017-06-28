using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;

public partial class tbl_cp_push_url_Editor : Shotgun.PagePlus.ShotgunPage
{
    tbl_cp_push_urlItem Row;
    int id;
    protected void Page_Load(object sender, EventArgs e)
    {
        LoadDefault();
        LoadCPUserInfo();

        if (IsPostBack)
        {
            var msg = SaveData();
            if (string.IsNullOrEmpty(msg))
                RedirectFromPage(id == 0 ? "添加成功" : "修改成功");
            Static.alert(msg);
        }
    }

    private void LoadCPUserInfo()
    {
        if (IsPostBack)
            return;
        var l = tbl_cpItem.GetQueries(dBase);
        l.Fields = new string[] { tbl_cpItem.Fields.id, tbl_cpItem.Fields.short_name };
        l.SortKey.Add(tbl_cpItem.Fields.short_name, Shotgun.Model.Filter.EM_SortKeyWord.asc);
        l.Filter.AndFilters.Add(tbl_cpItem.Fields.status, 1);

        l.PageSize = int.MaxValue;
        var dt = l.GetDataList();
        rblcp_id.DataSource = dt;
        rblcp_id.DataBind();
        rblcp_id.DataSource = null;
        if (Row != null)
        {
            rblcp_id.Items.SelectByValue(Row.cp_id.ToString());
        }
        else
        {
            int uid;
            if (int.TryParse(Request["uid"], out uid))
            {
                var item = rblcp_id.Items.FindByValue(uid.ToString());
                if (item != null)
                    rblcp_id.SelectedValue = item.Value;
            }
            //rblcp_id.Items.SelectByValue(uid.ToString());

        }


    }


    private void LoadDefault()
    {
        var idStr = Request["id"];
        if (string.IsNullOrEmpty(idStr))
            return;

        if (int.TryParse(Request["id"], out id))
            Row = tbl_cp_push_urlItem.GetRowById(dBase, id);

        if (Row == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        if (IsPostBack)
            return;

        txtname.Text = Row.name;


        lbref_count.Text = Row.ref_count.ToString();


        txturl.Text = Row.url;
        rblcp_id.Items.SelectByValue(Row.cp_id.ToString());
        txthold_percent.Text = Row.hold_percent.ToString();

        //txthold_start.Text = Row.hold_start;

        txtmax_amonut.Text = Row.hold_amount.ToString();

        if (Row.lastDate.Date == DateTime.Today)
        {
            lbamount.Text = Row.amount.ToString();
            //lbhold_count.Text = Row.hold_count;
        }

    }

    private string SaveData()
    {
        bool isNew = Row == null;
        if (isNew)
            Row = new tbl_cp_push_urlItem();

        int i;

        Row.name = txtname.Text;
        //Row.ref_count = txtref_count.Text;
        Row.url = txturl.Text;

        if (rblcp_id.SelectedIndex == -1)
            return "请选择渠道用户";

        Row.cp_id = int.Parse(rblcp_id.SelectedValue);

        if (!int.TryParse(txthold_percent.Text, out i))
            return "扣量比输入错误";

        Row.hold_percent = i;

        decimal d;
        if (!decimal.TryParse(txtmax_amonut.Text, out d))
            return "当日最大同步金额错误";
        Row.hold_amount = decimal.Parse(txtmax_amonut.Text);
        //Row.lastDate = txtlastDate.Text;
        //Row.amount = txtamount.Text;
        //Row.hold_start = txthold_start.Text;


        //Row.hold_count = txthold_count.Text;



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