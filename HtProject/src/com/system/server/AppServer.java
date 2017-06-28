package com.system.server;

import java.util.Map;

import com.system.dao.AppDao;
import com.system.model.AppModel;

public class AppServer {
	
	public Map<String, Object> loadApp(int pageindex)
	{
		return new AppDao().loadAppByPageindex(pageindex);
	}
	
	public Map<String, Object> loadApp()
	{
		return new AppDao().loadAppByPageindex();
	}
	
	public Map<String, Object> loadApp(int pageindex,String appname,String appkey,int appType)
	{
		return new AppDao().loadAppByPageindex(pageindex, appname, appkey,appType);
	}
	
	public int loadIdByName(String appname)
	{
		return new AppDao().loadIdByName(appname);
	}
	
	public AppModel loadAppById(int id)
	{
		AppModel model = new AppDao().loadAppById(id);
		return new AppDao().loadAppById(id);
	}
	
	public boolean updataApp(AppModel model)
	{
		return new AppDao().updataApp(model);
	}
	
	public boolean deletApp(int id){
		return new AppDao().deletApp(id);
	}
	
	public boolean addApp(AppModel model){
		return new AppDao().addApp(model);
	}
	
	public boolean updateAppLoginAccount(int appId,int userId)
	{
		return new AppDao().updateAppLoginAccount(appId, userId);
	}
	
	
}
