package com.pay.business.payv2.mapper;

import java.util.List;
import java.util.Map;

import com.pay.business.payv2.entity.Payv2ShopCode;
import com.core.teamwork.base.mapper.BaseMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2ShopCodeMapper extends BaseMapper<Payv2ShopCode>{

	List<Payv2ShopCode> queryByShopIdList(Map<String, Object> paramMap);

	List<Payv2ShopCode> queryByLeftJoin(Map<String, Object> paramMap);

}