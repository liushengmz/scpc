package com.pay.business.util.kftpay;

import com.core.teamwork.base.util.properties.PropertiesUtil;

public interface KftUrlConfig 
{
	String PAY_URL = PropertiesUtil.getProperty("rate", "kft_pay_url");
	
	String NOTIFY_URL = PropertiesUtil.getProperty("rate", "kft_pay_notify_url");
	
	String KEY_FILE_URL = PropertiesUtil.getProperty("rate", "kft_key_file_url");
	
	String KEY_FILE_PWD = PropertiesUtil.getProperty("rate", "kft_key_file_pwd");
	
	String KEY_CER_FILE_URL = PropertiesUtil.getProperty("rate", "kft_key_cer_file_url");
}
