package com.pay.business.payv2.service;

import java.util.List;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payv2.entity.Payv2PayUser;
import com.pay.business.payv2.mapper.Payv2PayUserMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayUserService extends BaseService<Payv2PayUser,Payv2PayUserMapper>  {

	public Payv2PayUser selectSingle(Payv2PayUser t);
	
	public List<Payv2PayUser> selectByObject(Payv2PayUser t);
	
	//根据用户唯一标识判断是否存在 不存在，就新增
	public Payv2PayUser selectByToken(String token,Long appId);
	
	/**
	 * 查询对象（缓存）
	 * @param t
	 * @return
	 */
	public Payv2PayUser selectSingle1(Payv2PayUser t);
}
