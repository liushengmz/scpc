package com.pay.business.order.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.order.entity.Payv2PayOrderGroup;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayOrderGroupMapper extends BaseMapper<Payv2PayOrderGroup>{
	public List<Payv2PayOrderGroup> selectByObject2(Map<String,Object> map);
}