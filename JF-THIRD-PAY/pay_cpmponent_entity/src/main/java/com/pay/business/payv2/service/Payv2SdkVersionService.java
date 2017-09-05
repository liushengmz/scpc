package com.pay.business.payv2.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Payv2SdkVersion;
import com.pay.business.payv2.mapper.Payv2SdkVersionMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2SdkVersionService extends BaseService<Payv2SdkVersion,Payv2SdkVersionMapper>  {

	public Payv2SdkVersion selectSingle(Payv2SdkVersion t);
	
	public List<Payv2SdkVersion> selectByObject(Payv2SdkVersion t);
	
	public Payv2SdkVersion getVersion(Map<String,Object> map);
}
