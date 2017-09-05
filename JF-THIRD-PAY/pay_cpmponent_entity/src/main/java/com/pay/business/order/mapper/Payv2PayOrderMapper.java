package com.pay.business.order.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.shopAppOrderVo;

/**
 * @author cyl
 * @version
 */
public interface Payv2PayOrderMapper extends BaseMapper<Payv2PayOrder> {
	public Integer getCount2(Map<String, Object> map);

	public List<Payv2PayOrder> pageQueryByObject2(Map<String, Object> map);
	
	/**
	 * 订单退款信息
	 */
	public List<Map<String, Object>> getRefundInfo(@Param("orderId")String orderId);
	
	/**
	 * 订单导出
	 */
	public List<Payv2PayOrder> getImportOrder(Map<String, Object> map);

	/**
	 * 获取在某一段时间的订单
	 * 
	 * @param map
	 * @return
	 */
	public List<Payv2PayOrder> selectByObjectDate(Map<String, Object> map);

	/**
	 * 最近一次支付成功的订单
	 * 
	 * @param ppo
	 * @return
	 */
	public Payv2PayOrder lastOrderOk(Payv2PayOrder ppo);

	/**
	 * 查询已优惠的额度
	 * 
	 * @param ppo
	 * @return
	 */
	public Double queryDiscountMoney(Payv2PayOrder ppo);

	/**
	 * 修改未支付订单的用户信息（从app支付转变h5，用户信息为null）
	 * 
	 * @param ppo
	 */
	public void updateUser(Payv2PayOrder ppo);

	/**
	 * @Title: getStatisticsCount
	 * @Description:统计总数
	 * @param map
	 * @return 设定文件
	 * @return Integer 返回类型
	 * @date 2017年2月9日 下午4:38:30
	 * @throws
	 */
	public Integer getStatisticsCount(Map<String, Object> map);

	/**
	 * @Title: getFailMoneySum
	 * @Description:获取成功交易金额
	 * @param map
	 * @return 设定文件
	 * @return Double 返回类型
	 * @date 2017年2月9日 下午5:42:54
	 * @throws
	 */
	public Double getMoneySum(Map<String, Object> map);

	/**
	 * @Title: getDayStatisticsInfoCount
	 * @Description: 获取今日数据详情
	 * @param map
	 * @return 设定文件
	 * @return List<Payv2PayOrder> 返回类型
	 * @date 2017年2月13日 上午11:13:58
	 * @throws
	 */
	public List<Payv2PayOrder> getDayStatisticsInfoCount(Map<String, Object> map);

	/**
	 * 查询两个时间段的订单的商户ID
	 * 
	 * @param map
	 * @return
	 */
	public List<Long> queryBetweenPayOrder(Map<String, Object> map);

	/**
	 * 查询两个时间的订单根据商户ID
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryBetweenPayOrderByCompanyId(Map<String, Object> map);

	/**
	 * 查询两个时间段的订单,根据商户ID 和 订单状态
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryPayOrderTime(Map<String, Object> map);

	/**
	 * 查询两个时间段的订单,用于插入dayway表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPayOrderSuccessPayWay(Map<String, Object> map);

	public Integer getPayOrderPageByObjectCount(Map<String, Object> map);

	public List<Payv2PayOrder> getPayOrderPageByObject(Map<String, Object> map);

	/**
	 * 获取当前商户统计
	 * 
	 * @param paramMap
	 *            (需指定开始和结束时间)
	 * @return
	 */
	public Map<String, Object> getCurrentStatisticsDayCompany(Map<String, Object> paramMap);

	/**
	 * 查询订单数据为保存到商户统计做准备
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompany(Map<String, Object> paramMap);

	/**
	 * 查询订单数据为保存到商户商品统计做准备
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompanyGoods(Map<String, Object> paramMap);

	/**
	 * 获取当前商户商品统计
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getCurrentStatisticsDayCompanyGoods(Map<String, Object> paramMap);

	public List<Map<String, Object>> queryOrderInfoToDayCompanyTime(Map<String, Object> paramMap);

	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompanyClear(Map<String, Object> paramMap);

	public List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(Map<String, Object> paramMap);

	/**
	 * 根据任何条件查询订单(只是会对payTime进行between查询[startTime,endTime])
	 * 
	 * @param paramMap
	 *            条件名称的key就是实体类的key
	 * @return
	 */
	public List<Payv2PayOrder> queryByPayTimeBetween(Map<String, Object> paramMap);

	public List<Payv2PayOrder> selectDetailList(Map<String, Object> paramMap);

	/**
	 * @Title: getDayOrderInfos
	 * @Description:查询某一个日期的数据统计
	 * @param @param map
	 * @param @return 设定文件
	 * @return Payv2PayOrder 返回类型
	 * @throws
	 */
	public Payv2PayOrder getDayOrderInfos(Map<String, Object> map);

	/**
	 * @Title: getMonthOrderInfos
	 * @Description: 查询某一个月的数据统计
	 * @param @param map
	 * @param @return 设定文件
	 * @return Payv2PayOrder 返回类型
	 * @throws
	 */
	public Payv2PayOrder getMonthOrderInfos(Map<String, Object> map);

	/**
	 * 
	 * @Title: getDayOrderInfoByPayId
	 * @Description:按照支付通道分组查询某一个日期的数据统计
	 * @param @param map
	 * @param @return 设定文件
	 * @return Payv2PayOrder 返回类型
	 * @throws
	 */
	public List<Payv2PayOrder> getDayOrderInfoByPayId(Map<String, Object> map);

	/**
	 * 
	 * @Title: getMonthOrderInfoByPayId
	 * @Description:按照支付通道分组 查询某一个月的数据统计
	 * @param @param map
	 * @param @return 设定文件
	 * @return Payv2PayOrder 返回类型
	 * @throws
	 */
	public List<Payv2PayOrder> getMonthOrderInfoByPayId(Map<String, Object> map);

	/**
	 * @Title: getOrderPageByAll
	 * @Description: 商户商铺APP分页查询订单详情
	 * @param @param map
	 * @param @return 设定文件
	 * @return List<shopAppOrderVo> 返回类型
	 * @throws
	 */
	public List<shopAppOrderVo> getOrderPageByAll(Map<String, Object> map);

	/**
	 * @Title: getOrderPageByAll
	 * @Description: 商户商铺APP分页查询总数
	 * @param @param map
	 * @param @return 设定文件
	 * @return List<shopAppOrderVo> 返回类型
	 * @throws
	 */
	public List<shopAppOrderVo> getOrderPageByCount(Map<String, Object> map);

	/**
	 * 对pay_money,pay_discount_money,discount_money进行聚合计算(company_id,app_id,
	 * pay_way_id会对这几个字段进行分组)
	 * 
	 * @param paramMap
	 *            Payv2PayOrder字段的任意名称
	 * @return key为allPayMoney, allPayDiscountMoney,allDiscountMoney
	 */
	public List<Map<String, Object>> sumMoney(Map<String, Object> paramMap);

	/**
	 * @Title: getSuccessOrderCount
	 * @Description:根据条件获取成功总订单数
	 * @param @param map
	 * @param @return 设定文件
	 * @return Integer 返回类型
	 * @throws
	 */
	public Integer getSuccessOrderCount(Map<String, Object> map);

	/**
	 * @Title: getSuccessOrderPageByAll
	 * @Description:据条件获取成功分页
	 * @param @param map
	 * @param @return 设定文件
	 * @return List<shopAppOrderVo> 返回类型
	 * @throws
	 */
	public List<shopAppOrderVo> getSuccessOrderPageByAll(Map<String, Object> map);

	/**
	 * @Title: getSuccessOrderRefundCount
	 * @Description:根据条件获取退款总数
	 * @param @param map
	 * @param @return 设定文件
	 * @return Integer 返回类型
	 * @throws
	 */
	public Integer getSuccessOrderRefundCount(Map<String, Object> map);

	/**
	 * @Title: getSuccessOrderRefundPageByAll
	 * @Description:根据条件获取退款分页数据
	 * @param @param map
	 * @param @return 设定文件
	 * @return List<shopAppOrderVo> 返回类型
	 * @throws
	 */
	public List<shopAppOrderVo> getSuccessOrderRefundPageByAll(Map<String, Object> map);
	
	public Payv2PayOrder selectSingleDetail(Payv2PayOrder t);
	/**
	* @Title: selectSingleDetail2 
	* @Description: 获取订单详情加支付通道各个回调参数
	* @param @param t
	* @param @return    设定文件 
	* @return Payv2PayOrder    返回类型 
	* @throws
	*/
	public Payv2PayOrder selectSingleDetail2(Payv2PayOrder t);
	/**
	 * 
	 * selectOrderSum 
	 * 获取页码显示上的各个总数
	 * @param map
	 * @return    设定文件 
	 * Map<String,Object>    返回类型
	 */
	public Map<String,Object> selectOrderSum(Map<String,Object> map);
	/**
	 * selectOrderByHour 
	 * 每小时统计订单金额与总数
	 * @param map
	 * @return    设定文件 
	 * List<Payv2PayOrder>    返回类型
	 */
	public List<Payv2PayOrder> selectOrderByHour(Map<String,Object> map);	
	/**
	 * 
	 * successData
	 * 数据汇总关键数据：已支付的金额与笔数
	 * @param map
	 * @return
	 * Map<String,Object>    返回类型
	 */
	Map<String,Object> successData(Map<String,Object> map);	
	/**
	 * 
	 * failData
	 * 数据汇总关键数据：已支付的金额与笔数
	 * @param map
	 * @return
	 * Map<String,Object>    返回类型
	 */
	Map<String,Object> failData(Map<String,Object> map);
	
	/**
	 * 
	 * groupByHour
	 * 以小时为单位查询实时统计数据
	 * @param map
	 * @return
	 * List<Map<String, Object>> 返回类型
	 */
	List<Map<String, Object>> groupByHour(Map<String,Object> map);
	/**
	 * 
	 * groupByDay
	 * 以天为单位查询实时统计数据
	 * @param map
	 * @return
	 * List<Map<String, Object>> 返回类型
	 */
	List<Map<String, Object>> groupByDay(Map<String,Object> map);
}