using System;
using System.Collections.Generic;
using System.Text;

namespace Shotgun.PagePlus
{
    /// <summary>
    /// GZip支持接口
    /// </summary>
    public interface IGZipPage
    {
        /// <summary>
        /// 客户端是否支持
        /// </summary>
        bool IsSupport { get ; }

        /// <summary>
        /// 是否允许压缩
        /// </summary>
        bool AllowCompress { get; set; }
    }
}
