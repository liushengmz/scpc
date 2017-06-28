using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.codepool.Handler
{
    public class PoolSecondHandler : Shotgun.PagePlus.SimpleHttpHandler
    {
        private CodePoolStep2 poolCore;
        public override void BeginProcess()
        {
            poolCore = new CodePoolStep2();
            poolCore.dBase = dBase;
            var db3 = (Shotgun.Database.IBaseDataPerformance)dBase;
            db3.EnableRecord = true;
            try
            {
                if (!poolCore.Init(Request))
                    return;

                poolCore.QueryCmd();
            }
            finally
            {
                poolCore.RemoveLocker();
                WriteResult();
                Shotgun.Library.SimpleLogRecord.WriteLog("PoolSqlRecord", "\r\n" + db3.PerformanceReport());
                Shotgun.Library.SimpleLogRecord.WriteLog("PoolSqlRecord", "\r\n------------------");
            }
        }

        private void WriteResult()
        {
            var prm = ResultModelManager.CreateModel();
            prm.Status = poolCore.ECode;
            prm.Description = poolCore.ErrorMesage;
            prm.OrderNum = poolCore.OrderNum;
            if (poolCore.spResult != null)
                prm.Action = poolCore.spResult;
            poolCore.SaveOrder();
            Response.Clear();
            var jstr = prm.ToString();
            //jstr.Replace(":null");
            Response.Write(jstr);
        }

    }
}
