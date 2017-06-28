package com.system.constant;

public class Constant
{
	//去MR里查询正常给CP结算的数据
	public static final int MR_SUMMER_QUERY_CPSP_NORMAL_DATA = 1;
	//去MR里查询给CP扣掉的数据
	public static final int MR_SUMMER_QUERY_CPSP_UNCLOSING_DATA = 2;
	//去MR里查询做了SP收入但不给CP结算的数据
	public static final int MR_SUMMER_QUERY_CP_UNCLOSING_DATA = 3;
	
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
