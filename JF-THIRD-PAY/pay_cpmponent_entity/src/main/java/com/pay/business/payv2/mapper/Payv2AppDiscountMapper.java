package com.pay.business.payv2.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2AppDiscount;

/**
 * @author cyl
 * @version 
 */
public interface Payv2AppDiscountMapper extends BaseMapper<Payv2AppDiscount>{
	public Integer  getCount2(Map<String,Object> map);
	public List<Payv2AppDiscount> pageQueryByObject2(Map<String,Object> map);
	
	/**
	 * 优惠方案列表
	 * @param pad
	 * @return
	 */
	public Payv2AppDiscount discountWay(Payv2AppDiscount pad);
}