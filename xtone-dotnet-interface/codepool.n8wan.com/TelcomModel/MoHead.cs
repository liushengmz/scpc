using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool.TelcomModel
{
    /// <summary>
    /// 上行SOAPHEADER
    /// </summary>
    public class MoHeader
    {
        /// <summary>
        /// 使用CPId，可选（用于鉴权）
        /// </summary>
        public string cpRevId { get; set; }
        /// <summary>
        /// CP运营平台分配的秘钥，可选（用于鉴权）
        /// </summary>
        public string cpRevpassword { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public string spId { get; set; }
        /// <summary>
        /// 业务流水号，可用于组合业务
        /// </summary>
        public string transactionId { get; set; }
        /// <summary>
        /// 事务关联ID，用于点播业务的事务关联，由平台产生。格式如下：MMDDHHMMSS+10位随机序列号；为空（二进制0）标识无效，可选
        /// </summary>
        public string linkId { get; set; }
        /// <summary>
        /// 产品接入码、ServiceKey，可选
        /// </summary>
        public string san { get; set; }
    }
}
