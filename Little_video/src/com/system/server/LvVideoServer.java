
package com.system.server;

import java.util.List;

import com.system.cache.LvVideoBaseCache;
import com.system.model.LvVideoBaseModel;

public class LvVideoServer
{
	public List<LvVideoBaseModel> getVideoByLevel(int levelId)
	{
		return LvVideoBaseCache.getVideosByLevel(levelId);
	}
}
