package com.pay.business.payway.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.mapper.Payv2PayWayMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayWayService extends BaseService<Payv2PayWay,Payv2PayWayMapper>  {

	public Payv2PayWay selectSingle(Payv2PayWay t);
	
	public List<Payv2PayWay> selectByObject(Payv2PayWay t);
	
	public void updatePayWay(Map<String,Object> map) throws Exception;
	
	/**
	 * 支付方式列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2PayWay> getPayv2PayWayList(Map<String, Object> map);
	
	/**
	 * 查询所有为钱包的支付方式
	 * @param map
	 * @return
	 */
	public List<Payv2PayWay> selectByPayWay(Map<String, Object> map);
	
	/**
	 * 查询对象（缓存）
	 * @param t
	 * @return
	 */
	public Payv2PayWay selectSingle1(Payv2PayWay t);
	
	/**
	 * 查询支付方式
	 * @param payMoney
	 * @param appId
	 * @param companyId
	 * @param wayType
	 * @param loc
	 * @param rateType
	 * @return
	 */
	public List<Payv2PayWay> getWalletConfig(Double payMoney, Long companyId, Integer wayType
			, Integer rateType);
	
	/**
	 * 根据渠道商id查询支付方式
	 * @param channelId
	 * @return
	 */
	public List<Payv2PayWay> queryByChannelId(Long channelId);
}
