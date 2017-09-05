package com.pay.business.payv2.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2AgentPlatform;
import com.pay.business.payv2.mapper.Payv2AgentPlatformMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2AgentPlatformService extends BaseService<Payv2AgentPlatform,Payv2AgentPlatformMapper>  {

	public Payv2AgentPlatform selectSingle(Payv2AgentPlatform t);
	
	public List<Payv2AgentPlatform> selectByObject(Payv2AgentPlatform t);
	
	/**
	 * 代理平台列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2AgentPlatform> getPayv2AgentPlatformList(Map<String, Object> map);

}
