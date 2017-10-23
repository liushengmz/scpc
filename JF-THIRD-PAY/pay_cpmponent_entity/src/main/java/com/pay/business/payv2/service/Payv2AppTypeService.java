package com.pay.business.payv2.service;

import java.util.List;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Payv2AppType;
import com.pay.business.payv2.mapper.Payv2AppTypeMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2AppTypeService extends BaseService<Payv2AppType,Payv2AppTypeMapper>  {

	public Payv2AppType selectSingle(Payv2AppType t);
	
	public List<Payv2AppType> selectByObject(Payv2AppType t);
}
