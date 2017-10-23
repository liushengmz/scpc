package com.pay.business.merchant.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussCompanyAppService extends BaseService<Payv2BussCompanyApp,Payv2BussCompanyAppMapper>  {

	public Payv2BussCompanyApp selectSingle(Payv2BussCompanyApp t);
	
	public List<Payv2BussCompanyApp> selectByObject(Payv2BussCompanyApp t);
	
	/**
	 * 商户APP列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2BussCompanyApp> payv2BussCompanyAppList(Map<String, Object> map) ;
	
	
	/**
	 * 根据APPID获取所有app
	 * @param t
	 * @return
	 */
	public PageObject<Payv2BussCompanyApp> selectByAppIds(Map<String, Object> map);
	
	
	public List<Payv2BussCompanyApp> selectByObject2(Map<String, Object> map);
	
	/**
	 * 更新应用app密钥,如果公司ID不给,则不传
	 * @param id
	 * @param companyId
	 * @return 返回生成的新密钥
	 */
	public String updateNewAppSecret(Long id,Long companyId);
	
	/**
	 * 检查appKey是否有效
	 * @param map
	 * @return
	 */
	public Payv2BussCompanyApp checkApp(String appKey);
	
	/**
	 * 获取商户的所有APP id编号
	 * 
	 * @param map
	 * @return
	 */
	List<Payv2BussCompanyApp> queryAppIdByCompanyId(Map<String, Object> map);
}
