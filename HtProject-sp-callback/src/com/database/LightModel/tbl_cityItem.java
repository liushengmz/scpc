package com.database.LightModel;

import java.util.*;

import com.database.Logical.LightDataModel;

import java.math.BigDecimal;

public class tbl_cityItem extends LightDataModel {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_city";

	/** 主键值 */
	private int _id;

	/** 省份ID */
	private int _province_id;
	/** 城市名称 */
	private String _name;

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
		return new String[] { null, "province_id", "name" };
	}

	public Boolean Is_province_idNull() {
		return IsNull(Fields.province_id);
	}

	public void Set_province_idNull() {
		SetNull(Fields.province_id);
	}

	public Boolean Is_nameNull() {
		return IsNull(Fields.name);
	}

	public void Set_nameNull() {
		SetNull(Fields.name);
	}

	public int get_id() {
		return this._id;
	}

	public void set_id(int value) {
		this._id = value;
	}

	/** 省份ID */
	public int get_province_id() {
		return this._province_id;
	}

	/** 省份ID */
	@SuppressWarnings("unused")
	public void set_province_id(int value) {
		RemoveNullFlag(Fields.province_id);
		SetFieldHasUpdate(Fields.province_id, this._province_id, value);
		this._province_id = value;
	}

	/** 城市名称 */
	public String get_name() {
		return this._name;
	}

	/** 城市名称 */
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

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** 省份ID */
		public final static String province_id = "province_id";
		/** 城市名称 */
		public final static String name = "name";

	}

}
