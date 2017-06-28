package com.database.LightModel;

import java.util.*;
import java.math.BigDecimal;

public class AutoCode extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_app_summer";

	/** 主键值 */
	private Integer _id;

	/** app id */
	private Integer _appid;
	/** 计费日期 */
	private Date _fee_date;
	/** 新增用户-真实数据 */
	private Integer _new_user_rows;
	/** 每日收入-真实数据 */
	private BigDecimal _amount;
	/** 展示的新增用户 */
	private Integer _show_new_user_rows;
	/** 展示的每日收入 */
	private BigDecimal _show_amount;
	/** 推广费用 */
	private BigDecimal _extend_fee;
	/** 利润 */
	private BigDecimal _profit;
	/** 状态，0未同步，1为已同步 */
	private Integer _status;

	private Date _create_date;

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
		return new String[] { null, "new_user_rows", "amount", "show_new_user_rows", "show_amount", "extend_fee",
				"profit", "status", "create_date" };
	}

	public Boolean Is_new_user_rowsNull() {
		return IsNull(Fields.new_user_rows);
	}

	public void Set_new_user_rowsNull() {
		SetNull(Fields.new_user_rows);
	}

	public Boolean Is_amountNull() {
		return IsNull(Fields.amount);
	}

	public void Set_amountNull() {
		SetNull(Fields.amount);
	}

	public Boolean Is_show_new_user_rowsNull() {
		return IsNull(Fields.show_new_user_rows);
	}

	public void Set_show_new_user_rowsNull() {
		SetNull(Fields.show_new_user_rows);
	}

	public Boolean Is_show_amountNull() {
		return IsNull(Fields.show_amount);
	}

	public void Set_show_amountNull() {
		SetNull(Fields.show_amount);
	}

	public Boolean Is_extend_feeNull() {
		return IsNull(Fields.extend_fee);
	}

	public void Set_extend_feeNull() {
		SetNull(Fields.extend_fee);
	}

	public Boolean Is_profitNull() {
		return IsNull(Fields.profit);
	}

	public void Set_profitNull() {
		SetNull(Fields.profit);
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

	public Integer get_id() {
		return this._id;
	}

	public void set_id(Integer value) {
		this._id = value;
	}

	/** app id */
	public Integer get_appid() {
		return this._appid;
	}

	/** app id */
	@SuppressWarnings("unused")
	public void set_appid(Integer value) {
		if (false && false)
			RemoveNullFlag(Fields.appid);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.appid);
			else
				RemoveNullFlag(Fields.appid);
		}

		SetFieldHasUpdate(Fields.appid, this._appid, value);
		this._appid = value;
	}

	/** 计费日期 */
	public Date get_fee_date() {
		return this._fee_date;
	}

	/** 计费日期 */
	@SuppressWarnings("unused")
	public void set_fee_date(Date value) {
		if (false && false)
			RemoveNullFlag(Fields.fee_date);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.fee_date);
			else
				RemoveNullFlag(Fields.fee_date);
		}

		SetFieldHasUpdate(Fields.fee_date, this._fee_date, value);
		this._fee_date = value;
	}

	/** 新增用户-真实数据 */
	public Integer get_new_user_rows() {
		return this._new_user_rows;
	}

	/** 新增用户-真实数据 */
	@SuppressWarnings("unused")
	public void set_new_user_rows(Integer value) {
		if (false && true)
			RemoveNullFlag(Fields.new_user_rows);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.new_user_rows);
			else
				RemoveNullFlag(Fields.new_user_rows);
		}

		SetFieldHasUpdate(Fields.new_user_rows, this._new_user_rows, value);
		this._new_user_rows = value;
	}

	/** 每日收入-真实数据 */
	public BigDecimal get_amount() {
		return this._amount;
	}

	/** 每日收入-真实数据 */
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

	/** 展示的新增用户 */
	public Integer get_show_new_user_rows() {
		return this._show_new_user_rows;
	}

	/** 展示的新增用户 */
	@SuppressWarnings("unused")
	public void set_show_new_user_rows(Integer value) {
		if (false && true)
			RemoveNullFlag(Fields.show_new_user_rows);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.show_new_user_rows);
			else
				RemoveNullFlag(Fields.show_new_user_rows);
		}

		SetFieldHasUpdate(Fields.show_new_user_rows, this._show_new_user_rows, value);
		this._show_new_user_rows = value;
	}

	/** 展示的每日收入 */
	public BigDecimal get_show_amount() {
		return this._show_amount;
	}

	/** 展示的每日收入 */
	@SuppressWarnings("unused")
	public void set_show_amount(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.show_amount);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.show_amount);
			else
				RemoveNullFlag(Fields.show_amount);
		}

		SetFieldHasUpdate(Fields.show_amount, this._show_amount, value);
		this._show_amount = value;
	}

	/** 推广费用 */
	public BigDecimal get_extend_fee() {
		return this._extend_fee;
	}

	/** 推广费用 */
	@SuppressWarnings("unused")
	public void set_extend_fee(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.extend_fee);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.extend_fee);
			else
				RemoveNullFlag(Fields.extend_fee);
		}

		SetFieldHasUpdate(Fields.extend_fee, this._extend_fee, value);
		this._extend_fee = value;
	}

	/** 利润 */
	public BigDecimal get_profit() {
		return this._profit;
	}

	/** 利润 */
	@SuppressWarnings("unused")
	public void set_profit(BigDecimal value) {
		if (true && true)
			RemoveNullFlag(Fields.profit);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.profit);
			else
				RemoveNullFlag(Fields.profit);
		}

		SetFieldHasUpdate(Fields.profit, this._profit, value);
		this._profit = value;
	}

	/** 状态，0未同步，1为已同步 */
	public Integer get_status() {
		return this._status;
	}

	/** 状态，0未同步，1为已同步 */
	@SuppressWarnings("unused")
	public void set_status(Integer value) {
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

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		/** app id */
		public final static String appid = "appid";
		/** 计费日期 */
		public final static String fee_date = "fee_date";
		/** 新增用户-真实数据 */
		public final static String new_user_rows = "new_user_rows";
		/** 每日收入-真实数据 */
		public final static String amount = "amount";
		/** 展示的新增用户 */
		public final static String show_new_user_rows = "show_new_user_rows";
		/** 展示的每日收入 */
		public final static String show_amount = "show_amount";
		/** 推广费用 */
		public final static String extend_fee = "extend_fee";
		/** 利润 */
		public final static String profit = "profit";
		/** 状态，0未同步，1为已同步 */
		public final static String status = "status";

		public final static String create_date = "create_date";

	}

}
