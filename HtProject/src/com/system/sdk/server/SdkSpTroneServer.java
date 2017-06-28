package com.system.sdk.server;

import java.util.List;
import java.util.Map;
import com.system.sdk.dao.SdkSpTroneDao;
import com.system.sdk.model.SdkSpTroneModel;

public class SdkSpTroneServer {
	public List<SdkSpTroneModel>loadSdkSpTrone(){
		return new SdkSpTroneDao().loadSdkSpTrone();
	}
	public SdkSpTroneModel loadSdkSpTroneById(int id){
		return new SdkSpTroneDao().loadSdkSpTroneById(id);
	}
	public Map<String,Object>loadSdkSpTrone(int pageIndex,String keyWord){
		return new SdkSpTroneDao().loadSdkSpTrone(pageIndex, keyWord);
	}
	
	public boolean updateSdkSpTrone(SdkSpTroneModel model){
		return new SdkSpTroneDao().updateSdkSpTrone(model);
	}
	public boolean addSdkSpTrone(SdkSpTroneModel model){
		return new SdkSpTroneDao().addSdkSpTrone(model);
	}
	public boolean deleteSdkSpTrone(int id){
		return new SdkSpTroneDao().deleteSdkSpTrone(id);
	}
}
