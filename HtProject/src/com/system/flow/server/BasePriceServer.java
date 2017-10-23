package com.system.flow.server;

import java.util.List;

import com.system.flow.dao.BasePriceDao;
import com.system.flow.model.BasePriceModel;

public class BasePriceServer
{
	public List<BasePriceModel> loadBasePrice()
	{
		return new BasePriceDao().loadBasePrice();
	}
}
