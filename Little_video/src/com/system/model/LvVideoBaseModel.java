
package com.system.model;

import java.util.Date;

public class LvVideoBaseModel
{
	int		id;
	String	name;
	int		imgId;
	int		level;
	int		time_length;
	int		length;
	String	path;
	String	remark;
	Date	create_date;
	
	public void setId(int id)
	{
		this.id = id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setImg_id(int img_id)
	{
		this.imgId = img_id;
	}
	public void setLevel(int level)
	{
		this.level = level;
	}
	public void setTimeLength(int time_length)
	{
		this.time_length = time_length;
	}
	public void setLength(int length)
	{
		this.length = length;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public void setCreateDate(Date create_date)
	{
		this.create_date = create_date;
	}
	public int getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public int getImgId()
	{
		return imgId;
	}
	public int getLevel()
	{
		return level;
	}
	public int getTimeLength()
	{
		return time_length;
	}
	public int getLength()
	{
		return length;
	}
	public String getPath()
	{
		return path;
	}
	public String getRemark()
	{
		return remark;
	}
	public Date getCreateDate()
	{
		return create_date;
	}
}
