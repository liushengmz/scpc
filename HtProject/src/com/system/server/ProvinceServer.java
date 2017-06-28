package com.system.server;

import java.util.List;

import com.system.dao.ProvinceDao;
import com.system.model.ProvinceModel;

public class ProvinceServer
{
	public List<ProvinceModel> loadProvince()
	{
		return new ProvinceDao().loadProvinceList();
	}
}
