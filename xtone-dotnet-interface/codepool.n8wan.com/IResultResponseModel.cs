using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.codepool
{
    public interface IResultResponseModel
    {
        ErrorCode Status { get; set; }

        string Description { get; set; }

        string OrderNum { get; set; }

        JToken Action { get; set; }

    }
}

