package com.pay.business.order.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.entity.Payv2PayOrderRefundVO;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayOrderRefundMapper extends BaseMapper<Payv2PayOrderRefund>{
	/**
	 * 查询某个订单退款总金额
	 * @param map
	 * @return
	 */
	public Double refundSum(Map<String,Object> map);
	
	/**
	 * 获取在某一段时间的退款单
	 * @param map
	 * @return
	 */
	public List<Payv2PayOrderRefund> selectByRefundTime(Map<String, Object> map);
	/**
	 * selectRefundByHour 
	 * 获取按每小时统计退款金额与订单数 
	 * @param map
	 * @return    设定文件 
	 * List<Payv2PayOrderRefund>    返回类型
	 */
	public List<Payv2PayOrderRefund> selectRefundByHour(Map<String,Object> map);
	
	/**
	 * 根据退款订单查询条件获取退款订单总数
	 * 
	 * @param map
	 * @return
	 */
	int getRefundsCount(Map<String, Object> map);
	/**
	 * 根据支付集订单号，退款订单号，退款时间，渠道商，商户，应用，支付渠道，支付类型，支付方式查询退款订单列表
	 * 
	 * @param map
	 * @return List<Payv2PayOrderRefund>
	 */
	List<Payv2PayOrderRefundVO> queryRefunds(Map<String,Object> map);
	
	/**
	 * 根据退款订单id查询订单详情
	 * 
	 * @param map
	 * @return Payv2PayOrderRefundVO
	 */
	Payv2PayOrderRefundVO refundDetail(Map<String,Object> map);
	
	
}