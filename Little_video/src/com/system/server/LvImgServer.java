
package com.system.server;

import com.system.cache.LvImgCache;
import com.system.model.LvImgModel;

public class LvImgServer
{

	public LvImgModel getLvImgById(int imgId)
	{
		return LvImgCache.getLvImgById(imgId);
	}

}
