package com.system.model;

public class SpModel
{
	private int spId;
	private String shortName;
	private String fullName;
	private int status;
	
	public int getSpId()
	{
		return spId;
	}
	public void setSpId(int spId)
	{
		this.spId = spId;
	}
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
		if(!(obj instanceof SpModel))
		{
			return false;
		}
		
		SpModel model = (SpModel)obj;
		
		try
		{
			if (model.getFullName().equalsIgnoreCase(fullName)
					&& model.getShortName().equalsIgnoreCase(shortName)
					&& (model.getSpId() == spId)
					&& (model.getStatus() == status))
				return true;
		}
		catch(Exception ex)
		{
			return false;
		}
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return spId + "-" + shortName + "-" + fullName + "-" + status;
	}
}
