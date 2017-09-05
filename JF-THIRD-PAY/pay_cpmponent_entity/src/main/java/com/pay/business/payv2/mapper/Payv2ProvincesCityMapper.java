package com.pay.business.payv2.mapper;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2ProvincesCity;

/**
 * @author cyl
 * @version 
 */
public interface Payv2ProvincesCityMapper extends BaseMapper<Payv2ProvincesCity>{
	/**
	 * 根据名称查询单个
	 * @return
	 */
	public Payv2ProvincesCity queryByName(Payv2ProvincesCity ppc);
}