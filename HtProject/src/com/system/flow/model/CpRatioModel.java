package com.system.flow.model;

public class CpRatioModel
{
	private int id;
	private int cpId;
	private String cpName;
	private int operator;
	private String operatorName;
	private int proId;
	private String proName;
	private int ratio;
	private int status;
	
	public int getId()
	{
		return id;
	}
	public int getCpId()
	{
		return cpId;
	}
	public String getCpName()
	{
		return cpName;
	}
	public int getOperator()
	{
		return operator;
	}
	public String getOperatorName()
	{
		return operatorName;
	}
	public int getProId()
	{
		return proId;
	}
	public String getProName()
	{
		return proName;
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
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public void setCpName(String cpName)
	{
		this.cpName = cpName;
	}
	public void setOperator(int operator)
	{
		this.operator = operator;
	}
	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}
	public void setProId(int proId)
	{
		this.proId = proId;
	}
	public void setProName(String proName)
	{
		this.proName = proName;
	}
	public void setRatio(int ratio)
	{
		this.ratio = ratio;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof CpRatioModel))
		{
			return false;
		}
		
		CpRatioModel cModel = (CpRatioModel)obj;
		
		if(this.ratio==cModel.getRatio() 
				&& this.status==cModel.getStatus())
			return true;
		
		return false;
	}
	
}
