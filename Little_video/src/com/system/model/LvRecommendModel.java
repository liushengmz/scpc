
package com.system.model;

import java.util.Date;

public class LvRecommendModel
{

	int		id;
	int		type;
	int		videoId;
	int		sortId;
	Date	createDate;

	public void setId(int id)
	{
		this.id = id;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public void setVideoId(int video_id)
	{
		this.videoId = video_id;
	}

	public void setSortId(int sort_id)
	{
		this.sortId = sort_id;
	}

	public void setCreateDate(Date create_date)
	{
		this.createDate = create_date;
	}

	public int getId()
	{
		return id;
	}

	public int getType()
	{
		return type;
	}

	public int getVideoId()
	{
		return videoId;
	}

	public int getSortId()
	{
		return sortId;
	}

	public Date getCreateDate()
	{
		return createDate;
	}
}
