package com.system.server;

import java.util.List;

import com.system.dao.SpApiUrlDao;
import com.system.model.SpApiUrlModel;

public class SpApiUrlServer
{
	public List<SpApiUrlModel> loadSpApiUrl()
	{
		return new SpApiUrlDao().loadSpApiUrl();
	}
}
