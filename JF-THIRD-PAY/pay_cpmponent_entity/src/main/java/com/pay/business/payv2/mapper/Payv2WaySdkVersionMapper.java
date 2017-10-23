package com.pay.business.payv2.mapper;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2WaySdkVersion;

/**
 * @author cyl
 * @version 
 */
public interface Payv2WaySdkVersionMapper extends BaseMapper<Payv2WaySdkVersion>{

	/**
	 * 得到最新版本
	 * @param t
	 * @return
	 */
	public Payv2WaySdkVersion getLastVersion(Payv2WaySdkVersion t);
}