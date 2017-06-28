package com.system.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.dao.CpBankInfoDao;
import com.system.model.CpBankModel;

public class CpBankInfoServer {
	public List<CpBankModel> loadCpBank(){
		return new CpBankInfoDao().loadCpBank();
	}
	public Map<String, Object> loadCpBank(int pageIndex,String keyWord,int type,int status){
		return new CpBankInfoDao().loadCpBank(pageIndex, keyWord, type, status);
				
	}
	public CpBankModel loadCpBankById(int id){
		return new CpBankInfoDao().loadCpBankById(id);
	}
	public boolean updateCpBank(CpBankModel model){
		return new CpBankInfoDao().updateCpBank(model);
	}
	public boolean addCpBank(CpBankModel model){
		return new CpBankInfoDao().addCpBank(model);
	}
	public boolean deleteCpBank(int id){
		return new CpBankInfoDao().deleteCpBank(id);
	}
	public static Map<String,Integer> checkData(int cpId,int type,int status,int id){
		if(status==0){
			Map<String, Integer> map=new HashMap<String,Integer>();
			map.put("flag", 0);
			return map;
		}else{
			return new CpBankInfoDao().checkData(cpId, type, status, id);
			
		}
				
	}

}
