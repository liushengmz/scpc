package com.system.server;

import java.util.List;

import com.system.dao.CpBillingDetailDao;
import com.system.model.CpBillingSptroneDetailModel;

public class CpBillingDetailServer
{
	public List<CpBillingSptroneDetailModel> loadCpBillingSpTroneDetail(final int cpBillingId)
	{
		return new CpBillingDetailDao().loadCpBillingSpTroneDetail(cpBillingId);
	}
	
	public CpBillingSptroneDetailModel getSingleCpBillingSpTroneDetailModel(int id)
	{
		return new CpBillingDetailDao().getSingleCpBillingSpTroneDetailModel(id);
	}
	
	//更新CP帐单下面的某个 SP TRONE 数据
	public void updateSingleCpBillingSpTroneDetail(CpBillingSptroneDetailModel model)
	{
		new CpBillingDetailDao().updateSingleCpBillingSpTroneDetail(model);
		
		new CpBillingServer().updateCpBilling(model.getCpBillingId());
	}
}
