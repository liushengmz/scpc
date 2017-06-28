using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class cp_PoolSetPriority : Shotgun.PagePlus.ShotgunPage
{
    int poolId;
    protected LightDataModel.tbl_cp_poolItem _poolModel;
    protected void Page_Load(object sender, EventArgs e)
    {
        int.TryParse(Request["id"], out poolId);
        _poolModel = LightDataModel.tbl_cp_poolItem.GetRowById(dBase, poolId);
        if (_poolModel == null)
        {
            RedirectFromPage("参数无效");
            return;
        }

        if (Request.HttpMethod == "POST")
        {
            SaveData();
            return;
        }


        var models = n8wan.codepool.Dao.PoolSet.QueryPoolSetInfoById(dBase, poolId);
        rplst.DataSource = models;
        rplst.DataBind();
    }

    private void SaveData()
    {
        var ids = Request["troneOrderId"].Split(new char[] { ',' });
        var sb = new System.Text.StringBuilder();

        foreach (var id in ids)
        {
            int i, priority;
            if (!int.TryParse(id, out i))
                continue;
            if (!int.TryParse(Request["p_" + i.ToString()], out priority))
                continue;
            bool iEnable = "1".Equals(Request["e_" + i.ToString()]);
            sb.AppendFormat("update tbl_cp_pool_set set status={0},priority={1} where id={2};\n", iEnable ? 1 : 0, priority, id);
        }

        if (sb.Length == 0)
            return;
        _poolModel.IgnoreEquals = true;
        _poolModel.name = Request["poolName"];
        if(string.IsNullOrEmpty(_poolModel.name))
            return;
        _poolModel.SaveToDatabase(dBase);
        dBase.ExecuteNonQuery(sb.ToString());
        RedirectFromPage("更新成功", "PoolSetList.aspx");
    }
}