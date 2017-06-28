<%@ WebHandler Language="C#" Class="xy_3rd_stdcall" %>

using System;
using System.Web;

public class xy_3rd_stdcall : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    protected override Shotgun.Database.IBaseDataClass2 CreateDBase()
    {
        return new Shotgun.Database.MySqlDBClass(false, "129Mysql");
    }

    public override void BeginProcess()
    {
        var product = Request["product"];
        int rows, fee;
        DateTime sumDate;
        if (!int.TryParse(Request["rows"], out rows))
        {
            Response.Write("error 数据量（条）错误");
            return;
        }
        if (!int.TryParse(Request["price"], out fee))
        {
            Response.Write("error 价格（分）");
            return;
        }
        if (!DateTime.TryParse(Request["date"], out sumDate))
        {
            Response.Write("error 计费日期");
            return;
        }
        if (string.IsNullOrEmpty(product))
        {
            Response.Write("error 产品名称不能为空");
            return;
        }

        var sb = new System.Text.StringBuilder();
        sb.Append("Insert into `tbl_3rd_summer_log` (product_name, sum_date, row_count, price) values");
        sb.AppendFormat("('{0}','{1:yyyy-MM-dd}',{2},{3});", dBase.SqlEncode(product), sumDate, rows, fee);
        sb.AppendLine();

        sb.Append("Insert into `tbl_3rd_summer` (product_name, sum_date, row_count, price,last_update) values");
        sb.AppendFormat("('{0}','{1:yyyy-MM-dd}',{2},{3},'{4}') ON DUPLICATE KEY UPDATE row_count={2},price={3},last_update='{4}';",
                dBase.SqlEncode(product), sumDate, rows, fee, DateTime.Now);
        sb.AppendLine();

        dBase.ExecuteNonQuery(sb.ToString());
        Response.Write("ok");




    }
}