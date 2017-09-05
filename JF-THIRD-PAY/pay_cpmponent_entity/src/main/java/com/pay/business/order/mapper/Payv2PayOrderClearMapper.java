package com.pay.business.order.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.order.entity.OrderClearVO;
import com.pay.business.order.entity.Payv2PayOrderClear;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayOrderClearMapper extends BaseMapper<Payv2PayOrderClear>{
	public void addOrderClearByOrder(Map<String,Object> map);
	public void addOrderClearByRefundOrder(Map<String,Object> map);
	public void updateByEntity2(Payv2PayOrderClear clear);
	
	/**
	 * @return	获取待对账账单时间
	 */
	List<String> getBills();
	
	/**
	 * @param date
	 * @return	根据时间获取对账列表
	 */
	List<Map<String,Object>> getBillList(String date);
	
	Integer getDifferCount(Map<String, Object> map);
	
	/**
	 * @param rateid
	 * @return	获取差错订单
	 */
	List<OrderClearVO> getDifferOrder(Map<String, Object> map);
	
	/**
	 * @param ids
	 * 平帐，更新状态、订单金额、手续费、成本费
	 */
	void updateClear(@Param("ids")String ids);
	
	/**
	 * @param date
	 * @param rateId
	 * 出账(status=5)
	 */
	void updateOutOrder(@Param("date")String date, @Param("rateId")String rateId);

	List<Map<String, Object>> getClearDetail(@Param("date")String date, @Param("rateId")String rateId);
	
	/**
	 * @param ids
	 * @return 需要更新的另外两张表数据
	 */
	List<Map<String, Object>> getOrdeClear(@Param("ids")String ids);
	
	/**
	 * isHaveBill
	 * 根据商户Id与账单时间判断该商户是否已经出账
	 * @param map
	 * @return
	 */
	List<Payv2PayOrderClear> isHaveBill(Map<String, Object> map);
	
	/**
	 * isHaveClear
	 * 根据商户Id与账单时间判断该商户是否有已经出帐的记录
	 * 
	 * @param map
	 * @return
	 */
	List<Payv2PayOrderClear> isHaveClear(Map<String, Object> map);
	
	/**
	 * 根据appId集合，商户id，对账时间搜索订单
	 * @param orderParam
	 * @return
	 */
	List<Payv2PayOrderClear> queryByApp(Map<String, Object> orderParam);
}