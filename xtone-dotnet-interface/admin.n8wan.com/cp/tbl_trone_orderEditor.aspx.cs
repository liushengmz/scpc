using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;

public partial class tbl_trone_order_Editor : Shotgun.PagePlus.ShotgunPage
{
    tbl_trone_orderItem Row;
    int trone_id, cpId;
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
        {
            InitNew();
            return;
        }

        if (int.TryParse(idStr, out id))
            Row = tbl_trone_orderItem.GetRowById(dBase, id);

        if (Row == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        mv.ActiveViewIndex = 1;
        if (IsPostBack)
            return;
        trone_id = Row.trone_id;
        cpId = Row.cp_id;

        txtorder_num.Text = Row.order_num;
        txtorder_trone_name.Text = Row.order_trone_name;
        chkdynamic.Checked = Row.is_dynamic;
        chkDisable.Checked = Row.disable;
        txtHold_amount.Text = Row.hold_amount.ToString();
        txtHold_precent.Text = Row.hold_percent.ToString();
        chkIsCustom.Checked = Row.hold_is_Custom;
        InitNew();
        InitPushUrl();
    }

    private void InitTrone()
    {
        var trone = tbl_troneItem.GetRowById(dBase, trone_id, new string[] { 
            tbl_troneItem.Fields.trone_name,tbl_troneItem.Fields.price,
            tbl_troneItem.Fields.is_dynamic,tbl_troneItem.Fields.orders });

        if (trone.is_dynamic)
        {
            if (Row == null)
                chkdynamic.Enabled = true;
        }
        else
        {
            chkdynamic.Enabled = false;
            chkdynamic.Checked = false;
        }
        if (string.IsNullOrEmpty(trone.orders))
            lbTrone_Name.Text = string.Format("{0}(<空>) - {1}", trone.trone_name, trone.price);
        else
            lbTrone_Name.Text = string.Format("{0}({1}) - {2}", trone.trone_name, trone.orders, trone.price);
        if (Row == null)
        {
            txtorder_num.Text = trone.orders;
            txtorder_trone_name.Text = string.Format("{0} - {1}", trone.trone_name, trone.price);
        }
    }

    private void InitNew()
    {
        if (trone_id == 0 && !int.TryParse(Request["trone_id"], out trone_id))
            throw new HttpException(404, "参数错误");
        if (cpId == 0)
            int.TryParse(Request["cpid"], out cpId);
        if (IsPostBack)
            return;
        InitTrone();
        if (cpId != 0)
        {
            mv.ActiveViewIndex = 1;
            var cp = tbl_cpItem.GetRowById(dBase, cpId, new string[] { tbl_cpItem.Fields.full_name });
            lbCpName.Text = cp.full_name;
            InitPushUrl();
            return;
        }
        var l = tbl_cpItem.GetQueries(dBase);
        l.Fields = new string[] { tbl_cpItem.Fields.id, tbl_cpItem.Fields.full_name };
        l.PageSize = int.MaxValue;
        rpCPList.DataSource = l.GetDataList();
        this.DataBind();
    }

    private void InitPushUrl()
    {
        var l = tbl_cp_push_urlItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_cp_push_urlItem.Fields.cp_id, cpId);
        l.Fields = new string[] { tbl_cp_push_urlItem.Fields.id, tbl_cp_push_urlItem.Fields.name, tbl_cp_push_urlItem.Fields.url };
        l.PageSize = int.MaxValue;
        var dt = l.GetDataList();
        bool iFound = false;
        dt.ForEach(e =>
        {
            e.name = string.Format("{0} - {1}", e.name, e.url);
            if (Row != null && e.id == Row.push_url_id)
                iFound = true;
        }
            );

        rblPush_Url_Id.DataValueField = tbl_cp_push_urlItem.Fields.id;
        rblPush_Url_Id.DataTextField = tbl_cp_push_urlItem.Fields.name;
        rblPush_Url_Id.DataSource = dt;
        rblPush_Url_Id.DataBind();
        if (iFound)
            rblPush_Url_Id.SelectedValue = Row.push_url_id.ToString();

    }

    private string SaveData()
    {
        bool isNew = Row == null;
        if (isNew)
        {
            Row = new tbl_trone_orderItem();
            Row.trone_id = trone_id;
            Row.cp_id = cpId;

            Row.create_date = DateTime.Now;
        }

        if (rblPush_Url_Id.SelectedIndex == -1)
            return "请选择同步地址";

        Row.order_num = txtorder_num.Text;
        Row.order_trone_name = txtorder_trone_name.Text;
        Row.is_dynamic = chkdynamic.Checked;
        Row.push_url_id = int.Parse(rblPush_Url_Id.SelectedValue);
        Row.disable = chkDisable.Checked;
        Row.hold_is_Custom = chkIsCustom.Checked;
        decimal dec;
        if (string.IsNullOrEmpty(txtHold_amount.Text))
            dec = 0;
        else if (!decimal.TryParse(txtHold_amount.Text, out dec))
            return "最大单日同步金额设置错误";
        Row.hold_amount = dec;
        int per;
        if (string.IsNullOrEmpty(txtHold_precent.Text))
            per = 0;
        else if (!int.TryParse(txtHold_precent.Text, out per))
            return "扣量比设置错误";
        Row.hold_percent = per;



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