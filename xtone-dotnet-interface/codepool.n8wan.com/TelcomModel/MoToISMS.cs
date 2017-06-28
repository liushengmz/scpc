using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool.TelcomModel
{
    class MoToISMS : ISMS_DataItem
    {
        private LightDataModel.tbl_moItem _mo;
        public MoToISMS(LightDataModel.tbl_moItem mo)
        {
            this._mo = mo;
        }

        public string imei
        {
            get
            {
                return _mo.imei;
            }
            set
            {
                _mo.imei = value;
            }
        }

        public string imsi
        {
            get
            {
                return _mo.imsi;
            }
            set
            {
                _mo.imsi = value;
            }
        }

        public int trone_id
        {
            get
            {
                return _mo.trone_id;
            }
            set
            {
                _mo.trone_id = value;
            }
        }

        public string mobile
        {
            get
            {
                return _mo.mobile;
            }
            set
            {
                _mo.mobile = value;
            }
        }

        public string ori_trone
        {
            get
            {
                return _mo.ori_trone;
            }
            set
            {
                _mo.ori_trone = value;
            }
        }

        public string ori_order
        {
            get
            {
                return _mo.ori_order;
            }
            set
            {
                _mo.ori_order = value;
            }
        }

        public string service_code
        {
            get
            {
                return _mo.service_code;
            }
            set
            {
                _mo.service_code = value;
            }
        }

        public string cp_param
        {
            get
            {
                return _mo.cp_param;
            }
            set
            {
                _mo.cp_param = value;
            }
        }

        public string linkid
        {
            get
            {
                return _mo.linkid;
            }
            set
            {
                _mo.linkid = value;
            }
        }

        public DateTime recdate
        {
            get
            {
                return _mo.recdate;
            }
            set
            {
                _mo.recdate = value;
            }
        }

        public int price
        {
            get
            {
                return _mo.price;
            }
            set
            {
                _mo.price = value;
            }
        }

        public string ip
        {
            get
            {
                return _mo.ip;
            }
            set
            {
                _mo.ip = value;
            }
        }

        public string mcc
        {
            get
            {
                return _mo.mcc;
            }
            set
            {
                _mo.mcc = value;
            }
        }

        public int sp_api_url_id
        {
            get
            {
                return _mo.sp_api_url_id;
            }
            set
            {
                _mo.sp_api_url_id = value;
            }
        }

        public int sp_id
        {
            get
            {
                return _mo.sp_id;
            }
            set
            {
                _mo.sp_id = value;
            }
        }

        public int trone_type
        {
            get
            {
                return _mo.trone_type;
            }
            set
            {
                _mo.trone_type = value;
            }
        }
    }
}
