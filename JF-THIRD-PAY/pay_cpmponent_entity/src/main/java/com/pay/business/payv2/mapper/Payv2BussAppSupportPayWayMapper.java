package com.pay.business.payv2.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussAppSupportPayWayMapper extends BaseMapper<Payv2BussAppSupportPayWay>{

	/**
	 * 根据支付ID获取所有app支持的支付通道
	 * @param t
	 * @return
	 */
	public List<Payv2BussAppSupportPayWay> selectByPayWayIds(Payv2BussAppSupportPayWay t);

	/**
	 * 查询通道费
	 * @param paramMap 根据商户ID,或者appid,或者支付渠道ID
	 * @return
	 */
	public List<Payv2BussAppSupportPayWay> queryPayWayRate(
			Map<String, Object> paramMap);
	
	/**
	 * 根据支付ID获取所有app支持的支付通道
	 * @param t
	 * @return
	 */
	public List<Payv2BussAppSupportPayWay> selectLinkByObject(Payv2BussAppSupportPayWay t);
}