using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using LightDataModel;

namespace FlowLibraryNet.Logical
{
    public abstract class FlowChargeHandler : FlowBaseHandler
    {
        Dao.CpOrderDao orderDao;
        private ChangeErrorEnum _errcode;
        private tbl_f_cp_order_listItem _orderInfo;

        public sealed override void BeginProcess()
        {
            WriteLog(Request.RawUrl);
            try
            {
                if (!LoadOrderInfo())
                {
                    WriteLog("订单加裁失败，{0}", ErrorMesage);
                }
                else if (!DoCharge())
                {
                    WriteLog("充值失败，{0}", ErrorMesage);
                }

            }
#if !DEBUG
            catch (Exception ex)
            {
                WriteLog("未处理错误：{0}", ex.ToString());
            }
#endif
            finally
            {
                FlushLog();
            }
            Response.Write(ErrorMesage);

        }

        protected abstract bool DoCharge();

        private bool LoadOrderInfo()
        {
            orderDao = new Dao.CpOrderDao(dBase);
            var idStr = Request["orderid"];
            if (string.IsNullOrEmpty(idStr))
                return SetError(ChangeErrorEnum.OrderNotFound);

            _orderInfo = orderDao.GetCustomId(idStr);
            if (_orderInfo == null)
                return SetError(ChangeErrorEnum.OrderNotFound);

            return SetSuccess();

        }

        protected bool SetError(ChangeErrorEnum status)
        {
            return SetError(status.ToString(), status);
        }

        protected bool SetError(string errMsg, ChangeErrorEnum status = ChangeErrorEnum.UnkonwError)
        {
            base.ErrorMesage = errMsg;
            this._errcode = status;
            return status == ChangeErrorEnum.OK;
        }

        protected bool SetSuccess()
        {
            return SetError(ChangeErrorEnum.OK);
        }

        public tbl_f_cp_order_listItem OrderInfo { get { return _orderInfo; } }

        public void UpdateSpResult(string code, string message)
        {
            if (OrderInfo == null)
                return;
            OrderInfo.sp_err_msg = message;
            OrderInfo.sp_status = code;
        }
    }
}
