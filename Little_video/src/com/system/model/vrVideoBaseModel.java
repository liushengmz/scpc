
package com.system.model;

/**
 * 合并了Recommand和VideoBaseModel,从VideoBaseModel派生
 * 
 * @author Shotgun
 *
 */
public class vrVideoBaseModel extends LvVideoBaseModel
{
	int	type;
	int	sortId;

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getSortId()
	{
		return sortId;
	}

	public void setSortId(int sortId)
	{
		this.sortId = sortId;
	}

}
