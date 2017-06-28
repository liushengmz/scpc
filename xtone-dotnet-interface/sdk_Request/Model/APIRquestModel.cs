using sdk_Request.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace sdk_Request.Model
{
    [DataContract]

    public class APIRquestModel : ICloneable
    {
        /// <summary>
        /// API平台，传递给SP使用的透传参数 (如果后台配置API匹配模式为透传参数时。此处存储的值和传递给SP的内容是一致)
        /// </summary>
        [DataMember]
        public String apiExdata;

        /// <summary>
        /// 指求请求的API接口代码ID（不能修改）
        /// </summary>
        [DataMember(Name = "apiOrderId")]
        public String tbl_sp_trone_api_id;
        /// <summary>
        /// 用户的IP（渠道传入）
        /// </summary>
        [DataMember]
        public int cid;
        /// <summary>
        /// 用户的IP（渠道传入）
        /// </summary>
        [DataMember]
        public String clientIp;
        /// <summary>
        /// 用户传入的验证码
        /// </summary>
        [DataMember]
        public String cpVerifyCode;
        /// <summary>
        /// 渠道的透传内容
        /// </summary>
        [DataMember]
        public String extrData;
        /// <summary>
        /// 对应tbl_api_order表的ID
        /// </summary>
        [DataMember]
        public int id;
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember]
        public String imei;
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember]
        public String imsi;
        /// <summary>
        /// 调用者IP的，非渠道传入IP
        /// </summary>
        [DataMember]
        public String ip;
        /// <summary>
        /// 扣量伪装（尚未支持）
        /// </summary>
        [DataMember]
        public int isHidden;
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember]
        public int lac;
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember]
        public String mobile;
        /// <summary>
        /// 短信上行内容 （一般由系统填充）
        /// </summary>
        [DataMember]
        public String msg;
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember]
        public String netType;
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember]
        public String packageName;
        /// <summary>
        ///短信上行端口 （一般由系统填充）
        /// </summary>
        [DataMember]
        public String port;
        [DataMember]
        public String sdkVersion;
        /// <summary>
        /// 用于存储SP回应结果的参数，用于关联同步的sp的订单号请存储到spLinkId字段
        /// </summary>
        [DataMember]
        public String spExField;
        /// <summary>
        /// 用于存储SP生成的订单号
        /// </summary>
        [DataMember]
        public String spLinkId;
        /// <summary>
        /// 相关联的SP通道ID
        /// </summary>
        [DataMember]
        public int troneId;
        /// <summary>
        /// 输出状态（一般由系统填充）
        /// </summary>
        [DataMember]
        public API_ERROR status;
        /// <summary>
        /// CP业务ID（等同于CP的Paycode）
        /// </summary>
        [DataMember(Name = "troneOrderId")]
        public String tbl_trone_order_id;
        /// <summary>
        /// 预留的SP需求字段，可根据业务要求渠道进行传参
        /// </summary>
        [DataMember]
        public String extraParams;

        /// <summary>
        /// 渠道传入参数
        /// </summary>
        [DataMember]
        public string iccid;
        /// <summary>
        /// 渠道传入参数
        /// </summary>
        [DataMember]
        public string userAgent;

        public object Clone()
        {
            var t = this.GetType();
            var fields = t.GetFields();
            var ret = new APIRquestModel();
            foreach (var f in fields)
            {
                var val = f.GetValue(this);
                f.SetValue(ret, val);
            }
            return ret;
        }
    }


}
