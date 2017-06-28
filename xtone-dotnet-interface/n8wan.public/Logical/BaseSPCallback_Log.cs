using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    partial class BaseSPCallback
    {//这部分代码用于生成跟踪日志记录

        Stopwatch _watch = null;
        StringBuilder _bugLog = null;
        long _lastDebugElapsed;
        string GetElapsedMilliseconds()
        {
            if (_watch != null)
            {
                var cur = _watch.ElapsedMilliseconds;
                var ret = string.Format("{0}ms(+{1})", cur, cur - _lastDebugElapsed);
                _lastDebugElapsed = cur;
                return ret;
            }

            _watch = new Stopwatch();
            _watch.Start();

            _lastDebugElapsed = 0;
            return "{0}ms(0)";
        }
        void WriteDebug(string funcName, bool IsExit)
        {
            WriteDebug(string.Format("{0}-{1}", IsExit ? "OUT" : "IN", funcName));
        }

        void WriteDebug(string msg)
        {
            if (_bugLog == null)
            {
                _bugLog = new StringBuilder();
                _bugLog.AppendFormat("{0:HH:MM:ss} {1} ", DateTime.Now, Request.Url.AbsolutePath);
            }
            _bugLog.AppendFormat("\r\n\t->({0}){1}", GetElapsedMilliseconds(), msg);
        }

        void FlushDebug()
        {
#if !DEBUG
            return;
#endif
            if (_bugLog == null || _bugLog.Length == 0)
                return;
            WriteDebug("Done");
            Shotgun.Library.SimpleLogRecord.WriteLog(Request.MapPath(string.Format("~/log/R_{0:yyyy-MM-dd}.log", DateTime.Now)), _bugLog.ToString());
            _bugLog.Clear();
        }
    }
}
