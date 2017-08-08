using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 日志记录对像
    /// </summary>
    public interface ILoger
    {
        void WriteLog(int p, string msg);
        void WriteLog(string msg);

        void WriteTrackLog(string msg);
    }
}
