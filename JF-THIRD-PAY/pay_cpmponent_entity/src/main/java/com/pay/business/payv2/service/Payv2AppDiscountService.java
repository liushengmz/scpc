package com.pay.business.payv2.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2AppDiscount;
import com.pay.business.payv2.mapper.Payv2AppDiscountMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2AppDiscountService extends BaseService<Payv2AppDiscount,Payv2AppDiscountMapper>  {

	public Payv2AppDiscount selectSingle(Payv2AppDiscount t);
	
	public List<Payv2AppDiscount> selectByObject(Payv2AppDiscount t);
	/**
	* @Title: getPageObject2 
	* @Description:分页查询
	* @param map
	* @return    设定文件 
	* @return PageObject<Payv2AppDiscount>    返回类型 
	* @date 2016年12月7日 上午11:02:37 
	* @throws
	*/
	public PageObject<Payv2AppDiscount> getPageObject2(Map<String, Object> map);
}
