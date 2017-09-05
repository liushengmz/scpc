package com.pay.business.record.service;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.PayDataRecord;
import com.pay.business.record.mapper.PayDataRecordMapper;

/**
* @Title: PayDataRecordService.java 
* @Package com.pay.business.payv2.service 
* @Description: 交易数据统计表 
* @author ZHOULIBO   
* @date 2017年8月1日 上午11:02:01 
* @version V1.0
*/
public interface PayDataRecordService extends BaseService<PayDataRecord,PayDataRecordMapper>  {
	/**
	 * setStatisticsOrderByHour 
	 *  按每小时去统计订单各个维度的数据
	 * @param map
	 * @throws Exception    设定文件 
	 * void    返回类型
	 */
	public void setStatisticsOrderByHour(Map<String,Object> map) throws Exception;
	
	/**
	 * 关键数据获取
	 * 
	 * @param map
	 * @return
	 */
	PayDataRecord curxData(Map<String, Object> map);
	/**
	 * 实时关键数据获取
	 * 
	 * @param map
	 * @return
	 */
	PayDataRecord curxDataNow(Map<String, Object> map);
	
	
	/**
	 * 以小时为单位查询趋势图数据
	 * 
	 * @param map
	 * @return
	 * @throws ParseException
	 */
	List<PayDataRecord> groupByHour(Map<String, Object> map)throws ParseException;
	
	/**
	 * 以天为单位查询趋势图数据
	 * 
	 * @param map
	 * @return
	 * @throws ParseException 
	 */
	List<PayDataRecord> groupByDay(Map<String, Object> map) throws ParseException;
}
