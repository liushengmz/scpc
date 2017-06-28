package com.database.LightModel;

import java.util.*;
import java.math.BigDecimal;

public class tbl_sp_trone_apiItem extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_sp_trone_api";

	/** 主键值 */
	private int _id;

	/** 业务通道API名字 */
	private String _name;
	/** 配匹字段，0:linkid,1:msg,2:cpprams */
	private int _match_field;
	/** 匹配关键字，用于去除sp固定的参数，提取有用的匹配关键字 */
	private String _match_keyword;
	/**
	 * API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=
	 * NETTYPE,7=CLIENTIP
	 */
	private String _api_fields;
	/** 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配 */
	private int _locate_match;

	private Date _create_date;
	/** 用户日限 */
	private BigDecimal _user_day_limit;
	/** 用户月限 */
	private BigDecimal _user_month_limit;
	/** 日限 */
	private BigDecimal _day_limit;
	/** 月限 */
	private BigDecimal _month_limit;
	/** 当前日额 */
	private BigDecimal _cur_day_limit;
	/** 当前月额 */
	private BigDecimal _cur_month_limit;

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
		return new String[] { null, "name", "match_field", "match_keyword", "api_fields", "locate_match",
				"user_day_limit", "user_month_limit", "day_limit", "month_limit", "cur_day_limit", "cur_month_limit" };
	}

	public Boolean Is_nameNull() {
		return IsNull(Fields.name);
	}

	public void Set_nameNull() {
		SetNull(Fields.name);
	}

	public Boolean Is_match_fieldNull() {
		return IsNull(Fields.match_field);
	}

	public void Set_match_fieldNull() {
		SetNull(Fields.match_field);
	}

	public Boolean Is_match_keywordNull() {
		return IsNull(Fields.match_keyword);
	}

	public void Set_match_keywordNull() {
		SetNull(Fields.match_keyword);
	}

	public Boolean Is_api_fieldsNull() {
		return IsNull(Fields.api_fields);
	}

	public void Set_api_fieldsNull() {
		SetNull(Fields.api_fields);
	}

	public Boolean Is_locate_matchNull() {
		return IsNull(Fields.locate_match);
	}

	public void Set_locate_matchNull() {
		SetNull(Fields.locate_match);
	}

	public Boolean Is_user_day_limitNull() {
		return IsNull(Fields.user_day_limit);
	}

	public void Set_user_day_limitNull() {
		SetNull(Fields.user_day_limit);
	}

	public Boolean Is_user_month_limitNull() {
		return IsNull(Fields.user_month_limit);
	}

	public void Set_user_month_limitNull() {
		SetNull(Fields.user_month_limit);
	}

	public Boolean Is_day_limitNull() {
		return IsNull(Fields.day_limit);
	}

	public void Set_day_limitNull() {
		SetNull(Fields.day_limit);
	}

	public Boolean Is_month_limitNull() {
		return IsNull(Fields.month_limit);
	}

	public void Set_month_limitNull() {
		SetNull(Fields.month_limit);
	}

	public Boolean Is_cur_day_limitNull() {
		return IsNull(Fields.cur_day_limit);
	}

	public void Set_cur_day_limitNull() {
		SetNull(Fields.cur_day_limit);
	}

	public Boolean Is_cur_month_limitNull() {
		return IsNull(Fields.cur_month_limit);
	}

	public void Set_cur_month_limitNull() {
		SetNull(Fields.cur_month_limit);
	}

	public int get_id() {
		return this._id;
	}

	public void set_id(int value) {
		this._id = value;
	}

	/** 业务通道API名字 */
	public String get_name() {
		return this._name;
	}

	/** 业务通道API名字 */
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

	/** 配匹字段，0:linkid,1:msg,2:cpprams */
	public int get_match_field() {
		return this._match_field;
	}

	/** 配匹字段，0:linkid,1:msg,2:cpprams */
	@SuppressWarnings("unused")
	public void set_match_field(int value) {
		RemoveNullFlag(Fields.match_field);
		SetFieldHasUpdate(Fields.match_field, this._match_field, value);
		this._match_field = value;
	}

	/** 匹配关键字，用于去除sp固定的参数，提取有用的匹配关键字 */
	public String get_match_keyword() {
		return this._match_keyword;
	}

	/** 匹配关键字，用于去除sp固定的参数，提取有用的匹配关键字 */
	@SuppressWarnings("unused")
	public void set_match_keyword(String value) {
		if (false && true)
			RemoveNullFlag(Fields.match_keyword);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.match_keyword);
			else
				RemoveNullFlag(Fields.match_keyword);
		}

		SetFieldHasUpdate(Fields.match_keyword, this._match_keyword, value);
		this._match_keyword = value;
	}

	/**
	 * API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=
	 * NETTYPE,7=CLIENTIP
	 */
	public String get_api_fields() {
		return this._api_fields;
	}

	/**
	 * API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=
	 * NETTYPE,7=CLIENTIP
	 */
	@SuppressWarnings("unused")
	public void set_api_fields(String value) {
		if (false && true)
			RemoveNullFlag(Fields.api_fields);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.api_fields);
			else
				RemoveNullFlag(Fields.api_fields);
		}

		SetFieldHasUpdate(Fields.api_fields, this._api_fields, value);
		this._api_fields = value;
	}

	/** 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配 */
	public int get_locate_match() {
		return this._locate_match;
	}

	/** 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配 */
	@SuppressWarnings("unused")
	public void set_locate_match(int value) {
		RemoveNullFlag(Fields.locate_match);
		SetFieldHasUpdate(Fields.locate_match, this._locate_match, value);
		this._locate_match = value;
	}

	public Date get_create_date() {
		return this._create_date;
	}

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

	/** 用户日限 */
	public BigDecimal get_user_day_limit() {
		return this._user_day_limit;
	}

	/** 用户日限 */
	@SuppressWarnings("unused")
	public void set_user_day_limit(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.user_day_limit);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.user_day_limit);
			else
				RemoveNullFlag(Fields.user_day_limit);
		}

		SetFieldHasUpdate(Fields.user_day_limit, this._user_day_limit, value);
		this._user_day_limit = value;
	}

	/** 用户月限 */
	public BigDecimal get_user_month_limit() {
		return this._user_month_limit;
	}

	/** 用户月限 */
	@SuppressWarnings("unused")
	public void set_user_month_limit(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.user_month_limit);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.user_month_limit);
			else
				RemoveNullFlag(Fields.user_month_limit);
		}

		SetFieldHasUpdate(Fields.user_month_limit, this._user_month_limit, value);
		this._user_month_limit = value;
	}

	/** 日限 */
	public BigDecimal get_day_limit() {
		return this._day_limit;
	}

	/** 日限 */
	@SuppressWarnings("unused")
	public void set_day_limit(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.day_limit);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.day_limit);
			else
				RemoveNullFlag(Fields.day_limit);
		}

		SetFieldHasUpdate(Fields.day_limit, this._day_limit, value);
		this._day_limit = value;
	}

	/** 月限 */
	public BigDecimal get_month_limit() {
		return this._month_limit;
	}

	/** 月限 */
	@SuppressWarnings("unused")
	public void set_month_limit(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.month_limit);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.month_limit);
			else
				RemoveNullFlag(Fields.month_limit);
		}

		SetFieldHasUpdate(Fields.month_limit, this._month_limit, value);
		this._month_limit = value;
	}

	/** 当前日额 */
	public BigDecimal get_cur_day_limit() {
		return this._cur_day_limit;
	}

	/** 当前日额 */
	@SuppressWarnings("unused")
	public void set_cur_day_limit(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.cur_day_limit);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.cur_day_limit);
			else
				RemoveNullFlag(Fields.cur_day_limit);
		}

		SetFieldHasUpdate(Fields.cur_day_limit, this._cur_day_limit, value);
		this._cur_day_limit = value;
	}

	/** 当前月额 */
	public BigDecimal get_cur_month_limit() {
		return this._cur_month_limit;
	}

	/** 当前月额 */
	@SuppressWarnings("unused")
	public void set_cur_month_limit(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.cur_month_limit);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.cur_month_limit);
			else
				RemoveNullFlag(Fields.cur_month_limit);
		}

		SetFieldHasUpdate(Fields.cur_month_limit, this._cur_month_limit, value);
		this._cur_month_limit = value;
	}

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** 业务通道API名字 */
		public final static String name = "name";
		/** 配匹字段，0:linkid,1:msg,2:cpprams */
		public final static String match_field = "match_field";
		/** 匹配关键字，用于去除sp固定的参数，提取有用的匹配关键字 */
		public final static String match_keyword = "match_keyword";
		/**
		 * API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=
		 * NETTYPE,7=CLIENTIP
		 */
		public final static String api_fields = "api_fields";
		/** 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配 */
		public final static String locate_match = "locate_match";

		public final static String create_date = "create_date";
		/** 用户日限 */
		public final static String user_day_limit = "user_day_limit";
		/** 用户月限 */
		public final static String user_month_limit = "user_month_limit";
		/** 日限 */
		public final static String day_limit = "day_limit";
		/** 月限 */
		public final static String month_limit = "month_limit";
		/** 当前日额 */
		public final static String cur_day_limit = "cur_day_limit";
		/** 当前月额 */
		public final static String cur_month_limit = "cur_month_limit";

	}

}
