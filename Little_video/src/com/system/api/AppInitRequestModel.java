
package com.system.api;

public class AppInitRequestModel extends BaseRequest
{
	String	imsi;
	String	mac;
	String	androidVersion;
	String	androidLevel;
	String	model;
	String	name;
	String	pwd;

	public String getImsi()
	{
		return imsi;
	}

	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}

	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public String getAndroidVersion()
	{
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion)
	{
		this.androidVersion = androidVersion;
	}

	public String getAndroidLevel()
	{
		return androidLevel;
	}

	public void setAndroidLevel(String androidLevel)
	{
		this.androidLevel = androidLevel;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;
	}

	public int getCity()
	{
		return 0;
	}

	public int getProvince()
	{
		return 32;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
}
