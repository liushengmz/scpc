package com.pay.business.payv2.service;

import java.util.List;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.mapper.Payv2ProvincesCityMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2ProvincesCityService extends BaseService<Payv2ProvincesCity,Payv2ProvincesCityMapper>  {

	public Payv2ProvincesCity selectSingle(Payv2ProvincesCity t);
	
	public List<Payv2ProvincesCity> selectByObject(Payv2ProvincesCity t);
}
