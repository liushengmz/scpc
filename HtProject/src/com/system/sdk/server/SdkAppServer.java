package com.system.sdk.server;

import java.util.List;

import com.system.sdk.dao.SdkAppDao;
import com.system.sdk.model.SdkAppModel;

public class SdkAppServer {
	public List<SdkAppModel> loadSdkApp(){
		return new SdkAppDao().loadSdkApp();
	}

}
