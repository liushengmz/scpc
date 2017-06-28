package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.SpDao;
import com.system.model.SpModel;
import com.system.util.ConfigManager;
import com.system.util.StringUtil;

public class SpServer
{
	public List<SpModel> loadSp()
	{
		return new SpDao().loadSp();
	}
	
	public List<SpModel> loadSpQiBa()
	{
		return new SpDao().loadSpQiBa();
	}
	
	public Map<String, Object> loadSp(int pageIndex)
	{
		return new SpDao().loadSp(pageIndex);
	}
	
	public Map<String, Object> loadSp(int pageIndex,String keyWord)
	{
		return new SpDao().loadSp(pageIndex, keyWord);
	}
	/**
	 * 增加SP状态查询
	 * @param pageIndex
	 * @param status
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadSp(int pageIndex,int status,String keyWord)
	{
		return new SpDao().loadSp(pageIndex,status, keyWord);
	}
	
	public Map<String, Object> loadSp(int userId,int pageIndex,int status,String keyWord)
	{
		return new SpDao().loadSp(userId,pageIndex,status, keyWord);
	}
	
	public Map<String, Object> loadSp(int pageIndex,String keyWord,int userId)
	{
		return new SpDao().loadSp(pageIndex, keyWord,userId);
	}
	
	public SpModel loadSpById(int id)
	{
		return new SpDao().loadSpById(id);
	}
	
	public boolean addSp(SpModel model)
	{
		return new SpDao().addSp(model);
	}
	
	public boolean updateSp(SpModel model)
	{
		return new SpDao().updateSp(model);
	}
	public int checkAdd(int userId){
		int commerceId = StringUtil.getInteger(ConfigManager.getConfigData("SP_COMMERCE_GROUP_ID"),-1);
		int count=new SpDao().checkAdd(userId,commerceId);
		if(count>=1){
			return 1;//在商务组里
		}else{
			return 0;
		}
	}
}
