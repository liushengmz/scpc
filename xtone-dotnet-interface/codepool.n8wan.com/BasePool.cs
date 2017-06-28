using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;

namespace n8wan.codepool
{
    class BasePool : Shotgun.Model.Logical.Logical
    {
        public LightDataModel.tbl_troneItem trone { get; set; }

        public LightDataModel.tbl_trone_orderItem troneOrder { get; set; }

        public LightDataModel.tbl_sp_troneItem spTrone { get; private set; }
        public LightDataModel.tbl_api_orderItem orderInfo { get; private set; }

        int province;

        public bool Init(LightDataModel.tbl_moItem mo)
        {
            if (orderInfo != null)
                return SetSuccess();
            if (trone == null)
                SetErrorMesage("通道信息不能为空");
            orderInfo = new LightDataModel.tbl_api_orderItem();
            spTrone = LightDataModel.tbl_sp_troneItem.GetRowById(dBase, trone.sp_trone_id);
            orderInfo.api_id = spTrone.trone_api_id;
            orderInfo.trone_id = trone.id;
            orderInfo.port = mo.ori_trone;
            orderInfo.msg = mo.ori_order;
            orderInfo.sp_linkid = mo.linkid;
            orderInfo.mobile = mo.mobile;
            orderInfo.FirstDate = DateTime.Now;
            orderInfo.api_exdata = mo.cp_param;
            orderInfo.ip = mo.ip;
            orderInfo.clientip = "127.0.0.1";
            orderInfo.city = mo.city_id;
            province = mo.province_id;

            if (troneOrder != null)
                orderInfo.trone_order_id = troneOrder.id;

            return SetSuccess();
        }

        /// <summary>
        /// 日月，省份限检查
        /// </summary>
        /// <returns></returns>
        public bool CheckLimit()
        {
            var spTrone = LightDataModel.tbl_sp_troneItem.GetRowById(dBase, trone.sp_trone_id);
            if (string.IsNullOrEmpty(spTrone.provinces))
            {
                orderInfo.status = 1009;
                return SetErrorMesage("省份信息未配置");
            }
            var provinces = spTrone.provinces.Split(new char[] { ',', ' ' }, StringSplitOptions.RemoveEmptyEntries);
            var city = LightDataModel.tbl_cityItem.GetRowById(dBase, orderInfo.city);

            if (!provinces.Contains(province.ToString()))
            {
                orderInfo.status = 1009;
                return SetErrorMesage("屏蔽省份");
            }


            //var troneIds = LightDataModel.tbl_troneItem.GetTroneIdsBySptroneId(dBase, spTrone.id);
            //var ulm = NoSqlModel.UserLimitModel.QueryUserLimit(orderInfo.imsi, troneIds.Select(e => e.id));
            //if (ulm == null)
            //{
            //    ulm = NoSqlModel.UserLimitModel.QueryUserLimitFromDBase(dBase, orderInfo.imsi, troneIds.Select(e => e.id));
            //}



            return SetSuccess();
        }



        /// <summary>
        /// 处理用户请求
        /// </summary>
        /// <returns></returns>
        public bool ProcesUserRequest()
        {

            if (!CheckLimit())
                return false;

            var url = string.Format("http://localhost:9191/api/kk{0}.ashx", orderInfo.api_id);
            var api = new Model.APIModel();

            orderInfo.SaveToDatabase(dBase);
            api.port = orderInfo.port;
            api.msg = orderInfo.msg;
            api.spLinkId = orderInfo.sp_linkid;
            api.mobile = orderInfo.mobile;
            api.id = orderInfo.id;
            api.tbl_sp_trone_api_id = orderInfo.api_id;
            api.tbl_trone_order_id = orderInfo.trone_order_id;
            api.apiExdata = orderInfo.api_exdata;
            api.spExField = orderInfo.sp_exField;
            api.troneId = orderInfo.trone_id;
            api.tbl_trone_order_id = orderInfo.trone_order_id;


            string json = null;
            using (var stm = new MemoryStream())
            {
                var dc = new DataContractJsonSerializer(api.GetType());
                dc.WriteObject(stm, api);
                json = ASCIIEncoding.UTF8.GetString(stm.ToArray());
            }
            try
            {
                var html = n8wan.Public.Library.DownloadHTML(url, json, 2000, null);
                api = Shotgun.Library.Static.JsonParser<Model.ApiResultModel>(html).Request;
                orderInfo.status = api.status;
                Shotgun.Library.SimpleLogRecord.WriteLog("telcom_mo", html);
                return SetSuccess();
            }
            catch (System.Net.WebException ex)
            {
                if (ex.Status == System.Net.WebExceptionStatus.Timeout)
                    orderInfo.status = 1023;
                else
                    orderInfo.status = 1010;
                return SetErrorMesage(string.Format("api接出口出错：url:{0},code:{1}", url, ex.Status));

            }
            finally
            {
                //Shotgun.Library.SimpleLogRecord.WriteLog("telcom_mo", string.Format("updated api.status={0},orderInfo.status={1}", api.status, orderInfo.status));
                orderInfo.SaveToDatabase(dBase);
            }


        }
    }
}
