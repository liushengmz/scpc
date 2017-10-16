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
}
