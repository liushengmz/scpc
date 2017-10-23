package com.pay.business.payv2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.business.payv2.entity.Payv2Bank;
import com.pay.business.payv2.mapper.Payv2BankMapper;
import com.pay.business.payv2.service.Payv2BankService;
import com.core.teamwork.base.service.impl.BaseServiceImpl;

/**
 * @author cyl
 * @version 
 */
@Service("payv2BankService")
public class Payv2BankServiceImpl extends BaseServiceImpl<Payv2Bank, Payv2BankMapper> implements Payv2BankService {
	// 注入当前dao对象
    @Autowired
    private Payv2BankMapper payv2BankMapper;

    public Payv2BankServiceImpl() {
        setMapperClass(Payv2BankMapper.class, Payv2Bank.class);
    }
    
 
}
