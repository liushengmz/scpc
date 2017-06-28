package com.system.server;

import com.system.dao.LvChannelDao;
import com.system.model.LvChannelModel;

public class LvChannelServer
{
	public LvChannelModel loadLvChannelModel(int userId,int symBiosIs)
	{
		return new LvChannelDao().loadLvChannelModel(userId, symBiosIs);
	}
}
