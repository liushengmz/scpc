//#define TDEBUG
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using Shotgun.Library;
using n8wan.Public.Model;

namespace n8wan.Public.Logical
{
    public class CPPusher : Shotgun.Model.Logical.Logical, ILoger
    {

        /// <summary>
        /// 写日志时文件锁，防止并发处理出错
        /// </summary>
        static object logFileLocker = new object();
        /// <summary>
        /// 订单ID
        /// </summary>
        private string _linkID;

        /// <summary>
        /// 最后一次推送的连接内容
        /// </summary>
        private string _url;
        /// <summary>
        /// 采用POST同步的数据
        /// </summary>
        private string _postdata;
        private LightDataModel.tbl_trone_orderItem _config;
        private LightDataModel.tbl_cp_push_urlItem _cp_push_url;
        private LightDataModel.tbl_troneItem _trone;

        static Random rnd;
        object sync = new object();

        public E_CP_SYNC_MODE PushFlag { get; set; }

        /// <summary>
        /// 根据TroneId，CPID 加载CP同步API配置
        /// </summary>
        /// <returns></returns>
        public virtual bool LoadCPAPI()
        {
            if (Trone == null)
                return false;
            var l = LightDataModel.tbl_trone_orderItem.GetQueries(dBase);
            l.Filter.AndFilters.Add(LightDataModel.tbl_trone_orderItem.Fields.trone_id, Trone.id);
            if (CP_Id != -1)
                l.Filter.AndFilters.Add(LightDataModel.tbl_trone_orderItem.Fields.cp_id, this.CP_Id);
            l.Filter.AndFilters.Add(LightDataModel.tbl_trone_orderItem.Fields.disable, false);
            var m = l.GetRowByFilters();
            if (m == null)
                return SetErrorMesage("CP_API Not Found!");
            SetConfig(m);
            return SetSuccess();
        }

        /// <summary>
        /// 设置渠道通道信息,同步信息,SP通道信息
        /// </summary>
        /// <param name="m"></param>
        protected void SetConfig(LightDataModel.tbl_trone_orderItem m)
        {
            _config = m;
            _cp_push_url = LightDataModel.tbl_cp_push_urlItem.GetRowByIdWithCache(dBase, m.push_url_id);
            if (_trone == null || _trone.id != m.trone_id)
                _trone = LightDataModel.tbl_troneItem.GetRowById(dBase, m.trone_id);
        }

        public Logical.ICPPushModel PushObject { get; set; }

        public string LogFile { get; set; }

        /// <summary>
        /// 匹配的SP指令ID
        /// </summary>
        public LightDataModel.tbl_troneItem Trone { get { return _trone; } set { _trone = value; } }

        public virtual bool DoPush()
        {
            this._linkID = PushObject.GetValue(Logical.EPushField.LinkID);
            this._url = null;

            if (PushObject.syn_flag == 1 && PushObject.cp_id != 34)
            {//已经同步的数据
                if (_cp_push_url.is_realtime)
                    SendQuery();
                return SetSuccess();
            }

            //更新日月限数据
            TroneDayLimit.UpdateDayLimit(dBase, Trone.sp_trone_id, _config.cp_id, Trone.price);

            DateTime today = DateTime.Today;


            IHold_DataItem holdCfg;
            if (_config.hold_is_Custom)
                holdCfg = _config;
            else
                holdCfg = _cp_push_url;

            if (holdCfg.lastDate != DateTime.Today)
            {//重置昨日数据，为今日新数据
                holdCfg.lastDate = today;
                holdCfg.amount = 0;
                holdCfg.push_count = 0;
            }


            if (IsCycHidden())
            {//扣量处理
                try
                {
                    PushObject.SetHidden(dBase, _config);
                    dBase.SaveData(_cp_push_url);
                    dBase.SaveData(_config);
                }
                catch (System.Data.Common.DbException ex)
                {
                    WriteLog(-1, string.Format("扣量数据保存失败{0}", ex.Message));
                    return SetErrorMesage(ex.Message);
                }
                WriteLog(-2, "扣量");
                return SetSuccess();
            }
            holdCfg.push_count++;
            holdCfg.amount += _trone.price;

            try
            {
                var cpmr = PushObject.SetPushed(dBase, _config);
                cpmr.syn_status = _cp_push_url.is_realtime ? 1 : 0;

                dBase.SaveData(cpmr);


                if (holdCfg.hold_start > holdCfg.push_count //未达到起扣数
                    || (holdCfg.hold_amount != 0 && holdCfg.hold_amount > holdCfg.amount)) //未达到最大同步金额
                {
                    dBase.SaveData(_cp_push_url);
                    dBase.SaveData(_config);
                }
                var daily = cpmr.CopyToDailyCpMr(null);
                try
                {
                    dBase.SaveData(daily);
                }
                catch { }
            }
            catch (System.Data.Common.DbException ex)
            {
                WriteLog(-1, ex.Message);
                return SetErrorMesage(ex.Message);
            }



            if (_cp_push_url.is_realtime)
                SendQuery();
            return SetSuccess();
        }

        /// <summary>
        /// 此次操作是否标记为扣量信息
        /// </summary>
        /// <returns></returns>
        protected bool IsCycHidden()
        {
            if (PushFlag == E_CP_SYNC_MODE.ForceHide)
                return true;
            else if (PushFlag == E_CP_SYNC_MODE.ForcePush)
                return false;

            if (_cp_push_url.cp_id == 34)
                return true;//未知CP的，直接隐藏
            if (!_cp_push_url.is_realtime)
                return false;//非实时同步，不进行扣量操作 
            if (IsNoHidden)
                return false;
            IHold_DataItem holdCfg = null;
            if (_config.hold_is_Custom)
            {
                var cpRate = LightDataModel.tbl_cp_trone_rateItem.QueryBySpTroneId(dBase, PushObject.sp_trone_id, _config.cp_id);
                holdCfg = LightDataModel.ProvinceHoldConfg.LoadProvinceData(_config, cpRate, PushObject.province_id);
                // holdCfg = _config;
            }
            else
                holdCfg = _cp_push_url;

            if (holdCfg.push_count < holdCfg.hold_start)
                return false;

            if (IsWhite())
                return false;

            if (holdCfg.hold_amount > 0)
            {
                if (holdCfg.amount >= holdCfg.hold_amount)
                    return DateTime.Now.Millisecond > 10;//扣99%
                else if ((holdCfg.amount / holdCfg.hold_amount) > 0.98m)//离满额只有2%时
                    return DateTime.Now.Millisecond > 500;//扣50%
            }


            if (holdCfg.hold_percent <= 0)
                return false;//不扣量
            if (holdCfg.hold_percent >= 100)
                return true;//全扣量

            var t = math2.GCD(holdCfg.hold_percent, 100);

            var cycHold = holdCfg.hold_percent / t;//扣量条数
            var cycCount = 100 / t;//最小扣量周期


            //var max = _config.CycCount - _config.LastCycCount; //未处理的周期量
            var max = cycCount - holdCfg.hold_CycCount;
            //var p = _config.CycHidden - _config.LastCycHidden;//当前周期未扣除的数量
            var p = cycHold - holdCfg.hold_CycProc;//当前周期未扣除的数量


            if (max <= 0)
            {//跑完周期，需要重置
                holdCfg.hold_CycCount = 0;
                holdCfg.hold_CycProc = 0;
                max = cycCount;
                p = cycHold;
            }
            holdCfg.hold_CycCount++;
            if (p <= 0)
                return false;//已经扣完了
            var r = (DateTime.Now.Millisecond % 100) * max;
            var pre = p * 100;
            var isHidden = r <= pre;
            if (isHidden)
                holdCfg.hold_CycProc++;
            return isHidden;
        }
        /// <summary>
        /// 是否采用同步（异步/同步）方法推送
        /// </summary>
        public bool IsSyncPush { get; set; }

        /// <summary>
        /// 是否要求不扣量，一般在补数据时使用
        /// </summary>
        /// <returns></returns>
        public bool IsNoHidden { get; set; }

        /// <summary>
        /// 白名单检查
        /// </summary>
        /// <returns></returns>
        private bool IsWhite()
        {
            var mobile = PushObject.GetValue(EPushField.Mobile);
            if (string.IsNullOrEmpty(mobile))
                return false;
            var q = LightDataModel.tbl_mobile_white_listItem.GetQueries(dBase);
            q.Filter.AndFilters.Add(LightDataModel.tbl_mobile_white_listItem.Fields.mobile, mobile);

            return q.ExecuteScalar(LightDataModel.tbl_mobile_white_listItem.identifyField) != null;

        }

        public string API_PushUrl { get { return _cp_push_url.url; } }

        protected virtual void SendQuery()
        {

            if (string.IsNullOrEmpty(API_PushUrl))
            {
                WriteLog(-1, "No Push URL");
                return;
            }
            var pm = new CPDataPushModel();
            pm.Mobile = PushObject.GetValue(Logical.EPushField.Mobile);
            pm.Servicecode = PushObject.GetValue(Logical.EPushField.ServiceCode);
            pm.Linkid = _linkID;
            pm.Msg = PushObject.GetValue(Logical.EPushField.Msg);
            pm.Port = PushObject.GetValue(Logical.EPushField.port);
            pm.Price = decimal.ToInt32(_trone.price * 100);
            pm.Cpparam = PushObject.GetValue(Logical.EPushField.cpParam);
#pragma warning disable
            pm.ProvinceId = int.Parse(PushObject.GetValue(EPushField.province));
#pragma warning restore
            pm.PayCode = _config.id;
            pm.VirtualMobile = GetVirtualMobile();
            pm.Url = API_PushUrl;
            asyncSendData(pm.ToString(), null);
            //var ptrs = new Dictionary<string, string>();
            //ptrs.Add("mobile", PushObject.GetValue(Logical.EPushField.Mobile));
            //ptrs.Add("servicecode", PushObject.GetValue(Logical.EPushField.ServiceCode));
            //ptrs.Add("linkid", _linkID);
            //ptrs.Add("msg", PushObject.GetValue(Logical.EPushField.Msg));
            ////ptrs.Add("status", PushObject.GetValue(Logical.EPushField.Status));
            //ptrs.Add("port", PushObject.GetValue(Logical.EPushField.port));

            //ptrs.Add("price", (_trone.price * 100).ToString("0"));
            //ptrs.Add("cpparam", PushObject.GetValue(Logical.EPushField.cpParam));
            //ptrs.Add("provinceId", PushObject.GetValue(EPushField.province));
            //ptrs.Add("paycode", _config.id.ToString("100000"));

            //ptrs.Add("virtualMobile", GetVirtualMobile());

            //string qs = UrlEncode(ptrs);
            //string url;
            //if (API_PushUrl.Contains('?'))
            //    url = API_PushUrl + "&" + qs;
            //else
            //    url = API_PushUrl + "?" + qs;
            //asyncSendData(url, null);
        }

        protected string GetVirtualMobile()
        {
            return CityToPhone.GetVirtualPhone(PushObject.GetValue(EPushField.Mobile), PushObject.province_id, PushObject.city_id);
        }

        protected void asyncSendData(string url, string postData)
        {
            this._url = url;
            this._postdata = postData;

            if (IsSyncPush)
                SendData(null);
            else
                System.Threading.ThreadPool.QueueUserWorkItem(SendData);

        }

        /// <summary>
        /// 真实的同步动作
        /// </summary>
        /// <param name="s"></param>
        private void SendData(object s)
        {

            if (this._url == null || this._url.StartsWith("#"))
            {
                WriteLog(0, "虚似推送");
                return;
            }

            if (File.Exists(@"E:\localFlag.txt"))
            {//本地测试不进行推送
                WriteLog(0, "未推送 请删除E:\\localFlag.txt");
                return;
            }

            System.Net.HttpWebRequest web = null;

            try
            {
                web = (System.Net.HttpWebRequest)System.Net.WebRequest.Create(_url);
            }
            catch (Exception ex)
            {
                WriteLog(-1, "Push URL Error:" + ex.Message);
                return;
            }

            System.Net.HttpWebResponse rsp = null;
            web.Timeout = 1000;
            web.AllowAutoRedirect = false;
            web.AutomaticDecompression = System.Net.DecompressionMethods.GZip;
            web.ServicePoint.Expect100Continue = false;


            var stwc = new System.Diagnostics.Stopwatch();
            stwc.Start();
            string msg = null;

            if (!String.IsNullOrEmpty(_postdata))
            {//采用用POST提交时
                web.Method = System.Net.WebRequestMethods.Http.Post;
                byte[] bin = ASCIIEncoding.UTF8.GetBytes(_postdata);
                web.ContentType = "Content-Type: application/x-www-form-urlencoded; charset=UTF-8";
                web.ContentLength = bin.Length;
                Stream stm = null;
                try
                {
                    stm = web.GetRequestStream();
                    stm.Write(bin, 0, bin.Length);
                    stm.Flush();
                }
                catch (System.Net.WebException ex)
                {
                    rsp = (System.Net.HttpWebResponse)ex.Response;
                    msg = ex.Message;
                }
                finally
                {
                    if (stm != null)
                        stm.Dispose();
                }
            }
            if (msg == null)
            {//写入数据无错误时
                try
                {
                    rsp = (System.Net.HttpWebResponse)web.GetResponse();
                }
                catch (System.Net.WebException ex)
                {
                    rsp = (System.Net.HttpWebResponse)ex.Response;
                    msg = ex.Message;
                }
            }

            if (rsp == null)
            {
                WriteLog(0, msg, stwc);
                return;
            }
            var code = rsp.StatusCode;
            try
            {
                using (var stm = rsp.GetResponseStream())
                {
                    using (var rd = new System.IO.StreamReader(stm))
                        msg = rd.ReadLine();
                }
            }
            catch (Exception ex)
            {
                msg = ex.Message;
            }
            if (!string.IsNullOrEmpty(msg) && msg.Length > 512)
                msg = msg.Substring(0, 510) + "...";

            WriteLog((int)code, msg, stwc);
        }

        protected void WriteLog(int p, string msg)
        {
            if (string.IsNullOrEmpty(LogFile))
                return;
            var fi = new FileInfo(LogFile);
            if (!fi.Directory.Exists)
                fi.Directory.Create();
            lock (logFileLocker)
            {
                StreamWriter stm = null;
                try
                {
                    stm = new StreamWriter(LogFile, true);
                    stm.WriteLine("{0:HH:mm:ss} {1} {2} {3} {4}", DateTime.Now, this._linkID, this._url, p, msg);
                }
                catch (Exception) { }
                finally
                {
                    if (stm != null)
                        stm.Dispose();
                }
            }
        }

        protected void WriteLog(int p, string msg, System.Diagnostics.Stopwatch stwc)
        {
            WriteLog(p, string.Format("{0:0}ms - {1}", stwc.Elapsed.TotalMilliseconds, msg));
        }


        //public int TroneId { get; set; }

        /// <summary>
        /// CP用户Id，-1表示，不指定,仅配对API_Config_Id
        /// </summary>
        public int CP_Id { get; set; }

        /// <summary>
        /// 取1W以内的随机数
        /// </summary>
        /// <returns></returns>
        int GetRndInt()
        {
            if (rnd == null)
                rnd = new Random();
            return rnd.Next(10000);
        }


        #region 日志记录
        public void WriteTrackLog(string msg)
        {
            if (TrackLog == null)
                return;
            TrackLog.AppendFormat("{0:hh:MM:ss}{1}", DateTime.Now, msg);
            TrackLog.AppendLine();
        }

        public StringBuilder TrackLog { get; set; }
        #endregion


        void ILoger.WriteLog(int p, string msg)
        {
            WriteLog(p, msg);
        }

        void ILoger.WriteLog(string msg)
        {
            if (string.IsNullOrEmpty(LogFile))
                return;
            var fi = new FileInfo(LogFile);
            if (!fi.Directory.Exists)
                fi.Directory.Create();
            lock (logFileLocker)
            {
                StreamWriter stm = null;
                try
                {
                    stm = new StreamWriter(LogFile, true);
                    stm.WriteLine("{0:HH:mm:ss} {1}", DateTime.Now, msg);
                }
                catch (Exception) { }
                finally
                {
                    if (stm != null)
                        stm.Dispose();
                }
            }
        }
    }
}
