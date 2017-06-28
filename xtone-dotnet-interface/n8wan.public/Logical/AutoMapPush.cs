using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace n8wan.Public.Logical
{
    public class AutoMapPush : CPPusher
    {
        private IEnumerable<LightDataModel.tbl_trone_orderItem> _allCfg;
        static object locker = new object();

        public override bool LoadCPAPI()
        {
            if (Trone == null)
                return false;

            _allCfg = tbl_trone_orderItem.QueryByTroneIdWithCache(dBase, Trone.id);

            if (_allCfg == null || _allCfg.Count() == 0)
            {
                WriteTrackLog("LoadCPAPI._allCfg.count()=0 or null");
                WriteTrackLog(tbl_trone_orderItem.GetCacheInfo());
                var m = CreateDefaultTrone();
                var t = new List<tbl_trone_orderItem>();
                t.Add(m);
                _allCfg = t;
            }
            else
                WriteTrackLog("_allCfg.count=" + _allCfg.Count().ToString());
            return SetSuccess();
        }


        public override bool DoPush()
        {
            //bool isRecord = false;
            tbl_trone_orderItem defCfg = null;
            if (PushObject.cp_id > 0 && PushObject.cp_id != 34)
            {
                defCfg = tbl_trone_orderItem.GetRowByIdWithCache(dBase, PushObject.trone_order_id);
                if (defCfg == null)
                    return SetErrorMesage("已经绑定的渠道业务信息丢失");
                base.SetConfig(defCfg);
                return base.DoPush();
            }
            tbl_trone_orderItem tOrder = null;
            int matchCount = 0;
            foreach (var m in _allCfg)
            {
                if (m.is_unknow)
                {
                    if (defCfg != null)
                        base.WriteLog(-3, string.Format("存在多个默认CP! cfgId:{0} linkid:{1}", m.id.ToString(), PushObject.GetValue(EPushField.LinkID)));
                    defCfg = m;
                    continue;
                }
                if (!IsMatch(m))
                    continue;

                if (tOrder == null)
                    tOrder = m;
                else
                {
                    base.WriteLog(-3, string.Format("配置有冲突! cfgId:{0} linkid:{1}", m.id.ToString(), PushObject.GetValue(EPushField.LinkID)));
                    WriteTrackLog(string.Format("配置有冲突! cfgId:{0} linkid:{1}", m.id.ToString(), PushObject.GetValue(EPushField.LinkID)));
                    continue;
                }
                //isRecord = true;
                matchCount++;
            }
            if (matchCount > 1)
            {
                SetErrorMesage(string.Format("匹配到{0}个CP业务", matchCount));
                return false;//匹配到多个渠道
            }

            if (tOrder != null)
            {//匹配到一个渠道
                base.SetConfig(tOrder);
                base.DoPush();
                return true;
            }

            if (PushObject.cp_id == 34)
            {
                WriteTrackLog("CP_id=34???");
                return true;
            }
            if (defCfg == null)
                defCfg = CreateDefaultTrone();
            base.SetConfig(defCfg);
            base.DoPush();
            WriteTrackLog("未匹配CP");
            return true;

        }

        private tbl_trone_orderItem CreateDefaultTrone()
        {

            lock (locker)
            {
                var ret = tbl_trone_orderItem.GetCacheUnkowOrder(Trone.id);
                if (ret != null)
                {
                    WriteTrackLog("CreateDefaultTrone，其它线程已经创建");
                    return ret;
                }

                ret = new tbl_trone_orderItem();
                ret.cp_id = 34; //未知CP的ID
                ret.create_date = DateTime.Now;
                ret.disable = false;
                ret.is_dynamic = false;
                ret.is_unknow = true;
                ret.order_num = "*";
                ret.order_trone_name = "未分配指令";
                ret.push_url_id = 47; //未知CP推送URL
                ret.trone_id = Trone.id;
                ret.SaveToDatabase(dBase);
                WriteTrackLog("CreateDefaultTrone，创建成功");
                return ret;
            }
        }

        private bool IsMatch(LightDataModel.tbl_trone_orderItem m)
        {
            Regex rx;
            string spMsg = base.PushObject.GetValue(Logical.EPushField.Msg);
            if (spMsg == null)
                spMsg = string.Empty;
            if (m.is_dynamic)
            {//CP可模糊的指令
                rx = Library.GetRegex(m.order_num);
                return rx.IsMatch(spMsg);
            }
            //CP精确指令
            return spMsg.Equals(m.order_num, StringComparison.OrdinalIgnoreCase);
        }

    }


}
