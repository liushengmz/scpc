using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 表示可用于SP信息同步的SMS表
    /// </summary>
    public interface ISMS_DataItem
    {
        string imei { get; set; }
        string imsi { get; set; }

        int trone_id { get; set; }

        string mobile { get; set; }

        /// <summary>
        /// port
        /// </summary>
        string ori_trone { get; set; }

        /// <summary>
        /// msg
        /// </summary>
        string ori_order { get; set; }

        string service_code { get; set; }

        string cp_param { get; set; }

        string linkid { get; set; }

        /// <summary>
        /// 接收日期,MO对应mo_date,MR对应mr_date
        /// </summary>
        DateTime recdate { get; set; }

        int price { get; set; }

        string ip { get; set; }

        string mcc { get; set; }

        /// <summary>
        /// 同步URLID
        /// </summary>
        int sp_api_url_id { get; set; }

        int sp_id { get; set; }

        int trone_type { get; set; }

    }
}
