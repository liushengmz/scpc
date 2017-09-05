package com.pay.business.payv2.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2WaySdkVersion;
import com.pay.business.payv2.mapper.Payv2WaySdkVersionMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2WaySdkVersionService extends BaseService<Payv2WaySdkVersion,Payv2WaySdkVersionMapper>  {

	public Payv2WaySdkVersion selectSingle(Payv2WaySdkVersion t);
	
	public List<Payv2WaySdkVersion> selectByObject(Payv2WaySdkVersion t);
	
	/**
	 * 钱包支付sdk列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2WaySdkVersion> payv2WaySdkVersionList(Map<String, Object> map);
	
	/**
	 * 得到最新版本
	 * @param t
	 * @return
	 */
	public Payv2WaySdkVersion getLastVersion(Map<String, Object> map);
}
