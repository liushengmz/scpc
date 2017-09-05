package com.pay.business.payv2.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.mapper.Payv2BussAppSupportPayWayMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussAppSupportPayWayService extends BaseService<Payv2BussAppSupportPayWay,Payv2BussAppSupportPayWayMapper>  {

	public Payv2BussAppSupportPayWay selectSingle(Payv2BussAppSupportPayWay t);
	
	public List<Payv2BussAppSupportPayWay> selectByObject(Payv2BussAppSupportPayWay t);
	
	/**
	 * 根据支付ID获取所有app支持的支付通道
	 * @param t
	 * @return
	 */
	public List<Payv2BussAppSupportPayWay> selectByPayWayIds(Payv2BussAppSupportPayWay t);
	
	/**
	 * 根据支付ID获取所有app支持的支付通道
	 * @param t
	 * @return
	 */
	public List<Payv2BussAppSupportPayWay> selectLinkByObject(Payv2BussAppSupportPayWay t);

	/**
	 * 查询通道费
	 * @param paramMap 根据商户ID,或者appid,或者支付渠道ID
	 * @return
	 */
	public List<Payv2BussAppSupportPayWay> queryPayWayRate(
			Map<String, Object> paramMap);
}
