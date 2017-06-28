package com.system.server;

import java.util.ArrayList;
import java.util.List;

import com.system.util.ServiceUtil;

public class NetServer
{
	public boolean synDataList(String url,List<String> list)
	{
		for(String data : list)
		{
			if(ServiceUtil.sendData(url + "?" + data, null, null).equalsIgnoreCase("OK"))
				continue;
			else
				return false;
		}
		return true;
	}
	
	/***
	 * 
	 * @param url	同步URL
	 * @param list	要同步的数据
	 * @return 未同步的数据
	 */
	public List<String> synMonthDataList(String url,List<String> list)
	{
		List<String> unSynList = new ArrayList<String>();
		
		int failCount = 0;
		
		boolean isContinue = true;
		
		String data = "";
		
		//如果失败10条以后，不再请求，直接把数据写入失败LIST
		for(int i=0; i<list.size(); i++)
		{
			data = list.get(i);
			
			if(!isContinue)
			{
				unSynList.add(data);
				continue;
			}
			
			if(ServiceUtil.sendData(url, null, data).equalsIgnoreCase("OK"))
				continue;
			else
			{
				failCount++;
				
				unSynList.add(data);
				
				if(failCount>10)
					isContinue = false;
			}
		}
		
		return unSynList;
	}
	
	
	
}




