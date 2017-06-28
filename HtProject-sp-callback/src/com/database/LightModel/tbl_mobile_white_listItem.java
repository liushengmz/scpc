package com.database.LightModel;
import java.util.*;

import com.database.Logical.LightDataModel;

public class tbl_mobile_white_listItem extends  LightDataModel {

	public final static String identifyField ="id";
	public final static String tableName ="tbl_mobile_white_list";
	 
	/**主键值*/
		private int _id;


	/**手机号或IMSI*/
	private String _mobile;

	private Date _adddate;


	@Override
	public String IdentifyField() { return identifyField; }

	@Override
	public String TableName(){ return tableName; }

	@Override
	protected String[] GetNullableFields() {
		return new String[]{null
			,"adddate"
};
	}

	public Boolean Is_adddateNull(){ return IsNull(Fields.adddate);}
	public void Set_adddateNull(){ SetNull(Fields.adddate);}

	
	
		public int get_id(){ return this._id;}
		
		public void set_id(int value){ this._id=value;}


 	/**手机号或IMSI*/
    public String get_mobile(){ return this._mobile; }
	/**手机号或IMSI*/
	@SuppressWarnings("unused")
    public void set_mobile(String value) { 
			if (false && false)
				RemoveNullFlag(Fields.mobile);
			else if (!false){
				if (value == null)
					SetNullFlag(Fields.mobile);
				else
					RemoveNullFlag(Fields.mobile);
			}

			SetFieldHasUpdate(Fields.mobile, this._mobile, value); 
			this._mobile = value;	}

    public Date get_adddate(){ return this._adddate; }
	
	@SuppressWarnings("unused")
    public void set_adddate(Date value) { 
			if (false && true)
				RemoveNullFlag(Fields.adddate);
			else if (!false){
				if (value == null)
					SetNullFlag(Fields.adddate);
				else
					RemoveNullFlag(Fields.adddate);
			}

			SetFieldHasUpdate(Fields.adddate, this._adddate, value); 
			this._adddate = value;	}



		/**数据表字段列表对像*/
		public final class Fields{
			private Fields(){}
			 
			
			public final static String id="id";
			/**主键*/
			public final static String PrimaryKey="id";

			/**手机号或IMSI*/
			public final static String mobile="mobile";

			public final static String adddate="adddate";

		}
	
}
