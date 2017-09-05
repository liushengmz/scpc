package com.pay.business.merchant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.merchant.entity.Payv2ChannelWay;
import com.pay.business.merchant.mapper.Payv2ChannelWayMapper;
import com.pay.business.merchant.service.Payv2ChannelWayService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2ChannelWayService")
public class Payv2ChannelWayServiceImpl extends BaseServiceImpl<Payv2ChannelWay, Payv2ChannelWayMapper> implements Payv2ChannelWayService {
	// 注入当前dao对象
    @Autowired
    private Payv2ChannelWayMapper payv2ChannelWayMapper;

    public Payv2ChannelWayServiceImpl() {
        setMapperClass(Payv2ChannelWayMapper.class, Payv2ChannelWay.class);
    }
    
 
}
