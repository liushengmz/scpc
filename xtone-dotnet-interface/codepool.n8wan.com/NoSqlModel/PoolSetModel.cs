using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NoSqlModel
{
    public class PoolSetModel : Shotgun.Model.Logical.LightDataModel
    {
        public int id { get; set; }
        public int cp_pool_id { get; set; }
        public int sp_trone_id { get; set; }
        public int trone_id { get; set; }
        public int trone_order_id { get; set; }

        public int priority { get; set; }

        public int status { get; set; }



        #region 基础LightDataModel

        public override string IdentifyField
        {
            get { return "id"; }
        }

        public override string TableName
        {
            get { return "no sql model"; }
        }

        protected override string[] GetNullableFields()
        {
            return null;
        }
        #endregion
    }
}
