package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2StatisticsDayWay;
import com.pay.business.record.entity.Payv2StatisticsDayWayBean;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayWayMapper extends BaseMapper<Payv2StatisticsDayWay>{
	/**
	* @Title: getDayStatisticsInfo 
	* @Description:获取查询今日支付比例柱状图
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayTimeBean>    返回类型 
	* @date 2017年2月11日 上午10:28:10 
	* @throws
	*/
	public List<Payv2StatisticsDayWayBean> getDayStatisticsWayInfo(Map<String,Object> map);
	/**
	* @Title: getYesterdayStatisticsWayInfo 
	* @Description:获取查询昨日支付比例柱状图
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayWayBean>    返回类型 
	* @date 2017年2月14日 上午9:26:55 
	* @throws
	*/
	public List<Payv2StatisticsDayWayBean> getYesterdayStatisticsWayInfo(Map<String,Object> map);
	/**
	* @Title: getlatelyDayWayInfo 
	* @Description:按支付渠道来计算近7 15 30日的数据 
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayWayBean>    返回类型 
	* @date 2017年2月14日 上午11:50:02 
	* @throws
	*/
	public List<Payv2StatisticsDayWayBean> getlatelyDayWayInfo(Map<String,Object> map);
	
	/**
	 * 自定义批量插入
	 * @param insertList
	 * @return
	 */
	int insertCustomBatch(List<Map<String, Object>> insertList);
	/**
	* @Title: getTimeWayInfo 
	* @Description:根据时间段/接入商 获取相关柱状图数据
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayWayBean>    返回类型 
	* @date 2017年2月15日 上午11:45:30 
	* @throws
	*/
	public List<Payv2StatisticsDayWayBean> getTimeWayInfo(Map<String,Object> map);
}