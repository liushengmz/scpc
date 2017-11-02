using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlowLibraryNet.Logical
{
    public interface IFlowOrderInfo
    {
        string SpOrderId { get; set; }
        ChangeOrderStatusEnum StatusE { get; set; }
        string SpErrorMsg { get; set; }
        string SpStatus { get; set; }
        int PriceId { get; set; }
        string Mobile { get; set; }

        int id { get; set; }
        int sp_api_id { get; set; }

        object this[string key] { get; set; }
    }
}
