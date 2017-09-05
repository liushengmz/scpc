package com.pay.business.payv2.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.payv2.entity.Payv2BankAppKey;
import com.pay.business.payv2.mapper.Payv2BankAppKeyMapper;
import com.pay.business.payv2.service.Payv2BankAppKeyService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2BankAppKeyService")
public class Payv2BankAppKeyServiceImpl extends BaseServiceImpl<Payv2BankAppKey, Payv2BankAppKeyMapper> implements Payv2BankAppKeyService {
	// 注入当前dao对象
    @Autowired
    private Payv2BankAppKeyMapper payv2BankAppKeyMapper;
    @Autowired
    private Payv2BussCompanyAppService payv2BussCompanyAppService;

    public Payv2BankAppKeyServiceImpl() {
        setMapperClass(Payv2BankAppKeyMapper.class, Payv2BankAppKey.class);
    }

	public PageObject<Payv2BankAppKey> payv2BankAppKeyList(
			Map<String, Object> map) {
		int totalData = payv2BankAppKeyMapper.getCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2BankAppKey> list = payv2BankAppKeyMapper.pageQueryByObject(pageHelper.getMap());
		Map<String,Object> param = new HashMap<>();
		for (Payv2BankAppKey payv2BankAppKey : list) {
			param.put("id", payv2BankAppKey.getAppId());
			Payv2BussCompanyApp payv2BussCompanyApp =payv2BussCompanyAppService.detail(param);
			payv2BankAppKey.setAppName(payv2BussCompanyApp.getAppName());
		}
		PageObject<Payv2BankAppKey> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}
    
 
}
