package com.system.model;

/**
 * CP单个充值的返回码
 * @author Andy.Chen
 *
 */
public class CpUserOrderResponseModel
{
	/**
	 * 返回码
	 */
	private int resultCode;
	/**
	 * 返回的信息
	 */
	private String resultMsg;
	/**
	 * 请求的定单号，原样返回
	 */
	private String orderId;
	/**
	 * 数据签名
	 */
	private String sign;
	
	public int getResultCode()
	{
		return resultCode;
	}
	public String getResultMsg()
	{
		return resultMsg;
	}
	public String getOrderId()
	{
		return orderId;
	}
	public String getSign()
	{
		return sign;
	}
	public void setResultCode(int resultCode)
	{
		this.resultCode = resultCode;
	}
	public void setResultMsg(String resultMsg)
	{
		this.resultMsg = resultMsg;
	}
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}
	
	@Override
	public String toString()
	{
		return "orderId:" + orderId + ";resultCode:" + resultCode + ";resultMsg:" + resultMsg;
	}
}
