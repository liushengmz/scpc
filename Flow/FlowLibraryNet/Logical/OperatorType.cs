using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlowLibraryNet.Logical
{
    public enum OperatorType
    {
        Unkonw = 0,
        /// <summary>
        /// 联通
        /// </summary>
        Unicom = 1,
        /// <summary>
        /// 电信
        /// </summary>
        Tcom = 2,
        /// <summary>
        /// 移动
        /// </summary>
        Cmcc = 3,
        /// <summary>
        /// 虚拟
        /// </summary>
        Virtual = 4
    }
}
