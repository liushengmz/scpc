using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool.TelcomModel
{
    public class Body
    {
        /// <summary>
        /// 短信内容
        /// </summary>
        public Message message { get; set; }
        /// <summary>
        /// 标识预先约定的准则，根据该准则，应用可收到短消息通知
        /// </summary>
        public string registrationIdentifier { get; set; }
    }
}
