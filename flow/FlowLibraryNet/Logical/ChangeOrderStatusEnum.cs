using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlowLibraryNet.Logical
{
    public enum ChangeOrderStatusEnum : byte
    {
        /// <summary>
        /// 0:未处理
        /// </summary>
        Unkonw,
        /// <summary>
        /// １处理中
        /// </summary>
        Procesing,
        /// <summary>
        /// 提交供应商成功
        /// </summary>
        Changing,
        /// <summary>
        /// ３提交ＳＰ失败
        /// </summary>
        SpError,
        /// <summary>
        /// ４充值成功
        /// </summary>
        Success,
        /// <summary>
        /// 5退款
        /// </summary>
        Cancel = 5
    }
}
