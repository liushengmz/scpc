#define TDEBUG
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using sdk_Request.Model;
using System.Threading;
using System.Threading.Tasks;

namespace sdk_Request.Logical
{
    public abstract class AsyncAPIRequestGet : APIRequestGet
    {

        /// <summary>
        /// 正在异步获取验证码的订单号
        /// </summary>
        static List<int> _inSOne = new List<int>();

        /// <summary>
        /// 当前订单是否在处理队列，用于防止取验证码接口缓慢导致，验证码提交失败
        /// </summary>
        /// <returns></returns>
        bool InSOne()
        {
            lock (_inSOne)
            {
                return _inSOne.Contains(OrderInfo.id);
            }
        }

        void RemoveSOneFlag()
        {
            if (Step != 1)
                return;

            lock (_inSOne)
            {
                if (_inSOne.Contains(OrderInfo.id))
                    _inSOne.Remove(OrderInfo.id);
            }
        }
        void AddSOneFlag()
        {
            if (Step != 1)
                return;
            lock (_inSOne)
            {
                if (!_inSOne.Contains(OrderInfo.id))
                    _inSOne.Add(OrderInfo.id);
            }
        }


        /// <summary>
        /// 同步模式最大等待时间，超时后即转入异步模试(单位：ms)
        /// </summary>
        protected virtual int TimeToAsyc { get { return 2 * 1000; } }


        /// <summary>
        /// 表示当前主线程是否还在等待处理结果
        /// </summary>
        bool IsEnd { get; set; }

        /// <summary>
        /// 主程线中止待等的时间
        /// </summary>
        DateTime EndTime { get; set; }

        /// <summary>
        /// 表示当前是否处理异步模式
        /// </summary>
        bool IsAsync { get; set; }


        /// <summary>
        /// 异步操作，不能使用此对像访问数据，否则可能在SP服务响应缓慢时，出现数据库连接用户过多,且不能自动关闭
        /// </summary>
        private new Shotgun.Database.IBaseDataClass2 dBase { get { if (IsAsync || IsEnd) throw new NotSupportedException("已经进入异步状态，不再支持此属性！"); return base.dBase; } }

        protected sealed override SP_RESULT GetSpCmd()
        {
            return StartAsync(this.AsyncGetVerifyCode);
        }

        protected sealed override SP_RESULT GetSpCmdStep2()
        {
            if (InSOne())
            {
                WriteLog("获取验证接口正在运行,将终止当前请求！");
                return new SP_RESULT();
            }
            return StartAsync(this.AsyncSubmitVerifyCode);
        }

        SP_RESULT StartAsync(Func<SP_RESULT> func)
        {
            SP_RESULT result = null;
            var perOrder = (APIRquestModel)OrderInfo.Clone();
            var task = new Task(() =>
            {
                if (IsEnd)
                    Thread.Sleep(10);
                try
                {
                    result = DoAsyncGetSpCmd(perOrder, func);
                }
                catch (Exception e)
                {
                    WriteLog(e.ToString());
                }
                finally
                {
                    lock (this)
                    {
                        if (IsEnd)
                        {
                            IsAsync = false;
                            FlushLog();
                        }
                    }
                }
            });
            task.Start();
            if (IsEnd)
            {
                WriteLog("非用户请求数据");
                return null;
            }
            if (task.Wait(TimeToAsyc))
            {//正常输出结果
                return result;
            }
            lock (this)
            {
                if (task.IsCompleted)
                    return result;
                this.EndTime = DateTime.Now;
                this.IsEnd = true;
                IsAsync = true;
#if TDEBUG
                WriteLog("async start step=" + Step);
#endif
                AddSOneFlag();
            }

            return new SP_RESULT();
        }


        private SP_RESULT DoAsyncGetSpCmd(APIRquestModel oldOrder, Func<SP_RESULT> func)
        {

            SP_RESULT result = null;
            try
            {
                result = func();
            }
            catch (Exception e)
            {
                WriteLog(e.ToString());
                SetError(API_ERROR.INNER_ERROR, e.Message);
            }

            lock (this)
            {
                if (!IsEnd)
                    return result;
            }
            FinalAsync(oldOrder, result);
            return null;
        }

        /// <summary>
        /// 异步数据保存
        /// </summary>
        /// <param name="oldOrder"></param>
        /// <param name="result"></param>
        void FinalAsync(APIRquestModel oldOrder, SP_RESULT result)
        {

            //处理异步结果写入数据库
            var ts = DateTime.Now - this.EndTime;
            if (ts.TotalSeconds < 1)//预留足够时间让前端系统处理数据
            {
                var delay = ts.TotalMilliseconds > 500 ? 500 : 1000;
#if TDEBUG
                WriteLog(string.Format("Delay {0}ms for main thread end", delay));
#endif
                Thread.Sleep(delay);
            }

            var ecode = result == null ? this.GetError() : result.status;

            if (Step == 2)
            {
                if ((int)ecode != 1013 && (int)ecode != 1011)
                    ecode = (API_ERROR)((int)ecode + 1000);
                else
                    ecode = API_ERROR.STEP2_OK;
            }





            var tabName = string.Format("daily_log.tbl_api_order_{0:yyyyMM}", DateTime.Today);

            var sb = new StringBuilder();

            var db = CreateDBase();


            sb.AppendFormat("update {0} set ", tabName);
            if (ecode != API_ERROR.OK && ecode != API_ERROR.STEP2_OK)
                sb.AppendFormat("`status`={0:d},", ecode);

            if (oldOrder.spLinkId != OrderInfo.spLinkId)
                sb.AppendFormat("`sp_linkid`='{0}',", db.SqlEncode(OrderInfo.spLinkId));
            if (oldOrder.apiExdata != OrderInfo.apiExdata)
                sb.AppendFormat("`api_exdata`='{0}',", db.SqlEncode(OrderInfo.apiExdata));
            if (oldOrder.spExField != OrderInfo.spExField)
                sb.AppendFormat("`sp_exField`='{0}',", db.SqlEncode(OrderInfo.spExField));
            var sql = sb.ToString();

            if (sql.IndexOf("=") != -1)
            {
                sb.Length--;
                sb.AppendFormat(" where id={0}", OrderInfo.id);
                sql = sb.ToString();
            }
            else
            {
                sb.Clear();
                sql = null;
            }

            try
            {
#if TDEBUG
                if (sb.Length > 10)
                    WriteLog("Async Execute sql:" + sql);
#endif
                if (!string.IsNullOrEmpty(sql))
                    db.ExecuteNonQuery(sb.ToString());

                if (Step == 1)
                {
                    sb.Clear();
                    sb.AppendFormat("select `cp_verifyCode` from {0} where id={1}", tabName, OrderInfo.id);
                    sql = sb.ToString();
#if TDEBUG
                    WriteLog("Async Execute sql:" + sql);
#endif

                    var vc = db.ExecuteScalar(sql);
                    if (vc != null && !DBNull.Value.Equals(vc) && !string.IsNullOrEmpty((string)vc))
                    {
                        OrderInfo.cpVerifyCode = (string)vc;
                    }
                }

            }
            catch (Exception ex)
            {
                WriteLog(ex.ToString());
            }
            finally
            {
                if (db is IDisposable)
                    ((IDisposable)db).Dispose();
            }

            IsAsync = false;
#if TDEBUG
            WriteLog("async done! step=" + Step);
#endif
            FlushLog();
            RemoveSOneFlag();
            if (Step == 1 && !string.IsNullOrEmpty(OrderInfo.cpVerifyCode))
            {
                WriteLog("验证码已经上行，启动二次提交");
                Step = 2;
                GetSpCmdStep2();
            }
        }


        protected abstract SP_RESULT AsyncGetVerifyCode();

        protected abstract SP_RESULT AsyncSubmitVerifyCode();


        protected internal override void FlushLog()
        {
            if (IsAsync)
                return;
            base.FlushLog();
        }

        protected override string DownloadHTML(string url, string postdata, int timeout, string encode, IDictionary<string, string> Heads)
        {
            return base.DownloadHTML(url, postdata, 60 * 1000, encode, Heads);
        }
    }
}
