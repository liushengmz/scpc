package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2StatisticsDayChannel;

/**
* @ClassName: Payv2StatisticsDayChannelMapper 
* @Description:渠道商每日数据统计业务mapper
* @author zhoulibo
* @date 2017年2月10日 下午2:34:21
*/
public interface Payv2StatisticsDayChannelMapper extends BaseMapper<Payv2StatisticsDayChannel>{
	/**
	* @Title: getYesterdayStatistics 
	* @Description:获取昨日数据
	* @param map
	* @return    设定文件 
	* @return Payv2StatisticsDayChannel    返回类型 
	* @date 2017年2月10日 下午2:34:12 
	* @throws
	*/
	public Payv2StatisticsDayChannel getYesterdayStatistics(Map<String,Object> map);
	/**
	* @Title: getBeforeYesterdayStatistics 
	* @Description:获取前天的数据
	* @param map
	* @return    设定文件 
	* @return Payv2StatisticsDayChannel    返回类型 
	* @date 2017年2月13日 下午3:01:03 
	* @throws
	*/
	public Payv2StatisticsDayChannel getBeforeYesterdayStatistics(Map<String,Object> map);
	/**
	* @Title: getYesterdayInfoList 
	* @Description:获取昨日详情数据
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayChannel>    返回类型 
	* @date 2017年2月14日 上午9:59:19 
	* @throws
	*/
	public List<Payv2StatisticsDayChannel> getYesterdayInfoList(Map<String,Object> map);
	/**
	* @Title: getLatelyDayStatistics 
	* @Description:获取最近 7  15 30 天的各项指标总数
	* @param map
	* @return    设定文件 
	* @return Payv2StatisticsDayChannel    返回类型 
	* @date 2017年2月14日 上午10:39:48 
	* @throws
	*/
	public Payv2StatisticsDayChannel getLatelyDayStatistics(Map<String,Object> map);
	/**
	* @Title: getLatelyDayInfoList 
	* @Description:获取最近 7  15 30 天 时间段数据详情
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayChannel>    返回类型 
	* @date 2017年2月14日 上午11:12:19 
	* @throws
	*/
	public List<Payv2StatisticsDayChannel> getLatelyDayInfoList(Map<String,Object> map);
	/**
	* @Title: getLatelyDayInfoByCompanyList 
	* @Description:以接入商为维度：获取获取最近 7  15 30 天关键数据详情
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayChannel>    返回类型 
	* @date 2017年2月14日 下午2:32:56 
	* @throws
	*/
	public List<Payv2StatisticsDayChannel> getLatelyDayInfoByCompanyList(Map<String,Object> map);
	
	/**
	 * 自定义批量插入
	 * @param insertList
	 * @return
	 */
	int insertCustomBatch(List<Map<String, Object>> insertList);
	/**
	* @Title: getTimeStatistics 
	* @Description:根据时间段选择或者接入商来计算各项指标总数
	* @param map
	* @return    设定文件 
	* @return Payv2StatisticsDayChannel    返回类型 
	* @date 2017年2月15日 上午9:49:44 
	* @throws
	*/
	public Payv2StatisticsDayChannel getTimeStatistics(Map<String,Object> map);
	/**
	* @Title: getTimeInfoList 
	* @Description:根据时间段选择或者接入商来计算各项指标曲线图数据（按每日分组）
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayChannel>    返回类型 
	* @date 2017年2月15日 上午9:55:58 
	* @throws
	*/
	public List<Payv2StatisticsDayChannel> getTimeInfoList(Map<String,Object> map);
	/**
	* @Title: getTimeInfoByCompanyList 
	* @Description:根据时间段选择或者接入商来计算关键数据详情
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayChannel>    返回类型 
	* @date 2017年2月15日 上午11:51:10 
	* @throws
	 */
	public List<Payv2StatisticsDayChannel> getTimeInfoByCompanyList(Map<String,Object> map);
}