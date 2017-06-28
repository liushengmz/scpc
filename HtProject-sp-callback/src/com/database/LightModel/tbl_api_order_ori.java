package com.database.LightModel;

import java.util.*;
import java.math.BigDecimal;

public abstract class tbl_api_order_ori extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";

	/** 主键值 */
	private int _id;

	/** trone_id */
	private int _trone_id;
	/** trone_order_id */
	private int _trone_order_id;

	private int _api_id;
	/** IMSI */
	private String _imsi;
	/** IMEI */
	private String _imei;
	/** 手机号 */
	private String _mobile;
	/** 区域码 */
	private int _lac;
	/** 基站编号 */
	private int _cid;
	/** CP透参 */
	private String _ExtrData;

	private String _sdkversion;

	private String _packagename;

	private String _ip;

	private String _clientip;

	private String _nettype;
	/** sp产生的订单号，通常在二次http或回传时匹配使用 */
	private String _sp_linkid;
	/** 存储除了LINKID之外的信息 */
	private String _sp_exField;
	/** 验证码 */
	private String _cp_verifyCode;
	/** 首次请求的时间 */
	private Date _FirstDate;
	/** 二次请求时间 */
	private Date _SecondDate;
	/** 一次指令，上行端口 */
	private String _port;
	/** 一次指令，上行指令 */
	private String _msg;
	/** 此会传递给SP，并在SP计费成功时回传。用于api回传匹配使用透传参数 */
	private String _api_exdata;
	/** 后面再确认 */
	private int _status;
	/** 额外的参数，防止SP的变态数据要求 */
	private String _extra_param;
	/** 是否直接扣量 */
	private String _is_hidden;

	@Override
	public String IdentifyField() {
		return identifyField;
	}

	@Override
	protected String[] GetNullableFields() {
		return new String[] { null, "api_id", "imsi", "imei", "mobile", "lac", "cid", "ExtrData", "sdkversion",
				"packagename", "ip", "clientip", "nettype", "sp_linkid", "sp_exField", "cp_verifyCode", "FirstDate",
				"SecondDate", "port", "msg", "api_exdata", "status", "extra_param", "is_hidden" };
	}

	public Boolean Is_api_idNull() {
		return IsNull(Fields.api_id);
	}

	public void Set_api_idNull() {
		SetNull(Fields.api_id);
	}

	public Boolean Is_imsiNull() {
		return IsNull(Fields.imsi);
	}

	public void Set_imsiNull() {
		SetNull(Fields.imsi);
	}

	public Boolean Is_imeiNull() {
		return IsNull(Fields.imei);
	}

	public void Set_imeiNull() {
		SetNull(Fields.imei);
	}

	public Boolean Is_mobileNull() {
		return IsNull(Fields.mobile);
	}

	public void Set_mobileNull() {
		SetNull(Fields.mobile);
	}

	public Boolean Is_lacNull() {
		return IsNull(Fields.lac);
	}

	public void Set_lacNull() {
		SetNull(Fields.lac);
	}

	public Boolean Is_cidNull() {
		return IsNull(Fields.cid);
	}

	public void Set_cidNull() {
		SetNull(Fields.cid);
	}

	public Boolean Is_ExtrDataNull() {
		return IsNull(Fields.ExtrData);
	}

	public void Set_ExtrDataNull() {
		SetNull(Fields.ExtrData);
	}

	public Boolean Is_sdkversionNull() {
		return IsNull(Fields.sdkversion);
	}

	public void Set_sdkversionNull() {
		SetNull(Fields.sdkversion);
	}

	public Boolean Is_packagenameNull() {
		return IsNull(Fields.packagename);
	}

	public void Set_packagenameNull() {
		SetNull(Fields.packagename);
	}

	public Boolean Is_ipNull() {
		return IsNull(Fields.ip);
	}

	public void Set_ipNull() {
		SetNull(Fields.ip);
	}

	public Boolean Is_clientipNull() {
		return IsNull(Fields.clientip);
	}

	public void Set_clientipNull() {
		SetNull(Fields.clientip);
	}

	public Boolean Is_nettypeNull() {
		return IsNull(Fields.nettype);
	}

	public void Set_nettypeNull() {
		SetNull(Fields.nettype);
	}

	public Boolean Is_sp_linkidNull() {
		return IsNull(Fields.sp_linkid);
	}

	public void Set_sp_linkidNull() {
		SetNull(Fields.sp_linkid);
	}

	public Boolean Is_sp_exFieldNull() {
		return IsNull(Fields.sp_exField);
	}

	public void Set_sp_exFieldNull() {
		SetNull(Fields.sp_exField);
	}

	public Boolean Is_cp_verifyCodeNull() {
		return IsNull(Fields.cp_verifyCode);
	}

	public void Set_cp_verifyCodeNull() {
		SetNull(Fields.cp_verifyCode);
	}

	public Boolean Is_FirstDateNull() {
		return IsNull(Fields.FirstDate);
	}

	public void Set_FirstDateNull() {
		SetNull(Fields.FirstDate);
	}

	public Boolean Is_SecondDateNull() {
		return IsNull(Fields.SecondDate);
	}

	public void Set_SecondDateNull() {
		SetNull(Fields.SecondDate);
	}

	public Boolean Is_portNull() {
		return IsNull(Fields.port);
	}

	public void Set_portNull() {
		SetNull(Fields.port);
	}

	public Boolean Is_msgNull() {
		return IsNull(Fields.msg);
	}

	public void Set_msgNull() {
		SetNull(Fields.msg);
	}

	public Boolean Is_api_exdataNull() {
		return IsNull(Fields.api_exdata);
	}

	public void Set_api_exdataNull() {
		SetNull(Fields.api_exdata);
	}

	public Boolean Is_statusNull() {
		return IsNull(Fields.status);
	}

	public void Set_statusNull() {
		SetNull(Fields.status);
	}

	public Boolean Is_extra_paramNull() {
		return IsNull(Fields.extra_param);
	}

	public void Set_extra_paramNull() {
		SetNull(Fields.extra_param);
	}

	public Boolean Is_is_hiddenNull() {
		return IsNull(Fields.is_hidden);
	}

	public void Set_is_hiddenNull() {
		SetNull(Fields.is_hidden);
	}

	public int get_id() {
		return this._id;
	}

	public void set_id(int value) {
		this._id = value;
	}

	/** trone_id */
	public int get_trone_id() {
		return this._trone_id;
	}

	/** trone_id */
	@SuppressWarnings("unused")
	public void set_trone_id(int value) {
		RemoveNullFlag(Fields.trone_id);
		SetFieldHasUpdate(Fields.trone_id, this._trone_id, value);
		this._trone_id = value;
	}

	/** trone_order_id */
	public int get_trone_order_id() {
		return this._trone_order_id;
	}

	/** trone_order_id */
	@SuppressWarnings("unused")
	public void set_trone_order_id(int value) {
		RemoveNullFlag(Fields.trone_order_id);
		SetFieldHasUpdate(Fields.trone_order_id, this._trone_order_id, value);
		this._trone_order_id = value;
	}

	public int get_api_id() {
		return this._api_id;
	}

	@SuppressWarnings("unused")
	public void set_api_id(int value) {
		RemoveNullFlag(Fields.api_id);
		SetFieldHasUpdate(Fields.api_id, this._api_id, value);
		this._api_id = value;
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

	/** 手机号 */
	public String get_mobile() {
		return this._mobile;
	}

	/** 手机号 */
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

	/** 区域码 */
	public int get_lac() {
		return this._lac;
	}

	/** 区域码 */
	@SuppressWarnings("unused")
	public void set_lac(int value) {
		RemoveNullFlag(Fields.lac);
		SetFieldHasUpdate(Fields.lac, this._lac, value);
		this._lac = value;
	}

	/** 基站编号 */
	public int get_cid() {
		return this._cid;
	}

	/** 基站编号 */
	@SuppressWarnings("unused")
	public void set_cid(int value) {
		RemoveNullFlag(Fields.cid);
		SetFieldHasUpdate(Fields.cid, this._cid, value);
		this._cid = value;
	}

	/** CP透参 */
	public String get_ExtrData() {
		return this._ExtrData;
	}

	/** CP透参 */
	@SuppressWarnings("unused")
	public void set_ExtrData(String value) {
		if (false && true)
			RemoveNullFlag(Fields.ExtrData);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.ExtrData);
			else
				RemoveNullFlag(Fields.ExtrData);
		}

		SetFieldHasUpdate(Fields.ExtrData, this._ExtrData, value);
		this._ExtrData = value;
	}

	public String get_sdkversion() {
		return this._sdkversion;
	}

	@SuppressWarnings("unused")
	public void set_sdkversion(String value) {
		if (false && true)
			RemoveNullFlag(Fields.sdkversion);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.sdkversion);
			else
				RemoveNullFlag(Fields.sdkversion);
		}

		SetFieldHasUpdate(Fields.sdkversion, this._sdkversion, value);
		this._sdkversion = value;
	}

	public String get_packagename() {
		return this._packagename;
	}

	@SuppressWarnings("unused")
	public void set_packagename(String value) {
		if (false && true)
			RemoveNullFlag(Fields.packagename);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.packagename);
			else
				RemoveNullFlag(Fields.packagename);
		}

		SetFieldHasUpdate(Fields.packagename, this._packagename, value);
		this._packagename = value;
	}

	public String get_ip() {
		return this._ip;
	}

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

	public String get_clientip() {
		return this._clientip;
	}

	@SuppressWarnings("unused")
	public void set_clientip(String value) {
		if (false && true)
			RemoveNullFlag(Fields.clientip);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.clientip);
			else
				RemoveNullFlag(Fields.clientip);
		}

		SetFieldHasUpdate(Fields.clientip, this._clientip, value);
		this._clientip = value;
	}

	public String get_nettype() {
		return this._nettype;
	}

	@SuppressWarnings("unused")
	public void set_nettype(String value) {
		if (false && true)
			RemoveNullFlag(Fields.nettype);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.nettype);
			else
				RemoveNullFlag(Fields.nettype);
		}

		SetFieldHasUpdate(Fields.nettype, this._nettype, value);
		this._nettype = value;
	}

	/** sp产生的订单号，通常在二次http或回传时匹配使用 */
	public String get_sp_linkid() {
		return this._sp_linkid;
	}

	/** sp产生的订单号，通常在二次http或回传时匹配使用 */
	@SuppressWarnings("unused")
	public void set_sp_linkid(String value) {
		if (false && true)
			RemoveNullFlag(Fields.sp_linkid);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.sp_linkid);
			else
				RemoveNullFlag(Fields.sp_linkid);
		}

		SetFieldHasUpdate(Fields.sp_linkid, this._sp_linkid, value);
		this._sp_linkid = value;
	}

	/** 存储除了LINKID之外的信息 */
	public String get_sp_exField() {
		return this._sp_exField;
	}

	/** 存储除了LINKID之外的信息 */
	@SuppressWarnings("unused")
	public void set_sp_exField(String value) {
		if (false && true)
			RemoveNullFlag(Fields.sp_exField);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.sp_exField);
			else
				RemoveNullFlag(Fields.sp_exField);
		}

		SetFieldHasUpdate(Fields.sp_exField, this._sp_exField, value);
		this._sp_exField = value;
	}

	/** 验证码 */
	public String get_cp_verifyCode() {
		return this._cp_verifyCode;
	}

	/** 验证码 */
	@SuppressWarnings("unused")
	public void set_cp_verifyCode(String value) {
		if (false && true)
			RemoveNullFlag(Fields.cp_verifyCode);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.cp_verifyCode);
			else
				RemoveNullFlag(Fields.cp_verifyCode);
		}

		SetFieldHasUpdate(Fields.cp_verifyCode, this._cp_verifyCode, value);
		this._cp_verifyCode = value;
	}

	/** 首次请求的时间 */
	public Date get_FirstDate() {
		return this._FirstDate;
	}

	/** 首次请求的时间 */
	@SuppressWarnings("unused")
	public void set_FirstDate(Date value) {
		if (false && true)
			RemoveNullFlag(Fields.FirstDate);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.FirstDate);
			else
				RemoveNullFlag(Fields.FirstDate);
		}

		SetFieldHasUpdate(Fields.FirstDate, this._FirstDate, value);
		this._FirstDate = value;
	}

	/** 二次请求时间 */
	public Date get_SecondDate() {
		return this._SecondDate;
	}

	/** 二次请求时间 */
	@SuppressWarnings("unused")
	public void set_SecondDate(Date value) {
		if (false && true)
			RemoveNullFlag(Fields.SecondDate);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.SecondDate);
			else
				RemoveNullFlag(Fields.SecondDate);
		}

		SetFieldHasUpdate(Fields.SecondDate, this._SecondDate, value);
		this._SecondDate = value;
	}

	/** 一次指令，上行端口 */
	public String get_port() {
		return this._port;
	}

	/** 一次指令，上行端口 */
	@SuppressWarnings("unused")
	public void set_port(String value) {
		if (false && true)
			RemoveNullFlag(Fields.port);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.port);
			else
				RemoveNullFlag(Fields.port);
		}

		SetFieldHasUpdate(Fields.port, this._port, value);
		this._port = value;
	}

	/** 一次指令，上行指令 */
	public String get_msg() {
		return this._msg;
	}

	/** 一次指令，上行指令 */
	@SuppressWarnings("unused")
	public void set_msg(String value) {
		if (false && true)
			RemoveNullFlag(Fields.msg);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.msg);
			else
				RemoveNullFlag(Fields.msg);
		}

		SetFieldHasUpdate(Fields.msg, this._msg, value);
		this._msg = value;
	}

	/** 此会传递给SP，并在SP计费成功时回传。用于api回传匹配使用透传参数 */
	public String get_api_exdata() {
		return this._api_exdata;
	}

	/** 此会传递给SP，并在SP计费成功时回传。用于api回传匹配使用透传参数 */
	@SuppressWarnings("unused")
	public void set_api_exdata(String value) {
		if (false && true)
			RemoveNullFlag(Fields.api_exdata);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.api_exdata);
			else
				RemoveNullFlag(Fields.api_exdata);
		}

		SetFieldHasUpdate(Fields.api_exdata, this._api_exdata, value);
		this._api_exdata = value;
	}

	/** 后面再确认 */
	public int get_status() {
		return this._status;
	}

	/** 后面再确认 */
	@SuppressWarnings("unused")
	public void set_status(int value) {
		RemoveNullFlag(Fields.status);
		SetFieldHasUpdate(Fields.status, this._status, value);
		this._status = value;
	}

	/** 额外的参数，防止SP的变态数据要求 */
	public String get_extra_param() {
		return this._extra_param;
	}

	/** 额外的参数，防止SP的变态数据要求 */
	@SuppressWarnings("unused")
	public void set_extra_param(String value) {
		if (false && true)
			RemoveNullFlag(Fields.extra_param);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.extra_param);
			else
				RemoveNullFlag(Fields.extra_param);
		}

		SetFieldHasUpdate(Fields.extra_param, this._extra_param, value);
		this._extra_param = value;
	}

	/** 是否直接扣量 */
	public String get_is_hidden() {
		return this._is_hidden;
	}

	/** 是否直接扣量 */
	@SuppressWarnings("unused")
	public void set_is_hidden(String value) {
		if (false && true)
			RemoveNullFlag(Fields.is_hidden);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.is_hidden);
			else
				RemoveNullFlag(Fields.is_hidden);
		}

		SetFieldHasUpdate(Fields.is_hidden, this._is_hidden, value);
		this._is_hidden = value;
	}

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** trone_id */
		public final static String trone_id = "trone_id";
		/** trone_order_id */
		public final static String trone_order_id = "trone_order_id";

		public final static String api_id = "api_id";
		/** IMSI */
		public final static String imsi = "imsi";
		/** IMEI */
		public final static String imei = "imei";
		/** 手机号 */
		public final static String mobile = "mobile";
		/** 区域码 */
		public final static String lac = "lac";
		/** 基站编号 */
		public final static String cid = "cid";
		/** CP透参 */
		public final static String ExtrData = "ExtrData";

		public final static String sdkversion = "sdkversion";

		public final static String packagename = "packagename";

		public final static String ip = "ip";

		public final static String clientip = "clientip";

		public final static String nettype = "nettype";
		/** sp产生的订单号，通常在二次http或回传时匹配使用 */
		public final static String sp_linkid = "sp_linkid";
		/** 存储除了LINKID之外的信息 */
		public final static String sp_exField = "sp_exField";
		/** 验证码 */
		public final static String cp_verifyCode = "cp_verifyCode";
		/** 首次请求的时间 */
		public final static String FirstDate = "FirstDate";
		/** 二次请求时间 */
		public final static String SecondDate = "SecondDate";
		/** 一次指令，上行端口 */
		public final static String port = "port";
		/** 一次指令，上行指令 */
		public final static String msg = "msg";
		/** 此会传递给SP，并在SP计费成功时回传。用于api回传匹配使用透传参数 */
		public final static String api_exdata = "api_exdata";
		/** 后面再确认 */
		public final static String status = "status";
		/** 额外的参数，防止SP的变态数据要求 */
		public final static String extra_param = "extra_param";
		/** 是否直接扣量 */
		public final static String is_hidden = "is_hidden";

	}

}
