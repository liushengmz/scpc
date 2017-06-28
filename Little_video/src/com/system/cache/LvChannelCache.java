
package com.system.cache;

import java.util.List;

import com.system.model.LvChannelModel;

public class LvChannelCache
{
	private static List<LvChannelModel> _lvChn;

	public static LvChannelModel getDataByChannelAndKey(String channel,
			String appKey)
	{
		if (_lvChn == null)
		{
			System.out.println("[lv]: chn cache not found!");
			return null;
		}
		for (LvChannelModel m : _lvChn)
		{
			if (m.getChannel().equalsIgnoreCase(channel)
					&& m.getAppkey().equalsIgnoreCase(appKey))
				return m;
		}

		System.out.println(String.format("[lv]:chn not match appkey:%s chn:%s ",
				channel, appKey));
		return null;
	}

	static void setCache(List<LvChannelModel> data)
	{
		_lvChn = data;
	}

	public static List<LvChannelModel> getCache()
	{
		return _lvChn;
	}

}
