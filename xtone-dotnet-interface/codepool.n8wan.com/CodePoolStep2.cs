using n8wan.codepool.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.codepool
{
    public class CodePoolStep2 : BaseCodePool
    {
        static LinkedList<int> fastLocker = new LinkedList<int>();
        private int _lockedId;

        public override bool Init(System.Web.HttpRequest request)
        {
            var vCode = request["vcode"];
            var tab = request["ordernum"];
            if (tab == null || tab.Length < 7)
                return SetErrorMesage(ErrorCode.Parameter_Miss, "ordernum不能为空");
            if (string.IsNullOrEmpty(vCode))
                return SetErrorMesage(ErrorCode.Parameter_Miss, "vcode不能为空");
            int y, m, d;
            if (!int.TryParse(tab.Substring(0, 4), out y))
                return SetErrorMesage(ErrorCode.Invalid_OrderNaum, "ordernum 无效");
            if (!int.TryParse(tab.Substring(4, 2), out m))
                return SetErrorMesage(ErrorCode.Invalid_OrderNaum, "ordernum 无效");
            if (!int.TryParse(tab.Substring(6), out d))
                return SetErrorMesage(ErrorCode.Invalid_OrderNaum, "ordernum 无效");
            if (m > 12 || m < 1 || y > 2050 || y < 2000)
                return SetErrorMesage(ErrorCode.Invalid_OrderNaum, "ordernum 无效");

            if (!CheckAndInsert(d))
                return SetErrorMesage(ErrorCode.Invalid_OrderNaum, "重复提交验证码");


            var q = LightDataModel.tbl_api_orderItem.GetQueries(dBase);
            q.TableDate = new DateTime(y, m, 1);
            q.Filter.AndFilters.Add(LightDataModel.tbl_api_orderItem.Fields.PrimaryKey, d);
            this._orderInfo = q.GetRowByFilters();
            if (_orderInfo == null)
                return SetErrorMesage(ErrorCode.Invalid_OrderNaum, "ordernum 无效");
            if (_orderInfo.status >= 2000 || !string.IsNullOrEmpty(_orderInfo.cp_verifyCode))
            {
                _orderInfo = null;//防止更新到数据库
                return SetErrorMesage(ErrorCode.Invalid_OrderNaum, "重复提交验证码");
            }
            _orderInfo.SecondDate = DateTime.Now;
            _orderInfo.cp_verifyCode = vCode;
            return SetSuccess();

        }

        public override ErrorCode ECode
        {
            get
            {
                var c = base.ECode;
                if (c == ErrorCode.OK || c == ErrorCode.STEP2_OK)
                    return ErrorCode.STEP2_OK;
                if (c == ErrorCode.Invalid_OrderNaum)
                    return c;
                if ((int)c >= 2000)
                    return c;
                return c + 1000;
            }
        }

        public override string GetApiToSpUrl()
        {
            var url = base.GetApiToSpUrl();
            if (string.IsNullOrEmpty(url))
                return null;
            return url + (url.Contains('?') ? "&" : "?") + "step=2";
        }

        /// <summary>
        /// 检查或添加
        /// </summary>
        /// <param name="id"></param>
        /// <returns>true,添加成功</returns>
        bool CheckAndInsert(int id)
        {
            lock (fastLocker)
            {
                if (fastLocker.Contains(id))
                    return false;
                fastLocker.AddLast(id);
                this._lockedId = id;
                return true;
            }
        }

        public void RemoveLocker()
        {
            if (_lockedId == 0)
                return;
            lock (fastLocker)
            {
                fastLocker.Remove(_lockedId);
            }
        }

        public override void SaveOrder()
        {
            if (this.ECode == ErrorCode.Invalid_OrderNaum)
                return;
            base.SaveOrder();
        }
    }
}
