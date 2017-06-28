package com.system.server;

import java.util.List;

import com.system.dao.CityDao;
import com.system.model.CityModel;

public class CityServer
{
	public List<CityModel> loadCityList()
	{
		return new CityDao().loadCityList();
	}
}
