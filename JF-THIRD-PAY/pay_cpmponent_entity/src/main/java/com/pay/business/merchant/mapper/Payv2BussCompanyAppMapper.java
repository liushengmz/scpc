package com.pay.business.merchant.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussCompanyAppMapper extends BaseMapper<Payv2BussCompanyApp>{

	/**
	 * 根据渠道商ID过滤掉非当前用户的app
	 * @param map
	 * @return
	 */
	public Integer getLinkCount(Map<String,Object> map);
	/**
	 * 根据渠道商ID过滤掉非当前用户的app
	 * @param map
	 * @return
	 */
	public List<Payv2BussCompanyApp> pageLinkQueryByObject(Map<String,Object> map);
	
	/**
	 * 根据APPID获取所有app 总数
	 * @param t
	 * @return
	 */
	public Integer selectCountByAppIds(Map<String,Object> map);
	/**
	 * 根据APPID获取所有app
	 * @param t
	 * @return
	 */
	public List<Payv2BussCompanyApp> selectByAppIds(Map<String,Object> map);
	
	public List<Payv2BussCompanyApp> selectByObject2(Map<String, Object> map);
	
	/**
	 * 根据appkey查询app
	 * @param map
	 * @return
	 */
	public Payv2BussCompanyApp queryByAppKey(Map<String, Object> map);
	
	/**
	 * 获取商户的所有APP id编号
	 * 
	 * @param map
	 * @return
	 */
	List<Payv2BussCompanyApp> queryAppIdByCompanyId(Map<String, Object> map);
}