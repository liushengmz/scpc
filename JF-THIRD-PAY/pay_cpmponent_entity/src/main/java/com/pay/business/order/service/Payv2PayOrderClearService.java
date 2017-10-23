package com.pay.business.order.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.order.entity.OrderClearVO;
import com.pay.business.order.entity.Payv2PayOrderClear;
import com.pay.business.order.entity.Payv2PayOrderVO;
import com.pay.business.order.mapper.Payv2PayOrderClearMapper;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.record.entity.Payv2DayCompanyClear;

/**
* @Title: Payv2PayOrderClearService.java 
* @Package com.pay.business.order.service 
* @Description: 对账表service 
* @author ZHOULIBO   
* @date 2017年8月3日 上午9:33:17 
* @version V1.0
*/
public interface Payv2PayOrderClearService extends BaseService<Payv2PayOrderClear,Payv2PayOrderClearMapper>  {

	/**
	 * job 
	 * 对账调度job
	 * @param alipayTime ：支付宝对账时间：格式为：yyyy-MM-dd
	 * @param weixinTime ：微信对账时间：   格式为：yyyyMMdd
	 * @param pav2PayOrderObjMap：某一天订单表的数据
	 * @param payv2PayOrderRefundObjMap：某一天的退款表的诗句
	 * @param payName：支付平台名字：支付宝，微信
	 * @param rate：支付通道对象
	 * @throws Exception    设定文件 
	 * void    返回类型
	 */
	public void job(String alipayTime,String weixinTime,Map<String, Object> pav2PayOrderObjMap,Map<String, Object> payv2PayOrderRefundObjMap,String payName,Payv2PayWayRate rate) throws Exception;
	
	/**
	 * 清单对账单列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2PayOrderClear> payv2PayOrderClearList(Map<String, Object> map) ;
	
	/**
	 * 
	 * appIdDownBill 
	 * 根据appId分组生成对账文件
	 * @param orderList
	 * @param alipayTime
	 * @param as
	 * @throws Exception    设定文件 
	 * void    返回类型
	 */
	public void appIdDownBill(List<Payv2PayOrderClear> orderList,String alipayTime,Date as
			,Payv2DayCompanyClear payv2DayCompanyClear,Map<String, Object> map) throws Exception;
	/**
	 * 
	 * companyIdDownBill 
	 * 根据companyId分组生成对账文件
	 * @param orderList
	 * @param alipayTime
	 * @param as
	 * @param map
	 * @throws Exception    设定文件 
	 * void    返回类型
	 */
	public void companyIdDownBill(List<Payv2PayOrderVO> orderList,String alipayTime,Date as,Map<Long,String> map) throws Exception;
	/**
	 * 
	 * comRateIdDownBill 
	 * 根据companyId和rateId分组生成对账文件
	 * @param orderList
	 * @param alipayTime
	 * @param as
	 * @param comMap
	 * @throws Exception    设定文件 
	 * void    返回类型
	 */
	public void comRateIdDownBill(List<Payv2PayOrderVO> orderList,String alipayTime,Date as,Map<Long,String> comMap) throws Exception;
	/**
	 * getOderAndRefundByTime 
	 * 在手动对账的时候获取 ：某一天的订单数据与退款数据
	 * @param time：查询时间格式为：yyyy-MM-dd
	 * @param rateId：支付通道ID；对应表：payv2_pay_way_rate的id
	 * @return
	 * @throws Exception    设定文件 
	 * List<Map<String,Object>>    返回类型:pav2PayOrderObjMap为订单map;payv2PayOrderRefundObjMap为退款订单map
	 */
	public List<Map<String,Object>> getOderAndRefundByTime (String time,Long rateId) throws Exception;
	
	/**
	 * @param rateid
	 * @return	获取差错订单
	 */
	public PageObject<OrderClearVO> getDifferOrder(Map<String, Object> map);
	
	/**
	 * @return
	 */
	public List<String> getBills();
	
	/**
	 * @param date
	 * @return
	 */
	public List<Map<String,Object>> getBillList(String date);

	/**
	 * isHaveBill
	 * 根据商户Id与账单时间判断该商户是否已经出账
	 * @param map
	 * @return
	 */
	int isHaveBill(Map<String, Object> map); 
	
	/**
	 * 平帐，更新状态、订单金额、手续费、成本费
	 * 交易更新订单表
	 * 退款更新退款表
	 */
	public void updateClear(String ids);
	
	/**
	 * @param date
	 * @param rateId
	 * 出账
	 * insert payv2_buss_way_detail
	 */
	public void updateOutOrder(String date, String rateId);
	
	/**
	 * 根据appId集合，商户id，对账时间搜索订单
	 * @param orderParam
	 * @return
	 */
	List<Payv2PayOrderClear> queryByApp(Map<String, Object> orderParam);
}
