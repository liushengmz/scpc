package com.pay.business.payv2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Payv2PlatformWay;
import com.pay.business.payv2.mapper.Payv2PlatformWayMapper;
import com.pay.business.payv2.service.Payv2PlatformWayService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2PlatformWayService")
public class Payv2PlatformWayServiceImpl extends BaseServiceImpl<Payv2PlatformWay, Payv2PlatformWayMapper> implements Payv2PlatformWayService {
	// 注入当前dao对象
    @Autowired
    private Payv2PlatformWayMapper payv2PlatformWayMapper;

    public Payv2PlatformWayServiceImpl() {
        setMapperClass(Payv2PlatformWayMapper.class, Payv2PlatformWay.class);
    }
    
 	public Payv2PlatformWay selectSingle(Payv2PlatformWay t) {
		return payv2PlatformWayMapper.selectSingle(t);
	}

	public List<Payv2PlatformWay> selectByObject(Payv2PlatformWay t) {
		return payv2PlatformWayMapper.selectByObject(t);
	}

	public Integer getPayv2PlatformWayNumber(Payv2PlatformWay t) {
		return payv2PlatformWayMapper.getCount2(t);
	}
}
