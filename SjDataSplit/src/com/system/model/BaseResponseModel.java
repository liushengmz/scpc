package com.system.model;

import net.sf.json.JSONObject;

/**
 * 返回给CP的MODEL
 * @author Andy.Chen
 *
 */
public class BaseResponseModel
{
	/**
	 * 访问状态
	 */
	private int status;
	/**
	 * 定单号
	 */
	private String orderNum;
	/**
	 * 服务器
	 */
	private JSONObject resultJson;
	
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public JSONObject getResultJson()
	{
		return resultJson;
	}
	public void setResultJson(JSONObject resultJson)
	{
		this.resultJson = resultJson;
	}
	public String getOrderNum()
	{
		return orderNum;
	}
	public void setOrderNum(String orderNum)
	{
		this.orderNum = orderNum;
	}
	
}
