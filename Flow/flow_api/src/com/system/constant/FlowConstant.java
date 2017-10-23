package com.system.constant;

/**
 * 用于存储各种服务器返回的错误结果，和数据库  tbl_f_result_code 表数据对应
 * @author Andy.Chen
 *
 */
public class FlowConstant
{
	/*		########################################################
	 *		以下一块内容是对 TO_B 请求的一个返回值
	 		#######################################################
	 */
	/**
	 * TOB 请求成功
	 */
	public static final int TO_B_REQUEST_SUCCESS = 10000;
	/**
	 * TOB 签名失败 
	 */
	public static final int TO_B_REQUEST_SIGN_ERROR = 10001;
	/**
	 * TOB 参数错误
	 */
	public static final int TO_B_REQUEST_PARAM_ERROR = 10002;
	/**
	 * TOB 缺少必要参数
	 */
	public static final int TO_B_REQUEST_LACK_PARAMS = 10003;
	/**
	 * TOB 非合作伙伴
	 */
	public static final int TO_B_REQUEST_NOT_COMPANY = 10004;
	/**
	 * TOB 子订单为空
	 */
	public static final int TO_B_REQUEST_CHILD_ORDER_EMPTY = 10005;
	/**
	 * IP鉴权失败
	 */
	public static final int TO_B_REQUERY_IP_AUTH_FAIL = 10006;
	/**
	 * CP 定单号重复
	 */
	public static final int TO_B_REQUEST_ORDER_REPEAT = 10007;
	/**
	 * 未知错误
	 */
	public static final int TO_B_REQUEST_UN_KNOW_ERROR = 10008;
	
	
	/*		########################################################
	 *		以下一块内容是CP单个请求充值的返回值(SCOR==>SINGLE CP ORDER REQUEST)
	 		#######################################################
	 */
	/**
	 * SCOR 请求成功 
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_SUCCESS = 11000;
	/**
	 * SCOR 签名失败 
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_SIGN_ERROR = 11001;
	/**
	 * SCOR 参数错误
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_PARAM_ERROR = 11002;
	/**
	 * SCOR 缺少必要参数
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_LACK_PARAMS = 11003;
	/**
	 * SCOR 非合作伙伴
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_NOT_COMPANY = 11004;
	/**
	 * SCOR IP鉴权失败
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_IP_AUTH_FAIL = 11006;
	/**
	 * SCOR 定单号重复
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_ORDER_REPEAT = 11007;
	/**
	 * SCOR 未知错误
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_UN_KNOW_ERROR = 11008;
	/**
	 * SCOR 号码错误
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_MOBILE_ERROR = 11009;
	/**
	 * SCOR 未识别地区的号码
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_UN_KNOW_MOBILE = 11010;
	/**
	 * SCOR 未合作的省份
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_UN_COMPANY_PROVINCE = 11011;
	/**
	 * SCOR CP 余额不足
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_NOT_SUFFICIENT_FUNDS = 11012;
	/**
	 * SCOR 没有可以充值的通道 
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_NO_SUITABLE_TRONE = 11013;
	/**
	 * SCOR 没有合适的流量充值包
	 */
	public static final int CP_SINGLE_ORDER_REQUEST_NOT_MATCH_FLOW_SIZE = 11014;
	
	
	/*		########################################################
	 *		以下是信息是系统特殊数据 
	 		#######################################################
	 */
	/**
	 * 
	 */
	public static final int FLOW_SYS_CP_CODE_BASE_COUNT = 80000;
	
	//public static final int FLOW_
}
