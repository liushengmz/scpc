package com.dianfu.logical.model;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.database.Interface.ISMS_DataItem;
import com.database.LightModel.tbl_moItem;
import com.database.LightModel.tbl_mrItem;
import com.shotgun.Tools.Funcs;

/** 表示接到的SP回调数据 */
public class SPDataModel {
	private String _imsi, _imei, _mobile, _port, _msg, _status, _cpparam, _linkid;
	private Integer _price, _ivrTime;
	private String _mcc, _serviceCode;
	DataType _type;

	enum fuck_switch {
		imsi, imei, mmc, mobile, status, cpparam, cp_param, servicecode, service_code, port, ori_trone, msg, ori_order, linkid, ivr_time, price

	}

	public String get_mcc() {
		return _mcc;
	}

	public void set_mcc(String _mcc) {
		this._mcc = _mcc;
	}

	/** HttpRequest对像 */
	public final HttpServletRequest Request;

	public SPDataModel(HttpServletRequest request) {
		Request = request;
	}

	public enum DataType {
		MO, MR
	}

	public boolean put(String key, String value) {
		// System.out.println("key " + key + "=" + value);
		fuck_switch fw = fuck_switch.valueOf(key.toLowerCase());

		switch (fw) {
		case imsi:
			_imsi = value;
			return true;
		case imei:
			_imei = value;
			return true;
		case mmc:
			_mcc = value;
			return true;
		case mobile:
			_mobile = value;
			return true;
		case status:
			_status = value;
			return true;
		case cpparam:
		case cp_param:
			_cpparam = value;
			return true;
		case servicecode:
		case service_code:
			set_serviceCode(value);
			return true;
		case port:
		case ori_trone:
			_port = value;
			return true;
		case msg:
		case ori_order:
			_msg = value;
			return true;
		case linkid:
			_linkid = value;
			return true;
		case ivr_time:
			_ivrTime = Funcs.parseInt(value, null);
			return true;
		case price:
			_price = Funcs.parseInt(value, null);
			return true;
		default:
			return false;
		}
	}

	public String get_serviceCode() {
		return _serviceCode;
	}

	public void set_serviceCode(String _serviceCode) {
		this._serviceCode = _serviceCode;
	}

	public String getImsi() {
		return _imsi;
	}

	/* SP回调的数据类别,MO 或 MR */
	public DataType getDataType() {
		return _type;
	}

	/* SP回调的数据类别,MO 或 MR */
	public void setDataType(DataType _type) {
		this._type = _type;
	}

	public void setImsi(String value) {
		_imsi = value;
	}

	public String getImei() {
		return _imei;
	}

	public void setImei(String value) {
		_imei = value;
	}

	public String getMobile() {
		return _mobile;
	}

	public void setMobile(String value) {
		_mobile = value;
	}

	/* 端口 */
	public String getPort() {
		return _port;
	}

	/* 端口 */
	public void setPort(String value) {
		_port = value;
	}

	/* 指令 */
	public String getMsg() {
		return _msg;
	}

	/* 指令 */
	public void setMsg(String value) {
		_msg = value;
	}

	/* 状态值,原值传入 */
	public String getStatus() {
		return _status;
	}

	/* 状态值,原值传入 */
	public void setStatus(String value) {
		_status = value;
	}

	public String getCpparam() {
		return _cpparam;
	}

	public void setCpparam(String value) {
		_cpparam = value;
	}

	/* SP传入的直接价格(单位:分),如果没有传入价格,可忽略 */
	public int getPrice() {
		return _price;
	}

	/* SP传入的直接价格(单位:分),如果没有传入价格,可忽略 */
	public void setPrice(int value) {
		_price = value;
	}

	/* IVR 时间长度,单位:分钟 */
	public int getIvrTime() {
		return _ivrTime;
	}

	/* IVR 时间长度,单位:分钟 */
	public void setIvrTime(int _ivrTime) {
		this._ivrTime = _ivrTime;
	}

	public String getLinkid() {
		return _linkid;
	}

	public void setLinkid(String _linkid) {
		this._linkid = _linkid;
	}

	/** 转化为数据库Model(MO或MR) */
	public ISMS_DataItem toDataItem() {
		ISMS_DataItem item = null;

		if (_type == DataType.MO) {
			tbl_moItem mo = new tbl_moItem();
			mo.set_create_date(new Timestamp(System.currentTimeMillis()));
			item = mo;
		} else {
			tbl_mrItem mr = new tbl_mrItem();
			if (_status != null)
				mr.set_status(_status);
			if (_ivrTime != null)
				mr.set_ivr_time(_ivrTime);
			mr.set_create_date(new Timestamp(System.currentTimeMillis()));
			item = mr;
		}
		if (_mcc != null)
			item.set_mcc(_mcc);
		if (_linkid != null)
			item.set_linkid(_linkid);
		if (_serviceCode != null)
			item.set_service_code(_serviceCode);
		if (_imsi != null)
			item.set_imsi(_imsi);
		if (_imei != null)
			item.set_imei(_imei);
		if (_mobile != null)
			item.set_mobile(_mobile);
		if (_port != null)
			item.set_ori_trone(_port);
		if (_msg != null)
			item.set_ori_order(_msg);
		if (_cpparam != null)
			item.set_cp_param(_cpparam);
		if (_price != null)
			item.set_price(_price);

		return item;

	}
}
