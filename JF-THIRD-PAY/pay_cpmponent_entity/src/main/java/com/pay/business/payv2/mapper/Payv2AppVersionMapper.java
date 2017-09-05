package com.pay.business.payv2.mapper;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2AppVersion;

/**
 * @author cyl
 * @version 
 */
public interface Payv2AppVersionMapper extends BaseMapper<Payv2AppVersion>{

	
	/**
	 * 得到最新的版本
	 * @param t
	 * @return
	 */
	public Payv2AppVersion  selectLatestByObject(Payv2AppVersion t);
}