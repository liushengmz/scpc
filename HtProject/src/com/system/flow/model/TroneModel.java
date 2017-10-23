package com.system.flow.model;

public class TroneModel
{
	private int id;
	private int spTroneId;
	private int proId;
	private int ratio;
	private int status;
	private String proName;
	
	public int getId()
	{
		return id;
	}
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public int getProId()
	{
		return proId;
	}
	public int getRatio()
	{
		return ratio;
	}
	public int getStatus()
	{
		return status;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public void setProId(int proId)
	{
		this.proId = proId;
	}
	public void setRatio(int ratio)
	{
		this.ratio = ratio;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getProName()
	{
		return proName;
	}
	public void setProName(String proName)
	{
		this.proName = proName;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof TroneModel))
			return false;
		
		TroneModel comModel = (TroneModel)obj;
		
		if(this.spTroneId == comModel.getSpTroneId()
				&& this.proId == comModel.getProId()
				&& this.ratio == comModel.getRatio()
				&& this.status == comModel.getStatus())
			return true;
		else
			return false;
		
	}
	
}
