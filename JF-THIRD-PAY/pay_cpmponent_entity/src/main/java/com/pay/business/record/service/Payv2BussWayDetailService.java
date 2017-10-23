package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2BussWayDetail;
import com.pay.business.record.mapper.Payv2BussWayDetailMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussWayDetailService extends BaseService<Payv2BussWayDetail,Payv2BussWayDetailMapper>  {
	
	/**
	 * 日账单详情
	 * @param map
	 * @return
	 */
	Map<String, Object>  dayBussWayDetaiList(Map<String, Object> map);

	/**
	 * 月账单详情
	 * @param bussWayDetailBean
	 * @return
	 */
	Map<Long, Payv2BussWayDetail> mouthBussWayDetaiList(Map<String, Object> map);
	
	/**
	 * 生成日账单
	 * 
	 * @param map
	 * @return
	 */
	List<Payv2BussWayDetail> queryByCom(Map<String, Object> map);
	
}
