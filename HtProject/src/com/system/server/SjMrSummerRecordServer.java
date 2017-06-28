package com.system.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.system.dao.SjMrSummerRecordDao;
import com.system.database.JdbcControl;
import com.system.model.SjMrSummerRecordModel;
import com.system.util.StringUtil;

public class SjMrSummerRecordServer
{
	public Map<String, Object> loadSjMrSummerRecordData(String startMonth,String endMonth,SjMrSummerRecordModel params,int pageIndex)
	{
		return new SjMrSummerRecordDao().loadSjMrSummerRecordData(startMonth, endMonth, params, pageIndex);
	}
	
	public boolean addSjMrSummerRecord(SjMrSummerRecordModel model)
	{
		return new SjMrSummerRecordDao().addSjMrSummerRecord(model);
	}
	
	public boolean delSjMrSummerRecord(int id)
	{
		return new SjMrSummerRecordDao().delSjMrSummerRecord(id);
	}
	
	public SjMrSummerRecordModel getSjMrSummerRecord(int id)
	{
		return new SjMrSummerRecordDao().getSjMrSummerRecord(id);
	}
	
	public boolean isExistDataInRecord(int year,int month,int troneOrderId)
	{
		return new SjMrSummerRecordDao().isExistDataInRecord(year, month, troneOrderId);
	}
	
	public boolean isExistDataInSummer(int year,int month,int troneOrderId)
	{
		return new SjMrSummerRecordDao().isExistDataInSummer(year, month, troneOrderId);
	}
	
	
	public boolean delSjMrSummer(SjMrSummerRecordModel model)
	{
		if(model==null)
			return false;

		SjMrSummerRecordDao dao = new SjMrSummerRecordDao();
		
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, model.getYear());
		ca.set(Calendar.MONTH,model.getMonth()-1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		String startDate = StringUtil.getDateFormat(ca.getTime());
		ca.add(Calendar.MONTH, 1);
		ca.add(Calendar.DAY_OF_YEAR, -1);
		String endDate = StringUtil.getDateFormat(ca.getTime());
		
		//删除大数据平台的数据
		if(model.getSaveLocate()==1)
		{
			dao.delSjMrSummerData(startDate, endDate, model.getTroneOrderId());
		}
		else if(model.getSaveLocate()==2)
		{
			dao.delMrSummerData(startDate, endDate, model.getTroneOrderId());
		}
		else if(model.getSaveLocate()==3)
		{
			dao.delSjMrSummerData(startDate, endDate, model.getTroneOrderId());
			dao.delMrSummerData(startDate, endDate, model.getTroneOrderId());
		}
		
		return true;
	}
	
	public boolean addSjMrSummer(SjMrSummerRecordModel model)
	{
		
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, model.getYear());
		ca.set(Calendar.MONTH,model.getMonth()-1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.add(Calendar.MONTH, 1);
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		int daysCount = ca.get(Calendar.DAY_OF_MONTH);
		
		List<List<Integer>> dataList = splitDataRowsWithCount(model.getSpDataRows(), model.getCpDataRows(), daysCount);
		
		String sqlSp = "INSERT INTO " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_mr_%s(sp_id,cp_id,mcc,province_id,city_id,trone_id,trone_order_id,mr_date,data_rows,amount,record_type) values\r\n";
		String sqlCp = "INSERT INTO " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_cp_mr_%s(cp_id,mcc,province_id,city_id,trone_order_id,mr_date,data_rows,amount,record_type) values\r\n";
		
		StringBuffer spBuffer = new StringBuffer();
		StringBuffer cpBuffer = new StringBuffer();
		
		for(int i=1; i<=daysCount; i++)
		{
			ca.set(Calendar.DAY_OF_MONTH,i);
			spBuffer.append("(" + model.getSpId() + "," + model.getCpId() + ",460,32,416," + model.getTroneId()	 + "," + model.getTroneOrderId() + ",'" + StringUtil.getDateFormat(ca.getTime())  + "'," + dataList.get(0).get(i-1) + ", " + dataList.get(0).get(i-1)*model.getPrice() + ",1),\r\n");
		}
		
		for(int i=1; i<=daysCount; i++)
		{
			ca.set(Calendar.DAY_OF_MONTH,i);
			cpBuffer.append("(" + model.getCpId() + ",460,32,416," + model.getTroneOrderId() + ",'" + StringUtil.getDateFormat(ca.getTime()) + "'," + dataList.get(1).get(i-1) + ", " +  dataList.get(1).get(i-1)*model.getPrice() +  ",1),\r\n");
		}
		
		JdbcControl control = new JdbcControl();
		
		//只存储大平台
		if(model.getSaveLocate()==1)
		{
			control.execute(String.format(sqlSp, "summer_2") + spBuffer.substring(0,spBuffer.length()-3) + ";\r\n"); 
			control.execute(String.format(sqlCp, "summer_2") + cpBuffer.substring(0,cpBuffer.length()-3) + ";\r\n"); 
		}
		//只存储商务
		else if(model.getSaveLocate()==2)
		{
			control.execute(String.format(sqlSp, "summer") + spBuffer.substring(0,spBuffer.length()-3) + ";\r\n"); 
			control.execute(String.format(sqlCp, "summer") + cpBuffer.substring(0,cpBuffer.length()-3) + ";\r\n"); 
		}
		//两者都存
		else if(model.getSaveLocate()==3)
		{
			control.execute(String.format(sqlSp, "summer_2") + spBuffer.substring(0,spBuffer.length()-3) + ";\r\n"); 
			control.execute(String.format(sqlCp, "summer_2") + cpBuffer.substring(0,cpBuffer.length()-3) + ";\r\n");
			control.execute(String.format(sqlSp, "summer") + spBuffer.substring(0,spBuffer.length()-3) + ";\r\n"); 
			control.execute(String.format(sqlCp, "summer") + cpBuffer.substring(0,cpBuffer.length()-3) + ";\r\n"); 
		}
		
		return true;
	}
	
	private List<List<Integer>> splitDataRowsWithCount(int spDataRows,int cpDataRows,int dayCount)
	{
		List<Integer> listSp = new ArrayList<Integer>();
		List<Integer> listCp = new ArrayList<Integer>();

		List<List<Integer>> list = new ArrayList<List<Integer>>();

		list.add(listSp);
		list.add(listCp);
		
		Random rand = new Random();
		
		int useSpData = 0;
		int useCpData = 0;
		
		for(int i=0; i<dayCount; i++)
		{
			if(i==dayCount-1)
			{
				listSp.add(spDataRows - useSpData);
				listCp.add(cpDataRows - useCpData);
				continue;
			}
			
			int curRowSpData = (spDataRows - useSpData)/(dayCount-i);
			int curRowCpData = (cpDataRows - useCpData)/(dayCount-i);
			
			int randData = curRowSpData*rand.nextInt(100000);
			
			boolean isAdd = randData%2==0 ? true :false;
			
			int curSpData = 0;
			int curCpData = 0;
			
			if (isAdd)
			{
				curSpData = curRowSpData += randData / 1000000;
				curCpData = curRowCpData += randData / 1000000;
			}
			else
			{
				curSpData = curRowSpData -= randData / 1000000;
				curCpData = curRowCpData -= randData / 1000000;
			}
			
			useSpData += curSpData;
			useCpData += curCpData;
			
			listSp.add(curSpData);
			
			if(spDataRows==cpDataRows)
			{
				listCp.add(curSpData);
			}
			else
			{
				listCp.add(curCpData);
			}
		}
		
		return list;
	}
	
	public static void main(String[] args)
	{
		SjMrSummerRecordModel model = new SjMrSummerRecordModel();
		model.setYear(2017);
		model.setMonth(2);
		
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, model.getYear());
		ca.set(Calendar.MONTH,model.getMonth()-1);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		
		String startDate = StringUtil.getDateFormat(ca.getTime());
		
		ca.add(Calendar.MONTH, 1);
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		String endDate = StringUtil.getDateFormat(ca.getTime());
		
		System.out.println(startDate + "---" + endDate);
	}

}
