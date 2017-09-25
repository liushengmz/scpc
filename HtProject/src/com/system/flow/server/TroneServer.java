package com.system.flow.server;

import java.util.List;

import com.system.flow.dao.TroneDao;
import com.system.flow.model.TroneModel;

public class TroneServer
{
	public List<TroneModel> loadTroneBySpTroneId(int spTroneId)
	{
		return new TroneDao().loadTroneBySpTroneId(spTroneId);
	}
}
