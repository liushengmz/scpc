package com.system.server;

import java.util.Map;

import com.sun.org.apache.regexp.internal.recompile;
import com.system.dao.AdAppDao;
import com.system.dao.AppDao;
import com.system.model.AdAppModel;
import com.system.model.AppModel;

public class AdAppServer {
	
	public Map<String, Object> loadApp(int pageindex)
	{
		return new AdAppDao().loadAppByPageindex(pageindex);
	}
	public Map<String, Object> loadAppToChannel()
	{
		return new AdAppDao().loadAppByPageindex2();
	}
	public Map<String, Object> loadApp()
	{
		return new AdAppDao().loadApp();
	}
	
	public Map<String, Object> loadApp(int pageindex,String appname,String appkey)
	{
		return new AdAppDao().loadAppByPageindex(pageindex, appname, appkey);
	}
	
	public int loadIdByName(String appname)
	{
		return new AdAppDao().loadIdByName(appname);
	}
	
	public AdAppModel loadAppById(int id)
	{
		return new AdAppDao().loadAppById(id);
	}
	
	public boolean updataApp(AdAppModel model)
	{
		return new AdAppDao().updataApp(model);
	}
	
	public boolean deletApp(int id){
		return new AdAppDao().deletApp(id);
	}
	
	public boolean addApp(AdAppModel model){
		return new AdAppDao().addApp(model);
	}
	
	public boolean updateAdAppAccount(int id,int userId)
	{
		return new AdAppDao().updateAdAppAccount(id, userId);
	}
	
	
}
