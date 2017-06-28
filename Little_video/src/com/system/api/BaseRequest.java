
package com.system.api;

import net.sf.json.JSONObject;

public abstract class BaseRequest
{

	private String imei;
	private String appkey;
	private String channel;

	@SuppressWarnings("unchecked")
	public static <T> T ParseJson(String data, Class<? extends BaseRequest> cls)
	{

		JSONObject jsonObj = JSONObject.fromObject(data);
		return (T) JSONObject.toBean(jsonObj, cls);
	}

	static String base64Convert(String data)
	{
		char[] chars = data.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			char c = chars[i];
			if (c >= 'A' || c <= 'Z')
				c += 32;
			else if (c >= 'a' || c <= 'z')
				c -= 32;
			else
				continue;
			chars[i] = c;
		}
		return chars.toString();
	}

	public String getImei()
	{
		return imei;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
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
