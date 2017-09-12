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
            var m = _ro.GetModel<tbl_f_cp_order_listItem>(customId);
            //if(m==null)
            return m;
        }

        public void Update(tbl_f_cp_order_listItem m)
        {
            _ro.SetModel(m, string.Format("{0}{1}", m.month, m.id));
            dBase.SaveData(m);
        }
    }
}
