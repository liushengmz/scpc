package com.pay.business.payv2.service;

import java.util.Map;

import com.pay.business.merchant.entity.Payv2BussCompanyApp;

/**
* @Title: AliPayService.java 
* @Package com.pay.business.payv2.service 
* @Description: 扫码支付Service
* @author ZHOULIBO   
* @date 2017年6月30日 上午10:02:06 
* @version V1.0
*/
public interface AliPayService {
	/**
	 * aliPaycreatePayAndOreder 
	 * 扫码支付：获取扫码支付拉起参数
	 * @param pbca
	 * @param payViewType
	 * @param map
	 * @return
	 * @throws Exception    设定文件 
	 * Map<String,String>    返回类型
	 */
	public Map<String,String> aliPaycreatePayAndOreder(Payv2BussCompanyApp pbca,Integer payViewType, Map<String, Object> map) throws Exception;
	/**
	 * verify 
	 * 支付宝支付-扫码支付回调验签
	 * @param map
	 * @param ALIPAY_RSA_PUBLIC
	 * @return    设定文件 
	 * boolean    返回类型
	 */
	public boolean verify(Map<String, String> map,String ALIPAY_RSA_PUBLIC); 
}
