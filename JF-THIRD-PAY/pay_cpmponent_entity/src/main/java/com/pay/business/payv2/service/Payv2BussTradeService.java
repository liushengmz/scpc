package com.pay.business.payv2.service;

import java.util.List;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Payv2BussTrade;
import com.pay.business.payv2.mapper.Payv2BussTradeMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussTradeService extends BaseService<Payv2BussTrade,Payv2BussTradeMapper>  {

	public Payv2BussTrade selectSingle(Payv2BussTrade t);
	
	public List<Payv2BussTrade> selectByObject(Payv2BussTrade t);
	
	public List<Payv2BussTrade> selectBySort(Payv2BussTrade t);
	
}
