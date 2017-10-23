package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.PayDataRecord;

/**
 * @author cyl
 * @version 
 */
public interface PayDataRecordMapper extends BaseMapper<PayDataRecord>{

	/**
	 * 关键数据获取
	 * 
	 * @param map
	 * @return PayDataRecord
	 */
	PayDataRecord curxData(Map<String, Object> map);
	
	/**
	 * 以小时为单位查询趋势图数据
	 * 
	 * @param map
	 * @return
	 */
	List<PayDataRecord> groupByHour(Map<String, Object> map);
	
	/**
	 * 以天为单位查询趋势图数据
	 * 
	 * @param map
	 * @return
	 */
	List<PayDataRecord> groupByDay(Map<String, Object> map);
}