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
    public class PoolResultModel : IResultResponseModel
    {
        private JToken _action;

        [JsonProperty("status")]
        public ErrorCode Status { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }

        [JsonProperty("orderNum", NullValueHandling = NullValueHandling.Ignore)]
        public string OrderNum { get; set; }

        [JsonProperty("jsonResult", NullValueHandling = NullValueHandling.Ignore)]
        public JToken Action
        {
            get { return _action; }
            set
            {
                _action = value;
                if (value == null)
                    return;

                var des = value["description"];
                if (des != null)
                {
                    this.Description = des.Value<string>();
                    des.Parent.Remove();
                }
            }
        }


        public override string ToString()
        {
            Newtonsoft.Json.JsonSerializerSettings jss = null;
            return Newtonsoft.Json.JsonConvert.SerializeObject(this, Newtonsoft.Json.Formatting.None, jss);
        }
    }
}
