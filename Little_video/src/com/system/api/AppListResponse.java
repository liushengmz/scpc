
package com.system.api;

import java.util.List;

public class AppListResponse extends baseResponse
{
	/**
	 * 版本ID
	 */
	int			levelId;
	/**
	 * 返回的视频个数
	 */
	int			count;
	/**
	 * 视频列表
	 */
	List<VideoItem>	videos;
	/**
	 * 顶部推荐视频表
	 */
	List<VideoItem>	topVideos;

	public int getLevelId()
	{
		return levelId;
	}

	public void setLevelId(int levelId)
	{
		this.levelId = levelId;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public List<VideoItem> getVideos()
	{
		return videos;
	}

	public void setVideos(List<VideoItem> videos)
	{
		this.videos = videos;
	}

	public List<VideoItem> getTopVideos()
	{
		return topVideos;
	}

	public void setTopVideos(List<VideoItem> topVideos)
	{
		this.topVideos = topVideos;
	}
}
