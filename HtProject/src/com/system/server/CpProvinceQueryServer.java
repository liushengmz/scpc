package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.CpProvinceQueryDao;

public class CpProvinceQueryServer
{
	public Map<Integer,Map<Float,List<Integer>>> loadCpProvince(final int cpId)
	{
		return new CpProvinceQueryDao().loadCpProvince(cpId);
	}
}
