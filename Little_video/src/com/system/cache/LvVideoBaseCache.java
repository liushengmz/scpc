
package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.model.LvVideoBaseModel;

public class LvVideoBaseCache
{
	private static List<LvVideoBaseModel> _LvVideoBase;

	public static LvVideoBaseModel getVideoById(int id)
	{
		if (_LvVideoBase == null)
			return null;

		for (LvVideoBaseModel m : _LvVideoBase)
		{
			if (m.getId() == id)
				return m;
		}
		return null;
	}

	static void setCache(List<LvVideoBaseModel> data)
	{
		_LvVideoBase = data;
	}

	public static List<LvVideoBaseModel> getCache()
	{
		return _LvVideoBase;
	}

	public static List<LvVideoBaseModel> getVideosByLevel(int levelId)
	{
		List<LvVideoBaseModel> all = _LvVideoBase;
		if (all == null)
			return null;
		ArrayList<LvVideoBaseModel> rlt = new ArrayList<LvVideoBaseModel>();
		for (LvVideoBaseModel m : all)
		{
			if (m.getLevel() == levelId)
				rlt.add(m);
		}
		return rlt;
	}
}
