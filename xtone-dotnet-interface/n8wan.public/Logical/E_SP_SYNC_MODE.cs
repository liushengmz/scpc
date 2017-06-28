using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// CP同步模式
    /// </summary>
    public enum E_CP_SYNC_MODE
    {
        /// <summary>
        /// 自动模式，按扣量设置，自动处理
        /// </summary>
        Auto,
        /// <summary>
        /// 不扣量
        /// </summary>
        ForcePush,
        /// <summary>
        /// 直接扣量
        /// </summary>
        ForceHide

    }
}
