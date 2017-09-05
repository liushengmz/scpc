package com.pay.business.payv2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Payv2AppType;
import com.pay.business.payv2.mapper.Payv2AppTypeMapper;
import com.pay.business.payv2.service.Payv2AppTypeService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2AppTypeService")
public class Payv2AppTypeServiceImpl extends BaseServiceImpl<Payv2AppType, Payv2AppTypeMapper> implements Payv2AppTypeService {
	// 注入当前dao对象
    @Autowired
    private Payv2AppTypeMapper payv2AppTypeMapper;

    public Payv2AppTypeServiceImpl() {
        setMapperClass(Payv2AppTypeMapper.class, Payv2AppType.class);
    }
    
 	public Payv2AppType selectSingle(Payv2AppType t) {
		return payv2AppTypeMapper.selectSingle(t);
	}

	public List<Payv2AppType> selectByObject(Payv2AppType t) {
		return payv2AppTypeMapper.selectByObject(t);
	}
}
