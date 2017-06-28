package com.database.LightModel;

import java.util.*;
import java.math.BigDecimal;

public class tbl_troneItem extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_trone";

	/** 主键值 */
	private int _id;

	/** tbl_sp表主键 */
	private int _sp_id;
	/** tbl_sp_api_url主键 */
	private int _sp_api_url_id;
	/** 通道号 */
	private String _trone_num;
	/** 指令集 */
	private String _orders;
	/** tbl_sp_trone主键 */
	private int _sp_trone_id;
	/** 通道名称 */
	private String _trone_name;

	private int _currency_id;
	/** 价格(元) */
	private BigDecimal _price;
	/** 状态，1为正常，0为停用 */
	private int _status;
	/** 创建时间 */
	private Date _create_date;
	/** 是否模糊指令，即是否允许使用通匹符进行配对 */
	private boolean _is_dynamic;
	/** 不规则指令，直接匹配价格 */
	private boolean _match_price;

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
		return new String[] { null, "sp_trone_id", "trone_name", "currency_id", "price", "status", "create_date",
				"is_dynamic" };
	}

	public Boolean Is_sp_trone_idNull() {
		return IsNull(Fields.sp_trone_id);
	}

	public void Set_sp_trone_idNull() {
		SetNull(Fields.sp_trone_id);
	}

	public Boolean Is_trone_nameNull() {
		return IsNull(Fields.trone_name);
	}

	public void Set_trone_nameNull() {
		SetNull(Fields.trone_name);
	}

	public Boolean Is_currency_idNull() {
		return IsNull(Fields.currency_id);
	}

	public void Set_currency_idNull() {
		SetNull(Fields.currency_id);
	}

	public Boolean Is_priceNull() {
		return IsNull(Fields.price);
	}

	public void Set_priceNull() {
		SetNull(Fields.price);
	}

	public Boolean Is_statusNull() {
		return IsNull(Fields.status);
	}

	public void Set_statusNull() {
		SetNull(Fields.status);
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

	public int get_id() {
		return this._id;
	}

	public void set_id(int value) {
		this._id = value;
	}

	/** tbl_sp表主键 */
	public int get_sp_id() {
		return this._sp_id;
	}

	/** tbl_sp表主键 */
	@SuppressWarnings("unused")
	public void set_sp_id(int value) {
		RemoveNullFlag(Fields.sp_id);
		SetFieldHasUpdate(Fields.sp_id, this._sp_id, value);
		this._sp_id = value;
	}

	/** tbl_sp_api_url主键 */
	public int get_sp_api_url_id() {
		return this._sp_api_url_id;
	}

	/** tbl_sp_api_url主键 */
	@SuppressWarnings("unused")
	public void set_sp_api_url_id(int value) {
		RemoveNullFlag(Fields.sp_api_url_id);
		SetFieldHasUpdate(Fields.sp_api_url_id, this._sp_api_url_id, value);
		this._sp_api_url_id = value;
	}

	/** 通道号 */
	public String get_trone_num() {
		return this._trone_num;
	}

	/** 通道号 */
	@SuppressWarnings("unused")
	public void set_trone_num(String value) {
		if (false && false)
			RemoveNullFlag(Fields.trone_num);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.trone_num);
			else
				RemoveNullFlag(Fields.trone_num);
		}

		SetFieldHasUpdate(Fields.trone_num, this._trone_num, value);
		this._trone_num = value;
	}

	/** 指令集 */
	public String get_orders() {
		return this._orders;
	}

	/** 指令集 */
	@SuppressWarnings("unused")
	public void set_orders(String value) {
		if (false && false)
			RemoveNullFlag(Fields.orders);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.orders);
			else
				RemoveNullFlag(Fields.orders);
		}

		SetFieldHasUpdate(Fields.orders, this._orders, value);
		this._orders = value;
	}

	/** tbl_sp_trone主键 */
	public int get_sp_trone_id() {
		return this._sp_trone_id;
	}

	/** tbl_sp_trone主键 */
	@SuppressWarnings("unused")
	public void set_sp_trone_id(int value) {
		RemoveNullFlag(Fields.sp_trone_id);
		SetFieldHasUpdate(Fields.sp_trone_id, this._sp_trone_id, value);
		this._sp_trone_id = value;
	}

	/** 通道名称 */
	public String get_trone_name() {
		return this._trone_name;
	}

	/** 通道名称 */
	@SuppressWarnings("unused")
	public void set_trone_name(String value) {
		if (false && true)
			RemoveNullFlag(Fields.trone_name);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.trone_name);
			else
				RemoveNullFlag(Fields.trone_name);
		}

		SetFieldHasUpdate(Fields.trone_name, this._trone_name, value);
		this._trone_name = value;
	}

	public int get_currency_id() {
		return this._currency_id;
	}

	@SuppressWarnings("unused")
	public void set_currency_id(int value) {
		RemoveNullFlag(Fields.currency_id);
		SetFieldHasUpdate(Fields.currency_id, this._currency_id, value);
		this._currency_id = value;
	}

	/** 价格(元) */
	public BigDecimal get_price() {
		return this._price;
	}

	/** 价格(元) */
	@SuppressWarnings("unused")
	public void set_price(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.price);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.price);
			else
				RemoveNullFlag(Fields.price);
		}

		SetFieldHasUpdate(Fields.price, this._price, value);
		this._price = value;
	}

	/** 状态，1为正常，0为停用 */
	public int get_status() {
		return this._status;
	}

	/** 状态，1为正常，0为停用 */
	@SuppressWarnings("unused")
	public void set_status(int value) {
		RemoveNullFlag(Fields.status);
		SetFieldHasUpdate(Fields.status, this._status, value);
		this._status = value;
	}

	/** 创建时间 */
	public Date get_create_date() {
		return this._create_date;
	}

	/** 创建时间 */
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

	/** 不规则指令，直接匹配价格 */
	public boolean get_match_price() {
		return this._match_price;
	}

	/** 不规则指令，直接匹配价格 */
	@SuppressWarnings("unused")
	public void set_match_price(boolean value) {
		RemoveNullFlag(Fields.match_price);
		SetFieldHasUpdate(Fields.match_price, this._match_price, value);
		this._match_price = value;
	}

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** tbl_sp表主键 */
		public final static String sp_id = "sp_id";
		/** tbl_sp_api_url主键 */
		public final static String sp_api_url_id = "sp_api_url_id";
		/** 通道号 */
		public final static String trone_num = "trone_num";
		/** 指令集 */
		public final static String orders = "orders";
		/** tbl_sp_trone主键 */
		public final static String sp_trone_id = "sp_trone_id";
		/** 通道名称 */
		public final static String trone_name = "trone_name";

		public final static String currency_id = "currency_id";
		/** 价格(元) */
		public final static String price = "price";
		/** 状态，1为正常，0为停用 */
		public final static String status = "status";
		/** 创建时间 */
		public final static String create_date = "create_date";
		/** 是否模糊指令，即是否允许使用通匹符进行配对 */
		public final static String is_dynamic = "is_dynamic";
		/** 不规则指令，直接匹配价格 */
		public final static String match_price = "match_price";

	}

}
