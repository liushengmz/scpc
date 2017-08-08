using LightDataModel;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 数据补推送数据生成
    /// </summary>
    public class AgainPusher : Shotgun.Model.Logical.Logical
    {

        static AgainPusher _lastInstance;
        private int _position;
        private int _total;
        static string _lastLog;

        private AgainPusher()
        {
            var h = System.Web.HttpContext.Current;
            if (h != null)
                this.logFile = h.Server.MapPath(string.Format("~/pushlog/R_{0:yyyyMMdd-HH}.log", DateTime.Now));
            else
                this.logFile = AppDomain.CurrentDomain.BaseDirectory + string.Format("pushlog/R_{0:yyyyMMdd-HH}.log", DateTime.Now);
        }


        public static AgainPusher getInstance()
        {
            if (_lastInstance != null)
                return null;
            return new AgainPusher();
        }
        public static string GetLastLogFile()
        {
            if (string.IsNullOrEmpty(_lastLog))
                return null;
            try
            {
                var f = new FileInfo(_lastLog);
                return f.Name;
            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// 获取处理进度
        /// </summary>
        /// <returns></returns>
        public static int GetPercent()
        {
            var t = _lastInstance;
            if (t == null)
                return -1;
            if (t._total == 0)
                return -1;
            return t._position << 8 | ((byte)((t._position << 8) / t._total));
        }

        /// <summary>
        /// 需要补同步的id
        /// </summary>
        public int[] MrIds { get; set; }
        /// <summary>
        /// MR的日期
        /// </summary>
        public DateTime MRDate { get; set; }

        public new Shotgun.Database.IBaseDataClass2 dBase { get { return base.dBase; } private set { base.dBase = value; } }

        /// <summary>
        /// 创建推送任务（不阻塞，后台自动推送）
        /// </summary>
        /// <returns></returns>
        public bool DoAgain()
        {
            if (_lastInstance != null)
                return false;
            _lastInstance = this;
            _lastLog = this.logFile;
            ThreadPool.QueueUserWorkItem(
                e =>
                {
                    try
                    {
                        dBase = new Shotgun.Database.DBDriver().CreateDBase();
                        syncCore();
                    }
                    catch (Exception ex)
                    {
                        WriteLog("处理错误!" + ex.ToString());
                    }
                    finally
                    {
                        _lastInstance = null;
                        ((IDisposable)dBase).Dispose();
                    }

                });
            return true;

        }

        void syncCore()
        {
            var q = tbl_mrItem.GetQueries(dBase);
            q.TableDate = MRDate;
            q.Filter.AndFilters.Add(tbl_mrItem.Fields.PrimaryKey, MrIds);
            q.PageSize = int.MaxValue;
            //q.Fields = new string[] { tbl_mrItem.Fields.PrimaryKey,tbl_mrItem.Fields. };
            List<tbl_mrItem> data = null;

            try
            {
                data = q.GetDataList();
            }
            catch (System.Data.Common.DbException)
            {
                WriteLog("数据库超时");
                return;
            }
            //List<string> urls = new List<string>();
            if (data == null || data.Count == 0)
            {
                WriteLog("无符条件记录；sql=" + q.LastSqlExecute);
                return;
            }
            this._total = data.Count;
            int err = 0;
            foreach (var m in data)
            {
                this._position++;
                try
                {
                    if (!pushMr(m))
                    {
                        WriteLog(ErrorMesage);
                    }
                    err = 0;
                }
                catch (System.Data.Common.DbException ex)
                {
                    WriteLog("错误:" + ex.Message);
                    err++;
                    if (err > 3)//连续出错时，中止推送
                    {
                        WriteLog("连续出错，任务中止！");
                        return;
                    }
                }
            }
            WriteLog("所有任务已经完成！");
            return;
        }


        private void WriteLog(string msg)
        {
            Shotgun.Library.SimpleLogRecord.WriteLog(logFile, msg);
        }

        private bool pushMr(tbl_mrItem m)
        {
            tbl_troneItem trone;

            if (!m.IsMatch)
            {
                trone = BaseSPCallback.FillToneId(dBase, m);
                if (trone == null)
                {
                    SetErrorMesage(tbl_troneItem.GetTroneErrMsg(m.trone_id));
                    return false;
                }
            }
            trone = LightDataModel.tbl_troneItem.GetRowById(dBase, m.trone_id);
            var sb = new StringBuilder(250);

            var apiPush = new n8wan.Public.Logical.HTAPIPusher()
            {
                dBase = dBase,
                Trone = trone,
                LogFile = logFile,
                IsSyncPush = true,
                TrackLog = sb
            };
            if (apiPush.LoadCPAPI())
            {
                apiPush.PushObject = m;
                if (apiPush.DoPush())
                {
                    return true;
                }
            }

            var cp = new n8wan.Public.Logical.AutoMapPush();
            cp.dBase = dBase;
            cp.Trone = trone;
            cp.LogFile = logFile;
            cp.IsSyncPush = true;
            cp.TrackLog = sb;



            if (!cp.LoadCPAPI())
            {
                SetErrorMesage(sb.ToString());
                return false;
            }

            cp.PushObject = m;
            if (cp.DoPush())
            {
                return true;
            }
            SetErrorMesage(sb.ToString());
            return false;

        }

        public string logFile { get; private set; }
    }
}
