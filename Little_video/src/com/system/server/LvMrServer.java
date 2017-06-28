
package com.system.server;

import com.system.dao.LvMrDao;
import com.system.model.LvMrModel;

public class LvMrServer
{
	public Boolean existed(String orderId)
	{
		return new LvMrDao().existed(orderId);
	}

	public void insert(LvMrModel mr)
	{
		new LvMrDao().insert(mr);
	}
}
