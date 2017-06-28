package com.system.sdk.server;

import java.util.List;

import com.system.sdk.dao.SdkChannelDao;
import com.system.sdk.model.SdkChannelModel;

public class SdkChannelServer {
	public List<SdkChannelModel>loadSdkChannel(){
		return new SdkChannelDao().loadSdkChannel();
	}

}
