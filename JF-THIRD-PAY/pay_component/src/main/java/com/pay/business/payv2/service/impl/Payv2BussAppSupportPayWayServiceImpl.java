package com.pay.business.payv2.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.mapper.Payv2BussAppSupportPayWayMapper;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2BussAppSupportPayWayService")
public class Payv2BussAppSupportPayWayServiceImpl extends BaseServiceImpl<Payv2BussAppSupportPayWay, Payv2BussAppSupportPayWayMapper> implements Payv2BussAppSupportPayWayService {
	// 注入当前dao对象
    @Autowired
    private Payv2BussAppSupportPayWayMapper payv2BussAppSupportPayWayMapper;

    public Payv2BussAppSupportPayWayServiceImpl() {
        setMapperClass(Payv2BussAppSupportPayWayMapper.class, Payv2BussAppSupportPayWay.class);
    }
    
 	public Payv2BussAppSupportPayWay selectSingle(Payv2BussAppSupportPayWay t) {
		return payv2BussAppSupportPayWayMapper.selectSingle(t);
	}

	public List<Payv2BussAppSupportPayWay> selectByObject(Payv2BussAppSupportPayWay t) {
		return payv2BussAppSupportPayWayMapper.selectByObject(t);
	}

	public List<Payv2BussAppSupportPayWay> selectByPayWayIds(
			Payv2BussAppSupportPayWay t) {
		return payv2BussAppSupportPayWayMapper.selectByPayWayIds(t);
	}

	public List<Payv2BussAppSupportPayWay> queryPayWayRate(
			Map<String, Object> paramMap) {
		return payv2BussAppSupportPayWayMapper.queryPayWayRate(paramMap);
	}

	public List<Payv2BussAppSupportPayWay> selectLinkByObject(
			Payv2BussAppSupportPayWay t) {
		return payv2BussAppSupportPayWayMapper.selectLinkByObject(t);
	}
}
