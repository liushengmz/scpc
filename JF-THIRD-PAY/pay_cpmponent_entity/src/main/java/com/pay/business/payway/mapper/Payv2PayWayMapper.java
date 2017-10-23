package com.pay.business.payway.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payway.entity.Payv2PayWay;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayWayMapper extends BaseMapper<Payv2PayWay>{


	/**
	 * 获取所有属于钱包类型的支付方式
	 * @param map
	 * @return
	 */
	public List<Payv2PayWay> selectByPayWay(Map<String, Object> map);
	
	/**
	 * 查询商户支持的支付方式
	 * @param map
	 * @return
	 */
	public List<Payv2PayWay> queryCompanyWay(Map<String,Object> map);
	
	/**
	 * 根据渠道商id查询支付方式
	 * @param channelId
	 * @return
	 */
	public List<Payv2PayWay> queryByChannelId(Long channelId);
}