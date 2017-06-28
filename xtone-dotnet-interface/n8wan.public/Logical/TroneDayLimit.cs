using LightDataModel;
using n8wan.Public.Model;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading;

namespace n8wan.Public.Logical
{

    /// <summary>
    /// 更新通道日月限数据
    /// </summary>
    public class TroneDayLimit : Shotgun.Model.Logical.Logical
    {
        /// <summary>
        /// 非API通道月数据
        /// </summary>
        static Dictionary<int, Model.SpTroneCountInfo> _nonApiMonth;
        /// <summary>
        /// 非API通道日数据
        /// </summary>
        static Dictionary<int, Model.SpTroneCountInfo> _nonApiDay;
        static DateTime _nonApiDate;
        /// <summary>
        /// API平台，用户日月限推送URL
        /// </summary>
        static string pushUrl;

        static TroneDayLimit()
        {
            pushUrl = ConfigurationManager.AppSettings["TroneDayLimit"];
            if (string.IsNullOrEmpty(pushUrl))
                return;
            if (pushUrl.Contains("?"))
                pushUrl += "&";
            else
                pushUrl += "?";
            _nonApiDate = DateTime.Today;
            _nonApiDay = new Dictionary<int, SpTroneCountInfo>();
            _nonApiMonth = new Dictionary<int, SpTroneCountInfo>();


        }
        public static void UpdateDayLimit(Shotgun.Database.IBaseDataClass2 dBase, int spTroneId, int cpId, decimal amount)
        {
            var m = LightDataModel.tbl_day_month_limitItem.GetOrCreateItem(dBase, spTroneId, cpId);
            m.cur_day_amount += amount;
            PushDayLimit(spTroneId, cpId, amount);
            try
            {
                dBase.SaveData(m);
            }
#if !DEBUG
            catch
            {
            }
#endif
            finally { }


        }

        private static void PushDayLimit(int spTroneId, int cpId, decimal amount)
        {
            if (string.IsNullOrEmpty(pushUrl) || !pushUrl.StartsWith("http", StringComparison.OrdinalIgnoreCase))
                return;

            var url = string.Format("{3}sptroneid={0}&cpid={1}&money={2}", spTroneId, cpId, amount, pushUrl);
            ThreadPool.QueueUserWorkItem(SendData, url);

        }

        private static void SendData(object state)
        {
            string url = (string)state;
            var web = System.Net.WebRequest.Create(url);
            web.Timeout = 1000;
            try
            {
                var rsp = web.GetResponse();
                rsp.Close();
            }
            catch { }
        }

        /*
        private static void UpdateNoNApiDayLimit(Shotgun.Database.IBaseDataClass2 dBase, tbl_sp_troneItem spTrone, decimal amount)
        {
            if (_nonApiDate.Date != DateTime.Today)
            {
                _nonApiDay.Clear();
                if (_nonApiDate.Month != DateTime.Today.Month)
                    _nonApiMonth.Clear();
                _nonApiDate = DateTime.Today;
            }

            if (spTrone.trone_api_id != 0)
                return;//api 方式
            if (spTrone.month_limit <= 0 || spTrone.day_limit <= 0)
                return;//日/月均无限制
            int fee = (int)(amount * 100);
            UpdateNoNApiDayLimit(dBase, spTrone, fee);

        }

        private static bool UpdateNoNApiLimit(Shotgun.Database.IBaseDataClass2 dBase, tbl_sp_troneItem spTrone, int amount, bool isMonth)
        {
            Dictionary<int, SpTroneCountInfo> dict;
            if (isMonth)
            {
                dict = _nonApiMonth;
                if (spTrone.month_limit <= 0)
                    return false;
            }
            else
            {
                dict = _nonApiDay;
                if (spTrone.day_limit <= 0)
                    return false;
            }
            SpTroneCountInfo info;
            bool isnew = false;

            if (!dict.ContainsKey(spTrone.trone_api_id))
            {
                info = SpTroneCountInfo.LoadFromDbase(dBase, spTrone.id, isMonth);
                dict[spTrone.id] = info;
                isnew = true;
            }
            else
            {
                info = dict[spTrone.id];
            }
            info.Count++;
            info.Sum += amount;
            return isnew;
        }
         */

    }
}
