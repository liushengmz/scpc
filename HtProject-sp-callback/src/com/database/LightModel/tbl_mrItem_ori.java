package com.database.LightModel;

import java.util.*;
import java.math.BigDecimal;

public abstract class tbl_mrItem_ori extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";
	//public final static String tableName = "tbl_mr_201602";

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
	/** 通道ID */
	private int _trone_id;
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
	/** 只作存储，真正计费采用通道费用，生成虚拟指令时需要使用 */
	private int _price;
	/** IP */
	private String _ip;
	/** 下行状态码 */
	private String _status;
	/** 同步给CP标识，默认1为需要同步，0为不需要同步，这个数据是读取表tbl_hold_config进行分析的 */
	private int _syn_flag;
	/** mo表名 */
	private String _mo_table;
	/** mo表对应的自增长 */
	private int _mo_id;
	/** 下行日期 */
	private Date _mr_date;
	/** 入库时间 */
	private Date _create_date;
	/** 通道是否匹配上 */
	private boolean _IsMatch;
	/** tbl_sp_api_url的键，表示从那个API通道过来的数据 */
	private int _sp_api_url_id;

	private int _sp_id;

	private int _cp_id;
	/** IVR的分钟数 */
	private int _ivr_time;
	/** 0为默认通道，1为包月,2为IVR */
	private int _trone_type;
	/** tbl_api_order_id */
	private int _api_order_id;

	@Override
	public String IdentifyField() {
		return identifyField;
	}

	@Override
	protected String[] GetNullableFields() {
		return new String[] { null, "imei", "imsi", "mobile", "mcc", "province_id", "city_id", "trone_id",
				"trone_order_id", "ori_trone", "ori_order", "linkid", "cp_param", "service_code", "price", "ip",
				"status", "syn_flag", "mo_table", "mo_id", "mr_date", "IsMatch", "sp_api_url_id", "sp_id", "cp_id",
				"ivr_time", "api_order_id" };
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

	public Boolean Is_trone_idNull() {
		return IsNull(Fields.trone_id);
	}

	public void Set_trone_idNull() {
		SetNull(Fields.trone_id);
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

	public Boolean Is_statusNull() {
		return IsNull(Fields.status);
	}

	public void Set_statusNull() {
		SetNull(Fields.status);
	}

	public Boolean Is_syn_flagNull() {
		return IsNull(Fields.syn_flag);
	}

	public void Set_syn_flagNull() {
		SetNull(Fields.syn_flag);
	}

	public Boolean Is_mo_tableNull() {
		return IsNull(Fields.mo_table);
	}

	public void Set_mo_tableNull() {
		SetNull(Fields.mo_table);
	}

	public Boolean Is_mo_idNull() {
		return IsNull(Fields.mo_id);
	}

	public void Set_mo_idNull() {
		SetNull(Fields.mo_id);
	}

	public Boolean Is_mr_dateNull() {
		return IsNull(Fields.mr_date);
	}

	public void Set_mr_dateNull() {
		SetNull(Fields.mr_date);
	}

	public Boolean Is_IsMatchNull() {
		return IsNull(Fields.IsMatch);
	}

	public void Set_IsMatchNull() {
		SetNull(Fields.IsMatch);
	}

	public Boolean Is_sp_api_url_idNull() {
		return IsNull(Fields.sp_api_url_id);
	}

	public void Set_sp_api_url_idNull() {
		SetNull(Fields.sp_api_url_id);
	}

	public Boolean Is_sp_idNull() {
		return IsNull(Fields.sp_id);
	}

	public void Set_sp_idNull() {
		SetNull(Fields.sp_id);
	}

	public Boolean Is_cp_idNull() {
		return IsNull(Fields.cp_id);
	}

	public void Set_cp_idNull() {
		SetNull(Fields.cp_id);
	}

	public Boolean Is_ivr_timeNull() {
		return IsNull(Fields.ivr_time);
	}

	public void Set_ivr_timeNull() {
		SetNull(Fields.ivr_time);
	}

	public Boolean Is_api_order_idNull() {
		return IsNull(Fields.api_order_id);
	}

	public void Set_api_order_idNull() {
		SetNull(Fields.api_order_id);
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

	/** 通道ID */
	public int get_trone_id() {
		return this._trone_id;
	}

	/** 通道ID */
	@SuppressWarnings("unused")
	public void set_trone_id(int value) {
		RemoveNullFlag(Fields.trone_id);
		SetFieldHasUpdate(Fields.trone_id, this._trone_id, value);
		this._trone_id = value;
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

	/** 只作存储，真正计费采用通道费用，生成虚拟指令时需要使用 */
	public int get_price() {
		return this._price;
	}

	/** 只作存储，真正计费采用通道费用，生成虚拟指令时需要使用 */
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

	/** 下行状态码 */
	public String get_status() {
		return this._status;
	}

	/** 下行状态码 */
	@SuppressWarnings("unused")
	public void set_status(String value) {
		if (false && true)
			RemoveNullFlag(Fields.status);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.status);
			else
				RemoveNullFlag(Fields.status);
		}

		SetFieldHasUpdate(Fields.status, this._status, value);
		this._status = value;
	}

	/** 同步给CP标识，默认1为需要同步，0为不需要同步，这个数据是读取表tbl_hold_config进行分析的 */
	public int get_syn_flag() {
		return this._syn_flag;
	}

	/** 同步给CP标识，默认1为需要同步，0为不需要同步，这个数据是读取表tbl_hold_config进行分析的 */
	@SuppressWarnings("unused")
	public void set_syn_flag(int value) {
		RemoveNullFlag(Fields.syn_flag);
		SetFieldHasUpdate(Fields.syn_flag, this._syn_flag, value);
		this._syn_flag = value;
	}

	/** mo表名 */
	public String get_mo_table() {
		return this._mo_table;
	}

	/** mo表名 */
	@SuppressWarnings("unused")
	public void set_mo_table(String value) {
		if (false && true)
			RemoveNullFlag(Fields.mo_table);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.mo_table);
			else
				RemoveNullFlag(Fields.mo_table);
		}

		SetFieldHasUpdate(Fields.mo_table, this._mo_table, value);
		this._mo_table = value;
	}

	/** mo表对应的自增长 */
	public int get_mo_id() {
		return this._mo_id;
	}

	/** mo表对应的自增长 */
	@SuppressWarnings("unused")
	public void set_mo_id(int value) {
		RemoveNullFlag(Fields.mo_id);
		SetFieldHasUpdate(Fields.mo_id, this._mo_id, value);
		this._mo_id = value;
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

	/** 通道是否匹配上 */
	public boolean get_IsMatch() {
		return this._IsMatch;
	}

	/** 通道是否匹配上 */
	@SuppressWarnings("unused")
	public void set_IsMatch(boolean value) {
		RemoveNullFlag(Fields.IsMatch);
		SetFieldHasUpdate(Fields.IsMatch, this._IsMatch, value);
		this._IsMatch = value;
	}

	/** tbl_sp_api_url的键，表示从那个API通道过来的数据 */
	public int get_sp_api_url_id() {
		return this._sp_api_url_id;
	}

	/** tbl_sp_api_url的键，表示从那个API通道过来的数据 */
	@SuppressWarnings("unused")
	public void set_sp_api_url_id(int value) {
		RemoveNullFlag(Fields.sp_api_url_id);
		SetFieldHasUpdate(Fields.sp_api_url_id, this._sp_api_url_id, value);
		this._sp_api_url_id = value;
	}

	public int get_sp_id() {
		return this._sp_id;
	}

	@SuppressWarnings("unused")
	public void set_sp_id(int value) {
		RemoveNullFlag(Fields.sp_id);
		SetFieldHasUpdate(Fields.sp_id, this._sp_id, value);
		this._sp_id = value;
	}

	public int get_cp_id() {
		return this._cp_id;
	}

	@SuppressWarnings("unused")
	public void set_cp_id(int value) {
		RemoveNullFlag(Fields.cp_id);
		SetFieldHasUpdate(Fields.cp_id, this._cp_id, value);
		this._cp_id = value;
	}

	/** IVR的分钟数 */
	public int get_ivr_time() {
		return this._ivr_time;
	}

	/** IVR的分钟数 */
	@SuppressWarnings("unused")
	public void set_ivr_time(int value) {
		RemoveNullFlag(Fields.ivr_time);

		SetFieldHasUpdate(Fields.ivr_time, this._ivr_time, value);
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

	/** tbl_api_order_id */
	public int get_api_order_id() {
		return this._api_order_id;
	}

	/** tbl_api_order_id */
	@SuppressWarnings("unused")
	public void set_api_order_id(int value) {
		RemoveNullFlag(Fields.api_order_id);
		SetFieldHasUpdate(Fields.api_order_id, this._api_order_id, value);
		this._api_order_id = value;
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
		/** 通道ID */
		public final static String trone_id = "trone_id";
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
		/** 只作存储，真正计费采用通道费用，生成虚拟指令时需要使用 */
		public final static String price = "price";
		/** IP */
		public final static String ip = "ip";
		/** 下行状态码 */
		public final static String status = "status";
		/** 同步给CP标识，默认1为需要同步，0为不需要同步，这个数据是读取表tbl_hold_config进行分析的 */
		public final static String syn_flag = "syn_flag";
		/** mo表名 */
		public final static String mo_table = "mo_table";
		/** mo表对应的自增长 */
		public final static String mo_id = "mo_id";
		/** 下行日期 */
		public final static String mr_date = "mr_date";
		/** 入库时间 */
		public final static String create_date = "create_date";
		/** 通道是否匹配上 */
		public final static String IsMatch = "IsMatch";
		/** tbl_sp_api_url的键，表示从那个API通道过来的数据 */
		public final static String sp_api_url_id = "sp_api_url_id";

		public final static String sp_id = "sp_id";

		public final static String cp_id = "cp_id";
		/** IVR的分钟数 */
		public final static String ivr_time = "ivr_time";
		/** 0为默认通道，1为包月,2为IVR */
		public final static String trone_type = "trone_type";
		/** tbl_api_order_id */
		public final static String api_order_id = "api_order_id";

	}

}
