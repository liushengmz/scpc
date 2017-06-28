package com.system.server;

import java.util.List;

import com.system.dao.WmTroneDao;
import com.system.model.WmTroneModel;

public class WmTroneServer
{
	public List<WmTroneModel> loadTroneByCpUserId(int userId)
	{
		return new WmTroneDao().loadTroneByCpUserId(userId);
	}
}
