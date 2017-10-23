package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyClear;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyClearMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyClearService extends BaseService<Payv2StatisticsDayCompanyClear,Payv2StatisticsDayCompanyClearMapper>  {

	/**
	 * 批量插入
	 * @param insertList
	 * @return
	 */
	int insertBatchStatisticsDayCompanyClear(List<Map<String,Object>> insertList);
	
	/**
	 * 静态统计两个时间段的每个商户的各个渠道支付数据
	 * @param minYesterDay
	 * @param maxYesterDay
	 */
	void statisticsYesterDayCompanyClear(String minYesterDay,String maxYesterDay);

	List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(
			boolean isToday, Long companyId, Integer licenseType,String startTime, String endTime,Integer queryType);
	
	/**
	 * 获取交易的记录
	 * @param isToday 是否为今天
	 * @param companyId 商户ID
	 * @param appId 应用ID(门店ID)
	 * @param licenseType 商户的类型(当前登录页面为线上还是线下)
	 * @param startTime
	 * @param endTime
	 * @param queryType 查询类型(1为查询金额,2为查询交易笔数)
	 * @return
	 */
	List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(
			boolean isToday, Long companyId, Long appId, Integer licenseType,String startTime, String endTime,Integer queryType);

	/**
	 * 获取月汇总(就是返回一个月的汇总交易数据)
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
	 * 获取时间区域内汇总交易数据,按支付通道、应用分组
	 * @param map
	 * @return
	 */
	List<Payv2StatisticsDayCompanyClear> getTimeZoneAggregateCompanyClear(
			Map<String, Object> map);
	
	/**
	 * 根据日、月时间分组获取账单
	 * @param map
	 * @return
	 */
	public PageObject<Payv2StatisticsDayCompanyClear> getPageQueryByGroupByTime(
			Map<String, Object> map) ;

}
