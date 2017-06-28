package com.system.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class SplitDataUtil
{
	private static List<Integer> splitDataWithCount(int data,int count)
	{
		List<Integer> list = new ArrayList<>();
		
		Random rand = new Random();
		
		int useData = 0;
		
		for(int i=0; i<count; i++)
		{
			
			if(i==count-1)
			{
				list.add(data - useData);
				continue;
			}
			
			int curRowData = (data - useData)/(count-i);
			
			int randData = curRowData*rand.nextInt(100000);
			
			boolean isAdd = randData%2==0 ? true :false;
			
			int curData = 0;
			
			if (isAdd)
				curData = curRowData += randData / 1000000;
			else
				curData = curRowData -= randData / 1000000;
			
			useData += curData;
			
			list.add(curData);
		}
		
		return list;
	}
	
	public static List<List<Integer>> splitDataWithCount(int dataRows,int showDataRows,int count)
	{
		List<Integer> listSp = new ArrayList<>();
		List<Integer> listCp = new ArrayList<>();

		List<List<Integer>> list = new ArrayList<>();

		list.add(listSp);
		list.add(listCp);
		
		Random rand = new Random();
		
		int useSpData = 0;
		int useCpData = 0;
		
		for(int i=0; i<count; i++)
		{
			if(i==count-1)
			{
				listSp.add(dataRows - useSpData);
				listCp.add(showDataRows - useCpData);
				continue;
			}
			
			int curRowSpData = (dataRows - useSpData)/(count-i);
			int curRowCpData = (showDataRows - useCpData)/(count-i);
			
			int randData = rand.nextInt(100000);
			
			int randSpData = curRowSpData*randData;
			int randCpData = curRowCpData*randData;
			
			boolean isAdd = randSpData%2==0 ? true :false;
			
			int curSpData = 0;
			int curCpData = 0;
			
			if (isAdd)
			{
				curSpData = curRowSpData += randSpData / 1000000;
				curCpData = curRowCpData += randCpData / 1000000;
			}
			else
			{
				curSpData = curRowSpData -= randSpData / 1000000;
				curCpData = curRowCpData -= randCpData / 1000000;
			}
			
			useSpData += curSpData;
			useCpData += curCpData;
			
			listSp.add(curSpData);
			
			if(dataRows==showDataRows)
			{
				listCp.add(curSpData);
			}
			else
			{
				listCp.add(curCpData);
			}
			//System.out.println(randSpData + "-" + randCpData + "-" + curSpData + "-" + curCpData + "-" + (curSpData-curCpData) + "-" + StringUtil.getPercent(curSpData-curCpData, curSpData));
		}
		
		return list;
	}
	
	
	public static List<List<Integer>> splitDataWithCount2(int dataRows,int showDataRows,int count,List<Integer> addList)
	{
		List<Integer> listSp = new ArrayList<>();
		List<Integer> listCp = new ArrayList<>();

		List<List<Integer>> list = new ArrayList<>();

		list.add(listSp);
		list.add(listCp);
		
		Random rand = new Random();
		
		int useSpData = 0;
		int useCpData = 0;
		
		for(int i=0; i<count; i++)
		{
			if(i==count-1)
			{
				listSp.add(dataRows - useSpData);
				listCp.add(showDataRows - useCpData);
				continue;
			}
			
			int curRowSpData = (dataRows - useSpData)/(count-i);
			int curRowCpData = (showDataRows - useCpData)/(count-i);
			
			int randData = rand.nextInt(100000);
			
			int randSpData = curRowSpData*randData;
			int randCpData = curRowCpData*randData;
			
			boolean isAdd = randSpData%2==0 ? true :false;
			
			if(addList.contains(i))
			{
				isAdd = true;
			}
			
			int curSpData = 0;
			int curCpData = 0;
			
			if (isAdd)
			{
				curSpData = curRowSpData += randSpData / 1000000;
				curCpData = curRowCpData += randCpData / 1000000;
			}
			else
			{
				curSpData = curRowSpData -= randSpData / 1000000;
				curCpData = curRowCpData -= randCpData / 1000000;
			}
			
			useSpData += curSpData;
			useCpData += curCpData;
			
			listSp.add(curSpData);
			
			if(dataRows==showDataRows)
			{
				listCp.add(curSpData);
			}
			else
			{
				listCp.add(curCpData);
			}
			//System.out.println(randSpData + "-" + randCpData + "-" + curSpData + "-" + curCpData + "-" + (curSpData-curCpData) + "-" + StringUtil.getPercent(curSpData-curCpData, curSpData));
		}
		
		return list;
	}
	
	public static List<List<Integer>> splitDataWithCount(int dataRows,int showDataRows,int year,int month)
	{
		List<Integer> listSp = new ArrayList<>();
		List<Integer> listCp = new ArrayList<>();

		List<List<Integer>> list = new ArrayList<>();

		list.add(listSp);
		list.add(listCp);
		
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.MONTH,month);
		ca.set(Calendar.DAY_OF_MONTH,1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		
		int curMonthDays = ca.get(Calendar.DAY_OF_MONTH);
		
		Random rand = new Random();
		
		int useSpData = 0;
		int useCpData = 0;
		
		for(int i=0; i<curMonthDays; i++)
		{
			if(i==curMonthDays-1)
			{
				listSp.add(dataRows - useSpData);
				listCp.add(showDataRows - useCpData);
				continue;
			}
			
			int curRowSpData = (dataRows - useSpData)/(curMonthDays-i);
			int curRowCpData = (showDataRows - useCpData)/(curMonthDays-i);
			
			int randData = rand.nextInt(1000);
			
			int randSpData = curRowSpData*randData;
			int randCpData = curRowCpData*randData;
			
			boolean isAdd = randSpData%2==0 ? true :false;
			
			ca.set(Calendar.DAY_OF_MONTH, i+1);
			boolean isWeekDay = false;
			
			if(ca.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY 
					|| ca.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			{
				isAdd= true;
				isWeekDay = true;
			}
			
			int curSpData = 0;
			int curCpData = 0;
			
			if (isAdd)
			{
				curSpData = curRowSpData += (randSpData + (isWeekDay ? 1000 : 0)) / 10000;
				curCpData = curRowCpData += (randCpData + (isWeekDay ? 1000 : 0)) / 10000;
			}
			else
			{
				curSpData = curRowSpData -= randSpData / 10000;
				curCpData = curRowCpData -= randCpData / 10000;
			}
			
			useSpData += curSpData;
			useCpData += curCpData;
			
			listSp.add(curSpData);
			
			if(dataRows==showDataRows)
			{
				listCp.add(curSpData);
			}
			else
			{
				listCp.add(curCpData);
			}
			System.out.println(StringUtil.getDateFormat(ca.getTime()) + "-->" + curSpData + "--" + curCpData);
		}
		
		return list;
	}
	
	public static String analyData(int troneId,int troneOrderId,int dataAmount,int showDataAmount, int year,int month,String remark)
	{
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.MONTH,month);
		ca.set(Calendar.DAY_OF_MONTH,1);
		ca.add(Calendar.DAY_OF_MONTH, -1);
		int monthDays = ca.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		List<List<Integer>> list = SplitDataUtil.splitDataWithCount(dataAmount,showDataAmount,monthDays);
		
		String sqlSp = "#" + year + "-" + month + ":" + remark + " \r\nINSERT INTO daily_log.tbl_mr_summer_2(sp_id,cp_id,mcc,province_id,city_id,trone_id,trone_order_id,mr_date,data_rows,amount,record_type) values\r\n";
		String sqlCp = "INSERT INTO daily_log.`tbl_cp_mr_summer_2`(cp_id,mcc,province_id,city_id,trone_order_id,mr_date,data_rows,amount,record_type) values\r\n";
		StringBuffer spBuffer = new StringBuffer();
		StringBuffer cpBuffer = new StringBuffer();
		
		for(int i=1; i<=monthDays; i++)
		{
			ca.set(Calendar.DAY_OF_MONTH,i);
			spBuffer.append("(1,1,460,32,416," + troneId + "," + troneOrderId + ",'" +sdf.format(ca.getTime())  + "'," + list.get(0).get(i-1) + ", " + list.get(0).get(i-1) + ",1),\r\n");
		}
		
		sqlSp += spBuffer.substring(0,spBuffer.length()-3);
		sqlSp += ";\r\n";
		
		for(int i=1; i<=monthDays; i++)
		{
			ca.set(Calendar.DAY_OF_MONTH,i);
			cpBuffer.append("(1,460,32,416," + troneOrderId + ",'" + sdf.format(ca.getTime()) + "'," + list.get(1).get(i-1) + ", " +  list.get(1).get(i-1) +  ",1),\r\n");
		}
		
		sqlCp += cpBuffer.substring(0,cpBuffer.length()-3);
		sqlCp += ";\r\n\r\n";
		
		return sqlSp + sqlCp;
	}
	
	public static void main(String[] args)
	{
//		List<Integer> list = SplitDataUtil.splitDataWithCount(1872, 31);
//		System.out.println(list);
//		int fff = 0;
//		for(int i=0; i<list.size(); i++)
//		{
//			fff += list.get(i);
//		}
//		System.out.println(fff);
		
//		List<List<Integer>> list = SplitDataUtil.splitDataWithCount(408358,397523,30);
//		
//		System.out.println(list.get(0));
//		System.out.println(list.get(1));
//		
//		int spCount = 0;
//		int cpCount = 0;
//		
//		for(int i=0; i<list.get(0).size(); i++)
//		{
//			spCount += list.get(0).get(i);
//		}
//		
//		for(int i=0; i<list.get(1).size(); i++)
//		{
//			cpCount += list.get(1).get(i);
//		}
//		System.out.println(spCount);
//		System.out.println(cpCount);
		
		
//		Calendar ca = Calendar.getInstance();
//		ca.set(Calendar.YEAR, 2016);
//		ca.set(Calendar.MONTH,1);
//		ca.set(Calendar.DAY_OF_MONTH,1);
//		//ca.add(Calendar.DAY_OF_MONTH, -1);
//		int monthData = ca.get(Calendar.DAY_OF_MONTH);
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime()));
//		System.out.println(monthData);
		
		//analyData(189, 385, 408358,397523, 2016, 2,"");
		
		System.out.println("start");
		
		String filePath = "D:/1.txt";
		
		List<String> list = FileUtil.readFileToList(filePath, "GBK");
		
		StringBuffer sb = new StringBuffer(512);
		
		for(String data : list)
		{
			String[] datas = data.split("\t");
			int troneId = 0;
			int troneOrderId = 0;
			int dataAmount = 0; 
			int dataShowAmount = 0;
			int year = 0;
			int month = 0;
			String remark;
			
			troneId = StringUtil.getInteger(datas[0], -1);
			troneOrderId = StringUtil.getInteger(datas[1], -1);
			dataAmount = StringUtil.getInteger(datas[2], 0);
			dataShowAmount = StringUtil.getInteger(datas[3], 0);
			year = StringUtil.getInteger(datas[4], 2000);
			month = StringUtil.getInteger(datas[5], 1);
			remark = StringUtil.getString(datas[6], "");
			
			sb.append(analyData(troneId,troneOrderId,dataAmount,dataShowAmount,year,month,remark));
		}
		
		//FileUtil.saveDataToFile(sb, "D:/SJ_SQL_FILES/", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".txt");
		
		System.out.println("finish");
	}
	
	public static List<List<Integer>> spritData(int spDataRows,int cpDataRows,int year,int month)
	{
		List<Integer> listSp = new ArrayList<>();
		List<Integer> listCp = new ArrayList<>();

		List<List<Integer>> list = new ArrayList<>();

		list.add(listSp);
		list.add(listCp);
		
		if(spDataRows<cpDataRows)
		{
			System.out.println("基础数据不正确：spDataRows:" + spDataRows + ";cpDataRows" + cpDataRows + ";year:" + year + ";month:" + month);
			return list;
		}
		
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.MONTH,month);
		ca.set(Calendar.DAY_OF_MONTH,1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		//把数据按 -%5 到 +5% 的区间浮动，然后逢周未就加上10%和平均值再浮动，确保周未的数据会比平时的数据多10%左右 
		int curMonthDays = ca.get(Calendar.DAY_OF_MONTH);
		
		int spAvgDataRows = spDataRows/curMonthDays;
		int spMinDataRows = (int)(spAvgDataRows*0.95); 
		int spFloatDataRows = (int)(spAvgDataRows*0.05);
		int spTenPercentDataRows = (int)(spAvgDataRows*0.1);
		
		int cpAvgDataRows = cpDataRows/curMonthDays;
		int cpMinDataRows = (int)(cpAvgDataRows*0.95); 
		int cpTenPercentDataRows = (int)(cpAvgDataRows*0.1);
		
		int useSpDataRows = 0;
		int curSpDataRows  = 0;
		int useCpDataRows = 0;
		int curCpDataRows  = 0;
		
		Random rand = new Random();
		
		for(int i=0; i<curMonthDays;i++)
		{
			boolean isWeekDay = false;
			
			ca.set(Calendar.DAY_OF_MONTH, i+1);
			
			if(ca.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY 
					|| ca.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			{
				isWeekDay = true;
			}
			
			int spRandData = spFloatDataRows==0 ? 0 : rand.nextInt(spFloatDataRows);
			
			boolean isAdd = (spRandData%2==0 || isWeekDay) ? true : false;
			
			if(isAdd)
			{
				curSpDataRows = spMinDataRows + (isWeekDay ? spTenPercentDataRows : 0) + spRandData; 
				curCpDataRows = cpMinDataRows + (isWeekDay ? cpTenPercentDataRows : 0) + spRandData; 
			}
			else
			{
				curSpDataRows = spAvgDataRows - spRandData;
				curCpDataRows = cpAvgDataRows - spRandData;
			}
			
			useSpDataRows += curSpDataRows;
			
			if(spDataRows==cpDataRows)
				curCpDataRows = curSpDataRows;
			
			useCpDataRows += curCpDataRows;
			
			listSp.add(curSpDataRows);
			listCp.add(curCpDataRows);
			
			//System.out.println(StringUtil.getDateFormat(ca.getTime()) + "-" + randData + "---" + curSpDataRows + "---" + curCpDataRows + "---(" + (curSpDataRows - curCpDataRows) + ")");
		}
		
		//把按上面算法算出来的数据，多出来或少了的数据，平分到每一天
		int spDivCount = spDataRows-useSpDataRows;
		int cpDivCount = cpDataRows-useCpDataRows;
		
		int spDivAvgCount = Math.abs(spDivCount)/curMonthDays;
		int cpDivAvgCount = Math.abs(cpDivCount)/curMonthDays;
		
		spDivAvgCount = spDivAvgCount==0 ? 1 : spDivAvgCount;
		cpDivAvgCount = cpDivAvgCount==0 ? 1 : cpDivAvgCount;
		
		int useSpDivCount = 0;
		int useCpDivCount = 0;
		
		if(spDivCount>0)
		{
			for(int i=0; i<curMonthDays;i++)
			{
				listSp.set(i, listSp.get(i) + spDivAvgCount);
				useSpDivCount += spDivAvgCount;
				if(useSpDivCount + spDivAvgCount >= spDivCount || (i+2)>=curMonthDays)
				{
					listSp.set(i+1, listSp.get(i+1) + (spDivCount-useSpDivCount));
					break;
				}
			}
		}
		else if(spDivCount<0)
		{
			for(int i=0; i<curMonthDays;i++)
			{
				listSp.set(i, listSp.get(i) - spDivAvgCount);
				useSpDivCount += spDivAvgCount;
				if(useSpDivCount + spDivAvgCount >= Math.abs(spDivCount) || (i+2)>=curMonthDays)
				{
					listSp.set(i+1, listSp.get(i+1) - (Math.abs(spDivCount)-useSpDivCount));
					break;
				}
			}
		}
		
		if(cpDivCount>0)
		{
			for(int i=0; i<curMonthDays;i++)
			{
				listCp.set(i, listCp.get(i) + cpDivAvgCount);
				useCpDivCount += cpDivAvgCount;
				if(useCpDivCount + cpDivAvgCount >= cpDivCount || (i+2)>=curMonthDays)
				{
					listCp.set(i+1, listCp.get(i+1) + (cpDivCount-useCpDivCount));
					break;
				}
			}
		}
		else if(cpDivCount<0)
		{
			for(int i=0; i<curMonthDays;i++)
			{
				listCp.set(i, listCp.get(i) - cpDivAvgCount);
				useCpDivCount += cpDivAvgCount;
				if(useCpDivCount + cpDivAvgCount >= Math.abs(cpDivCount) || (i+2)>=curMonthDays)
				{
					listCp.set(i+1, listCp.get(i+1) - (Math.abs(cpDivCount)-useCpDivCount));
					break;
				}
			}
		}
		
		//验证上游的数据是否一定大于或等于下游，如果不是，则要把少的数据拿出来平分到其它天的数据里去，但最终一定要保证上下游数据合理并且总数和传过来的数据一致
		int lastSpDataRows = 0;
		int lastCpDataRows = 0;
		
		while(true)
		{
			boolean isDataOk = true;
			lastSpDataRows = 0;
			lastCpDataRows = 0;
			for(int i=0; i<listSp.size(); i++)
			{
				lastSpDataRows += listSp.get(i);
				lastCpDataRows += listCp.get(i);
				int divData = listSp.get(i) - listCp.get(i);
				if(divData<0)
				{
					listCp.set(i,listCp.get(i) - Math.abs(divData));
					balanceData(listSp,listCp,Math.abs(divData));
					isDataOk = false;
					break;
				}
			}
			
			if(isDataOk)
				break;
		}
		
		//将最后一天的数据和前面的随机一天数据对换
		int randDays = rand.nextInt(curMonthDays-1);
		int tempSpDataRows = listSp.get(randDays);
		listSp.set(randDays,listSp.get(curMonthDays-1));
		listSp.set(curMonthDays-1,tempSpDataRows);
		
		int tempCpDataRows = listCp.get(randDays);
		listCp.set(randDays,listCp.get(curMonthDays-1));
		listCp.set(curMonthDays-1,tempCpDataRows);
		
		if(lastSpDataRows!=spDataRows || lastCpDataRows!= cpDataRows)
		{
			System.out.println("spDataRows:" + spDataRows + ";cpDataRows" + cpDataRows + ";year:" + year + ";month:" + month + ";lastSpDataRows:" + lastSpDataRows +";lastCpDataRows:" + lastCpDataRows);
		}
		
		return list;
	}
	
	public static void balanceData(List<Integer> spList,List<Integer> cpList,int dataRows)
	{
		System.out.println("balanceData data");
		int divAvgCount = dataRows/cpList.size();
		
		divAvgCount = divAvgCount==0 ? 1 : divAvgCount;
		
		int useDivCount =0;
		
		for(int i=0; i<cpList.size(); i++)
		{
			if(spList.get(i)-cpList.get(i)>=divAvgCount)
			{
				cpList.set(i,cpList.get(i)+divAvgCount);
				
				useDivCount += divAvgCount;
				
				if(useDivCount+divAvgCount>dataRows)
					break;
			}
		}
	}
}
