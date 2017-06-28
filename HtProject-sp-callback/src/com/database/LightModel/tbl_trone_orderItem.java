package com.database.LightModel;

import java.util.*;

import com.database.Interface.IHold_DataItem;

import java.math.BigDecimal;

public class tbl_trone_orderItem extends com.database.Logical.LightDataModel implements IHold_DataItem {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_trone_order";

	/** 主键值 */
	private int _id;

	/** 通道表id */
	private int _trone_id;
	/** 指令 */
	private String _order_num;
	/** cp表主键 */
	private int _cp_id;
	/** 指令通道名称（和CP业务挂钩） */
	private String _order_trone_name;

	private Date _create_date;
	/** 是否模糊指令，即是否允许使用通匹符进行配对 */
	private boolean _is_dynamic;
	/** 使用那个同步URL进行同步(tbl_cp_push_url.id) */
	private int _push_url_id;
	/** 指令此条配置是否有效 */
	private boolean _disable;
	/** 未知CP的数据，1为无主孤儿，0是有主的 */
	private boolean _is_unknow;
	/** -1表示同步URL扣量设置，0~99表示扣量百分比 */
	private int _hold_percent;
	/** 最多同步金额,-1表示按同步URL扣除设置 */
	private BigDecimal _hold_amount;
	/** 已经同步金额 */
	private BigDecimal _amount;
	/** 扣量周期，已经处理多少条数据 */
	private int _hold_CycCount;
	/** 扣量周期，已经扣除量 */
	private int _hold_CycProc;
	/** 已经推送条数 */
	private int _push_count;
	/** 是否采用同步URL的扣量设置,0:是;1:否 */
	private boolean _hold_is_Custom;

	private int _hold_start;

	private Date _lastDate;

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
		return new String[] { null, "order_num", "cp_id", "order_trone_name", "create_date", "is_dynamic",
				"push_url_id", "disable", "is_unknow", "hold_CycCount", "hold_CycProc", "push_count", "hold_is_Custom",
				"hold_start", "lastDate" };
	}

	public Boolean Is_order_numNull() {
		return IsNull(Fields.order_num);
	}

	public void Set_order_numNull() {
		SetNull(Fields.order_num);
	}

	public Boolean Is_cp_idNull() {
		return IsNull(Fields.cp_id);
	}

	public void Set_cp_idNull() {
		SetNull(Fields.cp_id);
	}

	public Boolean Is_order_trone_nameNull() {
		return IsNull(Fields.order_trone_name);
	}

	public void Set_order_trone_nameNull() {
		SetNull(Fields.order_trone_name);
	}

	public Boolean Is_create_dateNull() {
		return IsNull(Fields.create_date);
	}

	public void Set_create_dateNull() {
		SetNull(Fields.create_date);
	}

	public Boolean Is_is_dynamicNull() {
		return IsNull(Fields.is_dynamic);
	}

	public void Set_is_dynamicNull() {
		SetNull(Fields.is_dynamic);
	}

	public Boolean Is_push_url_idNull() {
		return IsNull(Fields.push_url_id);
	}

	public void Set_push_url_idNull() {
		SetNull(Fields.push_url_id);
	}

	public Boolean Is_disableNull() {
		return IsNull(Fields.disable);
	}

	public void Set_disableNull() {
		SetNull(Fields.disable);
	}

	public Boolean Is_is_unknowNull() {
		return IsNull(Fields.is_unknow);
	}

	public void Set_is_unknowNull() {
		SetNull(Fields.is_unknow);
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

	public Boolean Is_hold_is_CustomNull() {
		return IsNull(Fields.hold_is_Custom);
	}

	public void Set_hold_is_CustomNull() {
		SetNull(Fields.hold_is_Custom);
	}

	public Boolean Is_hold_startNull() {
		return IsNull(Fields.hold_start);
	}

	public void Set_hold_startNull() {
		SetNull(Fields.hold_start);
	}

	public Boolean Is_lastDateNull() {
		return IsNull(Fields.lastDate);
	}

	public void Set_lastDateNull() {
		SetNull(Fields.lastDate);
	}

	public int get_id() {
		return this._id;
	}

	public void set_id(int value) {
		this._id = value;
	}

	/** 通道表id */
	public int get_trone_id() {
		return this._trone_id;
	}

	/** 通道表id */
	@SuppressWarnings("unused")
	public void set_trone_id(int value) {
		RemoveNullFlag(Fields.trone_id);
		SetFieldHasUpdate(Fields.trone_id, this._trone_id, value);
		this._trone_id = value;
	}

	/** 指令 */
	public String get_order_num() {
		return this._order_num;
	}

	/** 指令 */
	@SuppressWarnings("unused")
	public void set_order_num(String value) {
		RemoveNullFlag(Fields.order_num);
		SetFieldHasUpdate(Fields.order_num, this._order_num, value);
		this._order_num = value;
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

	/** 指令通道名称（和CP业务挂钩） */
	public String get_order_trone_name() {
		return this._order_trone_name;
	}

	/** 指令通道名称（和CP业务挂钩） */
	@SuppressWarnings("unused")
	public void set_order_trone_name(String value) {
		if (false && true)
			RemoveNullFlag(Fields.order_trone_name);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.order_trone_name);
			else
				RemoveNullFlag(Fields.order_trone_name);
		}

		SetFieldHasUpdate(Fields.order_trone_name, this._order_trone_name, value);
		this._order_trone_name = value;
	}

	public Date get_create_date() {
		return this._create_date;
	}

	@SuppressWarnings("unused")
	public void set_create_date(Date value) {
		if (false && true)
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

	/** 是否模糊指令，即是否允许使用通匹符进行配对 */
	public boolean get_is_dynamic() {
		return this._is_dynamic;
	}

	/** 是否模糊指令，即是否允许使用通匹符进行配对 */
	@SuppressWarnings("unused")
	public void set_is_dynamic(boolean value) {
		RemoveNullFlag(Fields.is_dynamic);
		SetFieldHasUpdate(Fields.is_dynamic, this._is_dynamic, value);
		this._is_dynamic = value;
	}

	/** 使用那个同步URL进行同步(tbl_cp_push_url.id) */
	public int get_push_url_id() {
		return this._push_url_id;
	}

	/** 使用那个同步URL进行同步(tbl_cp_push_url.id) */
	@SuppressWarnings("unused")
	public void set_push_url_id(int value) {
		RemoveNullFlag(Fields.push_url_id);
		SetFieldHasUpdate(Fields.push_url_id, this._push_url_id, value);
		this._push_url_id = value;
	}

	/** 指令此条配置是否有效 */
	public boolean get_disable() {
		return this._disable;
	}

	/** 指令此条配置是否有效 */
	@SuppressWarnings("unused")
	public void set_disable(boolean value) {
		RemoveNullFlag(Fields.disable);
		SetFieldHasUpdate(Fields.disable, this._disable, value);
		this._disable = value;
	}

	/** 未知CP的数据，1为无主孤儿，0是有主的 */
	public boolean get_is_unknow() {
		return this._is_unknow;
	}

	/** 未知CP的数据，1为无主孤儿，0是有主的 */
	@SuppressWarnings("unused")
	public void set_is_unknow(boolean value) {
		RemoveNullFlag(Fields.is_unknow);
		SetFieldHasUpdate(Fields.is_unknow, this._is_unknow, value);
		this._is_unknow = value;
	}

	/** -1表示同步URL扣量设置，0~99表示扣量百分比 */
	public int get_hold_percent() {
		return this._hold_percent;
	}

	/** -1表示同步URL扣量设置，0~99表示扣量百分比 */
	@SuppressWarnings("unused")
	public void set_hold_percent(int value) {
		RemoveNullFlag(Fields.hold_percent);
		SetFieldHasUpdate(Fields.hold_percent, this._hold_percent, value);
		this._hold_percent = value;
	}

	/** 最多同步金额,-1表示按同步URL扣除设置 */
	public BigDecimal get_hold_amount() {
		return this._hold_amount;
	}

	/** 最多同步金额,-1表示按同步URL扣除设置 */
	@SuppressWarnings("unused")
	public void set_hold_amount(BigDecimal value) {
		if (true && false)
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

	/** 已经同步金额 */
	public BigDecimal get_amount() {
		return this._amount;
	}

	/** 已经同步金额 */
	@SuppressWarnings("unused")
	public void set_amount(BigDecimal value) {
		if (true && false)
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

	/** 是否采用同步URL的扣量设置,0:是;1:否 */
	public boolean get_hold_is_Custom() {
		return this._hold_is_Custom;
	}

	/** 是否采用同步URL的扣量设置,0:是;1:否 */
	@SuppressWarnings("unused")
	public void set_hold_is_Custom(boolean value) {
		RemoveNullFlag(Fields.hold_is_Custom);
		SetFieldHasUpdate(Fields.hold_is_Custom, this._hold_is_Custom, value);
		this._hold_is_Custom = value;
	}

	public int get_hold_start() {
		return this._hold_start;
	}

	@SuppressWarnings("unused")
	public void set_hold_start(int value) {
		RemoveNullFlag(Fields.hold_start);
		SetFieldHasUpdate(Fields.hold_start, this._hold_start, value);
		this._hold_start = value;
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

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** 通道表id */
		public final static String trone_id = "trone_id";
		/** 指令 */
		public final static String order_num = "order_num";
		/** cp表主键 */
		public final static String cp_id = "cp_id";
		/** 指令通道名称（和CP业务挂钩） */
		public final static String order_trone_name = "order_trone_name";

		public final static String create_date = "create_date";
		/** 是否模糊指令，即是否允许使用通匹符进行配对 */
		public final static String is_dynamic = "is_dynamic";
		/** 使用那个同步URL进行同步(tbl_cp_push_url.id) */
		public final static String push_url_id = "push_url_id";
		/** 指令此条配置是否有效 */
		public final static String disable = "disable";
		/** 未知CP的数据，1为无主孤儿，0是有主的 */
		public final static String is_unknow = "is_unknow";
		/** -1表示同步URL扣量设置，0~99表示扣量百分比 */
		public final static String hold_percent = "hold_percent";
		/** 最多同步金额,-1表示按同步URL扣除设置 */
		public final static String hold_amount = "hold_amount";
		/** 已经同步金额 */
		public final static String amount = "amount";
		/** 扣量周期，已经处理多少条数据 */
		public final static String hold_CycCount = "hold_CycCount";
		/** 扣量周期，已经扣除量 */
		public final static String hold_CycProc = "hold_CycProc";
		/** 已经推送条数 */
		public final static String push_count = "push_count";
		/** 是否采用同步URL的扣量设置,0:是;1:否 */
		public final static String hold_is_Custom = "hold_is_Custom";

		public final static String hold_start = "hold_start";

		public final static String lastDate = "lastDate";

	}

}
