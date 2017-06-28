using sdk_Request.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;


namespace sdk_Request.Model
{
    [DataContract]
    public class SP_RESULT
    {

        private string _descripton;

        [IgnoreDataMember]
        public API_ERROR status { get; set; }

        [DataMember(Order = 0)]
        public E_SMS_TYPE SMSType { get; set; }

        public SP_RESULT()
        {
            this.status = API_ERROR.OK;
        }


        [DataMember]
        public string description
        {
            get { return _descripton ?? status.ToString(); }
            set { _descripton = value; }
        }

        public string ToJson()
        {
            using (var stm = new System.IO.MemoryStream())
            {
                var dc = new DataContractJsonSerializer(this.GetType());
                dc.WriteObject(stm, this);
                return ASCIIEncoding.UTF8.GetString(stm.ToArray());
            }
        }
    }


    /// <summary>
    /// 二次短信
    /// </summary>
    [Obsolete("使用SP_2SMS_ResultV2替换")]
    public class SP_2SMS_Result : SP_SMS_Result
    {
        [DataMember]
        public string port2 { get; set; }
        [DataMember]
        public string msg2 { get; set; }
        /// <summary>
        /// 两条指令间隔时间(秒)
        /// </summary>
        [DataMember]
        public int interval;
    }

    /// <summary>
    /// 二次短信
    /// </summary>
    public class SP_2SMS_ResultV2 : SP_SMS_Result
    {
        [DataMember]
        public string port2 { get; set; }
        [DataMember]
        public string msg2 { get; set; }
        /// <summary>
        /// 两条指令间隔时间(秒)
        /// </summary>
        [DataMember]
        public int interval { get; set; }

        /// <summary>
        /// 短信2类型
        /// </summary>
        [DataMember]
        public E_SMS_TYPE SMSType2 { get; set; }

    }


    /// <summary>
    /// 一次短信
    /// </summary>
    public class SP_SMS_Result : SP_RESULT
    {
        [DataMember]
        public string port { get; set; }
        [DataMember]
        public string msg { get; set; }
    }

    /// <summary>
    /// 一次短信，带反馈URL，代码要求：发送短信后，在XX秒后，访问指定URL
    /// </summary>
    public class SP_SMS_Feedback_Result : SP_SMS_Result
    {
        [DataMember]
        public String FeedBack { get; set; }
        [DataMember]
        public int Interval { get; set; }
    }

    ///// <summary>
    ///// 验证码模式，第一步
    ///// </summary>
    //public class SP_VCode_Result : SP_RESULT
    //{
    //    public SP_VCode_Result()
    //    {
    //        SMSType = E_SMS_TYPE.VerifyCode;
    //    }
    //    [DataMember]
    //    public string OrderId;
    //}

}
