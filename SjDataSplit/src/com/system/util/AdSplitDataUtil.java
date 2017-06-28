package com.system.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AdSplitDataUtil
{
	private static List<Integer> splitDataWithCount(int dataRows,int count)
	{
		List<Integer> list = new ArrayList<>();

		Random rand = new Random();
		
		int useData = 0;
		
		for(int i=0; i<count; i++)
		{
			if(i==count-1)
			{
				list.add(rand.nextInt(count-1),dataRows - useData);
				break;
			}
			
			int curRowData = (dataRows - useData)/(count-i);
			
			int randData = rand.nextInt(10000);
			
			int randSpData = curRowData*randData;
			
			boolean isAdd = randSpData%2==0 ? true :false;
			
			int curData = 0;
			
			if (isAdd)
			{
				curData = curRowData += randSpData / 100000;
			}
			else
			{
				curData = curRowData -= randSpData / 100000;
			}
			
			useData += curData;
			
			list.add(curData);
			
		}
		
		return list;
	}
	
	public static void main(String[] args)
	{
		//UnionID-->cpId
		//GameId-->spTroneId
		
		
		String sql = "INSERT INTO htreport.datas(Date,UnionID,GameID,ActiveCount,Price,Money,CreateTime)VALUES\r\n";
		
		ArrayList<String> list = FileUtil.readFileToList("d:/2.txt", "GBK");
		
		StringBuffer sb = new StringBuffer(512);
		
		for(int i=0; i<list.size(); i++)
		{
			String oriStr = list.get(i);
			String[] datas = oriStr.split("\t");
			
			int spId = StringUtil.getInteger(datas[0], -1);
			String spName = StringUtil.getString(datas[1], "");
			int spTroneId = StringUtil.getInteger(datas[2], 1);
			String spTroneName = StringUtil.getString(datas[3], "");
			float spPrice = StringUtil.getFloat(datas[4], 0.0F);
			int year = StringUtil.getInteger(datas[7], 0);
			int month = StringUtil.getInteger(datas[8], 0);
			int cpId = StringUtil.getInteger(datas[9], -1);
			String cpName = StringUtil.getString(datas[10], "");
			float cpPrice = StringUtil.getFloat(datas[11], 0.0F);
			int cpDataCount = StringUtil.getInteger(datas[12], 0);
			float cpDataAmount = StringUtil.getFloat(datas[13], 0.0F);
			
			if(year<=2015 || month<=0 || cpId<=0 || cpPrice<=0 || cpDataCount<=0 || cpDataAmount<=0)
				continue;
			
			Calendar ca = Calendar.getInstance();
			ca.set(Calendar.YEAR, year);
			ca.set(Calendar.MONTH,month);
			ca.set(Calendar.DAY_OF_MONTH,1);
			ca.add(Calendar.DAY_OF_MONTH, -1);
			int monthDays = ca.get(Calendar.DAY_OF_MONTH);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<Integer> dataList = splitDataWithCount(cpDataCount,monthDays);
			
			for(int j=1; j<=dataList.size(); j++)
			{
				ca.set(Calendar.DAY_OF_MONTH, j);
				sb.append("('" + sdf.format(ca.getTime()) + "'," + cpId + "," + spTroneId + "," + dataList.get(j-1) + "," + cpPrice + "," + (cpPrice*dataList.get(j-1)) + ",'0000-00-00 00:00:00'),\r\n");
			}
		}
			
		sql += sb.substring(0,sb.length()-3) + ";";
		
		System.out.println(sql);
	}
}
