package com.pay.business.payv2.service;

import java.util.List;
import java.util.Map;

import com.pay.business.payv2.entity.Payv2ShopCode;
import com.pay.business.payv2.mapper.Payv2ShopCodeMapper;
import com.core.teamwork.base.service.BaseService;

/**
 * @author cyl
 * @version 
 */
public interface Payv2ShopCodeService extends BaseService<Payv2ShopCode,Payv2ShopCodeMapper>  {

	/**
	 * 查询门店列表(主要是shopId是list形式in查询)
	 * @param paramMap 其他参数都可以家
	 * @return
	 */
	List<Payv2ShopCode> queryByShopIdList(Map<String, Object> paramMap);

	/**
	 * 用于联合查询
	 * @param paramMap
	 * @return
	 */
	List<Payv2ShopCode> queryByLeftJoin(Map<String, Object> paramMap);

}
