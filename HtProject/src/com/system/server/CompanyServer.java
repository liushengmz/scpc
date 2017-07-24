package com.system.server;

import java.util.List;

import com.system.dao.CompanyDao;
import com.system.model.CompanyModel;

public class CompanyServer
{
	public List<CompanyModel> loadCompany()
	{
		return new CompanyDao().loadCompany();
	}
}
