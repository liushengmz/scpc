
package com.system.handler;

import java.util.ArrayList;
import java.util.List;

import com.system.api.AppListRequest;
import com.system.api.AppListResponse;
import com.system.api.BaseRequest;
import com.system.api.VideoItem;
import com.system.api.baseResponse;
import com.system.model.vrVideoBaseModel;
import com.system.server.LvVideoServer;

public class VideoListHttpHandler extends BaseFilter
{

	@Override
	protected baseResponse ProcessReuqest(String s)
	{
		AppListRequest m = BaseRequest.ParseJson(s, AppListRequest.class);
		Object obj = new LvVideoServer().getVideoByLevel(m.getLevelId());

		@SuppressWarnings("unchecked")
		List<vrVideoBaseModel> lst = (List<vrVideoBaseModel>) obj;

		AppListResponse rlt = new AppListResponse();
		rlt.setLevelId(m.getLevelId());
		rlt.setCount(lst.size());

		ArrayList<VideoItem> videos = new ArrayList<VideoItem>();
		ArrayList<VideoItem> topVideos = new ArrayList<VideoItem>();

		for (vrVideoBaseModel veido : lst)
		{
			if (veido.getType() == 1)
				topVideos.add(new VideoItem(veido));
			else
				videos.add(new VideoItem(veido));
		}
		rlt.setVideos(videos);
		rlt.setTopVideos(topVideos);

		return rlt;
	}

}
