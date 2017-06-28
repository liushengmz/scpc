package com.system.model;

public class TroneModel
{
	private int troneId;
	private int spTroneId;
	private String troneName;
	private float price;
	private int status;
	
	public int getTroneId()
	{
		return troneId;
	}
	public void setTroneId(int troneId)
	{
		this.troneId = troneId;
	}
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public String getTroneName()
	{
		return troneName;
	}
	public void setTroneName(String troneName)
	{
		this.troneName = troneName;
	}
	public float getPrice()
	{
		return price;
	}
	public void setPrice(float price)
	{
		this.price = price;
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
	public String toString()
	{
		return troneId + "-" + spTroneId + "-" + troneName + "-" + price + "-" + status;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof TroneModel))
			return false;
		
		TroneModel model = (TroneModel)obj;
		
		try
		{
			if (model.getSpTroneId() == spTroneId
					&& model.getTroneName().equalsIgnoreCase(troneName)
					&& model.getPrice() == price && model.status == status)
				return true;
		}
		catch(Exception ex){}
		
		return false;
	}
	
}
