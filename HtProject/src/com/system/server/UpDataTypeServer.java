package com.system.server;

import java.util.List;

import com.system.dao.UpDataTypeDao;
import com.system.model.UpDataTypeModel;

public class UpDataTypeServer {
	public List<UpDataTypeModel> loadUpDataType(){
		
		return new UpDataTypeDao().loadUpDataType();
	}

}
