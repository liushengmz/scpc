package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyClear;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyClearMapper extends BaseMapper<Payv2StatisticsDayCompanyClear>{

	/**
	 * 批量插入
	 * @param insertList
	 * @return
	 */
	int insertBatchStatisticsDayCompanyClear(
			List<Map<String, Object>> insertList);

	/**
	 * 获取交易的记录
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(
			Map<String, Object> paramMap);

	/**
	 * 获取一个月的汇总交易数据(会对商户 交易类型 应用分组)
	 * @param map
	 * @return
	 */
	List<Payv2StatisticsDayCompanyClear> getMonthAggregateCompanyClear(
			Map<String, Object> map);

	/**
	 * 获取日汇总(就是返回一个月的汇总交易数据)
	 * @param map
	 * @return
	 */
	List<Payv2StatisticsDayCompanyClear> getDayAggregateCompanyClear(
			Map<String, Object> map);
	
	/**
	 * 获取时间区域内汇总交易数据
	 * @param map
	 * @return
	 */
	public List<Payv2StatisticsDayCompanyClear> getTimeZoneAggregateCompanyClear(
			Map<String, Object> map);
	
	
	public Integer getPageQueryByGroupByTimeCount(Map<String,Object> map);
	
	/**
	 * 日月汇总交易账单列表
	 * @param map
	 * @return
	 */
	public List<Payv2StatisticsDayCompanyClear> getPageQueryByGroupByTime(Map<String, Object> map);

}