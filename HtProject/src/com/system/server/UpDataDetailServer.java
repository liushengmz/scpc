package com.system.server;

import java.util.List;

import com.system.dao.UpDataDetailDao;
import com.system.util.StringUtil;
import com.system.vo.UpDetailDataVo;

public class UpDataDetailServer {
	
	
	public List<UpDetailDataVo> loadUpDetailDataByCondition(String startDate,String endDate,int spId,int cpId,int spTroneId,int chkType,String keyWord)
	{
		String table = StringUtil.getMonthFormat(startDate.substring(0,10));
		String data = "";
		if(chkType==4){
		if(!StringUtil.isNullOrEmpty(keyWord)){
			String[] keys = keyWord.split("\r\n");
			for(String key : keys)
			{
				data += "'" +pacodeStr(key)+ "',";
			}
			data = data.substring(0,data.length()-1);
		}
		}else{
			if(!StringUtil.isNullOrEmpty(keyWord)){
				String[] keys = keyWord.split("\r\n");
				for(String key : keys)
				{
					data += "'" + key+ "',";
				}
				data = data.substring(0,data.length()-1);
			}
			
		}
		return new UpDataDetailDao().loadUpDetailDataByCondition(table, startDate, endDate, spId, cpId, spTroneId, chkType, data);
		}
	public  String pacodeStr(String payCode){
		if(payCode.length()==6){
			payCode=payCode.substring(2, payCode.length());
			return payCode;
		}else{
			return payCode;
		}
	}

}

