package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2StatisticsDayCompany;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyService extends BaseService<Payv2StatisticsDayCompany,Payv2StatisticsDayCompanyMapper>  {
	
	void statisticsYesterDayCompany(String minYesterDay,String maxYesterDay);
	
	int insertBatchToStatisticsDayCompany(List<Map<String,Object>> insertList);
	
	/**
	 * 查询关键数据指标
	 * @param isToday 是否今天
	 * @param companyId 商户ID
	 * @param companyType 商户类型(线上 线下?)
	 * @param startTime (查询开始时间)
	 * @param endTime (查询结束时间)
	 * @return
	 */
	Map<String,Object> getCurrentStatisticsDayCompany(boolean isToday,Long companyId,int companyType,String startTime,String endTime);
	
	/**
	 * 查询关键数据指标
	 * @param isToday 是否今天
	 * @param companyId 商户ID
	 * @param companyType 商户类型(线上 线下?)
	 * @param appId 商户应用ID
	 * @param startTime (查询开始时间)
	 * @param endTime (查询结束时间)
	 * @return
	 */
	Map<String,Object> getCurrentStatisticsDayCompany(boolean isToday,Long companyId,int companyType,Long appId,String startTime,String endTime);
}
