using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Shotgun.Database
{
    partial class BDClass : IBaseDataPerformance
    {
        #region IBaseDataClass3 成员

        bool _performanceRecord;
        long _totalTicks;
        StringBuilder _pReport;

        bool IBaseDataPerformance.EnableRecord
        {
            get
            {
                return _performanceRecord;
            }
            set
            {
                _performanceRecord = value;
                if (_performanceRecord)
                {
                    ticker = new System.Diagnostics.Stopwatch();
                    _pReport = new StringBuilder(250);

                }
                else
                {
                    ticker = null;
                    _pReport = null;
                }
            }
        }

        string IBaseDataPerformance.PerformanceReport()
        {
            if (_pReport == null)
                return "未开启统计功能";
            TimeSpan ts = new TimeSpan(_totalTicks);
            return string.Format("{0}总计：{1}ms", _pReport.ToString(), ts.TotalMilliseconds);
        }

        #endregion


        System.Diagnostics.Stopwatch ticker;
        protected void TimerStart()
        {
            if (!_performanceRecord)
                return;

            ticker.Start();
            //ticker.Start();
        }

        protected void TimerEnd(string TSql)
        {
            if (!_performanceRecord)
                return;
            ticker.Stop();
            _totalTicks += ticker.Elapsed.Ticks;
            _pReport.AppendFormat("+{0:0.00}ms\t{1}\r\n", ticker.Elapsed.TotalMilliseconds, TSql);
            ticker.Reset();
        }

    }
}
