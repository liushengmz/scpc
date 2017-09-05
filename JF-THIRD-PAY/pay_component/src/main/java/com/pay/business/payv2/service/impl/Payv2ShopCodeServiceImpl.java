package com.pay.business.payv2.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.business.payv2.entity.Payv2ShopCode;
import com.pay.business.payv2.mapper.Payv2ShopCodeMapper;
import com.pay.business.payv2.service.Payv2ShopCodeService;
import com.core.teamwork.base.service.impl.BaseServiceImpl;

/**
 * @author cyl
 * @version 
 */
@Service("payv2ShopCodeService")
public class Payv2ShopCodeServiceImpl extends BaseServiceImpl<Payv2ShopCode, Payv2ShopCodeMapper> implements Payv2ShopCodeService {
	// 注入当前dao对象
    @Autowired
    private Payv2ShopCodeMapper payv2ShopCodeMapper;

    public Payv2ShopCodeServiceImpl() {
        setMapperClass(Payv2ShopCodeMapper.class, Payv2ShopCode.class);
    }

	public List<Payv2ShopCode> queryByShopIdList(Map<String, Object> paramMap) {
		return payv2ShopCodeMapper.queryByShopIdList(paramMap);
	}

	public List<Payv2ShopCode> queryByLeftJoin(Map<String, Object> paramMap) {
		return payv2ShopCodeMapper.queryByLeftJoin(paramMap);
	}
    
 
}
