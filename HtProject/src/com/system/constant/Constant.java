package com.system.constant;

import com.system.util.ConfigManager;

public class Constant
{
	public static int PAGE_SIZE = 20;
	
	public static String CONSTANT_REPLACE_STRING = "CONSTANT_REPLACE_STRING";
	
	/**
	 * daily_config
	 */
	public static final String DB_DAILY_CONFIG = ConfigManager.getConfigData("DB_DAILY_CONFIG", "daily_config");
	/**
	 * daily_log
	 */
	public static final String DB_DAILY_LOG = ConfigManager.getConfigData("DB_DAILY_LOG", "daily_log");
	
	/**
	 * 点播类型数据查询
	 */
	public static final int MR_SUMMER_QUERY_NORMAL = 0;
	/**
	 * IVR数据类型查询
	 */
	public static final int MR_SUMMER_QUERY_IVR = 2;
	/**
	 * 隔天数据类型查询
	 */
	public static final int MR_SUMMER_QUERY_OTHER_DAY = 1;
	
}
