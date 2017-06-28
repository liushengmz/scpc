package com.database.LightModel;

public class tbl_phone_locateItem extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_phone_locate";

	/** 主键值 */
	private Integer _id;

	/** 电话号码前缀 */
	private String _phone;
	/** tbl_city主键ID */
	private Integer _city_id;
	/** 运营商 */
	private Integer _operator;

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
		return new String[] { null, "operator" };
	}

	public Boolean Is_operatorNull() {
		return IsNull(Fields.operator);
	}

	public void Set_operatorNull() {
		SetNull(Fields.operator);
	}

	public Integer get_id() {
		return this._id;
	}

	public void set_id(Integer value) {
		this._id = value;
	}

	/** 电话号码前缀 */
	public String get_phone() {
		return this._phone;
	}

	/** 电话号码前缀 */
	@SuppressWarnings("unused")
	public void set_phone(String value) {
		if (false && false)
			RemoveNullFlag(Fields.phone);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.phone);
			else
				RemoveNullFlag(Fields.phone);
		}

		SetFieldHasUpdate(Fields.phone, this._phone, value);
		this._phone = value;
	}

	/** tbl_city主键ID */
	public Integer get_city_id() {
		return this._city_id;
	}

	/** tbl_city主键ID */
	@SuppressWarnings("unused")
	public void set_city_id(Integer value) {
		if (false && false)
			RemoveNullFlag(Fields.city_id);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.city_id);
			else
				RemoveNullFlag(Fields.city_id);
		}

		SetFieldHasUpdate(Fields.city_id, this._city_id, value);
		this._city_id = value;
	}

	/** 运营商 */
	public Integer get_operator() {
		return this._operator;
	}

	/** 运营商 */
	@SuppressWarnings("unused")
	public void set_operator(Integer value) {
		if (false && true)
			RemoveNullFlag(Fields.operator);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.operator);
			else
				RemoveNullFlag(Fields.operator);
		}

		SetFieldHasUpdate(Fields.operator, this._operator, value);
		this._operator = value;
	}

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** 电话号码前缀 */
		public final static String phone = "phone";
		/** tbl_city主键ID */
		public final static String city_id = "city_id";
		/** 运营商 */
		public final static String operator = "operator";

	}

}
