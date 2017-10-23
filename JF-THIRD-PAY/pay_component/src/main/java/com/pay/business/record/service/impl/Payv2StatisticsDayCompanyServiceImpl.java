package com.pay.business.record.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.record.entity.Payv2StatisticsDayCompany;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyMapper;
import com.pay.business.record.service.Payv2StatisticsDayCompanyService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2StatisticsDayCompanyService")
public class Payv2StatisticsDayCompanyServiceImpl extends BaseServiceImpl<Payv2StatisticsDayCompany, Payv2StatisticsDayCompanyMapper> implements Payv2StatisticsDayCompanyService {
	// 注入当前dao对象
    @Autowired
    private Payv2StatisticsDayCompanyMapper payv2StatisticsDayCompanyMapper;

    @Autowired
    private Payv2PayOrderService payv2PayOrderService;
    
    public Payv2StatisticsDayCompanyServiceImpl() {
        setMapperClass(Payv2StatisticsDayCompanyMapper.class, Payv2StatisticsDayCompany.class);
    }
    
    public void statisticsYesterDayCompany(String minYesterDay,String maxYesterDay){
    	Map<String,Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("startTime", minYesterDay);
    	paramMap.put("endTime", maxYesterDay);
    	paramMap.put("successStatus", 1);
    	paramMap.put("failStatus", 2);
    	paramMap.put("processingStatus", 5);
    	List<Map<String,Object>> insertList = payv2PayOrderService.queryOrderInfoToStatisticsDayCompany(paramMap);
    	if(insertList!=null && insertList.size()>0){
    		int count = insertBatchToStatisticsDayCompany(insertList);
    		System.out.println("companyService 插入了:"+count);
    	}
    }
    
    public int insertBatchToStatisticsDayCompany(List<Map<String,Object>> insertList){
    	return payv2StatisticsDayCompanyMapper.insertBatchToStatisticsDayCompany(insertList);
    }

	public Map<String, Object> getCurrentStatisticsDayCompany(boolean isToday,
			Long companyId, int companyType, String startTime, String endTime) {
		return getCurrentStatisticsDayCompany(isToday, companyId, companyType, null, startTime, endTime);
	}

	public Map<String, Object> getCurrentStatisticsDayCompany(boolean isToday,
			Long companyId, int companyType, Long appId, String startTime,String endTime) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		if(appId!=null){
			paramMap.put("appId", appId);
		}
    	paramMap.put("companyId", companyId);
    	paramMap.put("companyType", companyType);
    	paramMap.put("successStatus", 1);
    	paramMap.put("failStatus", 2);
    	paramMap.put("processingStatus", 5);
    	paramMap.put("startTime", startTime);
    	paramMap.put("endTime", endTime);
    	Map<String,Object> resultMap = null;
    	if(isToday){//是今天 直接查询订单表
    		resultMap = payv2PayOrderService.getCurrentStatisticsDayCompany(paramMap);
    	}else{
    		resultMap = payv2StatisticsDayCompanyMapper.getCurrentStatisticsDayCompany(paramMap);
    	}
    	return resultMap;
	}
    
}
