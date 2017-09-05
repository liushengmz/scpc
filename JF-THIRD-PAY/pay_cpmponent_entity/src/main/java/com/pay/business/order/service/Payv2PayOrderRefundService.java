package com.pay.business.order.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.entity.Payv2PayOrderRefundVO;
import com.pay.business.order.mapper.Payv2PayOrderRefundMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayOrderRefundService extends BaseService<Payv2PayOrderRefund,Payv2PayOrderRefundMapper>  {

	public Payv2PayOrderRefund selectSingle(Payv2PayOrderRefund t);
	
	public List<Payv2PayOrderRefund> selectByObject(Payv2PayOrderRefund t);
	
	/**
	 * 支付退款操作
	 * @param map
	 * @return
	 */
	public Map<String,Object> payRefund(Map<String,Object> map,Long appId,String appSecret) throws Exception;
	
	/**
	 * 查询退款订单
	 * @param map
	 * @return
	 */
	public Map<String,Object> queryRefund(Map<String,Object> map,Payv2BussCompanyApp pbca);
	
	/**
	 * 支付退款操作(点游)
	 * @param map
	 * @return
	 */
	public Map<String,Object> dyPayRefund(Map<String,Object> map,Payv2BussCompanyApp pbca) throws Exception;
	
	/**
	 * 查询退款订单（点游）
	 * @param map
	 * @return
	 */
	public Map<String,Object> dyQueryRefund(Map<String,Object> map,Payv2BussCompanyApp pbca);
	/**
	 * 
	 * selectByRefundTime 
	 * 获取本地时间段的所有退款单 
	 * @param map
	 * @return
	 * @throws Exception    设定文件 
	 * List<Payv2PayOrderRefund>    返回类型
	 */
	public List<Payv2PayOrderRefund> selectByRefundTime(Map<String,Object> map)throws Exception;
	
	/**
	 * 根据支付集订单号，退款订单号，退款时间，渠道商，商户，应用，支付渠道，支付类型，支付方式查询退款订单列表
	 * 
	 * @param map
	 * @return List<Payv2PayOrderRefund>
	 */
	PageObject<Payv2PayOrderRefundVO> queryRefunds(Map<String,Object> map);
	
	/**
	 * 根据退款订单id查询订单详情
	 * 
	 * @param map
	 * @return Payv2PayOrderRefundVO
	 */
	Payv2PayOrderRefundVO refundDetail(Map<String,Object> map);
}
