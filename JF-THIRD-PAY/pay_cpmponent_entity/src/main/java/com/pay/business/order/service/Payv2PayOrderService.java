package com.pay.business.order.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.shopAppOrderVo;
import com.pay.business.order.mapper.Payv2PayOrderMapper;

/**
* @Title: Payv2PayOrderService.java 
* @Package com.pay.business.payv2.service 
* @Description: 订单service
* @author ZHOULIBO   
* @date 2017年7月8日 下午3:42:02 
* @version V1.0
*/
public interface Payv2PayOrderService extends BaseService<Payv2PayOrder,Payv2PayOrderMapper>  {
	/**
	* @Title: selectSingle 
	* @Description:订单查询
	* @param @param t
	* @param @return    设定文件 
	* @return Payv2PayOrder    返回类型 
	* @throws
	*/
	public Payv2PayOrder selectSingle(Payv2PayOrder t);
	/**
	* @Title: selectByObject 
	* @Description: 订单查询List
	* @param @param t
	* @param @return    设定文件 
	* @return List<Payv2PayOrder>    返回类型 
	* @throws
	*/
	public List<Payv2PayOrder> selectByObject(Payv2PayOrder t);
	/**
	* @Title: getPageObject 
	* @Description:获取订单列表
	* @param map
	* @param type	0不需退款信息1需要
	* @date 2016年12月7日 下午3:38:32 
	* @throws
	*/
	public PageObject<Payv2PayOrder> getPageObject(Map<String,Object> map, Integer type);
	
	/**
	 * 支付详情（保存用户，查询支付方式信息）h5网站支付
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> payOrderH5Detail(Map<String,Object> map,Payv2BussCompanyApp pbca) throws Exception;
	
	/**
	 * 支付详情（保存用户，查询支付方式信息）线下扫码支付
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> payOrderShopDetail(Map<String,Object> map) throws Exception;
	
	/**
	 * 支付参数签名
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public Map<String,String> payForOrder(Map<String,Object> map) throws Exception;
	/**
	 * 支付宝回调操作
	 * @param map
	 * @param createSign
	 */
	public boolean aliPayCallBack(Map<String, String> params,Payv2PayOrder payv2PayOrder) throws Exception;
	/**
	 * 支付宝回调操作（点游）
	 * @param map
	 * @param createSign
	 */
	public boolean dyAliPayCallBack(Map<String, String> params,Payv2PayOrder payv2PayOrder) throws Exception;
	/**
	 * 支付宝网站h5支付    获取表单
	 * @param map
	 * @return
	 */
	public Map<String, String> payH5Alipay(Map<String,Object> map) throws Exception;
	
	/**
	 * 查询订单
	 * @param map
	 * @return
	 */
	public Map<String,Object> queryOrderNum(Map<String,Object> map) throws Exception;
	
	/**
	 * 查询订单
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> queryOrder(Map<String,Object> map,Payv2BussCompanyApp pbca) throws Exception;
	
	/**
	 * 创建订单
	 * @param map
	 * @param createSign
	 */
	public Map<String,Object> buildOrder(Map<String, Object> map) throws Exception;
	
	
	/**
	* @Title: getDayStatistics 
	* @Description:获取各个维度的数据详情
	* @param map
	* @return
	* @throws Exception    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2017年2月9日 下午4:42:42 
	* @throws
	*/
	public Map<String,Object> getDayStatistics(Map<String,Object> map) throws Exception;
	

	/**
	 * 查询指定时间段的订单,对商户进行分组
	 * @param map
	 * @return
	 */
	public List<Long> queryBetweenPayOrder(Map<String, Object> map);

	/**
	 * 查询指定两个时间内的订单,根据商户ID 以及 支付状态
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryBetweenPayOrderByCompanyId(
			Map<String, Object> map);

	/**
	 * 查询一个时间段的订单总数及总金额
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryPayOrderTime(Map<String, Object> map);

	/**
	 * 查询支付类型的订单总数及总金额
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPayOrderSuccessPayWay(Map<String, Object> map);
	
	/**
	* @Title: getPayCompanyPageByObject 
	* @Description:获取订单列表
	* @param map
	* @return    设定文件 
	* @return PageObject<Payv2PayOrder>    返回类型 
	* @date 2017年2月20日 下午5:12:32 
	* @throws
	*/
	public PageObject<Payv2PayOrder> getPayOrderPageByObject(Map<String,Object> map);
	/**
	 * @Title: getDayMoneySum 
	 * @Description: 获取某一个商户今日的流水总金额
	 * @param @param map
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return double    返回类型 
	 * @throws
	 */
	public double getDayMoneySum(Map<String,Object> map) throws Exception;

	/**
	 * 获取当前商户统计
	 * @param paramMap (需指定开始和结束时间)
	 * @return
	 */
	public Map<String, Object> getCurrentStatisticsDayCompany(Map<String, Object> paramMap);

	/**
	 * 查询订单数据为保存到商户统计做准备
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompany(
			Map<String, Object> paramMap);

	/**
	 * 查询订单数据为保存到商户商品统计做准备
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompanyGoods(
			Map<String, Object> paramMap);

	/**
	 * 获取当前商户商品统计
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getCurrentStatisticsDayCompanyGoods(
			Map<String, Object> paramMap);

	/**
	 * 查询订单商户的每个应用指定时间内的数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryOrderInfoToDayCompanyTime(
			Map<String, Object> paramMap);

	/**
	 * 查询订单数据为保存到商户渠道统计做准备
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompanyClear(
			Map<String, Object> paramMap);

	public List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(
			Map<String, Object> paramMap);

	/**
	 * 根据任何条件查询订单(只是会对payTime进行between查询[startTime,endTime])
	 * @param paramMap 条件名称的key就是实体类的key
	 * @return
	 */
	public List<Payv2PayOrder> queryByPayTimeBetween(
			Map<String, Object> paramMap);

	/**
	 * 查询所有 包括companyName,appName,wayName,goodsName,channelName
	 * @param paramMap
	 * @return
	 */
	public List<Payv2PayOrder> selectDetailList(Map<String, Object> paramMap);
	
	/**
	 * @Title: getDayOrderInfos 
	 * @Description:查询某一个日期的数据统计 
	 * @param @param map
	 * @param @return    设定文件 
	 * @return Payv2PayOrder    返回类型 
	 * @throws
	 */
	public Payv2PayOrder getDayOrderInfos(Map<String,Object> map);
	/**
	 * @Title: getMonthOrderInfos 
	 * @Description: 查询某一个月的数据统计
	 * @param @param map
	 * @param @return    设定文件 
	 * @return Payv2PayOrder    返回类型 
	 * @throws
	 */
	public Payv2PayOrder getMonthOrderInfos(Map<String,Object> map);
	/**
	 * 
	 * @Title: getDayOrderInfoByPayId 
	 * @Description:按照支付通道分组查询某一个日期的数据统计 
	 * @param @param map
	 * @param @return    设定文件 
	 * @return Payv2PayOrder    返回类型 
	 * @throws
	 */
	public List<Payv2PayOrder> getDayOrderInfoByPayId(Map<String,Object> map);
	/**
	 * 
	 * @Title: getMonthOrderInfoByPayId 
	 * @Description:按照支付通道分组 查询某一个月的数据统计
	 * @param @param map
	 * @param @return    设定文件 
	 * @return Payv2PayOrder    返回类型 
	 * @throws
	 */
	public List<Payv2PayOrder> getMonthOrderInfoByPayId(Map<String,Object> map);
	/**
	 * @Title: getShopOrderPage 
	 * @Description: 获取商户商品APP订单列表
	 * @param @param map
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return PageObject<shopAppOrderVo>    返回类型 
	 * @throws
	 */
	public PageObject<shopAppOrderVo> getShopOrderPageByApp(Map<String,Object> map) throws Exception;
	
	/**
	 * 商户app条码支付
	 * @param map
	 * @return
	 */
	public Map<String,Object> barCodePay(Map<String,Object> map) throws Exception;
	
	/**
	 * 对pay_money,pay_discount_money,discount_money进行聚合计算(company_id,app_id,pay_way_id会对这几个字段进行分组)
	 * @param paramMap Payv2PayOrder字段的任意名称
	 * @return key为allPayMoney, allPayDiscountMoney,allDiscountMoney
	 */
	public List<Map<String,Object>> sumMoney(Map<String, Object> paramMap);
	
	/**
	 * 威富通微信回调操作
	 * @param map
	 * @param createSign
	 */
	public boolean wftWechatPayCallBack(Map<String, String> map) throws Exception;
	
	/**
	 * 支付详情（保存用户，查询支付方式信息）
	 * @param map
	 * @param paramMap
	 * @param pbca
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> payOrderDetail(Map<String,Object> paramMap
			,Payv2BussCompanyApp pbca) throws Exception;
	
	/**
	 * 创建订单(无界面支付)
	 * @param map
	 * @param paramMap
	 * @param pbca
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> payPage(String ip,String appType,Map<String,Object> paramMap,Payv2BussCompanyApp pbca,Integer payViewType) throws Exception;
	
	/**
	 * 支付集回调（有氧）
	 * @param map
	 * @param createSign
	 */
	public boolean yyPayCallBack(Map<String, Object> params) throws Exception;
	/**
	 * 
	* @Title: getPayWayMoney 
	* @Description: 通道费用最小值0.01分
	* @param @param payMoney
	* @param @param payWayRate
	* @param @return    设定文件 
	* @return Double    返回类型 
	* @throws
	 */
	public Double getPayWayMoney(Double payMoney, Double payWayRate);
	
	/**
	 * 获取订单详情
	 * @param t
	 * @return
	 */
	public Payv2PayOrder selectSingleDetail(Payv2PayOrder t);
	/**
	* @Title: getOrderInfo 
	* @Description: 根据订单号获取订单详情与回调参数
	* @param @param orderNum
	* @param @return    设定文件 
	* @return Payv2PayOrder    返回类型 
	* @throws
	*/
	public Payv2PayOrder getOrderInfo(String orderNum);
	/**
	* @Title: createOrder 
	* @Description: 创建订单公共类
	* @param @param map
	* @param @param payOrder
	* @param @param pbca
	* @param @param payUser
	* @param @param payMoney
	* @param @param rateType
	* @param @return    设定文件 
	* @return Payv2PayOrder    返回类型 
	* @throws
	*/
	public Payv2PayOrder createOrder(Map<String, Object> map, Payv2PayOrder payOrder, Payv2BussCompanyApp pbca, Double payMoney,
			Long payWayId, Long rateId,Double companyRate,Double costRate);
	
	/**
	 * 创建订单公共类(无界面)
	 * @param map
	 * @param pbca
	 * @param payViewType
	 * @param payType
	 * @return
	 */
	public Map<String, String> dyCreateOrder(Map<String, Object> map, Payv2BussCompanyApp pbca
			, Integer payViewType,Integer payType);
	/**
	 * 
	 * selectByObjectDate 
	 * 获取本地时间段的所有订单 
	 * @param map
	 * @return
	 * @throws Exception    设定文件 
	 * List<Payv2PayOrder>    返回类型
	 */
	public List<Payv2PayOrder> selectByObjectDate(Map<String, Object> map) throws Exception;
	/**
	 * 
	 * getImportOrder 
	 * TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return    设定文件 
	 * List<Payv2PayOrder>    返回类型
	 */
	public List<Payv2PayOrder> getImportOrder(Map<String, Object> map);
	/**
	 * 
	 * selectOrderSum 
	 * 获取订单列表的各个项目的总数 
	 * @param map
	 * @return    设定文件 
	 * Map<String,Object>    返回类型
	 */
	public Map<String,Object> selectOrderSum(Map<String, Object> map);
	
}
