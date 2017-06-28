using LightDataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 推字段
    /// </summary>
    public enum EPushField
    {
        Mobile,
        ServiceCode,
        LinkID,
        Msg,
        Status,
        /// <summary>
        /// 取得原始同步串
        /// </summary>
        [Obsolete("已经不支持")]
        OrgQueryString,
        /// <summary>
        /// 端口号
        /// </summary>
        port,
        /// <summary>
        /// 支付金额
        /// </summary>
        price,
        /// <summary>
        /// 透参信息
        /// </summary>
        cpParam,
        province,
        /// <summary>
        /// API订单号
        /// </summary>
        ApiOrderId,
        city
    }

    /// <summary>
    /// 推送模型
    /// </summary>
    public interface ICPPushModel
    {
        /// <summary>
        /// 取得相应的字段值
        /// </summary>
        /// <param name="f"></param>
        string GetValue(EPushField f);

        /// <summary>
        /// 标记为已经推送
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="CP_Cfg_Id">推送时采用的配置</param>
        LightDataModel.tbl_cp_mrItem SetPushed(Shotgun.Database.IBaseDataClass2 dBase, tbl_trone_orderItem tCfg);

        /// <summary>
        /// 标记为扣量记录
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="CP_Cfg_Id">推送时采用的配置ID</param>
        void SetHidden(Shotgun.Database.IBaseDataClass2 dBase, tbl_trone_orderItem tCfg);

        int cp_id { get; }

        int trone_order_id { get; }

        int syn_flag { get; }

        int trone_id { get; }
        int sp_trone_id { get; }

        int province_id { get; }

        int city_id { get; }

    }
}
