
package com.system.server;

import com.system.dao.LvRequestDao;
import com.system.model.LvRequestModel;

public class LvRequestServer
{
	public void Insert(LvRequestModel m)
	{
		new LvRequestDao().InsertOrderId(m);
	}

	public LvRequestModel getRequest(String orderId)
	{
		return new LvRequestDao().getRequestByOrderId(orderId);
	}

	public void updateStatus(String orderId, int payStatus, boolean iForce)
	{
		new LvRequestDao().updateStatus(orderId, payStatus, iForce);
	}
}
