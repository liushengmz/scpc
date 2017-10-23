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
import com.pay.business.record.entity.Payv2StatisticsDayChannel;
import com.pay.business.record.mapper.Payv2StatisticsDayChannelMapper;
import com.pay.business.record.service.Payv2StatisticsDayChannelService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2StatisticsDayChannelService")
public class Payv2StatisticsDayChannelServiceImpl extends BaseServiceImpl<Payv2StatisticsDayChannel, Payv2StatisticsDayChannelMapper> implements Payv2StatisticsDayChannelService {
	// 注入当前dao对象
    @Autowired
    private Payv2StatisticsDayChannelMapper payv2StatisticsDayChannelMapper;

    @Autowired
	private Payv2PayOrderService payv2PayOrderService;
    
    public Payv2StatisticsDayChannelServiceImpl() {
        setMapperClass(Payv2StatisticsDayChannelMapper.class, Payv2StatisticsDayChannel.class);
    }

	public int insertCustomBatch(List<Map<String, Object>> insertList) {
		return payv2StatisticsDayChannelMapper.insertCustomBatch(insertList);
	}

	public void statisticsYesterDayChannel(String minYesterDay,
			String maxYesterDay) {
		Map<String, Object> map = new HashMap<String, Object>();
		//查询昨日的支付时间
		map.put("start_time", minYesterDay);
		map.put("end_time", maxYesterDay);
		try {
			List<Long> companyList = payv2PayOrderService.queryBetweenPayOrder(map);
			if(companyList==null || companyList.size()==0){
				return;
			}
			int success_status = 1;
			int fail_status = 2;
			map.put("success_status", success_status);
			map.put("fail_status", fail_status);
			List<Map<String,Object>> insertList = new ArrayList<>();
			Date date = new Date();
			Date yester = DateUtil.addDay(date, -1);//统计的时间要用昨天的
			for(Long companyId : companyList){
				map.put("company_id", companyId);
				Map<String,Object> resultMap = payv2PayOrderService.queryBetweenPayOrderByCompanyId(map);
				resultMap.put("statistics_time",yester);
				resultMap.put("create_time", date);
				insertList.add(resultMap);
			}
			//批量插入
			if(insertList != null && insertList.size()>0){
				int insertCount = insertCustomBatch(insertList);
				System.out.println("day_channel 统计了:" + insertCount);
			}
		} catch (Exception e) {
			logger.error("day_channel统计异常:",e);
		}
	}
    
 
}
