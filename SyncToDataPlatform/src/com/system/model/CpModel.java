package com.system.model;

public class CpModel
{
	private int cpId;
	private String shortName;
	private String fullName;
	private int status;
	
	public String getShortName()
	{
		return shortName;
	}
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
	public String getFullName()
	{
		return fullName;
	}
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof CpModel))
		{
			return false;
		}
		
		CpModel model = (CpModel)obj;
		
		try
		{
			if (model.getFullName().equalsIgnoreCase(fullName)
					&& model.getShortName().equalsIgnoreCase(shortName)
					&& (model.getCpId() == cpId)
					&& (model.getStatus() == status))
				return true;
		}
		catch(Exception ex)
		{
			return false;
		}
		
		return false;
	}
	public int getCpId()
	{
		return cpId;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
}
