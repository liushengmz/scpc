package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyTime;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyTimeMapper extends BaseMapper<Payv2StatisticsDayCompanyTime>{

	/**
	 * 批量插入
	 * @param insertList
	 * @return
	 */
	int insertBatchToStatisticsDayCompanyTime(
			List<Map<String, Object>> insertList);

	/**
	 * 查询指定时间内的24时段数据
	 * @param paramMap
	 * @return
	 */
	List<Payv2StatisticsDayCompanyTime> getCurrentStatisticsDayCompanyTime(
			Map<String, Object> paramMap);

	List<Payv2StatisticsDayCompanyTime> getYesterDayCompanyTime(
			Map<String, Object> paramMap);

}