package com.system.server;
/**
 * 该类用于封装操作Channel表的方法
 * @author Administrator
 *
 */
import java.util.Map;

import com.system.dao.ChannelDao;
import com.system.model.ChannelModel;


public class ChannelServer {
	
	public Map<String, Object> loadChannel(int pageindex){
		return new ChannelDao().loadChannel(pageindex);
	}
	
	public Map<String, Object> loadChannel(int pageIndex,int appid,String appkey,String channel,int type)
	{
		return new ChannelDao().loadChannel(pageIndex, appid, appkey, channel, type);
	}
	
	public ChannelModel loadQdById(int id)
	{
		return new ChannelDao().loadQdById(id);
	}
	
	public boolean updataChannel(ChannelModel model)
	{
		return new ChannelDao().updataChannel(model);
	}
	
	public boolean addChannel(ChannelModel model)
	{
		return new ChannelDao().addChannel(model);
	}
	
	public boolean deletChannel(int id)
	{
		return new ChannelDao().deletChannel(id);
	}
	
	public boolean updateChannelAccount(int channelId,int userId)
	{
		return new ChannelDao().updateChannelAccount(channelId, userId);
	}
}
