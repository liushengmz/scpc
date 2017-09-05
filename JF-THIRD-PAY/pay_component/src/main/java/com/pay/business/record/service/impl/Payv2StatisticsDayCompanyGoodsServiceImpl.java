package com.pay.business.record.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyGoods;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyGoodsMapper;
import com.pay.business.record.service.Payv2StatisticsDayCompanyGoodsService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2StatisticsDayCompanyGoodsService")
public class Payv2StatisticsDayCompanyGoodsServiceImpl extends BaseServiceImpl<Payv2StatisticsDayCompanyGoods, Payv2StatisticsDayCompanyGoodsMapper> implements Payv2StatisticsDayCompanyGoodsService {
	// 注入当前dao对象
    @Autowired
    private Payv2StatisticsDayCompanyGoodsMapper payv2StatisticsDayCompanyGoodsMapper;
    
    @Autowired
    private Payv2PayOrderService payv2PayOrderService;

    public Payv2StatisticsDayCompanyGoodsServiceImpl() {
        setMapperClass(Payv2StatisticsDayCompanyGoodsMapper.class, Payv2StatisticsDayCompanyGoods.class);
    }

	public int insertBatchStatisticsDayCompanyGoods(
			List<Map<String, Object>> insertList) {
		return payv2StatisticsDayCompanyGoodsMapper.insertBatchStatisticsDayCompanyGoods(insertList);
	}

	public void statisticsYesterDayCompanyGoods(String minYesterDay,
			String maxYesterDay) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", minYesterDay);
    	paramMap.put("endTime", maxYesterDay);
    	paramMap.put("successStatus", 1);
		List<Map<String,Object>> insertList = payv2PayOrderService.queryOrderInfoToStatisticsDayCompanyGoods(paramMap);
		if(insertList!=null && insertList.size()>0){
			int count = insertBatchStatisticsDayCompanyGoods(insertList);
			System.out.println("goodsService 插入了:"+count);
		}
	}

	public List<Map<String, Object>> getCurrentStatisticsDayCompanyGoods(
			boolean isToday, Long companyId, int companyType, String startTime,
			String endTime,Integer queryType) {
		return getCurrentStatisticsDayCompanyGoods(isToday, companyId, null, companyType, startTime, endTime,queryType);
	}

	public List<Map<String, Object>> getCurrentStatisticsDayCompanyGoods(
			boolean isToday, Long companyId, Long appId, int companyType,
			String startTime, String endTime,Integer queryType) {
		//如果 appId为空 那么查询的是当前商户所有的
		Map<String,Object> paramMap = new HashMap<>();
		if(appId!=null){
			paramMap.put("appId", appId);
		}
		paramMap.put("companyId", companyId);
		paramMap.put("companyType", companyType);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("topNum", 5);//查几个
		paramMap.put("queryType", queryType);
		List<Map<String,Object>> resultList = null;
		if(isToday){
			resultList = payv2PayOrderService.getCurrentStatisticsDayCompanyGoods(paramMap);
		}else{
			resultList = payv2StatisticsDayCompanyGoodsMapper.getCurrentStatisticsDayCompanyGoods(paramMap);
		}
		return resultList;
	}

}
