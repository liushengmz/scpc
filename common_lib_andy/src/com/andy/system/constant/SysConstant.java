package com.andy.system.constant;

import com.andy.system.util.PropertyConfigUtil;

public class SysConstant
{
	/**
	 * config_main
	 */
	public static final String DB_CONFIG_MAIN = PropertyConfigUtil.getConfigData("DB_CONFIG_MAIN", "daily_config");
	/**
	 * log_main
	 */
	public static final String DB_LOG_MAIN = PropertyConfigUtil.getConfigData("DB_LOG_MAIN", "daily_log");
	/**
	 * SQL语句中替换的字符串
	 */
	public static final String CONSTANT_REPLACE_STRING = "CONSTANT_REPLACE_STRING_" + System.currentTimeMillis();
}
