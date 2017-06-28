using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;

namespace sdk_Request.Model
{

    [Flags]
    public enum WX_SMS_TYPE
    {
        Undefine = 0,
        /// <summary>
        /// 普通短信
        /// </summary>
        Text = 1,
        /// <summary>
        /// 使用了Base64编码
        /// </summary>
        Base64 = 2,
        /// <summary>
        /// 数据短信（二进制）
        /// </summary>
        Data = 4,
        Base64Data = Base64 | Data,
        Base64Text = Base64 | Text

    }

    public class wxSMS : sdk_Request.Model.SP_RESULT
    {
        public wxSMS()
        {
            this.type = WX_SMS_TYPE.Text;
            this.type2 = WX_SMS_TYPE.Text;
        }
        [DataMember]
        public string port { get; set; }
        [DataMember]
        public string msg { get; set; }
        [DataMember]
        public string port2 { get; set; }
        [DataMember]
        public string msg2 { get; set; }
        [DataMember]
        public WX_SMS_TYPE type { get; set; }
        [DataMember]
        public WX_SMS_TYPE type2 { get; set; }
        [DataMember]
        public int interval { get; set; }
        [IgnoreDataMember]
        public new sdk_Request.Logical.E_SMS_TYPE SMSType { get; set; }

    }
}
