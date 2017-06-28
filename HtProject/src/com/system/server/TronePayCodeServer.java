package com.system.server;

import com.system.dao.TronePayCodeDao;
import com.system.model.TronePayCodeModel;

public class TronePayCodeServer
{
	public boolean addTronePayCode(TronePayCodeModel model)
	{
		return new TronePayCodeDao().addTronePayCode(model);
	}
	
	public boolean updateTronePayCode(TronePayCodeModel model)
	{
		return new TronePayCodeDao().updateTronePayCode(model);
	}
	
	public TronePayCodeModel getTronePayCode(int tronePayCodeId)
	{
		return new TronePayCodeDao().getTronePayCode(tronePayCodeId);
	}
}
