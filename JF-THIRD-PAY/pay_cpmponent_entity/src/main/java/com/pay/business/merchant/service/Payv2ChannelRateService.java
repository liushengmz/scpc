package com.pay.business.merchant.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.merchant.entity.Payv2ChannelRate;
import com.pay.business.merchant.mapper.Payv2ChannelRateMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2ChannelRateService extends BaseService<Payv2ChannelRate,Payv2ChannelRateMapper>  {
	public List<Payv2ChannelRate> queryByChannelId(Map<String,Object> map);
}
