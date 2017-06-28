using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Database
{
    /// <summary>
    /// 提供性能追踪服务
    /// </summary>
    public interface IBaseDataPerformance
    {
        bool EnableRecord { get; set; }
        string PerformanceReport();
    }
}
