package com.pay.business.record.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.record.entity.Payv2CompanyQuota;
import com.pay.business.record.mapper.Payv2CompanyQuotaMapper;
import com.pay.business.record.service.Payv2CompanyQuotaService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2CompanyQuotaService")
public class Payv2CompanyQuotaServiceImpl extends BaseServiceImpl<Payv2CompanyQuota, Payv2CompanyQuotaMapper> implements Payv2CompanyQuotaService {
	// 注入当前dao对象
    @Autowired
    private Payv2CompanyQuotaMapper payv2CompanyQuotaMapper;

    public Payv2CompanyQuotaServiceImpl() {
        setMapperClass(Payv2CompanyQuotaMapper.class, Payv2CompanyQuota.class);
    }
    
 
}
