package com.database.LightModel;

public class tbl_sp_api_urlItem extends com.database.Logical.LightDataModel {

	public final static String identifyField = "id";
	public final static String tableName = "tbl_sp_api_url";

	/** 主键值 */
	private Integer _id;

	private Integer _sp_id;
	/** （虛似）文件名 */
	private String _virtual_page;
	/** 是否物理文件 */
	private Boolean _phy_file;
	/** 检查不是为MO同步,格式:FieldName:Regex */
	private String _MoCheck;

	private String _MoLink;

	private String _MrLink;
	/** Mo记录同步到Mr的字段;格式:SqlField1,SqlField2 */
	private String _MoToMr;
	/**
	 * 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
	 */
	private String _MoFieldMap;
	/**
	 * 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
	 */
	private String _MrFidldMap;
	/** Mo同步时,可接收的状态值.格式,正则表达式 */
	private String _MoStatus;
	/** Mr同步时,可接收的状态值.格式,正则表达式 */
	private String _MrStatus;
	/** 同步结果输出:格式:ok/existed/error */
	private String _MsgOutput;
	/** 是否失效 */
	private Boolean _Disable;
	/** 同步地址备注名 */
	private String _name;

	private String _urlPath;
	/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
	private String _MrPrice;
	/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
	private String _MoPrice;

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
		return new String[] { null, "virtual_page", "phy_file", "MoCheck", "MoLink", "MrLink", "MoToMr", "MoFieldMap",
				"MrFidldMap", "MoStatus", "MrStatus", "MsgOutput", "Disable", "name", "urlPath", "MrPrice", "MoPrice" };
	}

	public Boolean Is_virtual_pageNull() {
		return IsNull(Fields.virtual_page);
	}

	public void Set_virtual_pageNull() {
		SetNull(Fields.virtual_page);
	}

	public Boolean Is_phy_fileNull() {
		return IsNull(Fields.phy_file);
	}

	public void Set_phy_fileNull() {
		SetNull(Fields.phy_file);
	}

	public Boolean Is_MoCheckNull() {
		return IsNull(Fields.MoCheck);
	}

	public void Set_MoCheckNull() {
		SetNull(Fields.MoCheck);
	}

	public Boolean Is_MoLinkNull() {
		return IsNull(Fields.MoLink);
	}

	public void Set_MoLinkNull() {
		SetNull(Fields.MoLink);
	}

	public Boolean Is_MrLinkNull() {
		return IsNull(Fields.MrLink);
	}

	public void Set_MrLinkNull() {
		SetNull(Fields.MrLink);
	}

	public Boolean Is_MoToMrNull() {
		return IsNull(Fields.MoToMr);
	}

	public void Set_MoToMrNull() {
		SetNull(Fields.MoToMr);
	}

	public Boolean Is_MoFieldMapNull() {
		return IsNull(Fields.MoFieldMap);
	}

	public void Set_MoFieldMapNull() {
		SetNull(Fields.MoFieldMap);
	}

	public Boolean Is_MrFidldMapNull() {
		return IsNull(Fields.MrFidldMap);
	}

	public void Set_MrFidldMapNull() {
		SetNull(Fields.MrFidldMap);
	}

	public Boolean Is_MoStatusNull() {
		return IsNull(Fields.MoStatus);
	}

	public void Set_MoStatusNull() {
		SetNull(Fields.MoStatus);
	}

	public Boolean Is_MrStatusNull() {
		return IsNull(Fields.MrStatus);
	}

	public void Set_MrStatusNull() {
		SetNull(Fields.MrStatus);
	}

	public Boolean Is_MsgOutputNull() {
		return IsNull(Fields.MsgOutput);
	}

	public void Set_MsgOutputNull() {
		SetNull(Fields.MsgOutput);
	}

	public Boolean Is_DisableNull() {
		return IsNull(Fields.Disable);
	}

	public void Set_DisableNull() {
		SetNull(Fields.Disable);
	}

	public Boolean Is_nameNull() {
		return IsNull(Fields.name);
	}

	public void Set_nameNull() {
		SetNull(Fields.name);
	}

	public Boolean Is_urlPathNull() {
		return IsNull(Fields.urlPath);
	}

	public void Set_urlPathNull() {
		SetNull(Fields.urlPath);
	}

	public Boolean Is_MrPriceNull() {
		return IsNull(Fields.MrPrice);
	}

	public void Set_MrPriceNull() {
		SetNull(Fields.MrPrice);
	}

	public Boolean Is_MoPriceNull() {
		return IsNull(Fields.MoPrice);
	}

	public void Set_MoPriceNull() {
		SetNull(Fields.MoPrice);
	}

	public Integer get_id() {
		return this._id;
	}

	public void set_id(Integer value) {
		this._id = value;
	}

	public Integer get_sp_id() {
		return this._sp_id;
	}

	@SuppressWarnings("unused")
	public void set_sp_id(Integer value) {
		if (false && false)
			RemoveNullFlag(Fields.sp_id);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.sp_id);
			else
				RemoveNullFlag(Fields.sp_id);
		}

		SetFieldHasUpdate(Fields.sp_id, this._sp_id, value);
		this._sp_id = value;
	}

	/** （虛似）文件名 */
	public String get_virtual_page() {
		return this._virtual_page;
	}

	/** （虛似）文件名 */
	@SuppressWarnings("unused")
	public void set_virtual_page(String value) {
		if (false && true)
			RemoveNullFlag(Fields.virtual_page);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.virtual_page);
			else
				RemoveNullFlag(Fields.virtual_page);
		}

		SetFieldHasUpdate(Fields.virtual_page, this._virtual_page, value);
		this._virtual_page = value;
	}

	/** 是否物理文件 */
	public Boolean get_phy_file() {
		return this._phy_file;
	}

	/** 是否物理文件 */
	@SuppressWarnings("unused")
	public void set_phy_file(Boolean value) {
		if (true && true)
			RemoveNullFlag(Fields.phy_file);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.phy_file);
			else
				RemoveNullFlag(Fields.phy_file);
		}

		SetFieldHasUpdate(Fields.phy_file, this._phy_file, value);
		this._phy_file = value;
	}

	/** 检查不是为MO同步,格式:FieldName:Regex */
	public String get_MoCheck() {
		return this._MoCheck;
	}

	/** 检查不是为MO同步,格式:FieldName:Regex */
	@SuppressWarnings("unused")
	public void set_MoCheck(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MoCheck);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MoCheck);
			else
				RemoveNullFlag(Fields.MoCheck);
		}

		SetFieldHasUpdate(Fields.MoCheck, this._MoCheck, value);
		this._MoCheck = value;
	}

	public String get_MoLink() {
		return this._MoLink;
	}

	@SuppressWarnings("unused")
	public void set_MoLink(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MoLink);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MoLink);
			else
				RemoveNullFlag(Fields.MoLink);
		}

		SetFieldHasUpdate(Fields.MoLink, this._MoLink, value);
		this._MoLink = value;
	}

	public String get_MrLink() {
		return this._MrLink;
	}

	@SuppressWarnings("unused")
	public void set_MrLink(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MrLink);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MrLink);
			else
				RemoveNullFlag(Fields.MrLink);
		}

		SetFieldHasUpdate(Fields.MrLink, this._MrLink, value);
		this._MrLink = value;
	}

	/** Mo记录同步到Mr的字段;格式:SqlField1,SqlField2 */
	public String get_MoToMr() {
		return this._MoToMr;
	}

	/** Mo记录同步到Mr的字段;格式:SqlField1,SqlField2 */
	@SuppressWarnings("unused")
	public void set_MoToMr(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MoToMr);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MoToMr);
			else
				RemoveNullFlag(Fields.MoToMr);
		}

		SetFieldHasUpdate(Fields.MoToMr, this._MoToMr, value);
		this._MoToMr = value;
	}

	/**
	 * 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
	 */
	public String get_MoFieldMap() {
		return this._MoFieldMap;
	}

	/**
	 * 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
	 */
	@SuppressWarnings("unused")
	public void set_MoFieldMap(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MoFieldMap);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MoFieldMap);
			else
				RemoveNullFlag(Fields.MoFieldMap);
		}

		SetFieldHasUpdate(Fields.MoFieldMap, this._MoFieldMap, value);
		this._MoFieldMap = value;
	}

	/**
	 * 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
	 */
	public String get_MrFidldMap() {
		return this._MrFidldMap;
	}

	/**
	 * 字段映射(不含:link,price字段) 格式:urlField1:sqlField1,urlField2,sqlField2,.....
	 */
	@SuppressWarnings("unused")
	public void set_MrFidldMap(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MrFidldMap);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MrFidldMap);
			else
				RemoveNullFlag(Fields.MrFidldMap);
		}

		SetFieldHasUpdate(Fields.MrFidldMap, this._MrFidldMap, value);
		this._MrFidldMap = value;
	}

	/** Mo同步时,可接收的状态值.格式,正则表达式 */
	public String get_MoStatus() {
		return this._MoStatus;
	}

	/** Mo同步时,可接收的状态值.格式,正则表达式 */
	@SuppressWarnings("unused")
	public void set_MoStatus(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MoStatus);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MoStatus);
			else
				RemoveNullFlag(Fields.MoStatus);
		}

		SetFieldHasUpdate(Fields.MoStatus, this._MoStatus, value);
		this._MoStatus = value;
	}

	/** Mr同步时,可接收的状态值.格式,正则表达式 */
	public String get_MrStatus() {
		return this._MrStatus;
	}

	/** Mr同步时,可接收的状态值.格式,正则表达式 */
	@SuppressWarnings("unused")
	public void set_MrStatus(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MrStatus);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MrStatus);
			else
				RemoveNullFlag(Fields.MrStatus);
		}

		SetFieldHasUpdate(Fields.MrStatus, this._MrStatus, value);
		this._MrStatus = value;
	}

	/** 同步结果输出:格式:ok/existed/error */
	public String get_MsgOutput() {
		return this._MsgOutput;
	}

	/** 同步结果输出:格式:ok/existed/error */
	@SuppressWarnings("unused")
	public void set_MsgOutput(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MsgOutput);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MsgOutput);
			else
				RemoveNullFlag(Fields.MsgOutput);
		}

		SetFieldHasUpdate(Fields.MsgOutput, this._MsgOutput, value);
		this._MsgOutput = value;
	}

	/** 是否失效 */
	public Boolean get_Disable() {
		return this._Disable;
	}

	/** 是否失效 */
	@SuppressWarnings("unused")
	public void set_Disable(Boolean value) {
		if (true && true)
			RemoveNullFlag(Fields.Disable);
		else if (!true) {
			if (value == null)
				SetNullFlag(Fields.Disable);
			else
				RemoveNullFlag(Fields.Disable);
		}

		SetFieldHasUpdate(Fields.Disable, this._Disable, value);
		this._Disable = value;
	}

	/** 同步地址备注名 */
	public String get_name() {
		return this._name;
	}

	/** 同步地址备注名 */
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

	public String get_urlPath() {
		return this._urlPath;
	}

	@SuppressWarnings("unused")
	public void set_urlPath(String value) {
		if (false && true)
			RemoveNullFlag(Fields.urlPath);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.urlPath);
			else
				RemoveNullFlag(Fields.urlPath);
		}

		SetFieldHasUpdate(Fields.urlPath, this._urlPath, value);
		this._urlPath = value;
	}

	/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
	public String get_MrPrice() {
		return this._MrPrice;
	}

	/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
	@SuppressWarnings("unused")
	public void set_MrPrice(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MrPrice);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MrPrice);
			else
				RemoveNullFlag(Fields.MrPrice);
		}

		SetFieldHasUpdate(Fields.MrPrice, this._MrPrice, value);
		this._MrPrice = value;
	}

	/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
	public String get_MoPrice() {
		return this._MoPrice;
	}

	/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
	@SuppressWarnings("unused")
	public void set_MoPrice(String value) {
		if (false && true)
			RemoveNullFlag(Fields.MoPrice);
		else if (!false) {
			if (value == null)
				SetNullFlag(Fields.MoPrice);
			else
				RemoveNullFlag(Fields.MoPrice);
		}

		SetFieldHasUpdate(Fields.MoPrice, this._MoPrice, value);
		this._MoPrice = value;
	}

	/** 数据表字段列表对像 */
	public final class Fields {
		private Fields() {
		}

		public final static String id = "id";
		/** 主键 */
		public final static String PrimaryKey = "id";

		public final static String sp_id = "sp_id";
		/** （虛似）文件名 */
		public final static String virtual_page = "virtual_page";
		/** 是否物理文件 */
		public final static String phy_file = "phy_file";
		/** 检查不是为MO同步,格式:FieldName:Regex */
		public final static String MoCheck = "MoCheck";

		public final static String MoLink = "MoLink";

		public final static String MrLink = "MrLink";
		/** Mo记录同步到Mr的字段;格式:SqlField1,SqlField2 */
		public final static String MoToMr = "MoToMr";
		/**
		 * 字段映射(不含:link,price字段)
		 * 格式:urlField1:sqlField1,urlField2,sqlField2,.....
		 */
		public final static String MoFieldMap = "MoFieldMap";
		/**
		 * 字段映射(不含:link,price字段)
		 * 格式:urlField1:sqlField1,urlField2,sqlField2,.....
		 */
		public final static String MrFidldMap = "MrFidldMap";
		/** Mo同步时,可接收的状态值.格式,正则表达式 */
		public final static String MoStatus = "MoStatus";
		/** Mr同步时,可接收的状态值.格式,正则表达式 */
		public final static String MrStatus = "MrStatus";
		/** 同步结果输出:格式:ok/existed/error */
		public final static String MsgOutput = "MsgOutput";
		/** 是否失效 */
		public final static String Disable = "Disable";
		/** 同步地址备注名 */
		public final static String name = "name";

		public final static String urlPath = "urlPath";
		/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
		public final static String MrPrice = "MrPrice";
		/** 传入价格字段,格式:urlField,0/urlField,1/urlField,2,1a?:100,2a*:200 */
		public final static String MoPrice = "MoPrice";

	}

}
