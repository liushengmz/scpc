using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Model
{
    public class SpTroneCountInfo
    {
        /// <summary>
        /// SP 业务ID
        /// </summary>
        public int SpTroneId { get; set; }
        /// <summary>
        /// 收到计费条数
        /// </summary>
        public int Count { get; set; }
        /// <summary>
        /// 收到金额（单位：分）
        /// </summary>
        public int Sum { get; set; }

        internal static SpTroneCountInfo LoadFromDbase(Shotgun.Database.IBaseDataClass2 dBase, int spTroneId, bool isMonth)
        {
            var cmd = dBase.Command();
            var sql = "select count(0),sum(trone.price)*100 from daily_log.tbl_mr_{0:yyyyMM} mr"
                 + " left join daily_config.tbl_trone trone on trone.id=mr.trone_id where mr.sp_trone_id={1}";

            if (!isMonth)
                sql += " and mr_date='{0:yyyy-MM-dd}'";

            cmd.CommandText = string.Format(sql, DateTime.Today, spTroneId);
            using (cmd)
            {
                using (var rd = dBase.ExecuteReader(cmd))
                {
                    var info = new SpTroneCountInfo();
                    info.SpTroneId = spTroneId;
                    if (!rd.Read())
                    {
                        info.Count = int.Parse(rd.GetValue(0).ToString());
                        info.Sum = int.Parse(rd.GetValue(1).ToString());
                    }
                    return info;
                }
            }
        }
    }
}
