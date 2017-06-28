using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NoSqlModel
{
    /// <summary>
    /// 用户日月限信息
    /// </summary>
    public class CustomLimitInfo
    {
        public string CustomId { get; set; }
        public int DayCount { get; set; }
        /// <summary>
        /// 包括当日计费信息
        /// </summary>
        public int MonthCount { get; set; }
        public int DayAmount { get; set; }
        /// <summary>
        /// 包括当天计费信息
        /// </summary>
        public int MonthAmount { get; set; }

        //public List<CustomFeeModel> Detail { get; set; }
    }
}
