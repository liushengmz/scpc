package com.system.model;

public class BaseRequestModel
{
	private int cpId;
	private int spTroneId;
	private String cpParams;
	private int price;
	private String imei;
	private String imsi;
	private String phoneNum;
	private String packageName;
	private String androidVersion;
	private String netType;
	private String ip;
	private String clientIp;
	private int lac;
	private int cid;
	
	public int getLac()
	{
		return lac;
	}
	public void setLac(int lac)
	{
		this.lac = lac;
	}
	public int getCid()
	{
		return cid;
	}
	public void setCid(int cid)
	{
		this.cid = cid;
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
	public String getCpParams()
	{
		return cpParams;
	}
	public void setCpParams(String cpParams)
	{
		this.cpParams = cpParams;
	}
	public int getPrice()
	{
		return price;
	}
	public void setPrice(int price)
	{
		this.price = price;
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
	public String getPhoneNum()
	{
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}
	public String getPackageName()
	{
		return packageName;
	}
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}
	public String getNetType()
	{
		return netType;
	}
	public void setNetType(String netType)
	{
		this.netType = netType;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public String getClientIp()
	{
		return clientIp;
	}
	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}
	public String getAndroidVersion()
	{
		return androidVersion;
	}
	public void setAndroidVersion(String androidVersion)
	{
		this.androidVersion = androidVersion;
	}
}
