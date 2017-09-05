package com.pay.business.payv2.service;

import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2BankAppKey;
import com.pay.business.payv2.mapper.Payv2BankAppKeyMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BankAppKeyService extends BaseService<Payv2BankAppKey,Payv2BankAppKeyMapper>  {
	public PageObject<Payv2BankAppKey> payv2BankAppKeyList(Map<String,Object> map);
}
