package com.system.model;

public class CpRatioModel
{
	private int id;
	private int cpId;
	private int operator;
	private int proId;
	private int ratio;
	private String remark;
	
	public int getId()
	{
		return id;
	}
	public int getCpId()
	{
		return cpId;
	}
	public int getOperator()
	{
		return operator;
	}
	public int getProId()
	{
		return proId;
	}
	public int getRatio()
	{
		return ratio;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public void setOperator(int operator)
	{
		this.operator = operator;
	}
	public void setProId(int proId)
	{
		this.proId = proId;
	}
	public void setRatio(int ratio)
	{
		this.ratio = ratio;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	
}
