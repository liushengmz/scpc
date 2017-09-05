package com.pay.business.payv2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Payv2BussTrade;
import com.pay.business.payv2.mapper.Payv2BussTradeMapper;
import com.pay.business.payv2.service.Payv2BussTradeService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2BussTradeService")
public class Payv2BussTradeServiceImpl extends BaseServiceImpl<Payv2BussTrade, Payv2BussTradeMapper> implements Payv2BussTradeService {
	// 注入当前dao对象
    @Autowired
    private Payv2BussTradeMapper payv2BussTradeMapper;

    public Payv2BussTradeServiceImpl() {
        setMapperClass(Payv2BussTradeMapper.class, Payv2BussTrade.class);
    }
    
 	public Payv2BussTrade selectSingle(Payv2BussTrade t) {
		return payv2BussTradeMapper.selectSingle(t);
	}

	public List<Payv2BussTrade> selectByObject(Payv2BussTrade t) {
		return payv2BussTradeMapper.selectByObject(t);
	}

	public List<Payv2BussTrade> selectBySort(Payv2BussTrade t) {
		return payv2BussTradeMapper.selectBySort(t);
	}
}
