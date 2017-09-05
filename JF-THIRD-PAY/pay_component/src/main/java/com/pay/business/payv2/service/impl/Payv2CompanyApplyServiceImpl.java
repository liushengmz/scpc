package com.pay.business.payv2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.business.payv2.entity.Payv2CompanyApply;
import com.pay.business.payv2.mapper.Payv2CompanyApplyMapper;
import com.pay.business.payv2.service.Payv2CompanyApplyService;
import com.core.teamwork.base.service.impl.BaseServiceImpl;

/**
 * @author cyl
 * @version 
 */
@Service("payv2CompanyApplyService")
public class Payv2CompanyApplyServiceImpl extends BaseServiceImpl<Payv2CompanyApply, Payv2CompanyApplyMapper> implements Payv2CompanyApplyService {
	// 注入当前dao对象
    @Autowired
    private Payv2CompanyApplyMapper payv2CompanyApplyMapper;

    public Payv2CompanyApplyServiceImpl() {
        setMapperClass(Payv2CompanyApplyMapper.class, Payv2CompanyApply.class);
    }
    
 
}
