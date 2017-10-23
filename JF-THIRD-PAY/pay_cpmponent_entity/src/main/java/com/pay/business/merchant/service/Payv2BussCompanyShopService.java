package com.pay.business.merchant.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.mapper.Payv2BussCompanyShopMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussCompanyShopService extends BaseService<Payv2BussCompanyShop,Payv2BussCompanyShopMapper>  {

	public Payv2BussCompanyShop selectSingle(Payv2BussCompanyShop t);
	
	public List<Payv2BussCompanyShop> selectByObject(Payv2BussCompanyShop t);
	
	/**
	 * 商户商铺列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2BussCompanyShop> payv2BussCompanyShopList(Map<String, Object> map) ;
	/**
	* @Title: getLineShopList 
	* @Description:获取代理平台线下商铺
	* @param map
	* @return    设定文件 
	* @return List<Payv2BussCompanyShop>    返回类型 
	* @date 2016年12月28日 上午10:02:35 
	* @throws
	*/
	public List<Payv2BussCompanyShop> getLineShopList(Map<String, Object> map);
	
	/**
	 * 根据渠道商获取商铺列表
	 * @param t
	 * @return
	 */
	public PageObject<Payv2BussCompanyShop> selectByPayWayIds(Map<String, Object> map);
	
	/**
	 * 更新
	 * @param map
	 */
	public void updateFor(Map<String, Object> map);
	
	/**
	 * 批量二维码列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2BussCompanyShop> qrcodeList(Map<String,Object> map);
	
	public Map<String,Object> uploadZipCode(Map<String,Object> map) throws Exception;
	
	/**
	 * 批量二维码列表
	 * @param map
	 * @return
	 */
	public List<Payv2BussCompanyShop> allQrcodeList(Map<String,Object> map);

	/**
	 * 根据任意条件查询
	 * @param map key为实体对象Payv2BussCompanyShop的属性
	 * @return 只会返回 id,shop_key,shop_name,shop_card
	 */
	public List<Map<String, Object>> getshopIdNameList(Map<String, Object> map);
}
