<%@ WebHandler Language="C#" Class="getApiData" %>

using System;
using System.Web;
using System.Data;
using System.IO;
using System.Collections.Generic;
using System.Linq;
public class getApiData : Shotgun.PagePlus.SimpleHttpHandler<Shotgun.Database.MySqlDBClass>
{
    enum UserMode
    {
        Full,
        Cp,
        Sp
    }

    DateTime date;
    int s_hour, e_hour;
    UserMode userMode = UserMode.Full;
    static Dictionary<int, int> _maxIds;
    static DateTime _cacheDate;

    public override void BeginProcess()
    {

        if (!DateTime.TryParse(Request["date"], out date))
            date = DateTime.Today;
        if (!int.TryParse(Request["s_Hour"], out s_hour))
            s_hour = 0;
        if (!int.TryParse(Request["e_Hour"], out e_hour))
            e_hour = 23;
        if ("cp".Equals(Request["mode"], StringComparison.InvariantCultureIgnoreCase))
            userMode = UserMode.Cp;
        else if ("sp".Equals(Request["mode"], StringComparison.InvariantCultureIgnoreCase))
            userMode = UserMode.Sp;
        InitMaxIds();

        switch (Request["method"])
        {
            case "getdata": GetData(); break;
            default: InitHead(); break;
        }
    }


    void InitHead()
    {

        string ids = getIdsRang();
        if (!string.IsNullOrEmpty(ids))
        {
            ids = " where " + ids;
        }

        string sql = "select m.*,st.id sp_trone_id,st.sp_id , st.name sp_trone_name ,trone.trone_name,trone.price,sp.short_name sp_name , cp.short_name cp_name from ( \n"
                + " select trone_Id,id trone_order_id ,cp_id from daily_config.tbl_trone_order where id in( \n"
                + " select distinct trone_order_id  from daily_log.tbl_api_order_" + date.ToString("yyyyMM") + " api " + ids + " \n"
                + ") ) m left join daily_config.tbl_trone trone on trone.id=m.trone_Id \n"
                + " left join daily_config.tbl_sp_trone st on st.id=trone.sp_trone_id \n"
                + " left join daily_config.tbl_sp sp on sp.id=trone.sp_id \n"
                + " left join daily_config.tbl_cp cp on cp.id=m.cp_id";
        var userName = HttpContext.Current.User.Identity.Name;
        if (userMode == UserMode.Cp)
            sql += "\n left join daily_config.tbl_user user on cp.commerce_user_id=user.id where user.name='" + dBase.SqlEncode(userName) + "'";
        else if (userMode == UserMode.Sp)
            sql += "\n left join daily_config.tbl_user user on sp.commerce_user_id=user.id where user.name='" + dBase.SqlEncode(userName) + "'";


        var sb = new System.Text.StringBuilder("[");

        using (var dt = dBase.GetDataTable(sql))
        {
            WriteData(dt);
        }
    }
    public Dictionary<int, int> maxIds
    {
        get
        {
            if (_maxIds == null || _cacheDate.Date != DateTime.Today)
                InitMaxIds();
            return _maxIds;
        }
    }


    void GetData()
    {
        var paycode = Request["paycode"];
        if (string.IsNullOrEmpty(paycode))
            paycode = "0";
        else
            paycode = System.Text.RegularExpressions.Regex.Replace(paycode, "[^0-9,]", "");

        var idwhere = getIdsRang();
        if (!string.IsNullOrEmpty(idwhere))
            idwhere = " and " + idwhere;

        int groupType;
        int.TryParse(Request["type"], out groupType);
        string sql;
        switch (groupType)
        {
            //case 0:
            case 1:
                sql = "select trone_order_id , pro.name type ,status,count(0) count from daily_log.tbl_api_order_{2:yyyyMM} api left join daily_config.tbl_city ct on ct.id=api.city "
                    + " left join daily_config.tbl_province pro on pro.id=ct.province_id"
                    + "	where trone_order_id in({0}) {1} group by trone_order_id , pro.name ,status ";
                break;
            case 2:
                sql = "select trone_order_id ,trone_id type ,status,count(0) count from daily_log.tbl_api_order_{2:yyyyMM} api "
                    + "	where trone_order_id in({0}) {1} group by trone_order_id , trone_id ,status ";
                break;
            default:
                sql = "select trone_order_id , DATE_FORMAT(firstDate,'%y-%m-%d %H:00:00') type ,status,count(0) count from daily_log.tbl_api_order_{2:yyyyMM} api "
                    + "	where trone_order_id in({0}) {1} group by trone_order_id , DATE_FORMAT(firstDate,'%y-%m-%d %H:00:00') ,status ";
                break;
        }
        //sql = " select trone_order_id , DATE_FORMAT(firstDate,'%y-%m-%d %H:00:00') type ,status,count(0) count from daily_log.tbl_api_order_201702 "
        //    + "	where trone_order_id in(" + paycode + " ) " + idwhere + "  group by trone_order_id , DATE_FORMAT(firstDate,'%y-%m-%d %H:00:00') ,status ";

        sql = string.Format(sql, paycode, idwhere, date);


        using (var dt = dBase.GetDataTable(sql))
        {
            WriteData(dt);
        }
    }


    string getIdsRang()
    {
        if (maxIds.Count == 0)
            return string.Empty;
        var day = date.Day;
        int max = int.MaxValue, min = 0;
        foreach (var k in maxIds.Keys)
        {
            if (k < day && k > min)
                min = k;
            else if (k >= day && k < max)
                max = k;
        }



        if (min > 0 && max != int.MaxValue)
            return string.Format("api.id between {0} and {1}", maxIds[min], maxIds[max]);
        if (min > 0)
            return "api.id >" + maxIds[min];
        if (max > 0)
            return "api.id<" + maxIds[max];

        return string.Empty;

    }

    void InitMaxIds()
    {
        if (_maxIds != null)
            return;
        _maxIds = new Dictionary<int, int>();

        string datafile = Server.MapPath(string.Format("~/app_data/api_ids_{0:yy-MM}.txt", DateTime.Today));
        if (!File.Exists(datafile))
        {
            LoadNewIds();
            return;
        }

        var stm = new StreamReader(datafile);
        using (stm)
        {
            while (!stm.EndOfStream)
            {
                var line = stm.ReadLine();
                if (string.IsNullOrEmpty(line))
                    continue;
                var ar = line.Split(new char[] { ',', '\t' }, StringSplitOptions.RemoveEmptyEntries);
                int k, v;
                int.TryParse(ar[0], out k);
                int.TryParse(ar[1], out v);
                _maxIds[k] = v;
            }
        }

        LoadNewIds();
    }



    void LoadNewIds()
    {
        int lastDay = 0;
        if (_maxIds.Count > 0)
            lastDay = _maxIds.Keys.Max();
        var today = DateTime.Today.Day;
        if (today <= (lastDay + 1))
            return;


        var sql = "select DATE_FORMAT(firstDate,'%d') date ,max(id) from daily_log.tbl_api_order_" + DateTime.Today.ToString("yyyyMM")
            + " where id>" + (lastDay == 0 ? 0 : _maxIds[lastDay]) + "   group by DATE_FORMAT(firstDate,'%d') ";

        using (var dt = dBase.GetDataTable(sql))
        {
            foreach (DataRow r in dt.Rows)
            {
                if (r.IsNull(0))
                    continue;
                int day = int.Parse((string)r[0]);
                if (day == today)
                    continue;
                maxIds[day] = (int)r[1];
            }
        }
        string datafile = Server.MapPath(string.Format("~/app_data/api_ids_{0:yy-MM}.txt", DateTime.Today));
        using (var stm = new StreamWriter(datafile))
        {
            foreach (var kv in _maxIds)
            {
                stm.WriteLine("{0},{1}", kv.Key, kv.Value);
            }
        }



    }


    void WriteData(DataTable dt)
    {
        if (dt.Rows.Count == 0)
        {
            Response.Write("[]");
            return;
        }

        var cc = dt.Columns.Count;
        var sb = new System.Text.StringBuilder("[");



        foreach (DataRow dr in dt.Rows)
        {
            sb.Append("\n{");
            for (var i = 0; i < cc; i++)
            {
                sb.AppendFormat("\"{0}\":\"{1}\",", dt.Columns[i].ColumnName, dr[i]);
            }
            sb.Length--;
            sb.Append("},");
        }
        sb.Length--;
        sb.Append("]");

        Response.Write(sb.ToString());

    }
}