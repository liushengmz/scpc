using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NoSqlModel
{
    /// <summary>
    /// 用户历史计费统计，影射tbl_custom_fee_count
    /// </summary>
    public class CustomFeeModel : Shotgun.Model.Logical.LightDataModel, n8wan.Public.Logical.ITimeCacheItem
    {
        string _idStr;

 

        #region 基类方法实现
        int ITimeCacheItem.ItemExpire { get; set; }
        public override string IdentifyField
        {
            get { return "Id"; }
        }

        public override string TableName
        {
            get { return "No Sql"; }
        }

        protected override string[] GetNullableFields()
        {
            return null;
        }
        #endregion

        /// <summary>
        /// 用于生成缓存的索引key
        /// </summary>
        /// <param name="customId"></param>
        /// <param name="troneId"></param>
        /// <returns></returns>
        public static string GetIdKey(string customId, int troneId, DateTime mrDate)
        {
            return string.Format("{0}_{1}_{2:yyMMdd}", customId, troneId, mrDate);
        }

        public string Id
        {
            get
            {
                if (_idStr == null)
                    _idStr = GetIdKey(this.CustomId, this.TroneId, this.Date);
                return _idStr;
            }
        }
        public CustomFeeModel() { }

        public string CustomId { get; set; }
        public int TroneId { get; set; }

        public int Count { get; set; }

        public int SpToneId { get; set; }

        public DateTime Date { get; set; }

        /// <summary>
        /// 单价
        /// </summary>
        public int Fee { get; set; }

        /// <summary>
        /// Fee * Count
        /// </summary>
        public int TotalFee { get { return Fee * Count; } }





    }
}

