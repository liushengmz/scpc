using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NoSqlModel
{
    public class Sms2WebModel : Shotgun.Model.Logical.LightDataModel, n8wan.Public.Logical.ITimeCacheItem
    {

        public override string IdentifyField
        {
            get { return "mobile"; }
        }

        public override string TableName
        {
            get { return "No Sql Model"; }
        }

        protected override string[] GetNullableFields()
        {
            return null;
        }


        public string mobile { set; get; }

        public string orderNum { get; set; }


        int n8wan.Public.Logical.ITimeCacheItem.ItemExpire { get; set; }
    }
}
