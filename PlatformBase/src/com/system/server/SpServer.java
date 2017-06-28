package com.system.server;

import java.util.List;

import com.system.dao.SpDao;
import com.system.model.SpModel;

public class SpServer
{
	public List<SpModel> loadSpData(int coId)
	{
		return new SpDao().loadSpData(coId);
	}
}
