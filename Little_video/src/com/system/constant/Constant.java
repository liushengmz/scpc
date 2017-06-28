
package com.system.constant;

public class Constant
{
	public static final int	ERROR_SUCCESS			= 0;
	/**
	 * 缺少参数
	 */
	public static final int	ERROR_MISS_PARAMETER	= 1;
	/**
	 * 用户资料丢失
	 */
	public static final int	ERROR_UNKONW_USER		= 2;
	/**
	 * 用户订单已经支付（通常在产生订单时，购买等级比实际低或一样）
	 */
	public static final int	ERROR_ALREADY_PAID		= 3;
	/**
	 * 参数错误
	 */
	public static final int	ERROR_UNKONW_PARAMETER	= 4;

	/**
	 * 不允许跳级购买
	 */
	public static final int ERROR_SKIP_LEVEL = 5;
	
	/**
	 * 数据库忙（可能是参数错误引起）
	 */
	public static final int ERROR_DBASE_BUSY = 6;

	/**
	 * 没有支付通道（可能是Appkey /channel 错误引起）
	 */
	public static final int ERROR_NO_PAY_CHANNEL = 7;

	
	public static final int	ORDER_METHOD_QUERY	= 0;
	public static final int	ORDER_METHOD_CREATE	= 1;
	public static final int	ORDER_METHOD_UPDATE	= 2;
	
}
