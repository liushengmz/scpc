using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    partial class tbl_tmp_mrItem
    {
        public tbl_mrItem CopyToNewMr()
        {
            tbl_mrItem mr = new tbl_mrItem();
            mr.imei = this.imei;
            mr.imsi = this.imsi;
            mr.mobile = this.mobile;
            mr.mcc = this.mcc;
            mr.province_id = this.province_id;
            mr.city_id = this.city_id;
            mr.ori_trone = this.ori_trone;
            mr.ori_order = this.ori_order;
            mr.linkid = this.linkid;
            mr.cp_param = this.cp_param;
            mr.service_code = this.service_code;
            mr.price = this.price;
            mr.ip = this.ip;
            mr.status = this.status;
            mr.mr_date = DateTime.Now;
            mr.create_date = DateTime.Now;
            return mr;
        }
    }
}
