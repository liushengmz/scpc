package com.system.flow.model;

import java.util.List;

public class SpTroneModel
{
	private int id;
	private int spId;
	private String spName;
	private String spTroneName;
	private int spApiId;
	private int priceId;
	private int price;
	private int rang;
	private String flowName;
	private int flowSize;
	private int ratio;
	private int sendSms;
	private String proNames;
	private String remark;
	private int status;
	private List<TroneModel> troneList;
	private String spApiName;
	private String operatorName;
	
	
	public int getId()
	{
		return id;
	}
	public int getSpId()
	{
		return spId;
	}
	public void setSpId(int spId)
	{
		this.spId = spId;
	}
	public int getSpApiId()
	{
		return spApiId;
	}
	public int getPriceId()
	{
		return priceId;
	}
	public int getRatio()
	{
		return ratio;
	}
	public int getSendSms()
	{
		return sendSms;
	}
	public String getProNames()
	{
		return proNames;
	}
	public String getRemark()
	{
		return remark;
	}
	public int getStatus()
	{
		return status;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setSpApiId(int spApiId)
	{
		this.spApiId = spApiId;
	}
	public void setPriceId(int priceId)
	{
		this.priceId = priceId;
	}
	public void setRatio(int ratio)
	{
		this.ratio = ratio;
	}
	public void setSendSms(int sendSms)
	{
		this.sendSms = sendSms;
	}
	public void setProNames(String proNames)
	{
		this.proNames = proNames;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getSpName()
	{
		return spName;
	}
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public int getPrice()
	{
		return price;
	}
	public int getRang()
	{
		return rang;
	}
	public String getFlowName()
	{
		return flowName;
	}
	public int getFlowSize()
	{
		return flowSize;
	}
	public void setFlowName(String flowName)
	{
		this.flowName = flowName;
	}
	public void setFlowSize(int flowSize)
	{
		this.flowSize = flowSize;
	}
	public void setSpName(String spName)
	{
		this.spName = spName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public void setPrice(int price)
	{
		this.price = price;
	}
	public void setRang(int rang)
	{
		this.rang = rang;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public List<TroneModel> getTroneList()
	{
		return troneList;
	}
	public void setTroneList(List<TroneModel> troneList)
	{
		this.troneList = troneList;
	}
	public String getSpApiName()
	{
		return spApiName;
	}
	public void setSpApiName(String spApiName)
	{
		this.spApiName = spApiName;
	}
	public String getOperatorName()
	{
		return operatorName;
	}
	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}
	
	
	
	
}
