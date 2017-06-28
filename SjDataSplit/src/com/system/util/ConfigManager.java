
package com.system.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

public class ConfigManager
{
	private static final String CONFIG_PATH = "config.properties";
	
	private static Properties	prop			= null;
	
	private static long			lastModifyTime	= 0;
	
	private static String filePath = "";
	
	private static String defaultChartSet = "UTF-8";

	private ConfigManager()
	{
		
	}
	
	public static void setChartSet(String chartSet)
	{
		defaultChartSet = chartSet;
	}
	

	private static void init()
	{
		prop = new Properties();
		
		InputStreamReader isr = null;
		
		FileInputStream fis = null;
		
		InputStream is = null;
		
		try
		{
			String finalFilePath = (filePath == null || filePath.length() == 0) ? CONFIG_PATH : filePath;
			
			System.out.println("Config File Path:" + finalFilePath);
			
			if(!StringUtil.isNullOrEmpty(filePath))
			{
				fis = new FileInputStream(filePath);
				isr = new InputStreamReader(fis, StringUtil.isNullOrEmpty(defaultChartSet) ? "UTF-8" : defaultChartSet);
			}
			else
			{
				 ClassLoader loader = ConfigManager.class.getClassLoader();
				 
			     if (loader != null) 
			    	 is = loader.getResourceAsStream(CONFIG_PATH);
			     
			     if (is == null) 
			    	 is = ClassLoader.getSystemResourceAsStream(CONFIG_PATH);
			     
			     isr = new InputStreamReader(is, StringUtil.isNullOrEmpty(defaultChartSet) ? "UTF-8" : defaultChartSet);
			}
					
			prop.load(isr);
			
			File file = new File(finalFilePath);
			
			lastModifyTime = file.lastModified();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try{if(is!=null)is.close();}catch(Exception ex){};
			try{if(fis!=null)fis.close();}catch(Exception ex){};
			try{if(isr!=null)isr.close();}catch(Exception ex){};
		}
	}

	private static String getProperty(String key)
	{
		String result = "";
		
		if (prop == null)
		{
			init();
		}
		try
		{
			String finalFilePath = (filePath == null || filePath.length() == 0) ? CONFIG_PATH : filePath;
			
			File file = new File(finalFilePath);
			
			long tempTime = file.lastModified();
			
			if (tempTime > lastModifyTime)
			{
				prop.clear();
				init();
			}
			if (prop.containsKey(key))
			{
				result = prop.getProperty(key);
			}
		}
		catch (Exception exce)
		{
			exce.printStackTrace();
		}
		return result;
	}

	public static String getConfigData(String key)
	{
		return getProperty(key);
	}

	public static String getConfigData(String key, String defaultValue)
	{
		return getProperty(key).length() == 0 ? defaultValue : getProperty(key);
	}
	
	private static String getCurRunPath()
	{
		URL url = ConfigManager.class.getProtectionDomain().getCodeSource()
				.getLocation();

		String filePath = null;
		
		try
		{
			filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (filePath.endsWith(".jar"))
		{
			filePath = filePath.substring(0, filePath.lastIndexOf("/")) + File.separator + CONFIG_PATH;
		}

		File file = new File(filePath);

		filePath = file.getAbsolutePath();//得到windows下的正确路径
		
		return filePath;
	} 
	
	public static void setConfigFilePath(String configFilePath)
	{
		filePath = StringUtil.isNullOrEmpty(configFilePath) ? getCurRunPath() : configFilePath;
		
		File file = new File(filePath);
		
		boolean isExistFile = file.exists();
		
		System.out.println("config file path suc : " + isExistFile + "; filePath:" + filePath);
		
		if(!isExistFile)
			filePath = "";
			
	}
	
	public static String getConfigFilePath()
	{
		return filePath;
	}

}
