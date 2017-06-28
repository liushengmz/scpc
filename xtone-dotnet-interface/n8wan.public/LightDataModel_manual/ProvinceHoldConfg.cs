using n8wan.Public.Logical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace LightDataModel
{
    public class ProvinceHoldConfg : IHold_DataItem
    {

        static Dictionary<int, ProvinceHoldData> _allData;
        private IHold_DataItem _defCfg;
        ProvinceHoldData _data;

        static ProvinceHoldConfg()
        {
            _allData = new Dictionary<int, ProvinceHoldData>();
        }

        private ProvinceHoldConfg(IHold_DataItem defCfg, float percent, int idKey)
        {
            _defCfg = defCfg;
            hold_percent = (int)(percent * 100);
            lock (_allData)
            {
                if (_allData.ContainsKey(idKey))
                {
                    _data = _allData[idKey];
                }
                else
                {
                    _data = new ProvinceHoldData();
                    _allData[idKey] = _data;
                }
            }

        }

        /// <summary>
        /// 加载分省扣量数据
        /// </summary>
        /// <param name="defCfg"></param>
        /// <param name="prvCfg">为空时，直接反回默认扣量</param>
        /// <param name="ProvinceId">当前传入省份信息</param>
        /// <returns></returns>
        public static IHold_DataItem LoadProvinceData(tbl_trone_orderItem defCfg, tbl_cp_trone_rateItem prvCfg, int ProvinceId)
        {
            if (prvCfg == null || string.IsNullOrEmpty(prvCfg.province_hold_rate))
                return defCfg;
            //23,4,28,22,13,11,27=0.36;7,19,24,29,3,15,17,25=0.25;5,2,16=0.36;30,8,26,9,20=0.35
            var cfg = prvCfg.province_hold_rate.Replace(" ", "");

            var mc = Regex.Match(cfg, "((?in)(^|;)([\\d,]+?,|)" + ProvinceId.ToString() + "(,.+?|))=([\\d\\.]+)");

            if (!mc.Success)
                return defCfg;

            float rate;
            if (!float.TryParse(mc.Groups[2].Value, out rate))
                return defCfg;
            int bitsKey = prvCfg.sp_trone_id;
            bitsKey <<= 13;
            bitsKey |= defCfg.cp_id & 0x1fff;
            bitsKey <<= 13;
            bitsKey |= ProvinceId;

            //sp_trone id 13bit  + cp id 13bit + provinceId 6bit
            // 13bit max int 8191
            return new ProvinceHoldConfg(defCfg, rate, bitsKey);
        }

        public int hold_percent { get; set; }

        public decimal hold_amount
        {
            get { return _defCfg.hold_amount; }
        }

        public decimal amount
        {
            get
            {
                return _defCfg.amount;
            }
            set
            {
                _defCfg.amount = value;
            }
        }

        public int hold_CycCount
        {
            get
            {
                return _data.hold_CycCount;
            }
            set
            {
                _data.hold_CycCount = value;
            }
        }

        public int hold_CycProc
        {
            get
            {
                return _data.hold_CycProc;
            }
            set
            {
                _data.hold_CycProc = value;
            }
        }

        public int push_count
        {
            get
            {
                return _defCfg.push_count;
            }
            set
            {
                _defCfg.push_count = value;
            }
        }

        public int hold_start
        {
            get { return _defCfg.hold_start; }
        }

        public DateTime lastDate
        {
            get
            {
                return _defCfg.lastDate;
            }
            set
            {
                _defCfg.lastDate = value;
            }
        }
    }
}
