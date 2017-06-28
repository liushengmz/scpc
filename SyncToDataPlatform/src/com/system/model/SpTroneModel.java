package com.system.model;

public class SpTroneModel
{
	private int spTroneId;
	private int spId;
	private String name;
	private int productId;
	private int troneType;
	private int jsType;
	private float jieSuanLv;
	private int status;
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public int getSpId()
	{
		return spId;
	}
	public void setSpId(int spId)
	{
		this.spId = spId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getProductId()
	{
		return productId;
	}
	public void setProductId(int productId)
	{
		this.productId = productId;
	}
	public int getTroneType()
	{
		return troneType;
	}
	public void setTroneType(int troneType)
	{
		this.troneType = troneType;
	}
	public int getJsType()
	{
		return jsType;
	}
	public void setJsType(int jsType)
	{
		this.jsType = jsType;
	}
	public float getJieSuanLv()
	{
		return jieSuanLv;
	}
	public void setJieSuanLv(float jieSuanLv)
	{
		this.jieSuanLv = jieSuanLv;
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
		if(!(obj instanceof SpTroneModel))
			return false;
		
		SpTroneModel model = (SpTroneModel)obj;
		
		try
		{
			if ((model.getSpId() == spId)
					&& model.getName().equalsIgnoreCase(name)
					&& model.getProductId() == productId
					&& model.getTroneType() == troneType
					&& model.getJieSuanLv() == jieSuanLv
					&& model.getJsType() == jsType
					&& model.getStatus() == status)
				return true;
		}
		catch(Exception ex){return false;}

		return false;
	}
	
	@Override
	public String toString()
	{
		return spId + "-" + spTroneId + "-" + name + "-" + productId + "-" + troneType + "-" + jsType + "-" + jieSuanLv + "-" + status;
	}
	
}
