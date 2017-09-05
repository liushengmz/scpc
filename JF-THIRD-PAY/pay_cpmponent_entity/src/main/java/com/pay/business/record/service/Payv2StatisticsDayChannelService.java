package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2StatisticsDayChannel;
import com.pay.business.record.mapper.Payv2StatisticsDayChannelMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayChannelService extends BaseService<Payv2StatisticsDayChannel,Payv2StatisticsDayChannelMapper>  {

	/**
	 * 自定义批量插入
	 * @param insertList
	 * @return
	 */
	int insertCustomBatch(List<Map<String, Object>> insertList);

	/**
	 * 统计指定时间的数据
	 * @param minYesterDay
	 * @param maxYesterDay
	 */
	void statisticsYesterDayChannel(String minYesterDay, String maxYesterDay);

}
