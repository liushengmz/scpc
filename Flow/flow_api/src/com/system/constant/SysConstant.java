package com.system.constant;

import com.system.util.ConfigManager;

public class SysConstant
{
	/**
	 * daily_config
	 */
	public static final String DB_CONFIG_MAIN = ConfigManager.getConfigData("DB_CONFIG_MAIN", "daily_config");
	/**
	 * daily_log
	 */
	public static final String DB_LOG_MAIN = ConfigManager.getConfigData("DB_LOG_MAIN", "daily_log");
}
