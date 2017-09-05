package com.pay.business.record.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyTime;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyTimeMapper;
import com.pay.business.record.service.Payv2StatisticsDayCompanyTimeService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2StatisticsDayCompanyTimeService")
public class Payv2StatisticsDayCompanyTimeServiceImpl extends BaseServiceImpl<Payv2StatisticsDayCompanyTime, Payv2StatisticsDayCompanyTimeMapper> implements Payv2StatisticsDayCompanyTimeService {
	// 注入当前dao对象
    @Autowired
    private Payv2StatisticsDayCompanyTimeMapper payv2StatisticsDayCompanyTimeMapper;

    @Autowired
    private Payv2PayOrderService payv2PayOrderService;
    
    @Autowired
    private Payv2PayWayService payv2PayWayService;
    
    public Payv2StatisticsDayCompanyTimeServiceImpl() {
        setMapperClass(Payv2StatisticsDayCompanyTimeMapper.class, Payv2StatisticsDayCompanyTime.class);
    }

	public int insertBatchToStatisticsDayCompanyTime(List<Map<String, Object>> insertList) {
		return payv2StatisticsDayCompanyTimeMapper.insertBatchToStatisticsDayCompanyTime(insertList);
	}

	public void statisticsYesterDayCompanyTime(String minYesterDay,
			String maxYesterDay, String nowDateString) {
		//先查询公司 应该按照 应用来分组
		int successStatus = 1;
		ExecutorService pool = Executors.newFixedThreadPool(24);
		List<String> timeStartCompute = timeStartCompute(nowDateString);
		List<String> timeEndCompute = timeEndCompute(nowDateString);
		List<Map<String,Object>> insertList = new ArrayList<Map<String, Object>>();
		try {
			//查询24个小时段 每个小时段 的各分级的交易情况
			for(int i=0;i<24;i++){
				synchronized(this){
					Future<List<Map<String, Object>>> ft = pool.submit(new CompanyTimeSliceRunnable(timeStartCompute.get(i), timeEndCompute.get(i),successStatus));
					List<Map<String, Object>> list = ft.get();
					if(list!=null && list.size()>0){
						//查到多个list 因为不同的应用商户等等
						int size = list.size();
						for(int j=0;j<size;j++){
							Map<String, Object> resultMap = list.get(j);
							resultMap.put("timeType", i);
							insertList.add(resultMap);
							/*//如果size不等于时候 要把每个记录之前没有的记录加上
							Map<String,Object> tempMap = new HashMap<>();
							Map<String, Object> resultMap = list.get(j);
							tempMap.putAll(resultMap);
							tempMap.put("allSuccessCount", 0);
							tempMap.put("allSuccessMoney", 0.0);
							resultMap.put("timeType", i);
							//首先把companyId,appId,payWayId,licenseType值和value加在一起当作key
							String keys = mapKeysHandler(resultMap);
							//如果key不存在 那么添加key 且添加之前没有的时间段数据
							if(mapKeys.containsKey(keys)){
								//存在 , 比较值 和 i 的大小
								Integer keyValue = NumberUtils.createInteger(mapKeys.get(keys).toString());
								int nowKeyValue = keyValue+1;//加一是因为不用添加这次的
								if(i>nowKeyValue){
									//添加假数据
									for(int k=nowKeyValue;nowKeyValue<i;k++){
										tempMap.put("timeType", k);
										insertList.add(resultMap);
									}
								}else if(i==nowKeyValue){
									tempMap.put("timeType", nowKeyValue);
									insertList.add(tempMap);
								}
								if(i>keyValue){
									//添加假数据
									for(int k=keyValue;keyValue<i;k++){
										tempMap.put("timeType", k);
										insertList.add(tempMap);
									}
								}
								mapKeys.put(keys, i);
							}else{
								//不存在 添加 当前的value就是i
								mapKeys.put(keys, i);
								//insertList.add(resultMap);
								//还要再把之前的欠下来的给补上 , 肯定先不存在的 
								for(int k=0;k<i;k++){
									//添加假数据
									tempMap.put("timeType", k);
									insertList.add(tempMap);
								}
							}*/
						}
					}
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}finally{
			pool.shutdown();
		}
		if(insertList.size()>0){
			int count = insertBatchToStatisticsDayCompanyTime(insertList);
			System.out.println("timeService 插入了:"+count);
		}
	}
    public String mapKeysHandler(Map<String,Object> map){
    	String split = "-";
    	StringBuilder sb = new StringBuilder();
    	sb.append(map.get("companyId").toString()).append(split);
    	sb.append(map.get("appId").toString()).append(split);
    	sb.append(map.get("payWayId").toString()).append(split);
    	sb.append(map.get("licenseType").toString()).append(split);
    	return sb.toString();
    }
	public List<Payv2StatisticsDayCompanyTime> getCurrentStatisticsDayCompanyTime(
			boolean isToday, Long companyId, int companyType, String startTime,String endTime) {
		return getCurrentStatisticsDayCompanyTime(isToday, companyId, companyType, null, startTime, endTime);
	}

	public List<Payv2StatisticsDayCompanyTime> getCurrentStatisticsDayCompanyTime(
			boolean isToday, Long companyId, int companyType, Long appId,String startTime, String endTime) {
		return getCurrentStatisticsDayCompanyTime(isToday, companyId, companyType, appId, null, startTime, endTime);
	}

	public List<Payv2StatisticsDayCompanyTime> getCurrentStatisticsDayCompanyTime(
			boolean isToday, Long companyId, int companyType, Long appId,Long payWayId, String startTime, String endTime) {
		int successStatus = 1;
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("companyType", companyType);
		paramMap.put("appId", appId);
		paramMap.put("payWayId", payWayId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		String nowDateString = DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD);
		List<Payv2StatisticsDayCompanyTime> allList = null;
		if(isToday){
			String payWayName = "";
			if(payWayId != null){
				Map<String,Object> tempMap = new HashMap<>();
				tempMap.put("id", payWayId);
				Payv2PayWay detail = payv2PayWayService.detail(tempMap);
				payWayName = detail.getWayName();
			}
			List<String> timeStartCompute = timeStartCompute(nowDateString);
			List<String> timeEndCompute = timeEndCompute(nowDateString);
			ExecutorService pool = Executors.newFixedThreadPool(24);
			allList = new ArrayList<>();
			try {
				Payv2StatisticsDayCompanyTime companyTime = null;
				for(int i=0;i<24;i++){
					Future<List<Map<String, Object>>> ft = pool.submit(new CompanyTimeSliceRunnable(timeStartCompute.get(i), timeEndCompute.get(i),successStatus, companyId, companyType, appId, payWayId,1));//默认是不分组查询的
					List<Map<String, Object>> list = ft.get();
					if(list!=null && list.size()>0 && list.get(0)!=null){
						Map<String, Object> liMap = list.get(0);//只查询一个时间段的所有数据 所以只能有一条数据
						companyTime = new Payv2StatisticsDayCompanyTime();
						companyTime.setAppId(NumberUtils.createLong(liMap.get("appId").toString()));
						companyTime.setCompanyId(NumberUtils.createLong(liMap.get("companyId").toString()));
						companyTime.setPayWayId(NumberUtils.createLong(liMap.get("payWayId").toString()));
						companyTime.setSuccessOrderCount(NumberUtils.createInteger(liMap.get("allSuccessCount").toString()));
						companyTime.setSuccessOrderMoney(NumberUtils.createDouble(liMap.get("allSuccessMoney").toString()));
						companyTime.setTimeType(i);
						companyTime.setType(NumberUtils.createInteger(liMap.get("licenseType").toString()));
						companyTime.setPayWayName(liMap.get("payWayName").toString());
						allList.add(companyTime);
					}else{
						//如果没有 那么造假数据
						companyTime = new Payv2StatisticsDayCompanyTime();
						companyTime.setAppId(appId);
						companyTime.setCompanyId(companyId);
						companyTime.setPayWayId(payWayId);
						companyTime.setSuccessOrderCount(0);
						companyTime.setSuccessOrderMoney(0.0);
						companyTime.setTimeType(i);
						companyTime.setType(companyType);
						companyTime.setPayWayName(payWayName);
						allList.add(companyTime);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				pool.shutdown();
			}
		}else{
			allList = getStatisticsDayCompanyTime(paramMap);
		}
		return allList;
	}
	public List<Payv2StatisticsDayCompanyTime> getStatisticsDayCompanyTime(Map<String,Object> paramMap){
		return payv2StatisticsDayCompanyTimeMapper.getCurrentStatisticsDayCompanyTime(paramMap);
	}
	class CompanyTimeSliceRunnable implements Callable<List<Map<String,Object>>>{

		private String currentStartTime;
		private String currentEndTime;
		private int status;
		
		private Long companyId;
		private Integer companyType;
		private Long appId;
		private Long payWayId;
		private int isGroup;
		
		public CompanyTimeSliceRunnable(String currentStartTime,
				String currentEndTime, int status, Long companyId,
				Integer companyType, Long appId, Long payWayId,int isGroup) {
			this.currentStartTime = currentStartTime;
			this.currentEndTime = currentEndTime;
			this.status = status;
			
			this.companyId = companyId;
			this.companyType = companyType;
			this.appId = appId;
			this.payWayId = payWayId;
			this.isGroup = isGroup;
		}

		public CompanyTimeSliceRunnable(String currentStartTime,
				String currentEndTime, int status) {
			this.currentStartTime = currentStartTime;
			this.currentEndTime = currentEndTime;
			this.status = status;
			
			this.companyId = null;
			this.companyType = null;
			this.appId = null;
			this.payWayId = null;
			this.isGroup = 0;
		}

		@Override
		public List<Map<String, Object>> call() throws Exception {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("startTime", currentStartTime);
			paramMap.put("endTime", currentEndTime);
			paramMap.put("successStatus", status);
			
			paramMap.put("companyId", companyId);
			paramMap.put("companyType", companyType);
			paramMap.put("appId", appId);
			paramMap.put("payWayId", payWayId);
			paramMap.put("isGroup", isGroup);
			return payv2PayOrderService.queryOrderInfoToDayCompanyTime(paramMap);
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

	public List<Payv2StatisticsDayCompanyTime> getYesterDayCompanyTime(
			Long companyId, Integer licenseType, Long app_id, Long pay_way_id,
			String startTime, String endTime) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("companyType", licenseType);
		paramMap.put("appId", app_id);
		paramMap.put("payWayId", pay_way_id);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		return payv2StatisticsDayCompanyTimeMapper.getYesterDayCompanyTime(paramMap);
	}

}
