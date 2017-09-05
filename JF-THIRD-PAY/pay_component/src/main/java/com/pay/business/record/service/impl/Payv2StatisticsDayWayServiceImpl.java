package com.pay.business.record.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.record.entity.Payv2StatisticsDayWay;
import com.pay.business.record.mapper.Payv2StatisticsDayWayMapper;
import com.pay.business.record.service.Payv2StatisticsDayWayService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2StatisticsDayWayService")
public class Payv2StatisticsDayWayServiceImpl extends BaseServiceImpl<Payv2StatisticsDayWay, Payv2StatisticsDayWayMapper> implements Payv2StatisticsDayWayService {
	// 注入当前dao对象
    @Autowired
    private Payv2StatisticsDayWayMapper payv2StatisticsDayWayMapper;

    @Autowired
	private Payv2PayOrderService payv2PayOrderService;
    
    public Payv2StatisticsDayWayServiceImpl() {
        setMapperClass(Payv2StatisticsDayWayMapper.class, Payv2StatisticsDayWay.class);
    }

	public int insertCustomBatch(List<Map<String, Object>> insertList) {
		return payv2StatisticsDayWayMapper.insertCustomBatch(insertList);
	}

	public void statisticsYesterDayWay(String minYesterDay, String maxYesterDay) {
		int success_stauts = 1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start_time", minYesterDay);
		map.put("end_time", maxYesterDay);
		map.put("payStatus",success_stauts);
		List<Long> companyList = payv2PayOrderService.queryBetweenPayOrder(map);
		if(companyList==null || companyList.size()==0){
			return;
		}
		//获取所有成功订单总数
		List<Map<String,Object>> insertList = new ArrayList<>(companyList.size());
		for(Long company_id : companyList){
			map.put("company_id", company_id);
			List<Map<String, Object>> rList = payv2PayOrderService.getPayOrderSuccessPayWay(map);
			if(rList!=null && rList.size()>0){
				int size = rList.size();
				Date date = new Date();//统计的时间要用昨天的
				Date yester = DateUtil.addDay(date, -1);
				for(int i=0;i<size;i++){
					Map<String, Object> tempMap = rList.get(i);
					if(tempMap.get("allSuccessMoney")==null){
						tempMap.put("allSuccessMoney", 0.00);
					}
					tempMap.put("statistics_time",yester);
					tempMap.put("create_time",date);
					insertList.add(tempMap);
				}
			}
		}
		//然后插入,查询时候怎么展示 查询几天内所有的支付订单然后把数据加一起
		try {
			if(insertList != null && insertList.size()>0){
				int insertCount = insertCustomBatch(insertList);
				System.out.println("wayService 统计了:" + insertCount);
			}
		} catch (Exception e) {
			logger.error("wayService批量插入异常:"+insertList.toString(),e);
		}
	}
 
}
