package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyGoods;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyGoodsMapper extends BaseMapper<Payv2StatisticsDayCompanyGoods>{

	/**
	 * 批量插入
	 * @param insertList
	 * @return
	 */
	int insertBatchStatisticsDayCompanyGoods(
			List<Map<String, Object>> insertList);

	/**
	 * 获取指定商户的商品销售数据
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getCurrentStatisticsDayCompanyGoods(
			Map<String, Object> paramMap);

}