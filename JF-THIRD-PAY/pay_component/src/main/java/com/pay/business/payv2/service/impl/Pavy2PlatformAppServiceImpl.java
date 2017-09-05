package com.pay.business.payv2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Pavy2PlatformApp;
import com.pay.business.payv2.mapper.Pavy2PlatformAppMapper;
import com.pay.business.payv2.service.Pavy2PlatformAppService;

/**
 * @author cyl
 * @version 
 */
@Service("pavy2PlatformAppService")
public class Pavy2PlatformAppServiceImpl extends BaseServiceImpl<Pavy2PlatformApp, Pavy2PlatformAppMapper> implements Pavy2PlatformAppService {
	// 注入当前dao对象
    @Autowired
    private Pavy2PlatformAppMapper pavy2PlatformAppMapper;

    public Pavy2PlatformAppServiceImpl() {
        setMapperClass(Pavy2PlatformAppMapper.class, Pavy2PlatformApp.class);
    }
    
 	public Pavy2PlatformApp selectSingle(Pavy2PlatformApp t) {
		return pavy2PlatformAppMapper.selectSingle(t);
	}

	public List<Pavy2PlatformApp> selectByObject(Pavy2PlatformApp t) {
		return pavy2PlatformAppMapper.selectByObject(t);
	}
}
