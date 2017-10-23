package com.pay.business.merchant.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.merchant.entity.Payv2ChannelRate;
import com.pay.business.merchant.mapper.Payv2ChannelRateMapper;
import com.pay.business.merchant.service.Payv2ChannelRateService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2ChannelRateService")
public class Payv2ChannelRateServiceImpl extends BaseServiceImpl<Payv2ChannelRate, Payv2ChannelRateMapper> implements Payv2ChannelRateService {
	// 注入当前dao对象
    @Autowired
    private Payv2ChannelRateMapper payv2ChannelRateMapper;

    public Payv2ChannelRateServiceImpl() {
        setMapperClass(Payv2ChannelRateMapper.class, Payv2ChannelRate.class);
    }

	public List<Payv2ChannelRate> queryByChannelId(Map<String, Object> map) {
		
		return payv2ChannelRateMapper.queryByChannelId(map);
	}
    
 
}
