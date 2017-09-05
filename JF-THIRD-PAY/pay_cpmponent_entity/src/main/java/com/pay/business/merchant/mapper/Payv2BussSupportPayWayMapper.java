package com.pay.business.merchant.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussSupportPayWayMapper extends BaseMapper<Payv2BussSupportPayWay>{

	public List<Payv2BussSupportPayWay> selectByObjectForCompany(Payv2BussSupportPayWay t);

	public List<Map<String, Object>> queryPayWayIdAndNameByCompanyId(
			Long companyId);
	
	/**
	 * 根据商户ID获取分组后的支付渠道
	 * @param t
	 * @return
	 */
	public List<Payv2BussSupportPayWay> selectPayWayIdByGroup(Payv2BussSupportPayWay t);
	
	/**
	 * 根据支付类型和商户查询商户支持支付通道
	 * @param map
	 * @return
	 */
	public List<Payv2BussSupportPayWay> queryByCompany(Map<String,Object> map);
}