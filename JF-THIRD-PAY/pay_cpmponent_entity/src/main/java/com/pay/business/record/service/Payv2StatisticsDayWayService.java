package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2StatisticsDayWay;
import com.pay.business.record.mapper.Payv2StatisticsDayWayMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayWayService extends BaseService<Payv2StatisticsDayWay,Payv2StatisticsDayWayMapper>  {

	/**
	 * 自定义批量插入
	 * @param insertList
	 * @return
	 */
	int insertCustomBatch(List<Map<String, Object>> insertList);

	/**
	 *  统计指定时间的数据
	 * @param minYesterDay
	 * @param maxYesterDay
	 */
	void statisticsYesterDayWay(String minYesterDay, String maxYesterDay);

}
