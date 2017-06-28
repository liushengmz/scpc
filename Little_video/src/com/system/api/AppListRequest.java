package com.system.api;
/**
 * 视频列表请求Model
 * @author Administrator
 *
 */
public class AppListRequest extends BaseRequest
{
	/**
	 * 请求板块ID
	 */
	int levelId;

	public int getLevelId()
	{
		return levelId;
	}

	public void setLevelId(int levelId)
	{
		this.levelId = levelId;
	}


}
