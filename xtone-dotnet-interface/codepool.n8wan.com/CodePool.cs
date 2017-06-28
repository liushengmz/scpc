using LightDataModel;
using n8wan.codepool.Model;
using NoSqlModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using Newtonsoft.Json.Linq;
using n8wan.Public.Logical;

namespace n8wan.codepool
{
    public class CodePool : BaseCodePool
    {
        private LightDataModel.tbl_cityItem _city;
        private LightDataModel.tbl_cp_poolItem _poolInfo;
        private List<NoSqlModel.PoolSetModel> _poolSet;
        private tbl_sp_troneItem[] _spTrones;

        static StaticCacheTimeline<NoSqlModel.CustomLastTroneModel, long> customLastTroneCache;
        static CodePool()
        {
            customLastTroneCache = new StaticCacheTimeline<CustomLastTroneModel, long>();
            customLastTroneCache.Expired = new TimeSpan(0, 5, 0);
        }
        public CodePool()
        {
            timer = new System.Diagnostics.Stopwatch();
            timer.Start();
        }



        /// <summary>
        /// 用于填充订单基础信息
        /// </summary>
        /// <param name="request"></param>
        /// <returns></returns>
        public override bool Init(HttpRequest request)
        {
            if (_orderInfo != null)
                return SetSuccess();


            var str = request["poolid"] ?? request["feecode"];
            int i;
            if (string.IsNullOrEmpty(str))
                return SetErrorMesage(codepool.ErrorCode.Paycode_Error, "poolid 错误");

            if (str.StartsWith("1") && str.Length == 6)
            {
                if (!int.TryParse(str.Substring(1), out i))
                    return SetErrorMesage(codepool.ErrorCode.Paycode_Error, "poolid 错误");
                _poolInfo = new tbl_cp_poolItem() { name = "feecode to poolset", status = true, fee = i };
            }
            else
            {
                if (str.Length == 6)
                    str = str.Substring(1);

                if (!int.TryParse(str, out i))
                    return SetErrorMesage(codepool.ErrorCode.Paycode_Error, "poolid 错误");
                _poolInfo = LightDataModel.tbl_cp_poolItem.GetRowById(dBase, i);
            }

            if (_poolInfo == null)
                return SetErrorMesage(codepool.ErrorCode.Paycode_Error, "无效poolid");

            _orderInfo = new LightDataModel.tbl_api_orderItem();
            _orderInfo.IgnoreEquals = true;
            _orderInfo.cp_pool_id = _poolInfo.id;

            if (_poolInfo.id == 0)
            {
                _orderInfo.trone_order_id = _poolInfo.fee;
                _poolInfo.fee = 0;
            }

            _orderInfo.imei = request["imei"];
            _orderInfo.imsi = request["imsi"];
            _orderInfo.clientip = request["clientip"] ?? "0.0.0.0";

            if (int.TryParse(request["lac"], out i))
                _orderInfo.lac = i;
            if (int.TryParse(request["cid"], out i))
                _orderInfo.cid = i;

            _orderInfo.user_agent = request["useragent"];
            _orderInfo.iccid = request["iccid"];
            _orderInfo.packagename = request["package"];
            _orderInfo.mobile = request["phone"] ?? request["mobile"];
            _orderInfo.ExtrData = request["cpparams"];

            _orderInfo.ip = request.UserHostAddress;
            if (string.IsNullOrEmpty(_orderInfo.mobile))
            {
                if (string.IsNullOrEmpty(_orderInfo.imsi))
                    return SetErrorMesage(codepool.ErrorCode.Parameter_Miss, "imsi 或 phone 不能为空");
            }
            else if (_orderInfo.mobile.Length != 11)
            {
                return SetErrorMesage(ErrorCode.Invalid_Parameter, "手机号码长度错误");
            }
           
            InitUserExtrInfo();
            return SetSuccess();
        }

        /// <summary>
        /// 创建订单号，在外部初始化
        /// </summary>
        /// <returns></returns>
        public tbl_api_orderItem InitUserInfo(int poolId)
        {
            _poolInfo = LightDataModel.tbl_cp_poolItem.GetRowById(dBase, poolId);
            if (_orderInfo != null)
                return _orderInfo;
            return _orderInfo = new tbl_api_orderItem() { cp_pool_id = poolId, IgnoreEquals = true };
        }

        /// <summary>
        /// 初始化省份信息
        /// </summary>
        public void InitUserExtrInfo()
        {
            if (_orderInfo.city > 0)
                return;
            string s;
            if (!string.IsNullOrEmpty(_orderInfo.mobile))
                s = _orderInfo.mobile.Substring(0, 7);
            else
                s = n8wan.Public.Library.GetPhoneByImsi(_orderInfo.imsi);


            _orderInfo.fake_mobile = s;
            int i;
            if (int.TryParse(s, out i))
                _city = LightDataModel.tbl_phone_locateItem.GetRowByMobile(dBase, i);
            if (_city == null)
                _city = new LightDataModel.tbl_cityItem() { id = 416, province_id = 32 };
            _orderInfo.city = _city.id;
            _orderInfo.FirstDate = DateTime.Now;

        }


        /// <summary>
        /// 处理省份，屏蔽时间,用户日月限
        /// </summary>
        /// <returns></returns>
        public bool InitTrone()
        {
            if (_poolInfo.id == 0)
            {
                var m = Dao.PoolSet.TroneOrderToPoolSetMode(dBase, _orderInfo.trone_order_id);
                if (m == null)
                    return SetErrorMesage(ErrorCode.Paycode_Error, "无效计费点");
                _poolSet = new List<PoolSetModel>();
                _poolSet.Add(m);
                var trone = LightDataModel.tbl_troneItem.GetRowById(dBase, m.trone_id);
                _poolInfo.fee = decimal.ToInt32(trone.price * 100);
                _orderInfo.trone_id = trone.id;
            }
            else
                _poolSet = Dao.PoolSet.QueryPoolSetById(dBase, _poolInfo.id);

            if (_poolSet == null || _poolSet.Count == 0)
                return SetErrorMesage(codepool.ErrorCode.Paycode_Error, "未配置计费通道");

            var ids = _poolSet.Where(e => e.status == 1).Select(e => e.sp_trone_id).Distinct().ToArray();
            if (ids.Length == 0)
                return SetErrorMesage(ErrorCode.Paycode_Error, "此计费代码已经停止");
            var spTrones = LightDataModel.tbl_sp_troneItem.GetRowsById(dBase, ids);
            if (spTrones.Count == 0)
                return SetErrorMesage(codepool.ErrorCode.Paycode_Error, "无可用业务");

            var sptn1st = spTrones.FirstOrDefault();
            if (spTrones.Count == 1)//对单通道的池，特殊优化
                _orderInfo.api_id = sptn1st.trone_api_id;


            if (sptn1st.up_data_type == 2 || sptn1st.up_data_type == 3)
            {//联网验证码 ，联网验证码（手机回复）
                if (string.IsNullOrEmpty(_orderInfo.mobile))
                    return SetErrorMesage(ErrorCode.Invalid_Parameter, "phone 参数不能为空");
            }
            else if (string.IsNullOrEmpty(_orderInfo.imsi))
                return SetErrorMesage(ErrorCode.Invalid_Parameter, "imsi 参数不能为空");
            int stCount = spTrones.Count();

            var seconds = (int)(DateTime.Now - DateTime.Today).TotalSeconds;
            bool HasTone = false, isUserDayLimited = false, isUserMonthLimited = false, hasProvince = false, IsOpenTime = false;
            bool isSpTroneMonthLimited = false, isSpTroneDayLimited = false;

            _spTrones = spTrones.Where(e =>
                  {
                      //System.Diagnostics.Debug.WriteLine("Sp Trone check id={0}", e.id);
                      if (e.status == 0)
                          return false;
                      HasTone = true;
                      if (stCount > 1 || e.is_force_hold == 1)
                      {//存在多个sp通道时（池模式） 或是 指定了强制匹配省份
                          if (!e.ProvinceId.Contains(_city.province_id)) //省份过滤
                              return false;
                      }
                      hasProvince = true;
                      if (!e.IsOpenTime(seconds))//屏蔽时间处理
                          return false;
                      IsOpenTime = true;

                      switch (IsSpTroneLimited(e))//通道日月限
                      {
                          case 2: isSpTroneMonthLimited = true; return false;
                          case 1: isSpTroneDayLimited = true; return false;
                      }

                      switch (IsCustomLimited(e))//用户日月限
                      {
                          case 2: isUserMonthLimited = true; return false;
                          case 1: isUserDayLimited = true; return false;
                      }
                      return true;
                  }).ToArray();//省份匹配的业务

            if (_spTrones.Length == 0)
            {
                if (!HasTone)
                    return SetErrorMesage(ErrorCode.No_Trone_Config, "无可用通道信息");
                if (!hasProvince)
                    return SetErrorMesage(ErrorCode.AREA_CLOSE, "屏蔽省份");
                if (!IsOpenTime)
                    return SetErrorMesage(ErrorCode.Time_Close, "屏蔽时间");

                if (isSpTroneMonthLimited)
                    return SetErrorMesage(ErrorCode.SP_TRONE_MONTH_OVER_LIMIT, "通道月限");
                if (isSpTroneDayLimited)
                    return SetErrorMesage(ErrorCode.SP_TRONE_DAY_OVER_LIMIT, "通道日限");

                if (isUserDayLimited)
                    return SetErrorMesage(ErrorCode.SP_TRONE_USER_DAY_OVER_LIMIT, "用户日限");
                if (isUserMonthLimited)
                    return SetErrorMesage(ErrorCode.SP_TRONE_USER_MONTH_OVER_LIMIT, "用户月限");

                return SetErrorMesage(ErrorCode.No_Trone_Config, "无通道(省份/时间/用户)");
            }
            return SetSuccess();
        }


        /// <summary>
        /// 用户日月限判断
        /// </summary>
        /// <param name="spTrone"></param>
        /// <returns>1:日限，2:月限</returns>
        int IsCustomLimited(tbl_sp_troneItem spTrone)
        {
            int day = 0, month = 0;
            bool isCount = spTrone.limit_type == 1;


            day = decimal.ToInt32(spTrone.user_day_limit * (isCount ? 1 : 100));
            month = decimal.ToInt32(spTrone.user_month_limit * (isCount ? 1 : 100));

            if (day == 0 && month == 0)
                return 0;//不限量
            if (month == 0)
                month = int.MaxValue;
            else if (day == 0)
                day = int.MaxValue;
            var customId = _orderInfo.imsi;
            if (spTrone.up_data_type == 2 || spTrone.up_data_type == 3)
            {//联网验证码 ，联网验证码（手机回复）
                customId = _orderInfo.mobile;
            }
            if (string.IsNullOrEmpty(customId))
                return 0;
            var cli = Dao.CustomFee.QueryLimit(dBase, spTrone.id, customId);//此处应该正确选选择用户标识

            //以下假设此次计费成功，是否会超限
            if (isCount)
            {
                if (cli.DayCount >= day)
                    return 1;
                return cli.MonthCount >= month ? 2 : 0;
            }

            if ((cli.DayAmount + _poolInfo.fee) > day)
                return 1;
            return (cli.MonthAmount + _poolInfo.fee) > month ? 2 : 0;
        }

        int IsSpTroneLimited(tbl_sp_troneItem spTrone)
        {
            int day = 0, month = 0;
            bool isCount = spTrone.limit_type == 1;


            day = decimal.ToInt32(spTrone.day_limit * (isCount ? 1 : 100));
            month = decimal.ToInt32(spTrone.month_limit * (isCount ? 1 : 100));

            if (day == 0 && month == 0)
                return 0;//不限量
            if (month == 0)
                month = int.MaxValue;
            else if (day == 0)
                day = int.MaxValue;


            var cli = Dao.CustomFee.QueryLimit(dBase, spTrone.id, null);//此处应该正确选选择用户标识

            //以下假设此次计费成功，是否会超限
            if (isCount)
            {
                if (cli.DayCount >= day)
                    return 1;
                return cli.MonthCount >= month ? 2 : 0;
            }

            if ((cli.DayAmount + _poolInfo.fee) > day)
                return 1;
            return (cli.MonthAmount + _poolInfo.fee) > month ? 2 : 0;

        }

        /// <summary>
        /// 根据固定的优先级，选取通道信息
        /// </summary>
        /// <returns></returns>
        public PoolSetModel GetPSModelByPriority()
        {
            var ids = _spTrones.Select(e => e.id).ToArray();
            int pv = 0;
            var trones = _poolSet.Where(e =>
            {
                if (e.status == 1 && ids.Contains(e.sp_trone_id))
                { pv += e.priority; return true; }
                return false;
            }).OrderBy(e => e.id).ToArray();

            if (trones.Count() == 0)//正常情况不能为0
            {
                SetErrorMesage(ErrorCode.No_Trone_Config, "无可用通道");
                return null;
            }
            int rkey = -1;
            string seed = null;

            if (!string.IsNullOrEmpty(_orderInfo.imsi) && _orderInfo.imsi.Length > 8)
                seed = _orderInfo.imsi.Substring(_orderInfo.imsi.Length - 8);
            else if (!string.IsNullOrEmpty(_orderInfo.mobile))
                seed = _orderInfo.mobile.Substring(_orderInfo.mobile.Length - 8);

            if (!string.IsNullOrEmpty(seed))
            {
                if (!int.TryParse(seed, out rkey))
                    rkey = -1;
            }
            if (rkey <= -1)
                rkey = (int)((DateTime.Now.Ticks >> 16) & 0x7FFFFFFF);

            rkey %= pv;
            PoolSetModel def = null;
            foreach (var item in trones)
            {
                def = item;
                if (rkey < item.priority)
                    break;
                rkey -= item.priority;
            }

            _orderInfo.trone_id = def.trone_id;
            _orderInfo.trone_order_id = def.trone_order_id;
            _orderInfo.api_id = _spTrones.First(e => e.id == def.sp_trone_id).trone_api_id;

            return def;
        }


    }
}
