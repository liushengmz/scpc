package com.system.flow.server;

import java.util.List;

import com.system.flow.dao.SpDao;
import com.system.flow.model.SpModel;

public class SpServer
{
	public List<SpModel> loadSp(int status)
	{
		return new SpDao().loadSp(status);
	}
}
