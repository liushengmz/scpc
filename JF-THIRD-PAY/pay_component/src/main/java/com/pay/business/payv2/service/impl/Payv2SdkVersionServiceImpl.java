package com.pay.business.payv2.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Payv2SdkVersion;
import com.pay.business.payv2.mapper.Payv2SdkVersionMapper;
import com.pay.business.payv2.service.Payv2SdkVersionService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2SdkVersionService")
public class Payv2SdkVersionServiceImpl extends BaseServiceImpl<Payv2SdkVersion, Payv2SdkVersionMapper> implements Payv2SdkVersionService {
	// 注入当前dao对象
    @Autowired
    private Payv2SdkVersionMapper payv2SdkVersionMapper;

    public Payv2SdkVersionServiceImpl() {
        setMapperClass(Payv2SdkVersionMapper.class, Payv2SdkVersion.class);
    }
    
 	public Payv2SdkVersion selectSingle(Payv2SdkVersion t) {
		return payv2SdkVersionMapper.selectSingle(t);
	}

	public List<Payv2SdkVersion> selectByObject(Payv2SdkVersion t) {
		return payv2SdkVersionMapper.selectByObject(t);
	}

	/**
	 * 获取最新版本
	 */
	public Payv2SdkVersion getVersion(Map<String, Object> map) {
		return payv2SdkVersionMapper.getVersion(map);
	}
}
