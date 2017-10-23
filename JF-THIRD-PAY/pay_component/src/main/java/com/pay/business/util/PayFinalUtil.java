package com.pay.business.util;

public class PayFinalUtil {
	// 支付状态 支付成功回调失败
	public static final Integer PAY_ORDER_SUCCESS_BACKFAIL =5;
	// 支付状态 支付成功
	public static final Integer PAY_ORDER_SUCCESS =1;
	// 支付状态 支付失败
	public static final Integer PAY_ORDER_FAIL =2;
	// 支付状态  未支付
	public static final Integer PAY_ORDER_NOT =3;
	
	// 支付成功状态   
	public static final String PAY_STATUS_SUSSESS ="1";
	
	// 支付错误状态    支付已完成
	public static final String PAY_STATUS_FAIL_OK ="2";
	
	// 支付错误状态    订单错误
	public static final String PAY_STATUS_FAIL ="3";
	
	// 渠道key与appKey不匹配
	public static final String PAY_STATUS_KEY_FAIL ="4";
	
	// 支付查询   成功
	public static final String PAYORDER_QUERY_SUCCESS ="1";
	
	// 支付查询    错误
	public static final String PAYORDER_QUERY_FAIL ="-1";
	
	// 状态  启用
	public static final Integer PAY_STATUS_ENABLE =1;
	
	// 未删除状态
	public static final Integer PAY_STATUS_NOT_DELETE =2;
	
	// 优惠上线状态
	public static final Integer PAY_STATUS_SHOW =1;
	
	// 商户类型  APP
	public static final Integer PAY_MERCHANT_TYPE_APP =1;
	
	// 优惠类型  直减
	public static final Integer PAY_DISCOUNT_TYPE =1;
	
	// 优惠总额度类型  金额
	public static final Integer PAY_DISCOUNT_MONEY_TYPE =1;
	
	// 成功状态码
	public static final String SUSSESS_CODE = "200";
	
	//未支付
	public static final String CANCEL_CODE = "300";
	
	//未配置支付通道或支付类型不支持
	public static final String PAY_TYPE_FAIL ="PAY_TYPE_FAIL";
	
	// 通道下单错误
	public static final String RATE_FAIL ="RATE_FAIL";
}
