package com.pay.business.merchant.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.mapper.Payv2BussSupportPayWayMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussSupportPayWayService extends BaseService<Payv2BussSupportPayWay,Payv2BussSupportPayWayMapper>  {

	public Payv2BussSupportPayWay selectSingle(Payv2BussSupportPayWay t);
	
	public List<Payv2BussSupportPayWay> selectByObject(Payv2BussSupportPayWay t);
	
	public List<Payv2BussSupportPayWay> selectByObjectForCompany(Payv2BussSupportPayWay t);

	/**
	 * 获取该商户payv2_pay_way表的ID和name
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> queryPayWayIdAndNameByCompanyId(Long companyId);
	
	/**
	 * 根据商户ID获取分组后的支付渠道
	 * @param t
	 * @return
	 */
	public List<Payv2BussSupportPayWay> selectPayWayIdByGroup(Payv2BussSupportPayWay t);
	
	/**
	 * 查询对象（缓存）
	 * @param t
	 * @return
	 */
	public Payv2BussSupportPayWay selectSingle1(Payv2BussSupportPayWay t);
	
	/**
	 * 根据支付类型和商户查询商户支持支付通道
	 * @param companyId
	 * @param payViewType
	 * @param payType
	 * @return
	 */
	public List<Payv2BussSupportPayWay> queryByCompany(Long companyId,Integer payViewType,Integer payType,Double payMoney);
	
	/**
	 * 根据支付方式和商户查询商户支持支付通道
	 * @param companyId
	 * @param payViewType
	 * @param payType
	 * @return
	 */
	public List<Payv2BussSupportPayWay> queryByCompany(Long companyId,Long payWayId,Integer payType,Double payMoney);
	
	/**
	 * 获取支付方式、通道
	 * @param payWayList
	 * @param payMoney
	 * @param list
	 * @return
	 */
	public Payv2BussSupportPayWay getWayRate(List<Payv2BussSupportPayWay> payWayList,Double payMoney
			,Long companyId,Integer payViewType,Integer isRandom,Integer payType);
	
}
