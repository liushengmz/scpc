package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2StatisticsDayCompany;

/**
 * @author cyl
 * @version 
 */
public interface Payv2StatisticsDayCompanyMapper extends BaseMapper<Payv2StatisticsDayCompany>{

	/**
	 * 批量插入
	 * @param insertList
	 * @return
	 */
	int insertBatchToStatisticsDayCompany(List<Map<String, Object>> insertList);

	/**
	 * 查询关键数据指标(非今天)
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> getCurrentStatisticsDayCompany(
			Map<String, Object> paramMap);

}