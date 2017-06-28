package com.system.constant;

import com.system.util.ConfigManager;

public class Constant
{
	public static final int CP_CP_TRONE_NOT_EXIST = 1001;//业务不存在
	public static final int CP_CP_TRONE_STATUS_SUSPEND = 1002;//业务已暂停
	public static final int CP_VERIFY_CODE_API_NOT_EXIST = 1003;//二次验证码API不存在
	
	public static final int SP_TRONE_MONTH_OVER_LIMIT = 1004;//SP业务月限已超
	public static final int SP_TRONE_DAY_OVER_LIMIT = 1005;//SP业务日限已超
	public static final int CP_SP_TRONE_MONTH_OVER_LIMIT = 1006;//CP月限已超
	public static final int CP_SP_TRONE_DAY_OVER_LIMIT = 1007;//CP日限已超
	
	public static final int SP_TRONE_USER_MONTH_OVER_LIMIT = 1014;//业务用户月限已超
	public static final int SP_TRONE_USER_DAY_OVER_LIMIT = 1015;//业务用户日限已超
	
	public static final int SP_TRONE_PROVINCE_OVER_MONTH_LIMIT = 1016; //SP业务省份月限已超
	public static final int SP_TRONE_PROVINCE_OVER_DAY_LIMIT = 1017; //SP业务省份日限已超
	public static final int SP_TRONE_CITY_OVER_MONTH_LIMIT = 1018; //SP业务城市月限已超
	public static final int SP_TRONE_CITY_OVER_DAY_LIMIT = 1019; //SP业务城市日限已超
	
	public static final int SP_BLACK_PHONE_NUM = 1027; //黑名单用户
	
	//再往下加要往1100起
	
	public static final int CP_BASE_PARAMS_ERROR = 1008;//基础参数没匹配上
	public static final int CP_BASE_PARAMS_AREA_NOT_MATCH = 1009; //地区没匹配
	public static final int CP_SP_TRONE_ERROR = 1010;//第一次数据没取成功
	public static final int CP_SP_TRONE_SUC = 1011;//第一次数据取得成功
	public static final int CP_VERIFY_CODE_ERROR = 1012;//第二次数据没取成功
	public static final int CP_VERIFY_CODE_SUC = 1013;//第二次数据取得成功
	public static final int CP_PHONE_LOCATE_FAIL = 1100; //用户手机号或是IMSI定位失败
	
	public static final String BASE_TRONE_URL = ConfigManager.getConfigData("BASE_TRONE_URL", "http://h1.n8wan.com:9191/API/jj%s.ashx");
	
	public static final String NEXT_TRONE_URL = ConfigManager.getConfigData("BASE_TRONE_URL_2", "http://h1.n8wan.com:9191/API/jj%s.ashx?step=2");
}
