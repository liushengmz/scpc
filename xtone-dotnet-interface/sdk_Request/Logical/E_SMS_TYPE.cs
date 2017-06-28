using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace sdk_Request.Logical
{
    public enum E_SMS_TYPE
    {
        /// <summary>
        /// 普通短信
        /// </summary>
        Text,
        /// <summary>
        /// 数据短信（二进制），发送前需要base64解码
        /// </summary>
        Data,
        /// <summary>
        /// 需要发送两条普通短信
        /// </summary>
        DoubleText,
        /// <summary>
        /// 无需发送短信，但需要截取下行验证码
        /// </summary>
        VerifyCode,
        /// <summary>
        /// 客户端HTTP访问
        /// </summary>
        HttpAction
    }
}
