package com.pay.business.record.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyClear;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyClearMapper;
import com.pay.business.record.service.Payv2StatisticsDayCompanyClearService;

/**
 * @author cyl
 * @version
 */
@Service("payv2StatisticsDayCompanyClearService")
public class Payv2StatisticsDayCompanyClearServiceImpl extends BaseServiceImpl<Payv2StatisticsDayCompanyClear, Payv2StatisticsDayCompanyClearMapper> implements
		Payv2StatisticsDayCompanyClearService {
	// 注入当前dao对象
	@Autowired
	private Payv2StatisticsDayCompanyClearMapper payv2StatisticsDayCompanyClearMapper;

	@Autowired
	private Payv2PayOrderService payv2PayOrderService;

	public Payv2StatisticsDayCompanyClearServiceImpl() {
		setMapperClass(Payv2StatisticsDayCompanyClearMapper.class, Payv2StatisticsDayCompanyClear.class);
	}

	public int insertBatchStatisticsDayCompanyClear(List<Map<String, Object>> insertList) {
		return payv2StatisticsDayCompanyClearMapper.insertBatchStatisticsDayCompanyClear(insertList);
	}

	public void statisticsYesterDayCompanyClear(String minYesterDay, String maxYesterDay) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startTime", minYesterDay);
		paramMap.put("endTime", maxYesterDay);
		paramMap.put("successStatus", 1);
		// paramMap.put("refundStatus", 5);
		List<Map<String, Object>> insertList = payv2PayOrderService.queryOrderInfoToStatisticsDayCompanyClear(paramMap);
		if (insertList != null && insertList.size() > 0) {
			int count = insertBatchStatisticsDayCompanyClear(insertList);
			System.out.println("clearService 插入了:" + count);
		}
	}
	/**
	 * 获取基础数据：交易分布图数据
	 */
	public List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(boolean isToday, Long companyId, Long appId, Integer licenseType, String startTime,
			String endTime, Integer queryType) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("appId", appId);
		paramMap.put("companyType", licenseType);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("queryType", queryType);
		List<Map<String, Object>> resMap = null;
		//isToday：为true的时候：是查询的今日；所以查询的订单总表； 为false的是查询的是：商户每日账单表
		if (isToday) {
			resMap = payv2PayOrderService.getCurrentStatisticsDayCompanyClear(paramMap);
		} else {
			resMap = payv2StatisticsDayCompanyClearMapper.getCurrentStatisticsDayCompanyClear(paramMap);
		}
		return resMap;
	}

	public List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(boolean isToday, Long companyId, Integer licenseType, String startTime,
			String endTime, Integer queryType) {
		return getCurrentStatisticsDayCompanyClear(isToday, companyId, null, licenseType, startTime, endTime, queryType);
	}

	public List<Payv2StatisticsDayCompanyClear> getMonthAggregateCompanyClear(Map<String, Object> map) {
		return payv2StatisticsDayCompanyClearMapper.getMonthAggregateCompanyClear(map);
	}

	public List<Payv2StatisticsDayCompanyClear> getDayAggregateCompanyClear(Map<String, Object> map) {
		return payv2StatisticsDayCompanyClearMapper.getDayAggregateCompanyClear(map);
	}

	public List<Payv2StatisticsDayCompanyClear> getTimeZoneAggregateCompanyClear(Map<String, Object> map) {
		return payv2StatisticsDayCompanyClearMapper.getTimeZoneAggregateCompanyClear(map);
	}

	public PageObject<Payv2StatisticsDayCompanyClear> getPageQueryByGroupByTime(Map<String, Object> map) {
		int totalData = payv2StatisticsDayCompanyClearMapper.getPageQueryByGroupByTimeCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2StatisticsDayCompanyClear> list = payv2StatisticsDayCompanyClearMapper.getPageQueryByGroupByTime(pageHelper.getMap());
		PageObject<Payv2StatisticsDayCompanyClear> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

}
