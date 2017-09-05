package com.pay.business.payv2.service;

import java.util.List;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Payv2PlatformWay;
import com.pay.business.payv2.mapper.Payv2PlatformWayMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PlatformWayService extends BaseService<Payv2PlatformWay,Payv2PlatformWayMapper>  {

	public Payv2PlatformWay selectSingle(Payv2PlatformWay t);
	
	public List<Payv2PlatformWay> selectByObject(Payv2PlatformWay t);
	
	public Integer getPayv2PlatformWayNumber(Payv2PlatformWay t);
}
