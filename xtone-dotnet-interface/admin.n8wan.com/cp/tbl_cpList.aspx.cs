using LightDataModel;
using Shotgun.Model.Filter;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class tbl_cpList : Shotgun.PagePlus.ShotgunPage
{
    protected void Page_Load(object sender, EventArgs e)
    {
        var l = LightDataModel.tbl_userItem.GetQueries(dBase);
        l.PageSize = int.MaxValue;
        var tf = new TableFilter(LightDataModel.tbl_userItem.identifyField, LightDataModel.tbl_cpItem.tableName, LightDataModel.tbl_cpItem.Fields.user_id);
        tf.MaxRecord = int.MaxValue;

        l.Filter.AndFilters.Add(tf);


        var dt = l.GetDataList();
        rpLst.DataSource = dt;
        this.DataBind();
    }

    protected int GetCPIDByUserId(int userId)
    {
        var l = tbl_cpItem.GetQueries(dBase);
        l.Filter.AndFilters.Add(tbl_cpItem.Fields.user_id, userId);
        return (int)l.ExecuteScalar(tbl_cpItem.Fields.id);

    }




}