package com.pay.business.record.service;

import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.record.entity.Payv2DayCompanyClear;
import com.pay.business.record.mapper.Payv2DayCompanyClearMapper;

/**
 * @author cyl
 * @version
 */
public interface Payv2DayCompanyClearService extends
		BaseService<Payv2DayCompanyClear, Payv2DayCompanyClearMapper> {

	/**
	 * 商户日账单分页列表
	 * 
	 * @param map
	 * @return PageObject<Payv2DayCompanyClear>
	 */
	PageObject<Payv2DayCompanyClear> dayClearList(Map<String, Object> map);

	/**
	 * 当前账单清算
	 * 
	 * @param companyCheckId
	 *            商户账单号
	 * @return int 影响行数
	 */
	void moneyClear(String[] companyCheckId);

	/**
	 * 商户月账单列表
	 * 
	 * @param map
	 * @return PageObject<Payv2DayCompanyClear>
	 */
	Map<Long, Payv2DayCompanyClear> mouthClearList(Map<String, Object> map);

	/**
	 * 商户日账单数值汇总：累计手续费、累计手续费利润、累计已支付金额（元）、累计已退款金额（元）、累计支付净额（元）、累计结算金额（元）
	 * 
	 * @param map
	 * @return
	 */
	Payv2DayCompanyClear dayClearAllMoney(Map<String, Object> map);
	
	
	
	/**
	 * 查询单个对象
	 * 
	 * @param map
	 * @return
	 */
	Payv2DayCompanyClear selectObject(Map<String, Object> map);
	
	/**
	 * 生成日账单
	 * 
	 * @param payv2DayCompanyClear
	 */
	void updateStatus(Payv2DayCompanyClear payv2DayCompanyClear);
	
	void insertCompanyToClear();
	
	
}
