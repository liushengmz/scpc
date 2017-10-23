package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2StatisticsDayTime;
import com.pay.business.record.mapper.Payv2StatisticsDayTimeMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayTimeService extends BaseService<Payv2StatisticsDayTime,Payv2StatisticsDayTimeMapper>  {

	/**
	 * 自定义批量插入
	 * @param allList
	 * @return
	 */
	int insertCustomBatch(List<Map<String, Object>> allList);

	/**
	 * 统计指定时间的数据
	 * @param minYesterDay
	 * @param maxYesterDay
	 * @param nowDateString
	 */
	void statisticsYesterDayTime(String minYesterDay, String maxYesterDay,String nowDateString);

}
