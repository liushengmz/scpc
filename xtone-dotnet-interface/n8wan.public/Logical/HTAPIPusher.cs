using LightDataModel;
using Shotgun.Model.Filter;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace n8wan.Public.Logical
{
    public class HTAPIPusher : CPPusher
    {
        /// <summary>
        /// 主要用于API订单匹配验证
        /// </summary>
        tbl_sp_trone_apiItem _apiMatchAPI;
        private tbl_api_orderItem _apiOrder;

        /// <summary>
        /// 加载浩天API回调匹配信息
        /// </summary>
        /// <returns></returns>
        public override bool LoadCPAPI()
        {
            if (Trone == null)
                return false;
            tbl_sp_trone_apiItem m = tbl_sp_trone_apiItem.GetRowByTroneId(dBase, Trone.id);
            if (m == null)
            {
                WriteTrackLog(string.Format("troneId:{0} 非API 通道", Trone.id));
                return false;
            }
            _apiMatchAPI = m;
            return true;
        }


        public override bool DoPush()
        {

            if (_apiMatchAPI == null)
                return false;
            bool isNew;
            if (PushObject.cp_id > 0 && PushObject.cp_id != 34)
            {//已经关联的订单
                var apiID = PushObject.GetValue(EPushField.ApiOrderId);
                _apiOrder = LoadApiOrder(apiID);
                WriteTrackLog(string.Format("已经关联订单, api id={0}", apiID));
                isNew = false;
            }
            else
            {//未匹配的新订单
                _apiOrder = LoadApiOrder();
                isNew = true;
            }

            if (_apiOrder == null)
                return false;

            var apiStatus = _apiOrder.status % 10000;
            if (apiStatus != 1011 && apiStatus != 1013 && apiStatus != 2023)
            {
                WriteTrackLog("订单状态错误：" + apiStatus.ToString());
                return false;//api 上量是状态错误，如有数据回传，有可能同步状态有问题
            }

            //var tOrder = tbl_trone_orderItem.GetQueries(dBase);
            //tOrder.Filter.AndFilters.Add(tbl_trone_orderItem.Fields.id, _apiOrder.trone_order_id);
            //tOrder.Filter.AndFilters.Add(tbl_trone_orderItem.Fields.disable, false);
            //var m = tOrder.GetRowByFilters();
            var m = tbl_trone_orderItem.GetRowByIdWithCache(dBase, _apiOrder.trone_order_id);
            if (m == null || m.disable)
                return false;
            this.CP_Id = m.cp_id;
            SetConfig(m);//找到对应的渠道上量(相当于执行 base.LoadCPAPI())
            tbl_mrItem mr = null;
            if (PushObject is tbl_mrItem)
            {
                mr = ((tbl_mrItem)PushObject);
                mr.api_order_id = _apiOrder.id;
                mr.user_md10 = System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(string.Format("{0}_{1}_{2}", _apiOrder.imsi, _apiOrder.imei, _apiOrder.mobile), "MD5");
                if (string.IsNullOrEmpty(mr.mobile) && !string.IsNullOrEmpty(_apiOrder.mobile))
                    mr.mobile = _apiOrder.mobile;
                if (string.IsNullOrEmpty(mr.imsi) && !string.IsNullOrEmpty(_apiOrder.imsi))
                    mr.imsi = _apiOrder.imsi;
                if (mr.province_id == 32 && (!string.IsNullOrEmpty(mr.mobile) || !string.IsNullOrEmpty(mr.imsi)))
                {
                    var city = Library.GetCityInfo(dBase, mr.mobile, mr.imsi);
                    mr.city_id = city.id;
                    mr.province_id = city.province_id;
                }
            }


            var ret = base.DoPush();
            if (!ret)
                return false;

            if (isNew)
                UpdateTroneLimit();

            _apiOrder.IgnoreEquals = true;

            var aStatus = apiStatus + (PushObject.syn_flag == 0 ? 10000 : 20000);

            //扣量和非扣量标识
            _apiOrder.status = aStatus;// apiStatus + (PushObject.syn_flag == 0 ? 10000 : 20000);
            try
            {
                _apiOrder.SaveToDatabase(dBase);
            }
            catch (Exception ex)
            {
                Shotgun.Library.SimpleLogRecord.WriteLog("api_push",
                    string.Format("linkid{0} 更新api.status 出错：{1}", PushObject.GetValue(EPushField.LinkID), ex.ToString()));
            }
            finally
            {

            }
            return true;
        }

        /// <summary>
        /// 用于更新api通道的计费信息
        /// </summary>
        private void UpdateTroneLimit()
        {
            var targetUrl = System.Configuration.ConfigurationManager.AppSettings["apiTroneLimit"];
            if (string.IsNullOrEmpty(targetUrl))
                return;

            var mr = this.PushObject as tbl_mrItem;
            if (mr == null || mr.sp_trone_id == 0)
                return;

            tbl_sp_troneItem spTrone = tbl_sp_troneItem.GetRowById(dBase, mr.sp_trone_id);
            if (spTrone == null)
                return;

            var customId = spTrone.GetCustomId(_apiOrder.mobile, _apiOrder.imsi);

            var sql = string.Format("insert daily_log.tbl_custom_fee_count(custom_id, trone_id, fee_date, count,city_id) "
                + "values('{0}',{1},'{2:yyyy-MM-dd}',1,{3}) on duplicate key update count=count+1;",
                        dBase.SqlEncode(customId), this._apiOrder.trone_id, this._apiOrder.FirstDate, this._apiOrder.city);
            try
            {
                dBase.ExecuteNonQuery(sql);
            }
            catch (System.Data.DataException)
            {
            }
            var data = string.Format("troneId={0}&mrDate={1:yyyy-MM-dd}&city_id={2}&customId={3}&sptroneId={4}",
                    _apiOrder.trone_id, _apiOrder.FirstDate, _apiOrder.city, customId, spTrone.id);
            Shotgun.Library.AsyncRemoteRequest.RequestOnly(targetUrl, ASCIIEncoding.Default.GetBytes(data));
        }

        /// <summary>
        /// 根据ID，加载指令的订单
        /// </summary>
        /// <param name="id">为空或不存在时，返回NULL</param>
        /// <returns></returns>
        private tbl_api_orderItem LoadApiOrder(string id)
        {
            if (string.IsNullOrEmpty(id))
                return null;
            tbl_mrItem mr = (tbl_mrItem)PushObject;
            var l = tbl_api_orderItem.GetQueries(dBase);
            l.TableDate = mr.mr_date;
            l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.PrimaryKey, id);
            return l.GetRowByFilters();

        }

        private tbl_api_orderItem LoadApiOrder()
        {
            var l = tbl_api_orderItem.GetQueries(dBase);
            //l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.trone_id, TroneId);
            string ptr;
            switch (_apiMatchAPI.match_field_E)
            {//订单匹配条件生成
                case tbl_sp_trone_apiItem.EMathcField.Cpprams:
                    ptr = this.PushObject.GetValue(EPushField.cpParam);
                    if (string.IsNullOrEmpty(ptr))
                        return null;//同步配置错，SP并没有回传透传
                    l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.api_exdata, this.PushObject.GetValue(EPushField.cpParam));
                    break;
                case tbl_sp_trone_apiItem.EMathcField.LinkId:
                    l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.sp_linkid, this.PushObject.GetValue(EPushField.LinkID));
                    break;
                case tbl_sp_trone_apiItem.EMathcField.Msg:
                    l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.msg, this.PushObject.GetValue(EPushField.Msg));
                    l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.port, this.PushObject.GetValue(EPushField.port));
                    break;
                case tbl_sp_trone_apiItem.EMathcField.Msg_Not_Equal://同步指令与上行指令不一至时，使用“port,msg”拼接用逗号分隔，并在sp透传查找
                    l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.api_exdata,
                        string.Format("{0},{1}", PushObject.GetValue(EPushField.port), this.PushObject.GetValue(EPushField.Msg)));
                    break;
            }
            l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.api_id, _apiMatchAPI.id);
            l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.trone_id, Trone.id);
            //l.Filter.AndFilters.Add(tbl_api_orderItem.Fields.status, new int[] { 1011, 1013, 2023 });//一次成，2次成功，二次超时


            var t = l.GetRowByFilters();
            if (t != null)
                return t;
            var sql = l.LastSqlExecute;
            if (string.IsNullOrEmpty(sql))
                WriteTrackLog("api order not found,sql:null");
            else
                WriteTrackLog("api order not found,sql:" + sql);
            return null;
        }

        protected override void SendQuery()
        {
            if (string.IsNullOrEmpty(API_PushUrl))
            {
                WriteLog(-1, "No Push URL");
                return;
            }

            var ptr = new n8wan.Public.Model.CPDataPushModel();
            ptr.Mobile = PushObject.GetValue(Logical.EPushField.Mobile);
            ptr.Servicecode = PushObject.GetValue(Logical.EPushField.ServiceCode);
            ptr.Linkid = PushObject.GetValue(EPushField.LinkID);
            ptr.Msg = PushObject.GetValue(EPushField.Msg);
            ptr.Port = PushObject.GetValue(EPushField.port);
            ptr.Price = decimal.ToInt32(Trone.price * 100);
            ptr.Cpparam = _apiOrder.ExtrData;
            //ptr.ProvinceId = int.Parse(PushObject.GetValue(EPushField.province));
            ptr.PayCode = _apiOrder.trone_order_id;
            ptr.PoolId = _apiOrder.cp_pool_id;
            ptr.OrderNum = string.Format("{0:yyyyMM}{1}", _apiOrder.FirstDate, _apiOrder.id);
            ptr.VirtualMobile = base.GetVirtualMobile();
            ptr.Url = API_PushUrl;
            asyncSendData(ptr.ToString(), null);

            //var ptrs = new Dictionary<string, string>();
            //ptrs.Add("mobile", PushObject.GetValue(Logical.EPushField.Mobile));
            //ptrs.Add("servicecode", PushObject.GetValue(Logical.EPushField.ServiceCode));
            //ptrs.Add("linkid", PushObject.GetValue(Logical.EPushField.LinkID));
            //ptrs.Add("msg", PushObject.GetValue(Logical.EPushField.Msg));
            ////ptrs.Add("status", PushObject.GetValue(Logical.EPushField.Status));
            //ptrs.Add("port", PushObject.GetValue(Logical.EPushField.port));

            //ptrs.Add("price", (Trone.price * 100).ToString("0"));
            //ptrs.Add("cpparam", _apiOrder.ExtrData);
            //ptrs.Add("provinceId", PushObject.GetValue(EPushField.province));
            //if (_apiOrder.cp_pool_id == 0)
            //    ptrs.Add("paycode", _apiOrder.trone_order_id.ToString("100000"));
            //else
            //{//代码池同步，强制转换同步的paycode,msg,port
            //    ptrs.Add("paycode", "P" + _apiOrder.cp_pool_id.ToString("00000"));
            //    ptrs["msg"] = ptrs["paycode"];
            //    ptrs["port"] = ptrs["price"];
            //}
            //ptrs.Add("ordernum", string.Format("{0:yyyyMM}{1}", _apiOrder.FirstDate, _apiOrder.id));
            //ptrs.Add("virtualMobile", base.GetVirtualMobile());

            //string qs = UrlEncode(ptrs);
            //string url;
            //if (API_PushUrl.Contains('?'))
            //    url = API_PushUrl + "&" + qs;
            //else
            //    url = API_PushUrl + "?" + qs;

            //asyncSendData(url, null);

        }


    }
}
