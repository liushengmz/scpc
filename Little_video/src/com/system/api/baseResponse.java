
package com.system.api;

import net.sf.json.JSONObject;

public class baseResponse
{
	private int status;
	
	String toJson()
	{
		JSONObject jobj = JSONObject.fromObject(this);
		return jobj.toString();
	}
	
	
	@Override
	public String toString()
	{
		return this.toJson();
	}


	public int getStatus()
	{
		return status;
	}


	public void setStatus(int status)
	{
		this.status = status;
	}

}
