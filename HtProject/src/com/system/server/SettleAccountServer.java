package com.system.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.dao.SettleAcountDao;
import com.system.excel.ExcelManager;
import com.system.excel.ZipUtil;
import com.system.model.CpBillingModel;
import com.system.model.CpModel;
import com.system.model.SettleAccountModel;
import com.system.util.ConfigManager;
import com.system.vmodel.SpFinanceShowModel;

public class SettleAccountServer
{
	public List<SettleAccountModel> loadSpSettleAccountList(int spId,String startDate,String endDate,int dateType)
	{
		List<SettleAccountModel> list = new SettleAcountDao().loadSpSettleAccountData(spId, startDate, endDate,dateType);
		//List<SettleAccountModel> list = loadSpSettleAccountFromFile("c:/duizhangdan.txt");
		return list;
	}
	
	public List<SettleAccountModel> loadCpSettleAccountList(int cpId,String startDate,String endDate,int dateType)
	{
		List<SettleAccountModel> list = new SettleAcountDao().loadCpSettleAccountData(cpId, startDate, endDate,dateType);
		//List<SettleAccountModel> list = loadSpSettleAccountFromFile("c:/duizhangdan.txt");
		return list;
	}
	
	public Map<String, List<SpFinanceShowModel>> loadSpSettleAccountData(String startDate,String endDate,int spId,int jsType)
	{
		List<SpFinanceShowModel> list = new SettleAcountDao().loadSpSettleAccountData(startDate, endDate,spId,jsType);
		Map<String, List<SpFinanceShowModel>> map = new HashMap<String, List<SpFinanceShowModel>>();
		
		List<SpFinanceShowModel> tmpList = null;
		
		for(SpFinanceShowModel model : list)
		{
			if(map.containsKey(model.getShortName()))
			{
				tmpList = map.get(model.getShortName());
			}
			else
			{
				tmpList = new ArrayList<SpFinanceShowModel>();
				map.put(model.getShortName(), tmpList);
			}
			tmpList.add(model);
		}
		return map;
	}
	
	
	public Map<String, List<SpFinanceShowModel>> loadCpSettleAccountData(String startDate,String endDate,int cpId,int jsType)
	{
		List<SpFinanceShowModel> list = new SettleAcountDao().loadCpSettleAccountData(startDate, endDate,cpId,jsType);
		
		Map<String, List<SpFinanceShowModel>> map = new HashMap<String, List<SpFinanceShowModel>>();
		
		List<SpFinanceShowModel> tmpList = null;
		
		for(SpFinanceShowModel model : list)
		{
			if(map.containsKey(model.getShortName()))
			{
				tmpList = map.get(model.getShortName());
			}
			else
			{
				tmpList = new ArrayList<SpFinanceShowModel>();
				map.put(model.getShortName(), tmpList);
			}
			tmpList.add(model);
		}
		return map;
	}
	
	public Map<String, List<SpFinanceShowModel>> loadCpSettleAccountDataAll(String startDate,String endDate,int cpId,int jsType)
	{
		List<SpFinanceShowModel> list = new SettleAcountDao().loadCpSettleAccountDataAll(startDate, endDate,cpId,jsType);
		
		Map<String, List<SpFinanceShowModel>> map = new HashMap<String, List<SpFinanceShowModel>>();
		
		List<SpFinanceShowModel> tmpList = null;
		
		for(SpFinanceShowModel model : list)
		{
			if(map.containsKey(model.getShortName()))
			{
				tmpList = map.get(model.getShortName());
			}
			else
			{
				tmpList = new ArrayList<SpFinanceShowModel>();
				map.put(model.getShortName(), tmpList);
			}
			tmpList.add(model);
		}
		return map;
	}
	
	public Map<String, List<SpFinanceShowModel>> loadSpSettleAccountDataAll(String startDate,String endDate,int spId,int jsType)
	{
		List<SpFinanceShowModel> list = new SettleAcountDao().loadSpSettleAccountDataAll(startDate, endDate,spId,jsType);
		
		Map<String, List<SpFinanceShowModel>> map = new HashMap<String, List<SpFinanceShowModel>>();
		
		List<SpFinanceShowModel> tmpList = null;
		
		for(SpFinanceShowModel model : list)
		{
			if(map.containsKey(model.getShortName()))
			{
				tmpList = map.get(model.getShortName());
			}
			else
			{
				tmpList = new ArrayList<SpFinanceShowModel>();
				map.put(model.getShortName(), tmpList);
			}
			tmpList.add(model);
		}
		return map;
	}
	
	//channelType 1 SP 2 CP
	public void exportSettleAccount(int channelType,int dateType,String channelName,String startDate,String endDate,List<SettleAccountModel> list,OutputStream os)
	{
		String dateTypeValue = "";
		
		switch(dateType)
		{
			case 0:
				dateTypeValue = "对公周结";
				break;
			case 1:
				dateTypeValue = "对公双周结";
				break;
			case 2:
				dateTypeValue = "对公N+1结";
				break;
			case 3:
				dateTypeValue = "对私周结";
				break;
			case 4:
				dateTypeValue = "对私双周结";
				break;
			case 5:
				dateTypeValue = "对私月结";
				break;
			case 6:
				dateTypeValue = "见帐单结";
				break;
			case 7:
				dateTypeValue = "对公N+2结";
				break;
			case 8:
				dateTypeValue = "对公N+3结";
				break;
				
			default:
				break;
		}
		
		String date = getDateFormat(startDate,endDate);
		
		String filePath = ConfigManager.getConfigData("EXCEL_DEMO") + (channelType==1 ? "SpDemo.xls" : "CpDemo.xls");
		new ExcelManager().writeSettleAccountToExcel(dateTypeValue, date, channelName, list, filePath, os);
	}
	
	private String getDateFormat(String startDate,String endDate)
	{
		String date = startDate + ":" + endDate;
		try
		{
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			return sdf2.format(sdf1.parse(startDate)) + "-" + sdf2.format(sdf1.parse(endDate));
		}
		catch(Exception ex)
		{
			
		}
		return date;
	}
	/*
	 * SP商务只能看到自己的基础数据
	 * */
	public Map<String, List<SpFinanceShowModel>> loadSpSettleAccountDataAll(String startDate,String endDate,int spId,int jsType,int userId,int rightType)
	{
		List<SpFinanceShowModel> list = new SettleAcountDao().loadSpSettleAccountDataAll(startDate, endDate,spId,jsType,userId, rightType);
		
		Map<String, List<SpFinanceShowModel>> map = new HashMap<String, List<SpFinanceShowModel>>();
		
		List<SpFinanceShowModel> tmpList = null;
		
		for(SpFinanceShowModel model : list)
		{
			if(map.containsKey(model.getShortName()))
			{
				tmpList = map.get(model.getShortName());
			}
			else
			{
				tmpList = new ArrayList<SpFinanceShowModel>();
				map.put(model.getShortName(), tmpList);
			}
			tmpList.add(model);
		}
		return map;
	}
	/**
	 * SP商务只能查看自己账单基础数据
	 * @param startDate
	 * @param endDate
	 * @param spId
	 * @param jsType
	 * @return
	 */
	public Map<String, List<SpFinanceShowModel>> loadSpSettleAccountData(String startDate,String endDate,int spId,int jsType,int userId,int rightType)
	{
		List<SpFinanceShowModel> list = new SettleAcountDao().loadSpSettleAccountData(startDate, endDate,spId,jsType,userId,rightType);
		Map<String, List<SpFinanceShowModel>> map = new HashMap<String, List<SpFinanceShowModel>>();
		
		List<SpFinanceShowModel> tmpList = null;
		
		for(SpFinanceShowModel model : list)
		{
			if(map.containsKey(model.getShortName()))
			{
				tmpList = map.get(model.getShortName());
			}
			else
			{
				tmpList = new ArrayList<SpFinanceShowModel>();
				map.put(model.getShortName(), tmpList);
			}
			tmpList.add(model);
		}
		return map;
	}
	/**
	 * 导出excel zip文件
	 * @param channelType
	 * @param dateType
	 * @param channelName
	 * @param startDate
	 * @param endDate
	 * @param list
	 * @param os
	 * @throws IOException 
	 */
	//channelType 1 SP 2 CP
		public void exportSettleAccountZip(int channelType,int dateType,String channelName,String startDate,String endDate,List<SettleAccountModel> list,OutputStream os) throws IOException 
		{
			String dateTypeValue = "";
			String fileName=channelName + "(" + startDate + "_" + endDate + ").xls";
			switch(dateType)
			{
				case 0:
					dateTypeValue = "对公周结";
					break;
				case 1:
					dateTypeValue = "对公双周结";
					break;
				case 2:
					dateTypeValue = "对公N+1结";
					break;
				case 3:
					dateTypeValue = "对私周结";
					break;
				case 4:
					dateTypeValue = "对私双周结";
					break;
				case 5:
					dateTypeValue = "对私月结";
					break;
				case 6:
					dateTypeValue = "见帐单结";
					break;
				case 7:
					dateTypeValue = "对公N+2结";
					break;
				default:
					break;
			}
			
			String date = getDateFormat(startDate,endDate);
			
			String filePath = ConfigManager.getConfigData("EXCEL_DEMO") + (channelType==1 ? "SpDemo.xls" : "CpDemo.xls");
			//excel 保存路径
			String excelPath=ConfigManager.getConfigData("EXCEL_ZIP");
			//创建excel文件夹
			ZipUtil.createFile(excelPath);
			//创建excel文件
			new ExcelManager().writeSettleAccountToExcelZip(dateTypeValue, date, channelName, list, filePath,excelPath,fileName);
			//创建zip文件			
			String time=Calendar.getInstance().getTimeInMillis()+"";  
			ZipUtil.craeteZipPath(excelPath, time+"ZipDemo");
			//删除excel文件
			File file=new File(excelPath);
			ZipUtil.deleteExcelPath(file);
			//重新创建文件夹
			file.mkdirs();
			//写入流
			zipToOutstream(excelPath+time+"ZipDemo"+".zip",os);
			//删除zip文件
			ZipUtil.deleteExcelPath(new File(excelPath+time+"ZipDemo.zip"));

	
		}
		
		/**
		 * 导出excel zip文件
		 * @param channelType
		 * @param dateType
		 * @param channelName
		 * @param startDate
		 * @param endDate
		 * @param list
		 * @param os
		 * @throws IOException 
		 */
		//channelType 1 SP 2 CP
			public void exportSettleAccountBatchZip(int channelType,String cpBillingIds, OutputStream os) throws IOException 
			{
				String[] ids=cpBillingIds.split(",");
				CpBillingServer cpserver = new CpBillingServer();
				//excel 保存路径
				String excelPath=ConfigManager.getConfigData("EXCEL_ZIP");

				//生成excel文件
				for(String id : ids){
					List<SettleAccountModel> list = cpserver.exportExcelData(Integer.parseInt(id));
					CpBillingModel cpBillingModel = cpserver.getCpBillingModel(Integer.parseInt(id));
					String startDate=cpBillingModel.getStartDate();
					String endDate=cpBillingModel.getEndDate();
					int dateType=cpBillingModel.getJsType();
					String channelName=cpBillingModel.getCpName();
					String dateTypeValue = getValueByType(dateType);
					String fileName=channelName + "(" + startDate + "_" + endDate + ").xls";
					String date = getDateFormat(startDate,endDate);
					
					String filePath = ConfigManager.getConfigData("EXCEL_DEMO") + (channelType==1 ? "SpDemo.xls" : "CpDemo.xls");
					//创建excel文件夹
					ZipUtil.createFile(excelPath);
					//创建excel文件
					new ExcelManager().writeSettleAccountToExcelZip(dateTypeValue, date, channelName, list, filePath,excelPath,fileName);
			
//				
				}
				String time=Calendar.getInstance().getTimeInMillis()+"";  
				//创建zip文件			
				ZipUtil.craeteZipPath(excelPath, time+"ZipDemo");
//				删除excel文件
				File file=new File(excelPath);
				ZipUtil.deleteExcelPath(file);
//				重新创建文件夹
				file.mkdirs();
				//写入流
				zipToOutstream(excelPath+time+"ZipDemo.zip",os);
		
				ZipUtil.deleteExcelPath(new File(excelPath+time+"ZipDemo.zip"));

			}
		/**
		 * 写入流
		 * @param path
		 * @param os
		 */
		public void zipToOutstream(String path,OutputStream os){
			//写入流
			 int bytesum = 0;  
		     int byteread = 0;  
		  		  
		        try {  
		          
		        	FileInputStream  inStream = new FileInputStream (new File(path));
		  
		            byte[] buffer = new byte[1204];  
		            int length;  
		            while ((byteread = inStream.read(buffer)) != -1) {  
		                bytesum += byteread;  
		                System.out.println(bytesum);  
		                os.write(buffer, 0, byteread); 
		               
		            }  
		            if(null!=inStream){
		        		inStream.close();
		        	}
		        } catch (FileNotFoundException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }

		}
		/**
		 * 结算ID—结算类型
		 * @param dateType
		 * @return
		 */
		public String getValueByType(int dateType){
			String dateTypeValue = "";
			switch(dateType)
			{
				case 0:
					dateTypeValue = "对公周结";
					break;
				case 1:
					dateTypeValue = "对公双周结";
					break;
				case 2:
					dateTypeValue = "对公N+1结";
					break;
				case 3:
					dateTypeValue = "对私周结";
					break;
				case 4:
					dateTypeValue = "对私双周结";
					break;
				case 5:
					dateTypeValue = "对私月结";
					break;
				case 6:
					dateTypeValue = "见帐单结";
					break;
				case 7:
					dateTypeValue = "对公N+2结";
					break;
				default:
					break;
			}
		return dateTypeValue;
	}
		
		/**
		 * CP账单基础数据
		 * 导出excel zip文件
		 * @param channelType
		 * @param dateType
		 * @param channelName
		 * @param startDate
		 * @param endDate
		 * @param list
		 * @param os
		 * @throws IOException 
		 */
		//channelType 1 SP 2 CP
			public void exportSettleAccountBaseBatchZip(int channelType,String cpId,String startDate,String endDate,int dateType,OutputStream os) throws IOException 
			{
				String[] ids=cpId.split(",");
				//excel 保存路径
				String excelPath=ConfigManager.getConfigData("EXCEL_ZIP");
				List<CpModel> cpList=new CpServer().loadCp();
				//生成excel文件
				for(String id : ids){
					SettleAccountServer accountServer = new SettleAccountServer();
					List<SettleAccountModel> list = accountServer.loadCpSettleAccountList(Integer.parseInt(id), startDate, endDate,dateType);
					String cpName = "";
					CpModel cp=	new CpServer().loadCpById(Integer.parseInt(id));
					if(cp!=null){
						cpName=cp.getShortName();
					}
					
					
					String dateTypeValue = getValueByType(dateType);
					String fileName=cpName + "(" + startDate + "_" + endDate + ").xls";
					String date = getDateFormat(startDate,endDate);
					
					String filePath = ConfigManager.getConfigData("EXCEL_DEMO") + (channelType==1 ? "SpDemo.xls" : "CpDemo.xls");
					//创建excel文件夹
					ZipUtil.createFile(excelPath);
					//创建excel文件
					new ExcelManager().writeSettleAccountToExcelZip(dateTypeValue, date, cpName, list, filePath,excelPath,fileName);
				}
				String time=Calendar.getInstance().getTimeInMillis()+"";  
				//创建zip文件			
				ZipUtil.craeteZipPath(excelPath, time+"ZipDemo");
//				删除excel文件
				File file=new File(excelPath);
				ZipUtil.deleteExcelPath(file);
//				重新创建文件夹
				file.mkdirs();
				//写入流
				zipToOutstream(excelPath+time+"ZipDemo.zip",os);
		
				ZipUtil.deleteExcelPath(new File(excelPath+time+"ZipDemo.zip"));

			}
	 
}
