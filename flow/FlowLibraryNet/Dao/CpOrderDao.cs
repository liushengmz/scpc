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
        }

        /// <summary>
        /// 根据自定义订单，查找订单
        /// </summary>
        /// <param name="customId">自定义规则：yyyyMM+id,如：20170788888</param>
        /// <returns></returns>
        public tbl_f_cp_order_listItem GetCustomId(string customId)
        {
            customId = "SCOR_PREFIX_" + customId;
            var m = _ro.GetModel<tbl_f_cp_order_listItem>(customId);
            if (m != null)
                m.id = (int)(StackExchange.Redis.RedisValue)m["MONTH_TABLE_ID"];
            return m;
        }

        public void Update(tbl_f_cp_order_listItem m)
        {
            var customId = "SCOR_PREFIX_" + m.sp_order_id;
            _ro.SetModel(m, customId);

            var tmp = new tbl_f_cp_order_list_tempItem();
            tmp.id = m.id;
            tmp.sp_status = m.sp_status;
            tmp.sp_error_msg = m.sp_error_msg;
            tmp.status = m.status;

            dBase.SaveData(m);
            dBase.SaveData(tmp);

        }
    }
}
