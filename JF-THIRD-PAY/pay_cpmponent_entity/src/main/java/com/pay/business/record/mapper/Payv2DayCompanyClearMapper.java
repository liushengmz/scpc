package com.pay.business.record.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2DayCompanyClear;

/**
 * @author cyl
 * @version
 */
public interface Payv2DayCompanyClearMapper extends
		BaseMapper<Payv2DayCompanyClear> {

	/**
	 * 获取商户日账单总数
	 * 
	 * @param map
	 * @return
	 */
	int getDayClearListCount(Map<String, Object> map);

	/**
	 * 获取日账单分页列表
	 * 
	 * @param map
	 * @return
	 */
	List<Payv2DayCompanyClear> getDayClearList(HashMap<String, Object> map);

	/**
	 * 结算当前账单
	 * 
	 * @param companyCheckId
	 * @return
	 */
	void moneyClear(String[] companyCheckId);

	/**
	 * 商户日账单数值汇总：累计手续费、累计手续费利润、累计已支付金额（元）、累计已退款金额（元）、累计支付净额（元）、累计结算金额（元）
	 * 
	 * @param map
	 * @return
	 */
	Payv2DayCompanyClear dayClearAllMoney(Map<String, Object> map);
	/**
	 * 商户月账单列表
	 * 
	 * @param map
	 * @return
	 */
	List<Payv2DayCompanyClear> getMouthsClearList(Map<String, Object> map);
	
	/**
	 * 查询单个对象
	 * 
	 * @param map
	 * @return
	 */
	Payv2DayCompanyClear selectObject(Map<String, Object> map);

	@Select("select channel_id,id from payv2_buss_company where company_status='2' and is_delete='2'")
	List<Map<String, Object>> getCompanys();
	
	@Insert("insert payv2_day_company_clear(company_check_id,channel_id,company_id,clear_time,create_time,status) values(#{companycheckId},#{channelId},#{companyId},date_sub(curdate(),interval 1 day),now(),'3')")
	void insertClear(@Param("companycheckId")String companycheckId, @Param("companyId")String companyId, @Param("channelId")String channelId);
}