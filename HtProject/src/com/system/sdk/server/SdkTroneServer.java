package com.system.sdk.server;

import java.util.List;

import com.system.sdk.dao.SdkTroneDao;
import com.system.sdk.model.SdkTroneModel;

public class SdkTroneServer {
	public List<SdkTroneModel>loadSdkTrone(){
		return new SdkTroneDao().loadSdkTrone();
	}
}
