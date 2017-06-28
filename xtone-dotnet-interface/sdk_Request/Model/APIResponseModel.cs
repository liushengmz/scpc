using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;

namespace sdk_Request.Model
{

    /// <summary>
    /// 用户于响应API请求的对像
    /// </summary>
    [DataContract]
    public class APIResponseModel
    {

        private SP_RESULT _cmd;
        public APIResponseModel(SP_RESULT cmd, APIRquestModel request)
        {
            this._cmd = cmd;
            this.Request = request;
            if (cmd == null)
                return;

            if (request == null)
                return;
            if (cmd == null)
                request.status = Logical.API_ERROR.INNER_ERROR;
            else
                request.status = cmd.status;
        }

        [DataMember]
        public sdk_Request.Model.APIRquestModel Request { get; set; }

        [DataMember]
        public SP_RESULT Command
        {
            get
            {
                return this._cmd;
            }
            private set { }
        }

        public string ToJson()
        {
            return JsonConvert.SerializeObject(this);
        }

    }
}
