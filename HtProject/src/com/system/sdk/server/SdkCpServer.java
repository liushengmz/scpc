package com.system.sdk.server;

import java.util.List;

import com.system.sdk.dao.SdkCpDao;
import com.system.sdk.model.SdkCpModel;

public class SdkCpServer {
	public List<SdkCpModel>loadSdkCp(){
		return new SdkCpDao().loadSdkCp();
	}

}
