using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using FlowLibraryNet.Dao;
using LightDataModel;
using System.Threading;

namespace FlowLibraryNet.Logical
{
    public abstract class FlowCallbackHandler : FlowBaseHandler
    {
        private CpOrderDao _orderDao;
        private IFlowOrderInfo _orderInfo;

        public override void BeginProcess()
        {

            WriteLog(Request.RawUrl);
            if (Request.HttpMethod == "POST")
            {
                var stm = Request.InputStream;
                var bin = new byte[stm.Length];
                stm.Read(bin, 0, bin.Length);
                var txt = System.Text.ASCIIEncoding.UTF8.GetString(bin);
                WriteLog(txt);
                stm.Seek(0, System.IO.SeekOrigin.Begin);
            }

            try
            {
                if (!LoadOrderInfo())
                {
                    WriteError("Order not found!");
                    return;
                }

                UpdateOrderInfo();
                if (OrderInfo != null)
                    _orderDao.Update(OrderInfo);
                SendNotify();

                WriteSuccess();
            }
#if DEBUG 
            catch (Exception ex)
            {
                OrderInfo.StatusE = ChangeOrderStatusEnum.InnerError;
                WriteError("内部错误：" + ex.Message);
                WriteLog("未处理错误：{0}", ex.ToString());
            }
#endif
            finally
            {
                FlushLog();
            }
        }

        private void SendNotify()
        {
            var appset = System.Configuration.ConfigurationManager.AppSettings["NotifyForwardUrl"];
            if (string.IsNullOrEmpty(appset))
                throw new Exception("回调模板未匹配，config -> configuration/appSettings/NotifyForwardUrl");

            var url = string.Format(appset, Server.UrlEncode(OrderInfo.SpOrderId));
            ThreadPool.QueueUserWorkItem(e =>
            {
                string html;
                try
                {
                    html = Shotgun.Library.HttpUtil.DownloadHTML(url, null, 1000, null);
                }
                catch (Exception ex) { html = ex.Message; }
                Shotgun.Library.SimpleLogRecord.WriteLog("NotifyForwardUrl", string.Format("{0} {1}", url, html));
            }, null);
        }

        /// <summary>
        /// 用于填充sp订单信息及最终订单状态
        /// </summary>
        protected abstract void UpdateOrderInfo();
        /// <summary>
        /// 写入的处理结果：成功（回应sp）
        /// </summary>
        protected void WriteSuccess() => WriteResult("ok");

        /// <summary>
        /// 写入的处理结果：失败（回应sp）
        /// </summary>
        /// <param name="v"></param>
        protected void WriteError(string v) => WriteResult(v);

        protected IFlowOrderInfo OrderInfo { get => _orderInfo; }


        private bool LoadOrderInfo()
        {
            _orderDao = new Dao.CpOrderDao(dBase);
            var idStr = GetLinkId();
            if (string.IsNullOrEmpty(idStr))
                return false;

            _orderInfo = _orderDao.GetCustomId(idStr);
            if (_orderInfo == null)
                return false;

            return true;

        }


        protected abstract string GetLinkId();

        /// <summary>
        /// 定入最终的处理结果（回应sp）
        /// </summary>
        /// <param name="msg"></param>
        void WriteResult(string msg)
        {
            WriteLog("Result:{0}", msg);
            Response.Write(msg);
        }

        /// <summary>
        /// 获取回调地址
        /// </summary>
        /// <param name="spTroneId"></param>
        /// <returns></returns>
        public static string GetCallbackUrl(int spTroneId)
        {
            var appset = System.Configuration.ConfigurationManager.AppSettings["ChangeCallbackPrefixUrl"];
            if (string.IsNullOrEmpty(appset))
                throw new Exception("回调模板未匹配，config -> configuration/appSettings/ChangeCallbackPrefixUrl");
            return string.Format(appset, spTroneId);
        }


    }
}
