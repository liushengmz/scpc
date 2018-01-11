package com.pay.business.util.swt;

import com.core.teamwork.base.util.properties.PropertiesUtil;

public interface SwtUrlConfig 
{
	String PAY_URL = PropertiesUtil.getProperty("rate", "swt_pay_url");
	
	String NOTIFY_URL = PropertiesUtil.getProperty("rate", "swt_pay_notify_url");
}
