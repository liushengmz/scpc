package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2BussWayDetail;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussWayDetailMapper extends BaseMapper<Payv2BussWayDetail>{
	
	/**
	 * 账单详情
	 * @param map
	 * @return
	 */
	List<Payv2BussWayDetail>  bussWayDetaiList(Map<String, Object> map);
	
	void updateCompanyCheckId(Map<String, Object> map);
	

}