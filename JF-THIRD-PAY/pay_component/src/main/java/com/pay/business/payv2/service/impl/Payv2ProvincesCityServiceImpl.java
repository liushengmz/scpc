package com.pay.business.payv2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.mapper.Payv2ProvincesCityMapper;
import com.pay.business.payv2.service.Payv2ProvincesCityService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2ProvincesCityService")
public class Payv2ProvincesCityServiceImpl extends BaseServiceImpl<Payv2ProvincesCity, Payv2ProvincesCityMapper> implements Payv2ProvincesCityService {
	// 注入当前dao对象
    @Autowired
    private Payv2ProvincesCityMapper payv2ProvincesCityMapper;

    public Payv2ProvincesCityServiceImpl() {
        setMapperClass(Payv2ProvincesCityMapper.class, Payv2ProvincesCity.class);
    }
    
 	public Payv2ProvincesCity selectSingle(Payv2ProvincesCity t) {
		return payv2ProvincesCityMapper.selectSingle(t);
	}

	public List<Payv2ProvincesCity> selectByObject(Payv2ProvincesCity t) {
		return payv2ProvincesCityMapper.selectByObject(t);
	}
}
