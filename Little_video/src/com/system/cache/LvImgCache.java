
package com.system.cache;

import java.util.List;

import com.system.model.LvImgModel;

public class LvImgCache
{

	private static List<LvImgModel> _lvImg;

	public static LvImgModel getLvImgById(int id)
	{
		if (_lvImg == null)
			return null;

		for (LvImgModel m : _lvImg)
		{
			if (m.getId() == id)
				return m;
		}
		return null;
	}

	static void setCache(List<LvImgModel> data)
	{
		_lvImg = data;
	}

	static List<LvImgModel> getCache()
	{
		return _lvImg;
	}

}
