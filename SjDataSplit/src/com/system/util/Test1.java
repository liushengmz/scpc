package com.system.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Test1
{
	public static void main(String[] args)
	{
		//spritDataWithFloat(924000,900000,2017,6);
		
		//List<List<Integer>> list = SplitDataUtil.splitDataWithCount(924000, 900000, 2017, 6);
		
		List<List<Integer>> list = SplitDataUtil.spritData(48429, 45524, 2017, 5);
		List<Integer> listSp = list.get(0);
		List<Integer> listCp = list.get(1);
		int lastSpDataRows = 0;
		int lastCpDataRows = 0;
		for(int i=0; i<listSp.size(); i++)
		{
			lastSpDataRows += listSp.get(i);
			lastCpDataRows += listCp.get(i);
		}
		System.out.println(lastSpDataRows + "--" + lastCpDataRows);
		
	}
	
	public static void spritDataWithFloat(int spDataRows,int cpDataRows,int year,int month)
	{
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.MONTH,month);
		ca.set(Calendar.DAY_OF_MONTH,1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		
		int curMonthDays = ca.get(Calendar.DAY_OF_MONTH);
		
		List<Integer> weekDayList = new ArrayList<Integer>();
		
		for(int i=1; i<curMonthDays; i++)
		{
			ca.set(Calendar.DAY_OF_MONTH, i);
			
			if(ca.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY 
					|| ca.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			{
				weekDayList.add(i-1);
			}
		}
		
		int cutSpDataRows = (int) (spDataRows*0.03);
		int cutCpDataRows = spDataRows==cpDataRows ? cutSpDataRows : (int)(cpDataRows*0.03);
		
		cutSpDataRows = (cutSpDataRows/weekDayList.size())*weekDayList.size();
		cutCpDataRows = spDataRows==cpDataRows ? cutSpDataRows : (cutCpDataRows/weekDayList.size())*weekDayList.size();
		
		int singleSpDataRows = cutSpDataRows / weekDayList.size();
		int singleCpDataRows = cutCpDataRows / weekDayList.size();
		
		List<List<Integer>> list = SplitDataUtil.splitDataWithCount2(spDataRows - cutSpDataRows,cpDataRows - cutCpDataRows, curMonthDays,weekDayList);
		
		for(int i=0; i<weekDayList.size(); i++)
		{
			ca.set(Calendar.DAY_OF_MONTH, weekDayList.get(i));
			list.get(0).set(weekDayList.get(i), list.get(0).get(weekDayList.get(i)) + singleSpDataRows);
			list.get(1).set(weekDayList.get(i), list.get(1).get(weekDayList.get(i)) + singleCpDataRows);
		}
		
		for(int i=0; i<curMonthDays; i++)
		{
			ca.set(Calendar.DAY_OF_MONTH, i+1);
			System.out.println(StringUtil.getDateFormat(ca.getTime()) +"-->" + list.get(0).get(i) + "--" + list.get(1).get(i));
		}
	}
	
	public static List<List<Integer>> spritDataWith(int spDataRows,int cpDataRows,int year,int month)
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
		listSp.add(randDays,listSp.get(curMonthDays-1));
		listSp.add(curMonthDays-1,tempSpDataRows);
		int tempCpDataRows = listCp.get(randDays);
		listCp.add(randDays,listCp.get(curMonthDays-1));
		listCp.add(curMonthDays-1,tempCpDataRows);
		
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
