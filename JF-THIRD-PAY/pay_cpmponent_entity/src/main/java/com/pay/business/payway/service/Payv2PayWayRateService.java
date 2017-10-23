package com.pay.business.payway.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.entity.Payv2PayWayRateVO;
import com.pay.business.payway.mapper.Payv2PayWayRateMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayWayRateService extends BaseService<Payv2PayWayRate,Payv2PayWayRateMapper>  {
	/**
	 * 根据策略获取支付通道路由
	 * @param rateType
	 * @param payWayId
	 * @return
	 */
	public Payv2PayWayRate getPayWayRate(Integer rateType,Long payWayId,Long companyId,Long appId);
	
	/**
	 * 根据策略获取支付通道路由
	 * @param rateType
	 * @param payWayId
	 * @param payViewType   1支付宝，2微信  。。。。叠加字段
	 * @return
	 */
	public Payv2PayWayRate getPayWayRate(Integer rateType,Long companyId,Integer payViewType,Long appId);
	
	/**
	 * 根据id查询支付通道
	 * @param id
	 * @return
	 */
	public Payv2PayWayRate queryByid(Long id);
	
	/**
	 * 根据渠道商id和支付方式id查询通道
	 * @param map
	 * @return
	 */
	public List<Payv2PayWayRate> queryByChannelWayId(Map<String,Object> map);
	/**
	 * queryByDicRate 
	 * 获取通道
	 * @return
	 * @throws Exception    设定文件 
	 * List<Payv2PayWayRate>    返回类型
	 */
	public List<Payv2PayWayRate> queryByDicRate(); 
	
	
	/**
	 * 根据companyID获取其支持的支付渠道
	 * 
	 * @return List<Payv2PayWayRate> 返回类型
	 */
	List<Payv2PayWayRate> getPayWaysByCom(Map<String,Object> map);

	public void batchUpdate(Map<String, Object> paramMap);
	
	/**
	 * 通道导出
	 */
	List<Payv2PayWayRateVO> getExport(Map<String, Object> paramMap);

	public void updatePayWayRate(Map<String, Object> map);
}
