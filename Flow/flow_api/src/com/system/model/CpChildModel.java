package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class CpChildModel
{
	private int id;
	private int cpId;
	private int status;
	private String signKey;
	private List<String> ipList = new ArrayList<String>();
	
	public int getId()
	{
		return id;
	}
	public int getCpId()
	{
		return cpId;
	}
	public int getStatus()
	{
		return status;
	}
	public String getSignKey()
	{
		return signKey;
	}
	public List<String> getIpList()
	{
		return ipList;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public void setSignKey(String signKey)
	{
		this.signKey = signKey;
	}
	public void setIpList(List<String> ipList)
	{
		this.ipList = ipList;
	}
	
	
	
}
