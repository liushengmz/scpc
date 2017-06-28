
package com.system.model;

import java.util.Date;

public class LvUserModel
{
	int		id;
	String	imei;
	String	imsi;
	String	mac;
	String	androidVersion;
	String	androidLevel;
	String	model;
	int	level;
	int		city;
	int		province;
	String	name;
	String	pwd;
	Date	createDate;
	String appkey;
	String channel;
	
	public void setId(int id)
	{
		this.id = id;
	}
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}
	public void setMac(String mac)
	{
		this.mac = mac;
	}
	public void setAndroidVersion(String androidVersion)
	{
		this.androidVersion = androidVersion;
	}
	public void setAndroidLevel(String androidLevel)
	{
		this.androidLevel = androidLevel;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public void setLevel(int level)
	{
		this.level = level;
	}
	public void setCity(int city)
	{
		this.city = city;
	}
	public void setProvince(int province)
	{
		this.province = province;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public int getId()
	{
		return id;
	}
	public String getImei()
	{
		return imei;
	}
	public String getImsi()
	{
		return imsi;
	}
	public String getMac()
	{
		return mac;
	}
	public String getAndroidVersion()
	{
		return androidVersion;
	}
	public String getAndroidLevel()
	{
		return androidLevel;
	}
	public String getModel()
	{
		return model;
	}
	public int getLevel()
	{
		return level;
	}
	public int getCity()
	{
		return city;
	}
	public int getProvince()
	{
		return province;
	}
	public String getName()
	{
		return name;
	}
	public String getPwd()
	{
		return pwd;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public String getAppkey()
	{
		return appkey;
	}
	public void setAppkey(String appkey)
	{
		this.appkey = appkey;
	}
	public String getChannel()
	{
		return channel;
	}
	public void setChannel(String channel)
	{
		this.channel = channel;
	}
}
