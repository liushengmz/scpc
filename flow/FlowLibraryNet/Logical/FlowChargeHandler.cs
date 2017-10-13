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
        private tbl_f_basic_priceItem _basePrice;

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
                    OrderInfo.statusE = ChangeOrderStatusEnum.SpUnkowError;
                    WriteLog("充值失败，{0}", ErrorMesage);
                    if (_errcode == ChangeErrorEnum.OK)
                        SetError(ChangeErrorEnum.ChargeFail);
                }
                else
                    OrderInfo.statusE = ChangeOrderStatusEnum.Charging;
            }
#if !DEBUG 
            catch (Exception ex)
            {
                OrderInfo.statusE = ChangeOrderStatusEnum.InnerError;
                SetError("内部错误：" + ex.Message, ChangeErrorEnum.InnerError);
                WriteLog("未处理错误：{0}", ex.ToString());
                UpdateSpResult("N/A", ErrorMesage);
            }
#endif
            finally
            {
                FlushLog();
            }
            if (_orderInfo != null)
                orderDao.Update(_orderInfo);
            Response.Write(string.Format("{0},{1}", _errcode, ErrorMesage));

        }

        protected abstract bool DoCharge();

        private bool LoadOrderInfo()
        {
            orderDao = new Dao.CpOrderDao(dBase);
            var idStr = Request["key"];
#if DEBUG
            if (string.IsNullOrEmpty(idStr))
                idStr = System.Configuration.ConfigurationManager.AppSettings["TestOrderId"];
#endif
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
            OrderInfo.sp_error_msg = message;
            OrderInfo.sp_status = code;
        }

        public LightDataModel.tbl_f_basic_priceItem FlowSizeInfo
        {
            get
            {
                if (_basePrice != null)
                    return _basePrice;
                _basePrice = tbl_f_basic_priceItem.GetRowById(dBase, OrderInfo.base_price_id);
                if (_basePrice == null)
                    throw new ArgumentException("订单关联的基础价格信息丢失！");
                return _basePrice;
            }
        }


    }
}
