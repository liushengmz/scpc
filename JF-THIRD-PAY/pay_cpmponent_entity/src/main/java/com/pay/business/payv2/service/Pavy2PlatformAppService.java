package com.pay.business.payv2.service;

import java.util.List;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Pavy2PlatformApp;
import com.pay.business.payv2.mapper.Pavy2PlatformAppMapper;

/**
 * @author cyl
 * @version 
 */
public interface Pavy2PlatformAppService extends BaseService<Pavy2PlatformApp,Pavy2PlatformAppMapper>  {

	public Pavy2PlatformApp selectSingle(Pavy2PlatformApp t);
	
	public List<Pavy2PlatformApp> selectByObject(Pavy2PlatformApp t);
}
