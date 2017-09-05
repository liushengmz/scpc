package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyTime;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyTimeMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyTimeService extends BaseService<Payv2StatisticsDayCompanyTime,Payv2StatisticsDayCompanyTimeMapper>  {

	/**
	 * 批量插入
	 * @param insertList
	 * @return
	 */
	int insertBatchToStatisticsDayCompanyTime(List<Map<String,Object>> insertList);
	
	/**
	 * 统计商户每日各时间段数据
	 * @param minYesterDay
	 * @param maxYesterDay
	 * @param nowDateString
	 */
	void statisticsYesterDayCompanyTime(String minYesterDay, String maxYesterDay,String nowDateString);
	
	List<Payv2StatisticsDayCompanyTime> getCurrentStatisticsDayCompanyTime(boolean isToday,Long companyId,int companyType,String startTime,String endTime);
	
	List<Payv2StatisticsDayCompanyTime> getCurrentStatisticsDayCompanyTime(boolean isToday,Long companyId,int companyType,Long appId,String startTime,String endTime);
	
	/**
	 * 获取当前时间交易曲线图
	 * @param isToday 是否为今日
	 * @param companyId 商户ID
	 * @param companyType 商户类型
	 * @param appId 应用ID
	 * @param payWayId 支付渠道ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	List<Payv2StatisticsDayCompanyTime> getCurrentStatisticsDayCompanyTime(boolean isToday,Long companyId,int companyType,Long appId,Long payWayId,String startTime,String endTime);
	
	List<Payv2StatisticsDayCompanyTime> getStatisticsDayCompanyTime(Map<String,Object> paramMap);

	List<Payv2StatisticsDayCompanyTime> getYesterDayCompanyTime(Long companyId,Integer licenseType, Long app_id, Long pay_way_id, String startTime,
			String endTime);
}
