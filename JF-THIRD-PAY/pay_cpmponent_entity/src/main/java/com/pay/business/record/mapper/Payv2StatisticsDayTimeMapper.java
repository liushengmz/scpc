package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2StatisticsDayTime;
import com.pay.business.record.entity.Payv2StatisticsDayTimeBean;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayTimeMapper extends BaseMapper<Payv2StatisticsDayTime>{
	/**
	* @Title: getDayStatisticsInfo 
	* @Description:获取今日数据曲线图
	* @param map
	* @return    设定文件 
	* @return List<Payv2StatisticsDayTimeBean>    返回类型 
	* @date 2017年2月11日 上午10:28:10 
	* @throws
	*/
	public List<Payv2StatisticsDayTimeBean> getDayStatisticsInfo(Map<String,Object> map);
	
	/**
	 * 自定义批量插入
	 * @param allList
	 * @return
	 */
	int insertCustomBatch(List<Map<String, Object>> allList);

}