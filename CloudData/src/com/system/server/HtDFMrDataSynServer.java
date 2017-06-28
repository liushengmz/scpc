package com.system.server;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.system.dao.HtDfMrDao;
import com.system.model.HtDFMrModel;
import com.system.util.ConfigManager;
import com.system.util.FileUtil;
import com.system.util.StringUtil;

public class HtDFMrDataSynServer
{
	Logger logger = Logger.getLogger(HtDFMrDataSynServer.class);
	
	/**
	 * 同步普通点播数据到大数据
	 */
	public void startSynSmsData()
	{
		long startMils = System.currentTimeMillis();
		
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.HOUR_OF_DAY, -1);
		String table = StringUtil.getMonthFormat();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
		int startHour = ca.get(Calendar.HOUR_OF_DAY);
		
		logger.info("startSynData:" + table + "-" + "["+ date +"]" + "["+ startHour +","+ (startHour + 1) +"]");
		
		loadUnSynFileData();
		
		loadUnSynDbData(table,date,startHour);
		
		logger.info("endSynData spend mils:" + (System.currentTimeMillis() - startMils));
	}
	
	private void loadUnSynDbData(String table,String date,int startHour)
	{
		List<HtDFMrModel> list = new HtDfMrDao().loadHtDFMrData(table,date,startHour);
		
		logger.info("loadUnSynDbData size:" + list.size());
		
		if(list.isEmpty())
			return;
		
		List<String>  data =  new ArrayList<String>();
		
		for(HtDFMrModel model : list)
		{
			try
			{
				data.add(changeModelToUrl(model));
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		
		NetServer netServer = new NetServer();
		
		String synUrl = ConfigManager.getConfigData("CREDIT_SMS_SYN_DATA_URL", "");
		
		String baseFilePath = ConfigManager.getConfigData("UN_SYN_SMS_DATA_FILE_PATH","./");
		
		if(StringUtil.isNullOrEmpty(synUrl))
		{
			FileUtil.saveDataToFile(data, baseFilePath, "tbl_mr_" + date + "_" + startHour + ".txt");
			logger.info("loadUnSynDbData finish syn url empty");
		}
		else
		{
			if(!netServer.synDataList(synUrl, data))
			{
				FileUtil.saveDataToFile(data, baseFilePath, "tbl_mr_" + date + "_" + startHour + ".txt");
				logger.info("loadUnSynDbData finish syn data not finish");
			}
		}
		
		logger.info("loadUnSynDbData finish syn data");
	}
	
	private String changeModelToUrl(HtDFMrModel model)
	{
		StringBuffer sb = new StringBuffer();
		
		String serviceName = "";
		
		if(model.getSPID()==42 && model.getCPID()== 43)
		{
			serviceName =  model.getOPERATOR_NAME() + "-" + model.getSP_TORNE_NAME();
		}
		else
		{
			serviceName = model.getSP_NAME() + "-" + model.getSP_TORNE_NAME();
		}
		
		try
		{
			sb.append("IMEI=" + URLEncoder.encode(model.getIMEI(),"UTF-8") + "&");
			sb.append("IMSI=" + URLEncoder.encode(model.getIMSI(),"UTF-8") + "&");
			sb.append("PHONE=" + URLEncoder.encode(model.getPHONE(),"UTF-8") + "&");
			sb.append("SPID=" + model.getSPID() + "&");
			sb.append("SP_NAME=" + URLEncoder.encode(model.getSP_NAME(),"UTF-8") + "&");
			sb.append("SP_TRONE_ID=" + model.getSP_TRONE_ID() + "&");
			sb.append("SP_TORNE_NAME=" + URLEncoder.encode(model.getSP_TORNE_NAME(),"UTF-8") + "&");
			sb.append("SP_TRONE_TYPE=" + model.getSP_TRONE_TYPE() + "&");
			sb.append("TRONG_ID=" + model.getTRONG_ID() + "&");
			sb.append("TRORN_NAME=" + URLEncoder.encode(model.getTRORN_NAME(),"UTF-8") + "&");
			sb.append("ORDER=" + URLEncoder.encode(model.getORDER(),"UTF-8") + "&");
			sb.append("TRONE=" + URLEncoder.encode(model.getTRONE(),"UTF-8") + "&");
			sb.append("CPID=" + model.getCPID() + "&");
			sb.append("CP_NAME=" + URLEncoder.encode(model.getCP_NAME(),"UTF-8") + "&");
			sb.append("PRICE=" + model.getPRICE() + "&");
			sb.append("CREATE_DATE=" + URLEncoder.encode(model.getCREATE_DATE(),"UTF-8")  + "&");
			sb.append("LINKID=" + model.getLINKID() + "&");
			sb.append("CP_PARAM=" + model.getCP_PARAM() + "&");
			sb.append("TRONE_ORDER_ID=" + model.getTRONE_ORDER_ID() + "&");
			sb.append("PROVINCE_ID=" + model.getPROVINCE_ID() + "&");
			sb.append("CITY_ID=" + model.getCITY_ID() + "&");
			sb.append("PROVINCE_NAME=" + URLEncoder.encode(model.getPROVINCE_NAME(),"UTF-8") + "&");
			sb.append("CITY_NAME=" + URLEncoder.encode(model.getCITY_NAME(),"UTF-8") + "&");
			sb.append("IVR_TIME=" + model.getIVR_TIME() + "&");
			sb.append("SYN_FLAG=" + model.getSYN_FLAG() + "&");
			sb.append("OPERATOR=" + model.getOPERATOR() + "&");
			
			sb.append("SERVICE_NAME=" + URLEncoder.encode(serviceName,"UTF-8"));
		}
		catch(Exception ex)
		{
			logger.error("changeModelToUrl error:" + ex.getMessage());
		}
		
		return sb.toString();
	}
	
	private void loadUnSynFileData()
	{
		String baseFilePath = ConfigManager.getConfigData("UN_SYN_SMS_DATA_FILE_PATH","");
		
		if(StringUtil.isNullOrEmpty(baseFilePath))
			return;
		
		List<String> dataFiles = FileUtil.getChildFilePathsFromPath(baseFilePath);
		
		if(dataFiles.isEmpty())
			return;
		
		NetServer netServer = new NetServer();
		
		String synUrl = ConfigManager.getConfigData("CREDIT_SMS_SYN_DATA_URL", "");
		
		if(StringUtil.isNullOrEmpty(synUrl))
			return;
		
		for(String path : dataFiles)
		{
			if(path.contains("tbl_mr"))
			{
				List<String> data = FileUtil.readFileToList(baseFilePath + path, "UTF-8");
				
				if(data.isEmpty())
					continue;
				
				logger.info("loadUnSynFileData:" + path);
				
				//同步成功后删除存档
				if(netServer.synDataList(synUrl, data))
					FileUtil.delFile(baseFilePath + path);
			}
		}
	}
}
