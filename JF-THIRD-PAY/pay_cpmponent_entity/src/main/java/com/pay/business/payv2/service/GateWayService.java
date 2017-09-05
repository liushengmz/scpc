package com.pay.business.payv2.service;

import java.util.Map;

import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.order.entity.Payv2PayOrder;
/**
* @Title: GateWayService.java 
* @Package com.pay.business.payv2.service 
* @Description: 微信公众号支付API
* @author 周立波   
* @date 2017年6月27日 上午10:08:08 
* @version V1.0
*/
public interface GateWayService {
	/**
	* wechatGzhPay 
	* 微信公众号支付参数组装
	* @param map
	* @param pbca
	* @param payViewType
	* @return
	* @throws Exception    设定文件 
	* Map<String,String>    返回类型
	 */
	public Map<String,String> wechatGzhPay(Map<String, Object> map, Payv2BussCompanyApp pbca, Integer payViewType) throws Exception;
	/**
	 * hfbWxGzhSign 
	 * 汇付宝：回调验签  
	 * @param map
	 * @param payv2PayOrder
	 * @return
	 * @throws Exception    设定文件 
	 * boolean    返回类型
	 */
	public boolean hfbWxGzhSign(Map<String, String> map,Payv2PayOrder payv2PayOrder) throws Exception;
	/**
	 * wechatGzhPayByCrack 
	 * Map<String, Object> map
	 * @param dictName
	 * @param orderNum
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public Map<String,String> wechatGzhPayByCrack(Map<String,String> map) throws Exception;
}
