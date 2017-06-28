package com.system.server;

import java.util.List;

import com.system.dao.SpBillingDetailDao;
import com.system.model.SpBillingSptroneDetailModel;

public class SpBillingDetailServer
{
	public List<SpBillingSptroneDetailModel> loadSpBillingSpTroneDetail(final int spBillingId)
	{
		return new SpBillingDetailDao().loadSpBillingSpTroneDetail(spBillingId);
	}
	
	public SpBillingSptroneDetailModel getSingleSpBillingSpTroneDetailModel(int id)
	{
		return new SpBillingDetailDao().getSingleSpBillingSpTroneDetailModel(id);
	}
	
	//更新SP帐单下面的某个 SP TRONE 数据
	public void updateSingleSpBillingSpTroneDetail(SpBillingSptroneDetailModel model)
	{
		new SpBillingDetailDao().updateSingleSpBillingSpTroneDetail(model);
		
		new SpBillingServer().updateSpBilling(model.getSpBillingId());
	}
}
