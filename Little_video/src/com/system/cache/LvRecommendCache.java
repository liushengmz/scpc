package com.system.cache;

import java.util.List;

import com.system.model.LvRecommendModel;

public class LvRecommendCache
{
	private static List<LvRecommendModel> _LvRecommend;

	public static LvRecommendModel getLvRecommendById(int id)
	{
		if (_LvRecommend == null)
			return null;

		for (LvRecommendModel m : _LvRecommend)
		{
			if (m.getId() == id)
				return m;
		}
		return null;
	}
	
	static void setCache(List<LvRecommendModel> data)
	{
		_LvRecommend = data;
	}

	public static List<LvRecommendModel> getCache()
	{
		return _LvRecommend;
	}
}
