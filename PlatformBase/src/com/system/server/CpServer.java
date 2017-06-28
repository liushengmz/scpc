package com.system.server;

import java.util.List;

import com.system.dao.CpDao;
import com.system.model.CpModel;

public class CpServer
{
	public List<CpModel> loadCpData(int coId)
	{
		return new CpDao().loadCpData(coId);
	}
}
