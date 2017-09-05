package com.pay.business.record.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.record.entity.Payv2StatisticsDayTime;
import com.pay.business.record.mapper.Payv2StatisticsDayTimeMapper;
import com.pay.business.record.service.Payv2StatisticsDayTimeService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2StatisticsDayTimeService")
public class Payv2StatisticsDayTimeServiceImpl extends BaseServiceImpl<Payv2StatisticsDayTime, Payv2StatisticsDayTimeMapper> implements Payv2StatisticsDayTimeService {
	// 注入当前dao对象
    @Autowired
    private Payv2StatisticsDayTimeMapper payv2StatisticsDayTimeMapper;

    @Autowired
	private Payv2PayOrderService payv2PayOrderService;
    
    @Autowired
    private Payv2BussCompanyService payv2BussCompanyService;
    
    public Payv2StatisticsDayTimeServiceImpl() {
        setMapperClass(Payv2StatisticsDayTimeMapper.class, Payv2StatisticsDayTime.class);
    }

	public int insertCustomBatch(List<Map<String, Object>> allList) {
		return payv2StatisticsDayTimeMapper.insertCustomBatch(allList);
	}
	
	public void statisticsYesterDayTime(String minYesterDay, String maxYesterDay,String nowDateString) {
		int success_status = 1;
		ExecutorService pool = Executors.newFixedThreadPool(24);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start_time", minYesterDay);
		map.put("end_time", maxYesterDay);
		map.put("payStatus", success_status);
		List<Long> companyList = payv2PayOrderService.queryBetweenPayOrder(map);
		if(companyList==null || companyList.size()==0){
			return;
		}
		//在查询每个时间段的
		try {
			List<Map<String,Object>> allList = new ArrayList<>(companyList.size() * 24);
			Map<Long,Long> mapKeyes = new HashMap<>(companyList.size());
			for(Long company_id : companyList){
				//24个时间段
				Callable<Map<String,Object>> cb = null;
				List<Map<String,Object>> insertList = new ArrayList<>(24);
				List<String> timeStartCompute = timeStartCompute(nowDateString);
				List<String> timeEndCompute = timeEndCompute(nowDateString);
				for(int i=0;i<24;i++){
					cb = new TimeSliceRunnable(timeStartCompute.get(i),timeEndCompute.get(i), success_status,company_id);
					try {
						Future<Map<String, Object>> ft = pool.submit(cb);
						Map<String, Object> resultMap = ft.get();
						if(resultMap==null || resultMap.size()==0){
							resultMap = new HashMap<>();
							if(mapKeyes.containsKey(company_id)){
								resultMap.put("channel_id", mapKeyes.get(company_id));
							}else{
								//查询
								Map<String,Object> temp = new HashMap<>(2);
								temp.put("id", company_id);
								Payv2BussCompany detail = payv2BussCompanyService.detail(temp);
								if(detail != null && detail.getChannelId() != null){
									mapKeyes.put(detail.getId(), detail.getChannelId());
								}
								resultMap.put("channel_id", detail.getChannelId());
							}
							resultMap.put("allSuccessMoney", 0.0);
							resultMap.put("allSuccessCount", 0);
							resultMap.put("company_id", company_id);
							Date date = new Date();//统计的时间要用昨天的
							Date yester = DateUtil.addDay(date, -1);
							resultMap.put("statistics_time",yester);
							resultMap.put("create_time",date);
							resultMap.put("time_type", i);
							insertList.add(resultMap);
						}else{
							if(resultMap.get("allSuccessMoney") == null){
								resultMap.put("allSuccessMoney", 0.00);
							}
							if(resultMap.get("company_id") == null){
								resultMap.put("company_id", company_id);
							}
							resultMap.put("time_type", i);
							insertList.add(resultMap);
						}
						/*if(resultMap.get("allSuccessMoney")==null){
							resultMap.put("allSuccessMoney", 0.00);
						}
						resultMap.put("company_id", company_id);
						Date date = new Date();
						resultMap.put("statistics_time",date);
						resultMap.put("create_time",date);
						resultMap.put("time_type", i);
						insertList.add(resultMap);*/
						
					} catch (Exception e) {
						logger.error("timeService异步统计异常:",e);
					}
				}
				allList.addAll(insertList);
			}
			//批量插入所有
			if(allList != null && allList.size()>0){
				int insertCount = insertCustomBatch(allList);
				System.out.println("timeService 统计了:" + insertCount);
			}
		} catch (Exception e) {
			logger.error("timeService批量插入异常",e);
		}finally{
			pool.shutdown();
		}
	}
	class TimeSliceRunnable implements Callable<Map<String,Object>>{

		//private Payv2PayOrderService payv2PayOrderService;
		private String currentStartTime;
		private String currentEndTime;
		private int status;
		private Long company_id;
		
		public TimeSliceRunnable(
				String currentStartTime, String currentEndTime, int status,Long company_id) {
			//this.payv2PayOrderService = payv2PayOrderService;
			this.currentStartTime = currentStartTime;
			this.currentEndTime = currentEndTime;
			this.status = status;
			this.company_id = company_id;
		}

		@Override
		public Map<String, Object> call() throws Exception {
			Map<String,Object> map = new HashMap<>(6);
			map.put("thisStartTime", currentStartTime);
			map.put("thisEndTime", currentEndTime);
			map.put("payStatus",status);
			map.put("company_id",company_id);
			return payv2PayOrderService.queryPayOrderTime(map);
		}
		
	}

	public List<String> timeStartCompute(String nowDateString){
		List<String> nowList = new ArrayList<>(26);
		StringBuilder sb = null;
		for(int i=0;i<24;i++){
			sb = new StringBuilder();
			if(i<10){
				sb.append(nowDateString).append(" 0").append(i).append(":00:00");
			}else{
				sb.append(nowDateString).append(" ").append(i).append(":00:00");
			}
			nowList.add(sb.toString());
		}
		return nowList;
	}
	
	public List<String> timeEndCompute(String nowDateString){
		List<String> nowList = new ArrayList<>(26);
		StringBuilder sb = null;
		for(int i=0;i<24;i++){
			sb = new StringBuilder();
			if(i<10){
				sb.append(nowDateString).append(" 0").append(i).append(":59:59");
			}else{
				sb.append(nowDateString).append(" ").append(i).append(":59:59");
			}
			nowList.add(sb.toString());
		}
		return nowList;
	}
 
}
