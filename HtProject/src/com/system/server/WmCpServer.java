package com.system.server;

import java.util.List;

import com.system.dao.WmCpDao;
import com.system.model.WmCpModel;

public class WmCpServer
{
	public List<WmCpModel> loadCp()
	{
		return new WmCpDao().loadCp();
	}
}
