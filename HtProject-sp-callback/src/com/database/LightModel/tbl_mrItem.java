package com.database.LightModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.database.Interface.IDatabase;
import com.dianfu.Interface.ICPPushModel;

public class tbl_mrItem extends tbl_mrItem_ori implements com.database.Interface.ISMS_DataItem, ICPPushModel {

	final String pfxTable = "tbl_mr";
	public final static String pfxSchame = "daily_log";

	String _tabName;

	public tbl_mrItem() {
		this.SetTabelDate(new Date());
	}

	@Override
	public String TableName() {
		return _tabName;
	}

	public void SetTabelDate(Date date) {
		_tabName = String.format("%s_%s", pfxTable, new SimpleDateFormat("YYYYMM").format(date));
	}

	@Override
	public String Schema() {
		return pfxSchame;
	}

	@Override
	public Date get_recdate() {
		return this.get_mr_date();
	}

	@Override
	public void set_recdate(Date value) {
		this.set_mr_date(value);
	}

	@Override
	public String GetValue(EPushField f) {
		switch (f) {
		case Mobile:
			return this.get_mobile();
		case ServiceCode:
			return this.get_service_code();
		case LinkID:
			return this.get_linkid();
		case Msg: /** 端口号 */
			return this.get_ori_order();
		case port: /** 支付金额 */
			return this.get_ori_trone();
		case price: /** 透参信息 */
			return Integer.toString(this.get_price());
		case cpParam:
			return this.get_cp_param();
		case province:
			return Integer.toString(this.get_province_id());
		}
		return null;
	}

	@Override
	public tbl_cp_mrItem SetPushed(IDatabase dBase, tbl_trone_orderItem tCfg) {
		this.set_syn_flag(1);
		this.set_trone_order_id(tCfg.get_id());
		this.set_cp_id(tCfg.get_cp_id());
		dBase.saveData(this);
		return this.CopyToCP();

	}

	private tbl_cp_mrItem CopyToCP() {
		tbl_cp_mrItem cpmr = new tbl_cp_mrItem();

		if (!this.Is_imeiNull())
			cpmr.set_imei(this.get_imei());
		if (!this.Is_imsiNull())
			cpmr.set_imsi(this.get_imsi());
		if (!this.Is_mobileNull())
			cpmr.set_mobile(this.get_mobile());
		if (!this.Is_mccNull())
			cpmr.set_mcc(this.get_mcc());
		if (!this.Is_province_idNull())
			cpmr.set_province_id(this.get_province_id());
		if (!this.Is_city_idNull())
			cpmr.set_city_id(this.get_city_id());
		if (!this.Is_trone_order_idNull())
			cpmr.set_trone_order_id(this.get_trone_order_id());
		if (!this.Is_ori_troneNull())
			cpmr.set_ori_trone(this.get_ori_trone());
		if (!this.Is_ori_orderNull())
			cpmr.set_ori_order(this.get_ori_order());
		if (!this.Is_linkidNull())
			cpmr.set_linkid(this.get_linkid());
		if (!this.Is_cp_paramNull())
			cpmr.set_cp_param(this.get_cp_param());
		if (!this.Is_service_codeNull())
			cpmr.set_service_code(this.get_service_code());
		if (!this.Is_priceNull())
			cpmr.set_price(this.get_price());
		if (!this.Is_ipNull())
			cpmr.set_ip(this.get_ip());
		if (!this.Is_mr_dateNull())
			cpmr.set_mr_date(this.get_mr_date());
		if (!this.Is_ivr_timeNull())
			cpmr.set_ivr_time(this.get_ivr_time());

		cpmr.set_mr_table(this.TableName());
		cpmr.set_mr_id(this.get_id());
		cpmr.set_trone_type(this.get_trone_type());
		
		return cpmr;
	}

	@Override
	public void SetHidden(IDatabase dBase, tbl_trone_orderItem tCfg) {
		this.set_syn_flag(0);
		this.set_trone_order_id(tCfg.get_id());
		this.set_cp_id(tCfg.get_cp_id());
		dBase.saveData(this);
	}

}
