package com.system.server;
/**
 * 该类用于封装操作Channel表的方法
 * @author Administrator
 *
 */
import java.util.Map;

import com.system.dao.AdChannelDao;
import com.system.dao.ChannelDao;
import com.system.model.AdChannelModel;
import com.system.model.ChannelModel;


public class AdChannelServer {
	
	public Map<String, Object> loadChannel(int pageindex){
		return new AdChannelDao().loadChannel(pageindex);
	}
	
	public Map<String, Object> loadChannel(int pageIndex,int appid,String appkey,String channel,String channelname)
	{
		return new AdChannelDao().loadChannel(pageIndex, appid, appkey, channel,channelname);
	}
	
	public AdChannelModel loadQdById(int id)
	{
		return new AdChannelDao().loadQdById(id);
	}
	
	public boolean updataChannel(AdChannelModel model)
	{
		return new AdChannelDao().updataChannel(model);
	}
	
	public boolean addChannel(AdChannelModel model)
	{
		return new AdChannelDao().addChannel(model);
	}
	
	public boolean deletChannel(int id)
	{
		return new AdChannelDao().deletChannel(id);
	}
	
	public Map<String, Object> loadAdChannelName()
	{
		return new AdChannelDao().loadChannel();
	}
}
