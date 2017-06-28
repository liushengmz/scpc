
package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.model.LvLevelModel;

public class LvLevelCache
{
	private static List<LvLevelModel> _LvLevel;

	public static LvLevelModel getLvLevelByLevelId(int id)
	{
		if (_LvLevel == null)
			return null;

		for (LvLevelModel m : _LvLevel)
		{
			if (m.getLevel() == id)
				return m;
		}
		return null;
	}

	public static List<LvLevelModel> getLevelByAppkey(String app)
	{
		List<LvLevelModel> data = _LvLevel;
		if (data == null)
			return null;
		ArrayList<LvLevelModel> rlt = new ArrayList<LvLevelModel>();
		for (LvLevelModel m : data)
		{
			if (m.getAppkey().equalsIgnoreCase(app))
				rlt.add(m);
		}
		return rlt;
	}

	static void setCache(List<LvLevelModel> data)
	{
		_LvLevel = data;
	}

	public static List<LvLevelModel> getCache()
	{
		return _LvLevel;
	}

}
