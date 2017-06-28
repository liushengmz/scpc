
package com.system.model;

import java.util.Date;

public class LvImgModel
{
	int		id;
	String	name;
	int		length;
	String	path;
	Date	createDate;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date date)
	{
		this.createDate = date;
	}

}
