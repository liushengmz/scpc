package com.system.model;

public class CpSpTroneModel
{
	private int cpSpTroneId;
	private int cpId;
	private int spTroneId;
	private float rate;
	private int jsType;
	
	public int getCpSpTroneId()
	{
		return cpSpTroneId;
	}
	public void setCpSpTroneId(int cpSpTroneId)
	{
		this.cpSpTroneId = cpSpTroneId;
	}
	public int getCpId()
	{
		return cpId;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public float getRate()
	{
		return rate;
	}
	public void setRate(float rate)
	{
		this.rate = rate;
	}
	public int getJsType()
	{
		return jsType;
	}
	public void setJsType(int jsType)
	{
		this.jsType = jsType;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof CpSpTroneModel))
		{
			return false;
		}
		
		CpSpTroneModel model = (CpSpTroneModel)obj;
		
		try
		{
			if (model.getCpId() == cpId && model.getSpTroneId() == spTroneId
					&& model.getJsType() == jsType && model.getRate() == rate)
				return true;
		}
		catch(Exception ex)
		{
			return false;
		}
		
		return false;
	}
	
}
