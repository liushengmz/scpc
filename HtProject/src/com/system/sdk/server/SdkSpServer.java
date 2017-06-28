package com.system.sdk.server;

import java.util.List;
import java.util.Map;

import com.system.sdk.dao.SdkSpDao;
import com.system.sdk.model.SdkSpModel;

public class SdkSpServer {
	public List<SdkSpModel>loadSdkSp(){
		return new SdkSpDao().loadSdkSp();
		
	}
	public Map<String,Object>loadSdkSp(int pageIndex,String keyWord){
		return new SdkSpDao().loadSdkSp(pageIndex, keyWord);
	}
public boolean addSdkSp(SdkSpModel model){
	return new SdkSpDao().addSdkSp(model);
	
}
public boolean updateSdkSp(SdkSpModel model){
	return new SdkSpDao().updateSdkSp(model);
}
public SdkSpModel loadSdkSpById(int id){
	return new SdkSpDao().loadSdkSpById(id);
	
}
public boolean deleteSdkSp(int id){
	return new SdkSpDao().deleteSdkSp(id);
}
public static Map<String, Integer> checkData(String fullName,String shortName,int id){
	return new SdkSpDao().checkData(fullName,shortName,id);

}

}
