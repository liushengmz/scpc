package com.pay.business.order.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.order.entity.Payv2PayOrderGroup;
import com.pay.business.order.mapper.Payv2PayOrderGroupMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayOrderGroupService extends BaseService<Payv2PayOrderGroup,Payv2PayOrderGroupMapper>  {
	
	public  List<Payv2PayOrderGroup> getGroupList(Map<String,Object> map);
	/**
	* @Title: getGroup 
	* @Description: 获取订单分组id
	* @param @param map
	* @param @param pbca
	* @param @return    设定文件 
	* @return Long    返回类型 
	* @throws
	*/
	public Long getGroup(Map<String, Object> map, Payv2BussCompanyApp pbca);
}
