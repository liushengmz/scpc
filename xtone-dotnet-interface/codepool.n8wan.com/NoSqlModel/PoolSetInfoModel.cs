using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NoSqlModel
{
    public class PoolSetInfoModel : Shotgun.Model.Logical.LightDataModel
    {

        public int paycode { get; set; }

        public int trone_id { get; set; }

        public string trone_name { get; set; }
        public decimal price { get; set; }
        public int sp_trone_id { set; get; }
        public string name { get; set; }
        public int cp_pool_id { set; get; }
        public int sp_id { set; get; }
        public string provinces { get; set; }

        public int status { get; set; }

        public int priority { get; set; }

        public int cp_pool_set_id { get; set; }


        public override string IdentifyField
        {
            get { return "paycode"; }
        }

        public override string TableName
        {
            get { return "PoolSetInfoModel noSql"; }
        }

        protected override string[] GetNullableFields()
        {
            return null;
        }
    }
}
