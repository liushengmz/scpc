using FlowLibraryNet.Logical;
using LightDataModel;
using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlowLibraryNet.Dao
{
    public class CpOrderDao : Shotgun.Model.Logical.Logical
    {
        RedisOpeartor _ro;

        public CpOrderDao(IBaseDataClass2 db) : base(db)
        {
            _ro = new RedisOpeartor();
        }
        public override IBaseDataClass2 dBase
        {
            set
            {
                var db = base.dBase;
                if (db != null)
                    db.OnConnectionClosed -= DBase_OnConnectionClosed;

                if (value != null)
                    value.OnConnectionClosed += DBase_OnConnectionClosed;

                base.dBase = value;
            }
        }



        private void DBase_OnConnectionClosed(object sender, EventArgs e)
        {
            if (dBase.Disposed)
                _ro.Dispose();
            _ro = null;
        }

        /// <summary>
        /// 根据自定义订单，查找订单
        /// </summary>
        /// <param name="customId">自定义规则：yyyyMM+id,如：20170788888</param>
        /// <returns></returns>
        public IFlowOrderInfo GetCustomId(string customId)
        {
            customId = GetRedisKey(customId);
            IFlowOrderInfo m = null;
            if (customId.StartsWith("SCOR_PREFIX_"))
                m = _ro.GetModel<tbl_f_cp_order_listItem>(customId);
            else
                m = _ro.GetModel<tbl_f_ch_orderItem>(customId);

            if (m != null)
                m.id = (int)(StackExchange.Redis.RedisValue)m["MONTH_TABLE_ID"];
            return m;
        }

        public void Update(IFlowOrderInfo m)
        {
            var up = (Shotgun.Database.IUpatedataInfo)m;
            var customId = GetRedisKey(m.OrderId);
            _ro.SetModel(up, customId);

            //var tmp = new tbl_f_cp_order_list_tempItem();
            //tmp.id = m.id;
            //tmp.sp_status = m.sp_status;
            //tmp.sp_error_msg = m.sp_error_msg;
            //tmp.status = m.status;

            dBase.SaveData(up);
            //dBase.SaveData(tmp);

        }

        static string GetRedisKey(string vrKey)
        {
            if (vrKey.StartsWith("CC"))
                return "CCOD_" + vrKey;
            return "SCOR_PREFIX_" + vrKey;
        }
    }
}
