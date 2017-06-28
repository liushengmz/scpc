package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.CpDao;
import com.system.dao.SpDao;
import com.system.model.CpModel;
import com.system.util.ConfigManager;
import com.system.util.StringUtil;

public class CpServer {
	public List<CpModel> loadCp() {
		return new CpDao().loadCp();
	}

	public List<CpModel> loadCpQiBa() {
		return new CpDao().loadCpQiBa();
	}

	public Map<String, Object> loadCp(int pageIndex) {
		return new CpDao().loadCp(pageIndex);
	}

	public Map<String, Object> loadCp(int pageIndex, String keyWord) {
		return new CpDao().loadCp(pageIndex, keyWord);
	}
	/**
	 * 增加状态字段
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadCp(int pageIndex, int status, String keyWord) {
		return new CpDao().loadCp(pageIndex,status, keyWord);
	//	return new CpDao().loadCp(pageIndex, keyWord);
	}
	
	public Map<String, Object> loadCp(int userId,int pageIndex, int status, String keyWord) {
		return new CpDao().loadCp(userId,pageIndex,status, keyWord);
	//	return new CpDao().loadCp(pageIndex, keyWord);
	}
	
	public CpModel loadCpById(int id) {
		return new CpDao().loadCpById(id);
	}

	public List<CpModel> loadCpByIds(int[] ids) {
		return new CpDao().loadCpByIds(ids);
	}

	public List<CpModel> loadCpBySptone(int spTroneId) {
		return new CpDao().loadCpBySptone(spTroneId);
	}

	public boolean addCp(CpModel model) {
		return new CpDao().addCp(model);
	}

	public boolean updateCp(CpModel model) {
		return new CpDao().updateCp(model);
	}

	public boolean updateCpAccount(int cpId, int userId) {
		return new CpDao().updateCpAccount(cpId, userId);
	}
	public int checkAdd(int userId){
		int commerceId = StringUtil.getInteger(ConfigManager.getConfigData("CP_COMMERCE_GROUP_ID"),-1);
		int count=new CpDao().checkAdd(userId,commerceId);
		if(count>=1){
			return 1;//在商务组里
		}else{
			return 0;
		}
	}
	
	public Map<String, Object> loadCp(int pageIndex, String keyWord,int userId) {
		return new CpDao().loadCp(pageIndex, keyWord,userId);
	}

}
