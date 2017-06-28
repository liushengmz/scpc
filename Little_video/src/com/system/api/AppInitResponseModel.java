
package com.system.api;

public class AppInitResponseModel extends baseResponse
{
	String		name;
	int			level;
	LevelItem[]	levels;
	String password;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getLevel()
	{
		return level;
	}
	public void setLevel(int level)
	{
		this.level = level;
	}
	public LevelItem[] getLevels()
	{
		return levels;
	}
	public void setLevels(LevelItem[] levels)
	{
		this.levels = levels;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
 
}
