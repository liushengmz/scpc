using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace NoSqlModel
{
    /// <summary>
    /// 用户最近一次请求，使用的通道信息
    /// </summary>
    class CustomLastTroneModel : Shotgun.Model.Logical.LightDataModel, n8wan.Public.Logical.ITimeCacheItem
    {
        int n8wan.Public.Logical.ITimeCacheItem.ItemExpire { get; set; }

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


        public static long GetId(string customId, int troneId)
        {
            if (customId.Length > 15)
                customId = customId.Substring(0, 15);
            long key;
            if (!long.TryParse(customId, out key))
                return 0;

            key &= 0xFffFFffFFff;

            troneId &= 0xFFff;
            key |= (long)troneId << 47;
            return key;

        }


        public long Id { get { return GetId(this.CustomId, TroneId); } set { } }

        public string CustomId { get; set; }
        public int PoolId { get; set; }

        public int TroneId { get; set; }
    }
}
