package com.andy.system.model;

public class BaseApiReturnModel
{
	private int status;
	private String msg;
	private Object data;
	
	public int getStatus()
	{
		return status;
	}
	public String getMsg()
	{
		return msg;
	}
	public Object getData()
	{
		return data;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public void setData(Object data)
	{
		this.data = data;
	}
	
	
}
