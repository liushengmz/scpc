using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlowLibraryNet.Logical
{
    public interface IFlowOrderInfo
    {
        string OrderId { get; set; }
        ChangeOrderStatusEnum StatusE { get; set; }
        string SpErrorMsg { get; set; }
        string SpStatus { get; set; }
        int PriceId { get; }
        string Mobile { get; }

        /// <summary>
        /// 1 联通 2 电信 3 移动 4 虚拟
        /// </summary>
        OperatorType Operator { get; }

        /// <summary>
        /// 0全国 1省内
        /// </summary>
        int Rang { get; }

        int id { get; set; }
        int sp_api_id { get; set; }

        object this[string key] { get; set; }
    }
}
