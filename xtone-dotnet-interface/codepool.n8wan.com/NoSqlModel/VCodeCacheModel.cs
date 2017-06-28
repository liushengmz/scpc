using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NoSqlModel
{
    /// <summary>
    /// 用于第二步，防止验证码，重复提交
    /// </summary>
    public class VCodeCacheModel : Shotgun.Model.Logical.LightDataModel, n8wan.Public.Logical.ITimeCacheItem
    {
        public override string IdentifyField
        {
            get { return "Id"; }
        }

        public override string TableName
        {
            get { return "No sql model"; }
        }

        protected override string[] GetNullableFields()
        {
            return null;
        }


        public int Id { get; set; }



        int n8wan.Public.Logical.ITimeCacheItem.ItemExpire { get; set; }
    }
}
