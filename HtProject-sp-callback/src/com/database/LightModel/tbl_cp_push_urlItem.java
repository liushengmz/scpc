package com.database.LightModel;

import java.util.*;

import com.database.Interface.IHold_DataItem;

import java.math.BigDecimal;

public class tbl_cp_push_urlItem extends com.database.Logical.LightDataModel implements IHold_DataItem {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_cp_push_url";

	/** 主键值 */
	private int _id;

	private String _name;
	/** 指令引用数 */
	private int _ref_count;
	/** CP接收指令的URL */
	private String _url;
	/** cp表主键 */
	private int _cp_id;
	/** 扣量比量(0~99)百分比 */
	private int _hold_percent;
	/** 当日最大同步金额 */
	private BigDecimal _hold_amount;

	private Date _lastDate;
	/** 当日已经同步金额 */
	private BigDecimal _amount;
	/** 起扣条数 */
	private int _hold_start;
	/** 是否实时同步 */
	private boolean _is_realtime;
	/** 扣量周期，已经处理多少条数据 */
	private int _hold_CycCount;
	/** 扣量周期，已经扣除量 */
	private int _hold_CycProc;
	/** 已经推送条数 */
	private int _push_count;

	@Override
	public String IdentifyField() {
		return identifyField;
	}

	@Override
	public String TableName() {
		return tableName;
	}

	@Override
	protected String[] GetNullableFields() {
		return new String[] { null, "name", "ref_count", "url", "cp_id", "hold_percent", "hold_amount", "lastDate",
				"amount", "hold_start", "is_realtime", "hold_CycCount", "hold_CycProc", "push_count" };
	}

	public Boolean Is_nameNull() {
		return IsNull(Fields.name);
	}

	public void Set_nameNull() {
		SetNull(Fields.name);
	}

	public Boolean Is_ref_countNull() {
		return IsNull(Fields.ref_count);
	}

	public void Set_ref_countNull() {
		SetNull(Fields.ref_count);
	}

	public Boolean Is_urlNull() {
		return IsNull(Fields.url);
	}

	public void Set_urlNull() {
		SetNull(Fields.url);
	}

	public Boolean Is_cp_idNull() {
		return IsNull(Fields.cp_id);
	}

	public void Set_cp_idNull() {
		SetNull(Fields.cp_id);
	}

	public Boolean Is_hold_percentNull() {
		return IsNull(Fields.hold_percent);
	}

	public void Set_hold_percentNull() {
		SetNull(Fields.hold_percent);
	}

	public Boolean Is_hold_amountNull() {
		return IsNull(Fields.hold_amount);
	}

	public void Set_hold_amountNull() {
		SetNull(Fields.hold_amount);
	}

	public Boolean Is_lastDateNull() {
		return IsNull(Fields.lastDate);
	}

	public void Set_lastDateNull() {
		SetNull(Fields.lastDate);
	}

	public Boolean Is_amountNull() {
		return IsNull(Fields.amount);
	}

	public void Set_amountNull() {
		SetNull(Fields.amount);
	}

	public Boolean Is_hold_startNull() {
		return IsNull(Fields.hold_start);
	}

	public void Set_hold_startNull() {
		SetNull(Fields.hold_start);
	}

	public Boolean Is_is_realtimeNull() {
		return IsNull(Fields.is_realtime);
	}

	public void Set_is_realtimeNull() {
		SetNull(Fields.is_realtime);
	}

	public Boolean Is_hold_CycCountNull() {
		return IsNull(Fields.hold_CycCount);
	}

	public void Set_hold_CycCountNull() {
		SetNull(Fields.hold_CycCount);
	}

	public Boolean Is_hold_CycProcNull() {
		return IsNull(Fields.hold_CycProc);
	}

	public void Set_hold_CycProcNull() {
		SetNull(Fields.hold_CycProc);
	}

	public Boolean Is_push_countNull() {
		return IsNull(Fields.push_count);
	}

	public void Set_push_countNull() {
		SetNull(Fields.push_count);
	}

	public int get_id() {
		return this._id;
	}

	public void set_id(int value) {
		this._id = value;
	}

	public String get_name() {
		return this._name;
	}

	@SuppressWarnings("unused")
	public void set_name(String value) {
		if (false && true)
			RemoveNullFlag(Fields.name);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.name);
			else
				RemoveNullFlag(Fields.name);
		}

		SetFieldHasUpdate(Fields.name, this._name, value);
		this._name = value;
	}

	/** 指令引用数 */
	public int get_ref_count() {
		return this._ref_count;
	}

	/** 指令引用数 */
	@SuppressWarnings("unused")
	public void set_ref_count(int value) {
		RemoveNullFlag(Fields.ref_count);
		SetFieldHasUpdate(Fields.ref_count, this._ref_count, value);
		this._ref_count = value;
	}

	/** CP接收指令的URL */
	public String get_url() {
		return this._url;
	}

	/** CP接收指令的URL */
	@SuppressWarnings("unused")
	public void set_url(String value) {
		if (false && true)
			RemoveNullFlag(Fields.url);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.url);
			else
				RemoveNullFlag(Fields.url);
		}

		SetFieldHasUpdate(Fields.url, this._url, value);
		this._url = value;
	}

	/** cp表主键 */
	public int get_cp_id() {
		return this._cp_id;
	}

	/** cp表主键 */
	@SuppressWarnings("unused")
	public void set_cp_id(int value) {
		RemoveNullFlag(Fields.cp_id);
		SetFieldHasUpdate(Fields.cp_id, this._cp_id, value);
		this._cp_id = value;
	}

	/** 扣量比量(0~99)百分比 */
	public int get_hold_percent() {
		return this._hold_percent;
	}

	/** 扣量比量(0~99)百分比 */
	@SuppressWarnings("unused")
	public void set_hold_percent(int value) {
		RemoveNullFlag(Fields.hold_percent);
		SetFieldHasUpdate(Fields.hold_percent, this._hold_percent, value);
		this._hold_percent = value;
	}

	/** 当日最大同步金额 */
	public BigDecimal get_hold_amount() {
		return this._hold_amount;
	}

	/** 当日最大同步金额 */
	@SuppressWarnings("unused")
	public void set_hold_amount(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.hold_amount);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.hold_amount);
			else
				RemoveNullFlag(Fields.hold_amount);
		}

		SetFieldHasUpdate(Fields.hold_amount, this._hold_amount, value);
		this._hold_amount = value;
	}

	public Date get_lastDate() {
		return this._lastDate;
	}

	@SuppressWarnings("unused")
	public void set_lastDate(Date value) {
		if (false && true)
			RemoveNullFlag(Fields.lastDate);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.lastDate);
			else
				RemoveNullFlag(Fields.lastDate);
		}

		SetFieldHasUpdate(Fields.lastDate, this._lastDate, value);
		this._lastDate = value;
	}

	/** 当日已经同步金额 */
	public BigDecimal get_amount() {
		return this._amount;
	}

	/** 当日已经同步金额 */
	@SuppressWarnings("unused")
	public void set_amount(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.amount);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.amount);
			else
				RemoveNullFlag(Fields.amount);
		}

		SetFieldHasUpdate(Fields.amount, this._amount, value);
		this._amount = value;
	}

	/** 起扣条数 */
	public int get_hold_start() {
		return this._hold_start;
	}

	/** 起扣条数 */
	@SuppressWarnings("unused")
	public void set_hold_start(int value) {
		RemoveNullFlag(Fields.hold_start);
		this._hold_start = value;
	}

	/** 是否实时同步 */
	public boolean get_is_realtime() {
		return this._is_realtime;
	}

	/** 是否实时同步 */
	@SuppressWarnings("unused")
	public void set_is_realtime(boolean value) {
		RemoveNullFlag(Fields.is_realtime);
		SetFieldHasUpdate(Fields.is_realtime, this._is_realtime, value);
		this._is_realtime = value;
	}

	/** 扣量周期，已经处理多少条数据 */
	public int get_hold_CycCount() {
		return this._hold_CycCount;
	}

	/** 扣量周期，已经处理多少条数据 */
	@SuppressWarnings("unused")
	public void set_hold_CycCount(int value) {
		RemoveNullFlag(Fields.hold_CycCount);
		SetFieldHasUpdate(Fields.hold_CycCount, this._hold_CycCount, value);
		this._hold_CycCount = value;
	}

	/** 扣量周期，已经扣除量 */
	public int get_hold_CycProc() {
		return this._hold_CycProc;
	}

	/** 扣量周期，已经扣除量 */
	@SuppressWarnings("unused")
	public void set_hold_CycProc(int value) {
		RemoveNullFlag(Fields.hold_CycProc);
		SetFieldHasUpdate(Fields.hold_CycProc, this._hold_CycProc, value);
		this._hold_CycProc = value;
	}

	/** 已经推送条数 */
	public int get_push_count() {
		return this._push_count;
	}

	/** 已经推送条数 */
	@SuppressWarnings("unused")
	public void set_push_count(int value) {
		RemoveNullFlag(Fields.push_count);
		SetFieldHasUpdate(Fields.push_count, this._push_count, value);
		this._push_count = value;
	}

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		public final static String name = "name";
		/** 指令引用数 */
		public final static String ref_count = "ref_count";
		/** CP接收指令的URL */
		public final static String url = "url";
		/** cp表主键 */
		public final static String cp_id = "cp_id";
		/** 扣量比量(0~99)百分比 */
		public final static String hold_percent = "hold_percent";
		/** 当日最大同步金额 */
		public final static String hold_amount = "hold_amount";

		public final static String lastDate = "lastDate";
		/** 当日已经同步金额 */
		public final static String amount = "amount";
		/** 起扣条数 */
		public final static String hold_start = "hold_start";
		/** 是否实时同步 */
		public final static String is_realtime = "is_realtime";
		/** 扣量周期，已经处理多少条数据 */
		public final static String hold_CycCount = "hold_CycCount";
		/** 扣量周期，已经扣除量 */
		public final static String hold_CycProc = "hold_CycProc";
		/** 已经推送条数 */
		public final static String push_count = "push_count";

	}

}
