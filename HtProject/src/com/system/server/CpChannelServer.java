package com.system.server;

import java.util.Map;

import com.system.dao.CpChannelDao;
import com.system.model.CpChannelModel;

public class CpChannelServer {
	public Map<String, Object> loadCpChannel(int pageIndex)
	{
		return new CpChannelDao().loadCpChannel(pageIndex);
	}
	
	public boolean addcpchannel(CpChannelModel model)
	{
		return new CpChannelDao().addCpChannel(model);
	}
	
	public boolean deletCpChannel(int id)
	{
		return new CpChannelDao().deletCpChannel(id);
	}
	
	public CpChannelModel loadCpChannelById(int id)
	{
		return new CpChannelDao().loadCpChannelById(id);
	}
	
	public boolean updateCpChannel(CpChannelModel model)
	{
		return new CpChannelDao().updateCpChannel(model);
	}
	
	public Map<String, Object> loadCpChannel(int pageIndex,String startdate,String enddate,int appid,int channelid)
	{
		return new CpChannelDao().loadCpChannel(pageIndex, startdate, enddate, appid, channelid);
	}
	
	public Map<String, Object> loadQdShow(int pageIndex,int userid,String startDate,String endDate,String appname,String channel)
	{
		return new CpChannelDao().loadQdShow(pageIndex, userid, startDate, endDate, appname, channel);
	}
}
