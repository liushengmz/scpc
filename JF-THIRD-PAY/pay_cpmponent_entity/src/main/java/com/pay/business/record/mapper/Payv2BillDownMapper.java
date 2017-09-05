package com.pay.business.record.mapper;

import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2BillDown;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BillDownMapper extends BaseMapper<Payv2BillDown>{
	public Payv2BillDown selectByAppId(Map<String,Object> map);
}