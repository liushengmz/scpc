package com.system.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WmDataSplitUtil
{
	public static void main(String[] args)
	{
		System.out.println("start...");
		String sql = "INSERT INTO daily_log.tbl_wm_summer_2(trone_order_id,mr_date,data_rows,data_amount,show_data_rows,show_data_amount) VALUES\r\n";
		ArrayList<String> dataList = FileUtil.readFileToList("D:/3.txt", "GBK");
		Calendar ca = Calendar.getInstance();
		StringBuffer sb = new StringBuffer(512);
		sb.append(sql);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		for(String str : dataList)
		{
			int troneOrderId = 0;
			float spPrice = 0;
			int spDataRows = 0;
			int year = 0;
			int month = 0;
			float cpPrice = 0;
			int cpDataRows = 0;
			String[] datas = str.split("\t");
			troneOrderId = StringUtil.getInteger(datas[0], -1);
			spPrice = StringUtil.getFloat(datas[4], 0);
			spDataRows = StringUtil.getInteger(datas[8], 0);
			year = StringUtil.getInteger(datas[10], 0);
			month = StringUtil.getInteger(datas[11], 0);
			cpPrice = StringUtil.getFloat(datas[13], 0);
			cpDataRows = StringUtil.getInteger(datas[15], 0);
			ca.set(year, month, 1);
			ca.add(Calendar.DAY_OF_MONTH, -1);
			int monthDays = ca.get(Calendar.DAY_OF_MONTH);
			
			List<List<Integer>> list = SplitDataUtil.spritData(spDataRows,cpDataRows,year,month);
			List<Integer> spList = list.get(0);
			List<Integer> cpList = list.get(1);
			for(int i=0; i<monthDays; i++)
			{
				ca.set(Calendar.DAY_OF_MONTH, i+1);
				sb.append("(" + troneOrderId + ",'" + sdf.format(ca.getTime()) + "'," + spList.get(i) + ","+ StringUtil.getDecimalFormat(spList.get(i)*spPrice) +"," + cpList.get(i) + ","+ StringUtil.getDecimalFormat(cpList.get(i)*cpPrice) +"),\r\n");
			}
		}
		
		sb.delete(sb.length()-3, sb.length());
		
		sb.append(";");
		
		FileUtil.saveDataToFile(sb, "D:/SJ_SQL_FILES/", "WmDataSplitUtil_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".txt");
		
		System.out.println("finish");
	}
}
