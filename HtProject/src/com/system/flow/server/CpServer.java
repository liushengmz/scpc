package com.system.flow.server;

import java.util.List;

import com.system.flow.dao.CpDao;
import com.system.flow.model.CpModel;

public class CpServer
{
	public List<CpModel> loadCp(int status)
	{
		return new CpDao().loadCp(status);
	}
		
	public CpModel getCpById(int cpId)
	{
		return new CpDao().getCpById(cpId);
	}
}
