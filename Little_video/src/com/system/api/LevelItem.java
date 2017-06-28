
package com.system.api;

import com.system.model.LvLevelModel;

/**
 * 用户等级价格，类同 LvLevelModel
 * 
 * @author Shotgun
 *
 */
public class LevelItem
{
	int		levelId;
	int		price;
	String	remark;

	public LevelItem()
	{
	}

	public LevelItem(LvLevelModel m)
	{
		this.setLevelId(m.getLevel());
		this.setPrice(m.getPrice());
		this.setRemark(m.getRemark());
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public int getLevelId()
	{
		return levelId;
	}

	public void setLevelId(int levelId)
	{
		this.levelId = levelId;
	}
}
