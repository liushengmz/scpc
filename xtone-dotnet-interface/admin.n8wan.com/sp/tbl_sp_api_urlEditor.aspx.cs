using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Shotgun.Database;
using Shotgun.Library;
using LightDataModel;
using System.Text.RegularExpressions;
public partial class tbl_sp_api_url_Editor : Shotgun.PagePlus.ShotgunPage
{
    tbl_sp_api_urlItem Row;
    int id;
    protected void Page_Load(object sender, EventArgs e)
    {
        LoadDefault();
        LoadSPInfo();
        if (IsPostBack)
        {
            var msg = SaveData();
            if (string.IsNullOrEmpty(msg))
                RedirectFromPage(id == 0 ? "添加成功" : "修改成功");
            Static.alert(msg);
        }
    }

    void LoadSPInfo()
    {
        if (IsPostBack)
            return;
        var l = tbl_spItem.GetQueries(dBase);
        l.Fields = new string[] { tbl_spItem.Fields.id, tbl_spItem.Fields.short_name };
        l.PageSize = int.MaxValue;
        l.Filter.AndFilters.Add(tbl_spItem.Fields.status, 1);
        ddlSp_id.Enabled = false;

        if (Row != null)
            l.Filter.AndFilters.Add(tbl_spItem.Fields.id, Row.sp_id);
        else
        {
            int spId;
            int.TryParse(Request["spId"], out spId);
            if (spId != 0)
                l.Filter.AndFilters.Add(tbl_spItem.Fields.id, spId);
            else
            {
                ddlSp_id.Enabled = true;
                ddlSp_id.AppendDataBoundItems = true;
                l.SortKey.Add(tbl_spItem.Fields.short_name, Shotgun.Model.Filter.EM_SortKeyWord.asc);
            }
        }
        var dt = l.GetDataList();
        ddlSp_id.DataSource = dt;

    }

    private void LoadDefault()
    {
        var idStr = Request["id"];
        if (string.IsNullOrEmpty(idStr))
            return;

        if (int.TryParse(Request["id"], out id))
            Row = tbl_sp_api_urlItem.GetRowById(dBase, id);

        if (Row == null)
        {
            form1.Controls.Clear();
            form1.InnerHtml = "<h1>ID无效！</h1>";
            return;
        }
        if (IsPostBack)
            return;

        txtvirtual_page.Text = Row.virtual_page;
        chkphy_file.Checked = Row.phy_file;
        txtMoCheck.Text = Row.MoCheck;
        txtMoLink.Text = Row.MoLink;
        txtMrLink.Text = Row.MrLink;
        DataToChkbox(Row.MoToMr);

        DataToTextBox(true, Row.MrFidldMap);
        DataToTextBox(false, Row.MoFieldMap);

        //txtMoFieldMap_IMSI.Text = Row.MoFieldMap;
        //txtMrFidldMap.Text = Row.MrFidldMap;
        // txtMoStatus.Text = Row.MoStatus;
        txtMrStatus.Text = Row.MrStatus;
        txtMsgOutput.Text = Row.MsgOutput;
        txtMr_price.Text = Row.MrPrice;
        txtMo_price.Text = Row.MoPrice;
        txtName.Text = Row.name;
        txtsp_server_ips.Text = Row.sp_server_ips;
        this.chkDisable.Checked = Row.Disable;


    }

    private void DataToChkbox(string chkName)
    {
        if (string.IsNullOrEmpty(chkName))
            return;

        var fields = chkName.ToLower().Split(new char[] { ',' });
        foreach (var f in fields)
        {
            var fName = f;
            switch (f)
            {
                case "port": fName = "ori_trone"; break;
                case "msg": fName = "ori_trone"; break;
            }
            var chkBox = (CheckBox)chkCpy_price.Parent.FindControl("chkCpy_" + f);
            if (chkBox == null)
                continue;
            chkBox.Checked = true;
        }
    }

    private void DataToTextBox(bool IsMr, string Data)
    {
        if (string.IsNullOrEmpty(Data))
            return;
        var ars = Data.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
        foreach (var ar in ars)
        {
            var fs = ar.Split(new char[] { ':' }, StringSplitOptions.RemoveEmptyEntries);
            string uf, sf;
            uf = fs[0];
            sf = (fs.Length == 1) ? fs[0] : fs[1];
            var tBox = FindTextBox(IsMr, sf);
            if (tBox == null)
            {
                //Response.Write(uf);
                continue;
            }
            tBox.Text = uf.Replace('|', ',');
        }
    }

    private string SaveData()
    {
        bool isNew = Row == null;
        if (isNew)
        {
            Row = new tbl_sp_api_urlItem();
            Row.sp_id = int.Parse(ddlSp_id.SelectedValue);
        }

        Row.virtual_page = txtvirtual_page.Text;
        Row.phy_file = chkphy_file.Checked;
        Row.MoCheck = txtMoCheck.Text;
        Row.MoLink = txtMoLink.Text;
        Row.MrLink = txtMrLink.Text;
        Row.MoToMr = chkToData();

        Row.MoFieldMap = GetMaps(false);
        Row.MrFidldMap = GetMaps(true);
        //Row.MoStatus = txtMoStatus.Text;
        Row.MrStatus = txtMrStatus.Text;
        Row.MsgOutput = txtMsgOutput.Text;
        Row.Disable = chkDisable.Checked;
        Row.name = txtName.Text;
        Row.MoPrice = txtMo_price.Text;
        Row.MrPrice = txtMr_price.Text;
        Row.sp_server_ips = txtsp_server_ips.Text.Trim();

        if (!string.IsNullOrEmpty(Row.MoCheck))
        {
            if (string.IsNullOrEmpty(Row.MoLink))
                return "MoLink不能为空";
            if (string.IsNullOrEmpty(Row.MoFieldMap))
                return "MoFieldMap不能为空";
            Row.MoFieldMap = Row.MoFieldMap.Replace(" ", string.Empty);
            if (string.IsNullOrEmpty(Row.MoToMr) && string.IsNullOrEmpty(Row.MrFidldMap))
                return "MoToMr 与 MrFidldMap 不能同时为空";
            if (!string.IsNullOrEmpty(Row.MoToMr))
                Row.MoToMr = Row.MoToMr.Replace(" ", string.Empty);
            if (string.IsNullOrEmpty(Row.MrFidldMap))
                Row.MrFidldMap = Row.MrFidldMap.Replace(" ", string.Empty);
        }
        else
        {
            if (string.IsNullOrEmpty(Row.MrFidldMap))
                return "MrFidldMap不能为空";
            Row.MrFidldMap = Row.MrFidldMap.Replace(" ", string.Empty);
        }

        if (Row.phy_file && NameExisted())
            return "virtual_page已经存在，不能重复";

        if (isNew || Row.phy_file)
            Row.urlPath = string.Format("/sp/{0}.ashx", Row.virtual_page);
        else
            Row.urlPath = string.Format("/sp/{0}/{1}.ashx", Row.id, Row.virtual_page);


        try
        {
            Row.SaveToDatabase(dBase);
            if (isNew && !Row.phy_file)
                Row.urlPath = string.Format("/sp/{0}/{1}.ashx", Row.id, Row.virtual_page);
            Row.SaveToDatabase(dBase);

        }
        catch (Exception ex)
        {
            return "保存出错\n" + ex.Message;
        }
        return null;
    }

    private string chkToData()
    {
        var sb = new System.Text.StringBuilder();
        if (chkCpy_mobile.Checked)
            sb.Append(",mobile");
        if (chkCpy_ori_order.Checked)
            sb.Append(",ori_order");
        if (chkCpy_ori_trone.Checked)
            sb.Append(",ori_trone");
        if (chkCpy_price.Checked)
            sb.Append(",price");
        if (sb.Length < 3)
            return null;

        sb.Remove(0, 1);
        return sb.ToString();

    }

    private string GetMaps(bool isMr)
    {
        string[] SZ = { "IMSI", "IMEI", "Mobile", "MMC", "Ori_Trone", "Ori_Order", "CP_Param", "Service_Code", "Status", "Ivr_Time" };
        var maps = string.Empty;
        foreach (var f in SZ)
        {
            var tBox = FindTextBox(isMr, f);
            if (tBox == null || string.IsNullOrEmpty(tBox.Text))
                continue;
            maps += "," + tBox.Text.Replace(',', '|') + ":" + f;
        }

        if (string.IsNullOrEmpty(maps))
            return null;
        return maps.Substring(1);
    }

    private bool NameExisted()
    {
        var l = tbl_sp_api_urlItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.virtual_page, Row.virtual_page);
        l.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.Disable, false);
        if (Row.id > 0)
            l.Filter.AndFilters.Add(tbl_sp_api_urlItem.Fields.id, Row.id, Shotgun.Model.Filter.EM_DataFiler_Operator.Not_Equal);
        return l.ExecuteScalar(tbl_sp_api_urlItem.Fields.sp_id) != null;
    }

    public TextBox FindTextBox(bool isMr, string id)
    {
        id = id.ToLower();
        switch (id)
        {
            case "port": id = "ori_trone"; break;
            case "msg": id = "ori_order"; break;
            case "servicecode": id = "service_code"; break;
        }
        return (TextBox)txtMsgOutput.Parent.FindControl(string.Format("txt{0}_{1}", isMr ? "MrFieldMap" : "MoFieldMap", id));
    }
}