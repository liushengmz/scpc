package com.database.LightModel;

import java.util.*;
import java.math.BigDecimal;

public abstract class tbl_cp_mr_ori extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";

	/** 主键值 */
	private int _id;

	/** IMEI */
	private String _imei;
	/** IMSI */
	private String _imsi;
	/** MOBILE */
	private String _mobile;
	/** 国家码 */
	private String _mcc;
	/** 省份id */
	private int _province_id;
	/** 城市id */
	private int _city_id;
	/** 通道指令id */
	private int _trone_order_id;
	/** 原始通道 */
	private String _ori_trone;
	/** 原始指令 */
	private String _ori_order;
	/** linkid */
	private String _linkid;
	/** CP透参参数 */
	private String _cp_param;
	/** 传SP传过来的数据 */
	private String _service_code;

	private int _price;
	/** IP */
	private String _ip;
	/** 同步给CP标识，默认0为未同步，1为已同步 */
	private int _syn_status;
	/** mr表名 */
	private String _mr_table;
	/** mr表对应的自增长 */
	private int _mr_id;
	/** IVR的分钟数 */
	private int _ivr_time;
	/** 0为默认通道，1为包月,2为IVR */
	private int _trone_type;
	/** 下行日期 */
	private Date _mr_date;
	/** 入库时间 */
	private Date _create_date;

	@Override
	public String IdentifyField() {
		return identifyField;
	}

	@Override
	protected String[] GetNullableFields() {
		return new String[] { null, "imei", "imsi", "mobile", "mcc", "province_id", "city_id", "trone_order_id",
				"ori_trone", "ori_order", "linkid", "cp_param", "service_code", "price", "ip", "syn_status", "mr_table",
				"mr_id", "ivr_time", "mr_date" };
	}

	public Boolean Is_imeiNull() {
		return IsNull(Fields.imei);
	}

	public void Set_imeiNull() {
		SetNull(Fields.imei);
	}

	public Boolean Is_imsiNull() {
		return IsNull(Fields.imsi);
	}

	public void Set_imsiNull() {
		SetNull(Fields.imsi);
	}

	public Boolean Is_mobileNull() {
		return IsNull(Fields.mobile);
	}

	public void Set_mobileNull() {
		SetNull(Fields.mobile);
	}

	public Boolean Is_mccNull() {
		return IsNull(Fields.mcc);
	}

	public void Set_mccNull() {
		SetNull(Fields.mcc);
	}

	public Boolean Is_province_idNull() {
		return IsNull(Fields.province_id);
	}

	public void Set_province_idNull() {
		SetNull(Fields.province_id);
	}

	public Boolean Is_city_idNull() {
		return IsNull(Fields.city_id);
	}

	public void Set_city_idNull() {
		SetNull(Fields.city_id);
	}

	public Boolean Is_trone_order_idNull() {
		return IsNull(Fields.trone_order_id);
	}

	public void Set_trone_order_idNull() {
		SetNull(Fields.trone_order_id);
	}

	public Boolean Is_ori_troneNull() {
		return IsNull(Fields.ori_trone);
	}

	public void Set_ori_troneNull() {
		SetNull(Fields.ori_trone);
	}

	public Boolean Is_ori_orderNull() {
		return IsNull(Fields.ori_order);
	}

	public void Set_ori_orderNull() {
		SetNull(Fields.ori_order);
	}

	public Boolean Is_linkidNull() {
		return IsNull(Fields.linkid);
	}

	public void Set_linkidNull() {
		SetNull(Fields.linkid);
	}

	public Boolean Is_cp_paramNull() {
		return IsNull(Fields.cp_param);
	}

	public void Set_cp_paramNull() {
		SetNull(Fields.cp_param);
	}

	public Boolean Is_service_codeNull() {
		return IsNull(Fields.service_code);
	}

	public void Set_service_codeNull() {
		SetNull(Fields.service_code);
	}

	public Boolean Is_priceNull() {
		return IsNull(Fields.price);
	}

	public void Set_priceNull() {
		SetNull(Fields.price);
	}

	public Boolean Is_ipNull() {
		return IsNull(Fields.ip);
	}

	public void Set_ipNull() {
		SetNull(Fields.ip);
	}

	public Boolean Is_syn_statusNull() {
		return IsNull(Fields.syn_status);
	}

	public void Set_syn_statusNull() {
		SetNull(Fields.syn_status);
	}

	public Boolean Is_mr_tableNull() {
		return IsNull(Fields.mr_table);
	}

	public void Set_mr_tableNull() {
		SetNull(Fields.mr_table);
	}

	public Boolean Is_mr_idNull() {
		return IsNull(Fields.mr_id);
	}

	public void Set_mr_idNull() {
		SetNull(Fields.mr_id);
	}

	public Boolean Is_ivr_timeNull() {
		return IsNull(Fields.ivr_time);
	}

	public void Set_ivr_timeNull() {
		SetNull(Fields.ivr_time);
	}

	public Boolean Is_mr_dateNull() {
		return IsNull(Fields.mr_date);
	}

	public void Set_mr_dateNull() {
		SetNull(Fields.mr_date);
	}

	public int get_id() {
		return this._id;
	}

	public void set_id(int value) {
		this._id = value;
	}

	/** IMEI */
	public String get_imei() {
		return this._imei;
	}

	/** IMEI */
	@SuppressWarnings("unused")
	public void set_imei(String value) {
		if (false && true)
			RemoveNullFlag(Fields.imei);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.imei);
			else
				RemoveNullFlag(Fields.imei);
		}

		SetFieldHasUpdate(Fields.imei, this._imei, value);
		this._imei = value;
	}

	/** IMSI */
	public String get_imsi() {
		return this._imsi;
	}

	/** IMSI */
	@SuppressWarnings("unused")
	public void set_imsi(String value) {
		if (false && true)
			RemoveNullFlag(Fields.imsi);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.imsi);
			else
				RemoveNullFlag(Fields.imsi);
		}

		SetFieldHasUpdate(Fields.imsi, this._imsi, value);
		this._imsi = value;
	}

	/** MOBILE */
	public String get_mobile() {
		return this._mobile;
	}

	/** MOBILE */
	@SuppressWarnings("unused")
	public void set_mobile(String value) {
		if (false && true)
			RemoveNullFlag(Fields.mobile);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.mobile);
			else
				RemoveNullFlag(Fields.mobile);
		}

		SetFieldHasUpdate(Fields.mobile, this._mobile, value);
		this._mobile = value;
	}

	/** 国家码 */
	public String get_mcc() {
		return this._mcc;
	}

	/** 国家码 */
	@SuppressWarnings("unused")
	public void set_mcc(String value) {
		if (false && true)
			RemoveNullFlag(Fields.mcc);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.mcc);
			else
				RemoveNullFlag(Fields.mcc);
		}

		SetFieldHasUpdate(Fields.mcc, this._mcc, value);
		this._mcc = value;
	}

	/** 省份id */
	public int get_province_id() {
		return this._province_id;
	}

	/** 省份id */
	@SuppressWarnings("unused")
	public void set_province_id(int value) {
		RemoveNullFlag(Fields.province_id);
		SetFieldHasUpdate(Fields.province_id, this._province_id, value);
		this._province_id = value;
	}

	/** 城市id */
	public int get_city_id() {
		return this._city_id;
	}

	/** 城市id */
	@SuppressWarnings("unused")
	public void set_city_id(int value) {
		RemoveNullFlag(Fields.city_id);
		SetFieldHasUpdate(Fields.city_id, this._city_id, value);
		this._city_id = value;
	}

	/** 通道指令id */
	public int get_trone_order_id() {
		return this._trone_order_id;
	}

	/** 通道指令id */
	@SuppressWarnings("unused")
	public void set_trone_order_id(int value) {
		RemoveNullFlag(Fields.trone_order_id);
		SetFieldHasUpdate(Fields.trone_order_id, this._trone_order_id, value);
		this._trone_order_id = value;
	}

	/** 原始通道 */
	public String get_ori_trone() {
		return this._ori_trone;
	}

	/** 原始通道 */
	@SuppressWarnings("unused")
	public void set_ori_trone(String value) {
		if (false && true)
			RemoveNullFlag(Fields.ori_trone);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.ori_trone);
			else
				RemoveNullFlag(Fields.ori_trone);
		}

		SetFieldHasUpdate(Fields.ori_trone, this._ori_trone, value);
		this._ori_trone = value;
	}

	/** 原始指令 */
	public String get_ori_order() {
		return this._ori_order;
	}

	/** 原始指令 */
	@SuppressWarnings("unused")
	public void set_ori_order(String value) {
		if (false && true)
			RemoveNullFlag(Fields.ori_order);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.ori_order);
			else
				RemoveNullFlag(Fields.ori_order);
		}

		SetFieldHasUpdate(Fields.ori_order, this._ori_order, value);
		this._ori_order = value;
	}

	/** linkid */
	public String get_linkid() {
		return this._linkid;
	}

	/** linkid */
	@SuppressWarnings("unused")
	public void set_linkid(String value) {
		if (false && true)
			RemoveNullFlag(Fields.linkid);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.linkid);
			else
				RemoveNullFlag(Fields.linkid);
		}

		SetFieldHasUpdate(Fields.linkid, this._linkid, value);
		this._linkid = value;
	}

	/** CP透参参数 */
	public String get_cp_param() {
		return this._cp_param;
	}

	/** CP透参参数 */
	@SuppressWarnings("unused")
	public void set_cp_param(String value) {
		if (false && true)
			RemoveNullFlag(Fields.cp_param);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.cp_param);
			else
				RemoveNullFlag(Fields.cp_param);
		}

		SetFieldHasUpdate(Fields.cp_param, this._cp_param, value);
		this._cp_param = value;
	}

	/** 传SP传过来的数据 */
	public String get_service_code() {
		return this._service_code;
	}

	/** 传SP传过来的数据 */
	@SuppressWarnings("unused")
	public void set_service_code(String value) {
		if (false && true)
			RemoveNullFlag(Fields.service_code);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.service_code);
			else
				RemoveNullFlag(Fields.service_code);
		}

		SetFieldHasUpdate(Fields.service_code, this._service_code, value);
		this._service_code = value;
	}

	public int get_price() {
		return this._price;
	}

	@SuppressWarnings("unused")
	public void set_price(int value) {
		RemoveNullFlag(Fields.price);
		SetFieldHasUpdate(Fields.price, this._price, value);
		this._price = value;
	}

	/** IP */
	public String get_ip() {
		return this._ip;
	}

	/** IP */
	@SuppressWarnings("unused")
	public void set_ip(String value) {
		if (false && true)
			RemoveNullFlag(Fields.ip);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.ip);
			else
				RemoveNullFlag(Fields.ip);
		}

		SetFieldHasUpdate(Fields.ip, this._ip, value);
		this._ip = value;
	}

	/** 同步给CP标识，默认0为未同步，1为已同步 */
	public int get_syn_status() {
		return this._syn_status;
	}

	/** 同步给CP标识，默认0为未同步，1为已同步 */
	@SuppressWarnings("unused")
	public void set_syn_status(int value) {
		RemoveNullFlag(Fields.syn_status);
		SetFieldHasUpdate(Fields.syn_status, this._syn_status, value);
		this._syn_status = value;
	}

	/** mr表名 */
	public String get_mr_table() {
		return this._mr_table;
	}

	/** mr表名 */
	@SuppressWarnings("unused")
	public void set_mr_table(String value) {
		if (false && true)
			RemoveNullFlag(Fields.mr_table);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.mr_table);
			else
				RemoveNullFlag(Fields.mr_table);
		}

		SetFieldHasUpdate(Fields.mr_table, this._mr_table, value);
		this._mr_table = value;
	}

	/** mr表对应的自增长 */
	public int get_mr_id() {
		return this._mr_id;
	}

	/** mr表对应的自增长 */
	@SuppressWarnings("unused")
	public void set_mr_id(int value) {
		RemoveNullFlag(Fields.mr_id);
		this._mr_id = value;
	}

	/** IVR的分钟数 */
	public int get_ivr_time() {
		return this._ivr_time;
	}

	/** IVR的分钟数 */
	@SuppressWarnings("unused")
	public void set_ivr_time(int value) {
		RemoveNullFlag(Fields.ivr_time);
		this._ivr_time = value;
	}

	/** 0为默认通道，1为包月,2为IVR */
	public int get_trone_type() {
		return this._trone_type;
	}

	/** 0为默认通道，1为包月,2为IVR */
	@SuppressWarnings("unused")
	public void set_trone_type(int value) {
		RemoveNullFlag(Fields.trone_type);
		SetFieldHasUpdate(Fields.trone_type, this._trone_type, value);
		this._trone_type = value;
	}

	/** 下行日期 */
	public Date get_mr_date() {
		return this._mr_date;
	}

	/** 下行日期 */
	@SuppressWarnings("unused")
	public void set_mr_date(Date value) {
		if (false && true)
			RemoveNullFlag(Fields.mr_date);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.mr_date);
			else
				RemoveNullFlag(Fields.mr_date);
		}

		SetFieldHasUpdate(Fields.mr_date, this._mr_date, value);
		this._mr_date = value;
	}

	/** 入库时间 */
	public Date get_create_date() {
		return this._create_date;
	}

	/** 入库时间 */
	@SuppressWarnings("unused")
	public void set_create_date(Date value) {
		if (false && false)
			RemoveNullFlag(Fields.create_date);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.create_date);
			else
				RemoveNullFlag(Fields.create_date);
		}

		SetFieldHasUpdate(Fields.create_date, this._create_date, value);
		this._create_date = value;
	}

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** IMEI */
		public final static String imei = "imei";
		/** IMSI */
		public final static String imsi = "imsi";
		/** MOBILE */
		public final static String mobile = "mobile";
		/** 国家码 */
		public final static String mcc = "mcc";
		/** 省份id */
		public final static String province_id = "province_id";
		/** 城市id */
		public final static String city_id = "city_id";
		/** 通道指令id */
		public final static String trone_order_id = "trone_order_id";
		/** 原始通道 */
		public final static String ori_trone = "ori_trone";
		/** 原始指令 */
		public final static String ori_order = "ori_order";
		/** linkid */
		public final static String linkid = "linkid";
		/** CP透参参数 */
		public final static String cp_param = "cp_param";
		/** 传SP传过来的数据 */
		public final static String service_code = "service_code";

		public final static String price = "price";
		/** IP */
		public final static String ip = "ip";
		/** 同步给CP标识，默认0为未同步，1为已同步 */
		public final static String syn_status = "syn_status";
		/** mr表名 */
		public final static String mr_table = "mr_table";
		/** mr表对应的自增长 */
		public final static String mr_id = "mr_id";
		/** IVR的分钟数 */
		public final static String ivr_time = "ivr_time";
		/** 0为默认通道，1为包月,2为IVR */
		public final static String trone_type = "trone_type";
		/** 下行日期 */
		public final static String mr_date = "mr_date";
		/** 入库时间 */
		public final static String create_date = "create_date";

	}

}
