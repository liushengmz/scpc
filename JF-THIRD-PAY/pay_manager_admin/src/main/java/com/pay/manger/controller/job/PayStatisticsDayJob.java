package com.pay.manger.controller.job;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.record.service.Payv2StatisticsDayChannelService;
import com.pay.business.record.service.Payv2StatisticsDayTimeService;
import com.pay.business.record.service.Payv2StatisticsDayWayService;

/**
 * 每天统计支付数据
 * @ClassName: PayStatisticsDayJob 
 * @Description: 
 * @author yangyu
 * @date 2017年2月11日 上午9:04:58
 */
@Component("payStatisticsDayJob")
public class PayStatisticsDayJob {
	
	@Autowired
	private Payv2StatisticsDayChannelService payv2StatisticsDayChannelService;
	
	@Autowired
	private Payv2StatisticsDayTimeService payv2StatisticsDayTimeService;
	
	@Autowired
	private Payv2StatisticsDayWayService payv2StatisticsDayWayService;
	
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	
	/**
	 * 数据统计
	 */
	public void dataCount(){
		String minSuffix = " 00:00:00";
		String maxSuffix = " 23:59:59";
		final String nowDateString = dateOptimization(getAppointDate(-1));
		final String minYesterDay = nowDateString + minSuffix;
		final String maxYesterDay = nowDateString + maxSuffix;
		new Thread(new Runnable() {
			//统计day_channel
			@Override
			public void run() {
				payv2StatisticsDayChannelService.statisticsYesterDayChannel(minYesterDay,maxYesterDay);
			}
		},"channelService").start();
		
		new Thread(new Runnable() {
			//统计day_time
			@Override
			public void run() {
				payv2StatisticsDayTimeService.statisticsYesterDayTime(minYesterDay,maxYesterDay, nowDateString);
			}
		},"timeService").start();
		
		new Thread(new Runnable() {
			//统计day_way
			@Override
			public void run() {
				payv2StatisticsDayWayService.statisticsYesterDayWay(minYesterDay,maxYesterDay);
			}
		},"wayService").start();
		
	}
	
	/**
	 * 获取当前日期的,指定天数日期
	 * @param day -1代表前一天,正1代表后一天,0代表当天
	 * @return
	 */
	public Date getAppointDate(int day){
		return getAppointDate(new Date(),day);
	}
	
	public Date getAppointDate(Date date,int day){
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(date);//把当前时间赋给日历
		calendar.add(Calendar.DATE,day);  //设置为前一天
		return calendar.getTime();   //得到前一天的时间
	}
	
	public void test(){
		String minYesterDay = "2017-02-07 00:00:00";
		String maxYesterDay = "2017-02-07 23:59:59";
		String nowDateString = "2017-02-07";
		payv2StatisticsDayTimeService.statisticsYesterDayTime(minYesterDay,maxYesterDay, nowDateString);
	}
	
	public String dateOptimization(Date date){
		return DateUtil.DateToString(date, DateStyle.YYYY_MM_DD);
	}
	
}
