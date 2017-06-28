package com.system.server.xy;

import java.util.Map;

import org.apache.log4j.Logger;

import com.system.dao.xy.*;

public class XyCpsServer {
	Logger log = Logger.getLogger(XyCpsServer.class);
	
	public Map<String, Object> loadUserData(String startDate,String endDate,String appKey,String channelKey,int pageIndex)
	{
		return new XyUserDao().loadUserData(startDate, endDate, appKey, channelKey,pageIndex);
	}
	
	public boolean updateQdData(int id,double showDataRows)
	{
		return new XyUserDao().updateQdData(id,showDataRows);
	}
	
	
}
