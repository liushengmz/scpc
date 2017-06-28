package com.system.server;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.dao.HtDfMrDao;
import com.system.model.HtDFMonthModel;
import com.system.util.ConfigManager;
import com.system.util.FileUtil;
import com.system.util.StringUtil;

public class HtDFMonthDataSynServer
{
	Logger logger = Logger.getLogger(HtDFMrDataSynServer.class);
	
	public void startSynMonthData()
	{
		long startMils = System.currentTimeMillis();
		
		Calendar ca = Calendar.getInstance();
		
		ca.add(Calendar.MONTH, -1);
		
		ca.set(Calendar.DAY_OF_MONTH, 1);
		
		String startDate = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
		
		ca.add(Calendar.MONTH,1);//月增加1天 
		ca.add(Calendar.DAY_OF_MONTH,-1);//日期倒数一日,既得到本月最后一天 
		
		String endDate = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
		
		loadUnSynSpMonthDbData(startDate,endDate);
		
		loadUnSynCpMonthDbData(startDate,endDate);
		
		logger.info("endSynMonthData spend mils:" + (System.currentTimeMillis() - startMils));
	}
	
	private void loadUnSynSpMonthDbData(String startDate,String endDate)
	{
		List<HtDFMonthModel> list = new HtDfMrDao().loadHtDFMonthSpData(startDate, endDate);
		
		logger.info("loadUnSynSpMonthDbData size:" + list.size());
		
		if(list.isEmpty())
			return;
		
		List<String>  data =  new ArrayList<String>();
		
		String month = startDate.replace("-", "");
		
		for(HtDFMonthModel model : list)
		{
			try
			{
				data.add(changeModelToUrl(month,model,1));
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		
		String synUrl = ConfigManager.getConfigData("CREDIT_MONTH_SYN_DATA_URL", "");
		
		String baseFilePath = ConfigManager.getConfigData("UN_SYN_MONTH_DATA_FILE_PATH","./");
		
		NetServer netServer = new NetServer();
		
		if(StringUtil.isNullOrEmpty(synUrl))
		{
			FileUtil.saveDataToFile(data, baseFilePath, "tbl_month_sp_" + month + ".txt");
			logger.info("loadUnSynSpMonthDbData finish syn data of syn url empty");
			return;
		}
		
		List<String> unSynList = netServer.synMonthDataList(synUrl, data);
		
		if(unSynList.size()>0)
		{
			FileUtil.saveDataToFile(data, baseFilePath, "tbl_month_sp_" + month + ".txt");
			logger.info("loadUnSynSpMonthDbData finish syn data of syn url empty");
			return;
		}
		
		logger.info("loadUnSynDbData finish syn data");
	}
	
	private String changeModelToUrl(String month,HtDFMonthModel model,int type)
	{
		String json = "";
		try
		{
			int operator = 1;
			switch(model.getOprator())
			{
				case 1:
					operator = 2;
					break;
					
				case 2:
					operator = 3;
					break;
					
				case 3:
					operator = 1;
					break;
					
				default:
					break;
			}
			
			model.setDevId(4);
			model.setCompanyName(URLEncoder.encode("浩天投资有限公司","UTF-8"));
			model.setServiceName("sms");
			model.setProductName(URLEncoder.encode(model.getProductName(),"UTF-8"));
			model.setOprator(operator);
			model.setChannelName(URLEncoder.encode(model.getChannelName(),"UTF-8"));
			model.setIsDeducted(type);
			model.setStaticDate(month);
			
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(model);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return json;
	}
	
	private void loadUnSynCpMonthDbData(String startDate,String endDate)
	{
		List<HtDFMonthModel> list = new HtDfMrDao().loadHtDFMonthCpData(startDate, endDate);
		
		logger.info("loadUnSynCpMonthDbData size:" + list.size());
		
		if(list.isEmpty())
			return;
		
		List<String>  data =  new ArrayList<String>();
		
		String month = startDate.replace("-", "");
		
		for(HtDFMonthModel model : list)
		{
			try
			{
				data.add(changeModelToUrl(month,model,0));
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		
		String synUrl = ConfigManager.getConfigData("CREDIT_MONTH_SYN_DATA_URL", "");
		
		String baseFilePath = ConfigManager.getConfigData("UN_SYN_MONTH_DATA_FILE_PATH","./");
		
		NetServer netServer = new NetServer();
		
		if(StringUtil.isNullOrEmpty(synUrl))
		{
			FileUtil.saveDataToFile(data, baseFilePath, "tbl_month_cp_" + month + ".txt");
			logger.info("loadUnSynSpMonthDbData finish syn data of syn url empty");
			return;
		}
		
		List<String> unSynList = netServer.synMonthDataList(synUrl, data);
		
		if(unSynList.size()>0)
		{
			FileUtil.saveDataToFile(data, baseFilePath, "tbl_month_cp_" + month + ".txt");
			logger.info("loadUnSynSpMonthDbData finish syn data of syn url empty");
			return;
		}
	}
	
	//一小时一次把原来未同步的包月数据同步出去
	public void loadUnSynMonthData()
	{
		String baseFilePath = ConfigManager.getConfigData("UN_SYN_MONTH_DATA_FILE_PATH","");
		
		if(StringUtil.isNullOrEmpty(baseFilePath))
			return;
		
		List<String> dataFiles = FileUtil.getChildFilePathsFromPath(baseFilePath);
		
		if(dataFiles.isEmpty())
			return;
		
		String synUrl = ConfigManager.getConfigData("CREDIT_MONTH_SYN_DATA_URL", "");
		
		if(StringUtil.isNullOrEmpty(synUrl))
			return;
		
		NetServer netServer = new NetServer();
		
		for(String path : dataFiles)
		{
			if(path.contains("tbl_month_"))
			{
				List<String> data = FileUtil.readFileToList(baseFilePath + path, "UTF-8");
				
				if(data.isEmpty())
					continue;
				
				logger.info("loadUnSynFileMonthData:" + path);
				
				List<String> unSynData = netServer.synMonthDataList(synUrl, data);
				
				if(unSynData.isEmpty())
				{
					FileUtil.delFile(baseFilePath + path);
				}
				else
				{
					if(unSynData.size()!=data.size())
					{
						FileUtil.delFile(baseFilePath + path);
						FileUtil.saveDataToFile(unSynData, baseFilePath, path);
					}
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		new HtDFMonthDataSynServer().startSynMonthData();
	}
}
