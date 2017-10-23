package com.pay.business.merchant.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussCompanyShopMapper extends BaseMapper<Payv2BussCompanyShop>{


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
	public List<Payv2BussCompanyShop> pageLinkQueryByObject(HashMap<String,Object> map);
	/**
	* @Title: getLineShopList 
	* @Description:获取代理平台线下的商铺
	* @param map
	* @return    设定文件 
	* @return List<Payv2BussCompanyShop>    返回类型 
	* @date 2016年12月28日 上午10:06:52 
	* @throws
	*/
	public List<Payv2BussCompanyShop> getLineShopList(Map<String, Object> map);
	
	public Integer selectCountsByPayWayIds(Map<String, Object> map);
	
	/**
	 * 根据渠道商获取商铺列表
	 * @param t
	 * @return
	 */
	public List<Payv2BussCompanyShop> selectByPayWayIds(Map<String, Object> map);
	
	public Integer qrcodeListCount(Map<String,Object> map);
	
	public List<Payv2BussCompanyShop> qrcodeList(Map<String,Object> map);
	
	public List<Payv2BussCompanyShop> allQrcodeList(Map<String,Object> map);
	
	/**
	 * 根据任意条件查询
	 * @param map key为实体对象Payv2BussCompanyShop的属性
	 * @return 只会返回 id,shop_key,shop_name,shop_card
	 */
	public List<Map<String, Object>> getshopIdNameList(Payv2BussCompanyShop payv2BussCompanyShop);
}