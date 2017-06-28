using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool.TelcomModel
{
    /// <summary>
    /// 短信
    /// </summary>
    public class Message
    {
        /// <summary>
        /// 短消息中的文本
        /// </summary>
        public string message { get; set; }
        /// <summary>
        /// 指示短消息发送者的名称，即作为消息发起者显示在用户终端上的名称
        /// </summary>
        public string senderAddress { get; set; }
        /// <summary>
        /// 与被调用的消息业务相关的号码，即，终端用来发送消息的目标地址
        /// </summary>
        public string smsServiceActivationNumber { get; set; }
    }

   
}
