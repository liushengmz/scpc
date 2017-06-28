using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_sp_trone_apiItem
    {
        public enum EMathcField
        {
            LinkId,
            /// <summary>
            /// 同步与上行一致
            /// </summary>
            Msg,
            Cpprams,
            /// <summary>
            /// 同步指令端口和指令与上行不一致
            /// </summary>
            Msg_Not_Equal
        }

        /// <summary>
        /// 根据SP_trone_id找出对应的配置数据
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="spTroneId"></param>
        /// <returns></returns>
        public static tbl_sp_trone_apiItem GetRowByTroneId(Shotgun.Database.IBaseDataClass2 dBase, int TroneId)
        {
            var sql = "select * from tbl_sp_trone_api where id in ( select trone_api_id from tbl_sp_trone where id in(select sp_trone_id from tbl_trone where id="
                + TroneId + ")) limit 1 ";
            var l = GetQueries(dBase);

            var ms = l.GetDataListBySql(sql);
            if (ms == null || ms.Count == 0)
                return null;
            return ms[0];
        }


        public EMathcField match_field_E { get { return (EMathcField)this.match_field; } set { this.match_field = (int)value; } }
    }
}
