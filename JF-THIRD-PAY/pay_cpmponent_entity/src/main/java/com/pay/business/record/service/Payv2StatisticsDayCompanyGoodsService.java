package com.pay.business.record.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyGoods;
import com.pay.business.record.mapper.Payv2StatisticsDayCompanyGoodsMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyGoodsService extends BaseService<Payv2StatisticsDayCompanyGoods,Payv2StatisticsDayCompanyGoodsMapper>  {

	/**
	 * 批量插入
	 * @param insertList
	 * @return
	 */
	int insertBatchStatisticsDayCompanyGoods(List<Map<String,Object>> insertList);
	
	/**
	 * 静态统计两个时间段每个商户的每个商品交易数据
	 * @param minYesterDay
	 * @param maxYesterDay
	 */
	void statisticsYesterDayCompanyGoods(String minYesterDay,String maxYesterDay);
	
	List<Map<String,Object>> getCurrentStatisticsDayCompanyGoods(boolean isToday,Long companyId,int companyType,String startTime,String endTime,Integer queryType);
	
	/**
	 * 获取交易记录
	 * @param isToday 是否今天
	 * @param companyId 商户ID
	 * @param appId 应用ID(可以为空)
	 * @param companyType 商户当前状态(查询线上还是线下)
	 * @param startTime 订单支付起始时间
	 * @param endTime 订单支付结束时间
	 * @param queryType 查询类型(1或者2 跟sql有关)
	 * @return
	 */
	List<Map<String,Object>> getCurrentStatisticsDayCompanyGoods(boolean isToday,Long companyId,Long appId,int companyType,String startTime,String endTime,Integer queryType);
}
