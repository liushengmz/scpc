package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.BlackDao;
import com.system.model.BlackModel;

public class BlackServer {
	public Map<String, Object> loadBlack(int pageIndex,String keyWord) {
		return new BlackDao().loadBlack(pageIndex, keyWord);
	}
	
	public List<BlackModel> loadBlack(){
		return new BlackDao().loadBlack();
	}
	public boolean updateBlack(BlackModel model){
		return new BlackDao().updateBlack(model);
	}
	public boolean addBlack(BlackModel model){
		return new BlackDao().addBlack(model);
	}
	public BlackModel loadBlackById(int id){
		return new BlackDao().loadBlackById(id);
	}
	/**
	 * 
	 * @param model 实例
	 * @param cyType 添加类型
	 */
	public void addBlack(BlackModel model,int cyType){
		String plData=model.getPlData();
		String remark=model.getRemark();
		String[] strings=null;
		if(plData.contains(",")){
			strings=plData.split(",");
		}
		if(plData.contains("\r\n")){
			strings=plData.split("\r\n");
		}
		String colName="phone";
		if(cyType==1){
			colName="phone";
		}
		if(cyType==2){
			colName="imei";
		}
		if(cyType==3){
			colName="imsi";
		}
			new BlackDao().addBlack(model,strings,colName);
	      	
	}
	public boolean delete(int id){
		return new BlackDao().delete(id);
	}
}
