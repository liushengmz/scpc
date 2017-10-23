package com.pay.business.merchant.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.merchant.entity.Payv2ChannelRate;

/**
 * @author cyl
 * @version 
 */
public interface Payv2ChannelRateMapper extends BaseMapper<Payv2ChannelRate>{
	public List<Payv2ChannelRate> queryByChannelId(Map<String,Object> map);
	
	/**
	 * 根据渠道商id删除通道
	 * @param channelId
	 */
	public void deleteByChannelId(Long channelId);
}