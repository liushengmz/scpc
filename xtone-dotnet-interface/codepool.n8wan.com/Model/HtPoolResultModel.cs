using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;


namespace n8wan.codepool.Model
{
    /// <summary>
    /// 响应结果
    /// </summary>
    public class HtPoolResultModel : IResultResponseModel
    {
        [JsonProperty("code")]
        public ErrorCode Status { get; set; }

        [JsonProperty("message", NullValueHandling = NullValueHandling.Ignore)]
        public string Description
        {
            get { return null; }
            set
            {
                var act = Action;
                if (act == null)
                    Action = act = new JObject();
                act["description"] = value;
            }
        }

        [JsonProperty("orderNum", NullValueHandling = NullValueHandling.Ignore)]
        public string OrderNum { get; set; }

        [JsonProperty("action")]
        public JToken Action { get; set; }


        public override string ToString()
        {
            Newtonsoft.Json.JsonSerializerSettings jss = null;
            return Newtonsoft.Json.JsonConvert.SerializeObject(this, Newtonsoft.Json.Formatting.None, jss);
        }
    }
}
