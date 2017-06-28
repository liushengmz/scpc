package com.database.LightModel;

public class TestModel extends com.database.Logical.LightDataModel {

 
	private String _name;

	@Override
	protected String[] GetNullableFields() {
		// TODO Auto-generated method stub
		return new String[]{};
	}
	
	public String get_name(){
		return this._name;
	}
	
	public void set_name(String value){
		 this._name=value;
	}

	@Override
	public String TableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IdentifyField() {
		// TODO Auto-generated method stub
		return null;
	}

 
	
	
}
