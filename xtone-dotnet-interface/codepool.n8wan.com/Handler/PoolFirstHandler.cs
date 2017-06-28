using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace n8wan.codepool.Handler
{
    public class PoolFirstHandler : Shotgun.PagePlus.SimpleHttpHandler
    {
        private CodePool poolCore;
        public sealed override void BeginProcess()
        {
            poolCore = new CodePool();
            poolCore.dBase = dBase;
            var db3 = (Shotgun.Database.IBaseDataPerformance)dBase;
            db3.EnableRecord = true;
            try
            {
                if (!Init())
                    return;

                var pm = poolCore.GetPSModelByPriority();
                if (pm == null)
                    return;
                poolCore.QueryCmd();
            }
            finally
            {
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
            Response.Write(jstr);
        }

        bool Init()
        {
            if (!poolCore.Init(Request))
                return false;
            if (!poolCore.InitTrone())
                return false;
            return true;
        }
    }
}
