package com.pay.business.payv2.mapper;

import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2SdkVersion;

/**
 * @author cyl
 * @version 
 */
public interface Payv2SdkVersionMapper extends BaseMapper<Payv2SdkVersion>{
	public Payv2SdkVersion getVersion(Map<String,Object> map);
}