package com.dianfu.Interface;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_cp_mrItem;
import com.database.LightModel.tbl_trone_orderItem;

public interface ICPPushModel {
	/** 推字段 */
	public enum EPushField {
		Mobile, ServiceCode, LinkID, Msg, /** 端口号 */
		port, /** 支付金额 */
		price, /** 透参信息 */
		cpParam, province
	}

	/** 取得相应的字段值 */
	String GetValue(EPushField f);

	/**
	 * 标记为已经推送
	 * 
	 * @param dBase
	 * @param tCfg
	 *            推送时采用的配置
	 * @return mr 复制到cp_mr，未存入数据库
	 */
	tbl_cp_mrItem SetPushed(IDatabase dBase, tbl_trone_orderItem tCfg);

	/**
	 * 标记为扣量记录
	 * 
	 * @param dBase
	 * @param tCfg
	 *            推送时采用的配置ID
	 */
	void SetHidden(IDatabase dBase, tbl_trone_orderItem CP_Cfg_Id);

	/* 渠道ID */
	int get_cp_id();
}
