package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.SpTroneDao;
import com.system.dao.TroneDao;
import com.system.model.TroneModel;
import com.system.util.ConfigManager;
import com.system.util.StringUtil;

public class TroneServer
{
	public List<TroneModel> loadTroneList()
	{
		return new TroneDao().loadTrone();
	}
	
	public List<TroneModel> loadTrone(int spTroneId)
	{
		return new TroneDao().loadTrone(spTroneId);
	}
	
	public Map<String, Object> loadTrone(int spId,int pageIndex)
	{
		return new TroneDao().loadTrone(spId, pageIndex);
	}
	
	public Map<String, Object> loadTrone(int pageIndex,String keyWord)
	{
		return new TroneDao().loadTrone(pageIndex, keyWord);
	}
	
	public Map<String, Object> loadTrone(int userId,int pageIndex,String keyWord)
	{
		return new TroneDao().loadTrone(userId,pageIndex, keyWord);
	}
	
	public List<TroneModel> loadSpTrone()
	{
		return new TroneDao().loadSpTrone();
	}
	
	public TroneModel getTroneById(int id)
	{
		return new TroneDao().getTroneById(id);
	}
	
	public void addTrone(TroneModel model)
	{
		new TroneDao().addTrone(model);
	}
	
	public int insertTrone(TroneModel model)
	{
		return new TroneDao().insertTrone(model);
	}
	
	public void updateTrone(TroneModel model)
	{
		new TroneDao().updateTrone(model);
	}
	
	public void deleteTrone(int delId)
	{
		new TroneDao().deleteTrone(delId);
	}
	
	public int checkAdd(int userId){
		int commerceId = StringUtil.getInteger(ConfigManager.getConfigData("SP_COMMERCE_GROUP_ID"),-1);
		int count=new TroneDao().checkAdd(userId,commerceId);
		if(count>=1){
			return 1;//在商务组里
		}else{
			return 0;
		}
	}
	public Map<String, Object> loadTrone(int pageIndex,String keyWord,int userId)
	{
		return new TroneDao().loadTrone(pageIndex, keyWord,userId);
	} 
}
