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
    [DataContract]
    public class APIModel
    {
        /// <summary>
        /// API平台，传递给SP使用的透传参数 (如果后台配置API匹配模式为透传参数时。此处存储的值和传递给SP的内容是一致)
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String apiExdata { get; set; }

        /// <summary>
        /// 指求请求的API接口代码ID（不能修改）
        /// </summary>
        [DataMember(Name = "apiOrderId")]
        public int tbl_sp_trone_api_id { get; set; }
        /// <summary>
        /// 用户的IP（渠道传入）
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public int cid { get; set; }
        /// <summary>
        /// 用户的IP（渠道传入）
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String clientIp { get; set; }
        /// <summary>
        /// 用户传入的验证码
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String cpVerifyCode { get; set; }
        /// <summary>
        /// 渠道的透传内容
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String extrData { get; set; }
        /// <summary>
        /// 对应tbl_api_order表的ID
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public int id { get; set; }
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String imei { get; set; }
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String imsi { get; set; }
        /// <summary>
        /// 调用者IP的，非渠道传入IP
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String ip { get; set; }
        /// <summary>
        /// 扣量伪装（尚未支持）
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public int isHidden { get; set; }
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public int lac { get; set; }
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public string mobile { get; set; }
        /// <summary>
        /// 短信上行内容 （一般由系统填充）
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public string msg { get; set; }
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String netType { get; set; }
        /// <summary>
        /// 渠道传入
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String packageName { get; set; }
        /// <summary>
        ///短信上行端口 （一般由系统填充）
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String port { get; set; }
        [DataMember(EmitDefaultValue = false)]
        public String sdkVersion { get; set; }
        /// <summary>
        /// 用于存储SP回应结果的参数，用于关联同步的sp的订单号请存储到spLinkId字段
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String spExField { get; set; }
        /// <summary>
        /// 用于存储SP生成的订单号
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String spLinkId { get; set; }
        /// <summary>
        /// 相关联的SP通道ID
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public int troneId { get; set; }
        /// <summary>
        /// 输出状态（一般由系统填充）
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public int status { get; set; }
        /// <summary>
        /// CP业务ID（等同于CP的Paycode）
        /// </summary>
        [DataMember(Name = "troneOrderId")]
        public int tbl_trone_order_id { get; set; }
        /// <summary>
        /// 预留的SP需求字段，可根据业务要求渠道进行传参
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public String extraParams { get; set; }

        /// <summary>
        /// 渠道传入参数
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public string iccid { get; set; }
        /// <summary>
        /// 渠道传入参数
        /// </summary>
        [DataMember(EmitDefaultValue = false)]
        public string userAgent { get; set; }

        public object Clone()
        {
            var t = this.GetType();
            var fields = t.GetFields();
            var ret = new APIModel();
            foreach (var f in fields)
            {
                var val = f.GetValue(this);
                f.SetValue(ret, val);
            }
            return ret;
        }

        internal static APIModel CopyFrom(LightDataModel.tbl_api_orderItem _orderInfo)
        {
            var m = new APIModel();
            m.apiExdata = _orderInfo.api_exdata;
            m.cid = _orderInfo.cid;
            m.clientIp = _orderInfo.clientip;
            m.cpVerifyCode = _orderInfo.cp_verifyCode;
            m.extraParams = _orderInfo.extra_param;
            m.extrData = _orderInfo.ExtrData;
            m.iccid = _orderInfo.iccid;
            m.id = _orderInfo.id;
            m.imei = _orderInfo.imei;
            m.imsi = _orderInfo.imsi;
            m.ip = _orderInfo.ip;
            //m.isHidden = _orderInfo.is_hidden;
            m.lac = _orderInfo.lac;
            m.mobile = _orderInfo.mobile;
            m.msg = _orderInfo.msg;
            m.netType = _orderInfo.nettype;
            m.packageName = _orderInfo.packagename;
            m.port = _orderInfo.port;
            m.sdkVersion = _orderInfo.sdkversion;
            m.spExField = _orderInfo.sp_exField;
            m.spLinkId = _orderInfo.sp_linkid;
            m.status = _orderInfo.status;
            m.tbl_sp_trone_api_id = _orderInfo.api_id;
            m.tbl_trone_order_id = _orderInfo.trone_order_id;
            m.troneId = _orderInfo.trone_id;
            m.userAgent = _orderInfo.user_agent;
            return m;
        }

        public override string ToString()
        {
            using (var stm = new MemoryStream())
            {
                var dc = new DataContractJsonSerializer(this.GetType());
                dc.WriteObject(stm, this);
                return ASCIIEncoding.UTF8.GetString(stm.ToArray());
            }
        }
    }

}

