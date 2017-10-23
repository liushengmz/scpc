package com.system.vo;

public class DetailDataVo
{
	private int id;
	private String imei;
	private String imsi;
	private String mobile;
	private String provinceName;
	private String cityName;
	private String spName;
	private String spTroneName;
	private float price;
	private String cpName;
	private int synFlag;
	private String createDate;
	private String order;
	private String troneNum;
	private String linkId;
	private String configOrder;
	private String status;
	
	public String getConfigOrder()
	{
		return configOrder;
	}
	public String getConfigTrone()
	{
		return configTrone;
	}
	public void setConfigOrder(String configOrder)
	{
		this.configOrder = configOrder;
	}
	public void setConfigTrone(String configTrone)
	{
		this.configTrone = configTrone;
	}
	private String configTrone;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getOrder()
	{
		return order;
	}
	public void setOrder(String order)
	{
		this.order = order;
	}
	public String getTroneNum()
	{
		return troneNum;
	}
	public void setTroneNum(String troneNum)
	{
		this.troneNum = troneNum;
	}
	
	public String getImei()
	{
		return imei;
	}
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	public String getImsi()
	{
		return imsi;
	}
	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getProvinceName()
	{
		return provinceName;
	}
	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}
	public String getCityName()
	{
		return cityName;
	}
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	public String getSpName()
	{
		return spName;
	}
	public void setSpName(String spName)
	{
		this.spName = spName;
	}
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public float getPrice()
	{
		return price;
	}
	public void setPrice(float price)
	{
		this.price = price;
	}
	public String getCpName()
	{
		return cpName;
	}
	public void setCpName(String cpName)
	{
		this.cpName = cpName;
	}
	public int getSynFlag()
	{
		return synFlag;
	}
	public void setSynFlag(int synFlag)
	{
		this.synFlag = synFlag;
	}
	public String getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}
	public String getLinkId()
	{
		return linkId;
	}
	public void setLinkId(String linkId)
	{
		this.linkId = linkId;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	
	
}
