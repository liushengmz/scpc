package com.system.model;

import java.util.List;

public class PayCodeExportModel
{
	private int spTroneId;
	private int spId;
	private String spTroneName;
	private String upDataTypeName;
	private String privinces;
	private String privincesName;
	private String remark;
	private List<PayCodeExportChildModel> childList;
	
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public String getUpDataTypeName()
	{
		return upDataTypeName;
	}
	public void setUpDataTypeName(String upDataTypeName)
	{
		this.upDataTypeName = upDataTypeName;
	}
	public String getPrivinces()
	{
		return privinces;
	}
	public void setPrivinces(String privinces)
	{
		this.privinces = privinces;
	}
	public String getPrivincesName()
	{
		return privincesName;
	}
	public void setPrivincesName(String privincesName)
	{
		this.privincesName = privincesName;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public List<PayCodeExportChildModel> getChildList()
	{
		return childList;
	}
	public void setChildList(List<PayCodeExportChildModel> childList)
	{
		this.childList = childList;
	}
	public int getSpId()
	{
		return spId;
	}
	public void setSpId(int spId)
	{
		this.spId = spId;
	}
}
