package com.system.server;

import java.util.List;

import com.system.dao.WmSpDao;
import com.system.model.WmSpModel;

public class WmSpServer
{
	public List<WmSpModel> loadSp()
	{
		return new WmSpDao().loadSp();
	}
}
