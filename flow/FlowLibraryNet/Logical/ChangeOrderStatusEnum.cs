using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlowLibraryNet.Logical
{
    public enum ChangeOrderStatusEnum
    {
        /// <summary>
        /// 0:未处理
        /// </summary>
        Unkonw = 0,


        /// <summary>
        /// 充值成功(收到成功回调)
        /// </summary>
        Success = 2,
        /// <summary>
        /// 提交供应商成功
        /// </summary>
        Charging = 12003,
        /// <summary>
        ///SP未处理的错误返回（接口返回失败）
        /// </summary>
        SpUnkowError = 12001,
        /// <summary>
        /// 内部错误充值时发错误（与SP对接有问题或充值代码有bug）
        /// </summary>
        InnerError = 12005,
        /// <summary>
        ///  充值失败（收到失败的回调）
        /// </summary>
        ChargFail = 12004,
        /// <summary>
        /// 创建订单超时
        /// </summary>
        GatewayTimeout = 12006,
        /// <summary>
        /// 创建订单时出错
        /// </summary>
        GatewayError = 12007,
        /// <summary>
        /// SP预付费金额不足
        /// </summary>
        BalanceLow = 12002


    }
}
