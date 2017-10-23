package com.pay.business.payway.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.entity.Payv2PayWayRateVO;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayWayRateMapper extends BaseMapper<Payv2PayWayRate>{
	
	/**
	 * 根据类型查询支付通道
	 * @param t
	 * @return
	 */
	public Payv2PayWayRate getRateByType(Payv2PayWayRate t);
	
	/**
	 * 根据商户和支付方式查询通道
	 * @param map
	 * @return
	 */
	public List<Payv2PayWayRate> queryByCompany(Map<String,Object> map);
	
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
	 * 根据支付方式查询通道
	 * @return
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