package com.pay.business.payv2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.business.payv2.entity.Payv2PayShopQrcode;
import com.pay.business.payv2.mapper.Payv2PayShopQrcodeMapper;
import com.pay.business.payv2.service.Payv2PayShopQrcodeService;
import com.core.teamwork.base.service.impl.BaseServiceImpl;

/**
 * @author cyl
 * @version 
 */
@Service("payv2PayShopQrcodeService")
public class Payv2PayShopQrcodeServiceImpl extends BaseServiceImpl<Payv2PayShopQrcode, Payv2PayShopQrcodeMapper> implements Payv2PayShopQrcodeService {
	// 注入当前dao对象
    @Autowired
    private Payv2PayShopQrcodeMapper payv2PayShopQrcodeMapper;

    public Payv2PayShopQrcodeServiceImpl() {
        setMapperClass(Payv2PayShopQrcodeMapper.class, Payv2PayShopQrcode.class);
    }
    
 
}
