package com.pay.company.controller.job;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.record.service.Payv2StatisticsDayCompanyClearService;
import com.pay.business.record.service.Payv2StatisticsDayCompanyGoodsService;
import com.pay.business.record.service.Payv2StatisticsDayCompanyService;
import com.pay.business.record.service.Payv2StatisticsDayCompanyTimeService;

@Component
public class Payv2AllStatisticsDayCompanyJob {

	@Autowired
	private Payv2StatisticsDayCompanyService payv2StatisticsDayCompanyService;
	
	@Autowired
	private Payv2StatisticsDayCompanyClearService payv2StatisticsDayCompanyClearService;
	
	@Autowired
	private Payv2StatisticsDayCompanyGoodsService payv2StatisticsDayCompanyGoodsService;
	
	@Autowired
	private Payv2StatisticsDayCompanyTimeService payv2StatisticsDayCompanyTimeService;
	
	public void test(){
		String minSuffix = " 00:00:00";
		String maxSuffix = " 23:59:59";
		Date nowDate = DateUtil.addDay(new Date(), -1);
		final String nowDateString = DateUtil.DateToString(nowDate, DateStyle.YYYY_MM_DD);
		final String minYesterDay = nowDateString + minSuffix;
		final String maxYesterDay = nowDateString + maxSuffix;
		//payv2StatisticsDayCompanyService.statisticsYesterDayCompany(minYesterDay, maxYesterDay);
		//payv2StatisticsDayCompanyClearService.statisticsYesterDayCompanyClear(minYesterDay, maxYesterDay);
		//payv2StatisticsDayCompanyGoodsService.statisticsYesterDayCompanyGoods(minYesterDay, maxYesterDay);
		payv2StatisticsDayCompanyTimeService.statisticsYesterDayCompanyTime(minYesterDay, maxYesterDay, nowDateString);
	}
	
	/**
	 * 数据统计
	 */
	public void dataCount(){
		String minSuffix = " 00:00:00";
		String maxSuffix = " 23:59:59";
		Date nowDate = DateUtil.addDay(new Date(), -1);
		final String nowDateString = DateUtil.DateToString(nowDate, DateStyle.YYYY_MM_DD);
		final String minYesterDay = nowDateString + minSuffix;
		final String maxYesterDay = nowDateString + maxSuffix;
		new Thread(new Runnable() {
			@Override
			public void run() {
				payv2StatisticsDayCompanyService.statisticsYesterDayCompany(minYesterDay, maxYesterDay);
			}
		},"companyService").start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				payv2StatisticsDayCompanyClearService.statisticsYesterDayCompanyClear(minYesterDay, maxYesterDay);
			}
		},"clearService").start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				payv2StatisticsDayCompanyGoodsService.statisticsYesterDayCompanyGoods(minYesterDay, maxYesterDay);
			}
		},"goodsService").start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				payv2StatisticsDayCompanyTimeService.statisticsYesterDayCompanyTime(minYesterDay, maxYesterDay, nowDateString);
			}
		},"timeService").start();
		
	}
}
