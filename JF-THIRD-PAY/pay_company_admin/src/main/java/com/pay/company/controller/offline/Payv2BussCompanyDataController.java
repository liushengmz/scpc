package com.pay.company.controller.offline;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyTime;
import com.pay.business.record.service.Payv2StatisticsDayCompanyClearService;
import com.pay.business.record.service.Payv2StatisticsDayCompanyGoodsService;
import com.pay.business.record.service.Payv2StatisticsDayCompanyService;
import com.pay.business.record.service.Payv2StatisticsDayCompanyTimeService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.controller.admin.BaseManagerController;
import com.pay.company.util.ListUtils;

/**
 * @Title: Payv2BussCompanyDataController.java
 * @Package com.pay.company.controller.offline
 * @Description: 商户订单数据统计controller
 * @author ZHOULIBO
 * @date 2017年6月3日 上午10:25:27
 * @version V1.0
 */
@Controller
@RequestMapping("/offline/bussCompanyData/*")
public class Payv2BussCompanyDataController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper> {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private Payv2StatisticsDayCompanyService payv2StatisticsDayCompanyService;

	@Autowired
	private Payv2StatisticsDayCompanyTimeService payv2StatisticsDayCompanyTimeService;

	@Autowired
	private Payv2StatisticsDayCompanyClearService payv2StatisticsDayCompanyClearService;

	@Autowired
	private Payv2StatisticsDayCompanyGoodsService payv2StatisticsDayCompanyGoodsService;

	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;

	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;

	/**
	 * 关键数据指标
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("curxData")
	@ResponseBody
	public Map<String, Object> curxData(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		// 今天 昨天 前七天 前三十天 自定义日期
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> commonVali = commonVali(map);
		if (commonVali != null) {
			return commonVali;
		}
		Map<String, Object> paramMap = commonTimeParam(map);
		Long app_id = null;
		if (map.containsKey("appId") && StringUtils.isNotBlank(map.get("appId").toString())) {
			app_id = NumberUtils.createLong(map.get("appId").toString());
			paramMap.put("appId", app_id);
		}
		Payv2BussCompany bussCompany = getAdmin();
		if (bussCompany == null || bussCompany.getCurrentUserStatus() == null || bussCompany.getId() == null) {
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN, null, "用户Session不存在或失效,请重新登录");
		}
		Long companyId = bussCompany.getId();
		Integer licenseType = bussCompany.getCurrentUserStatus();
		try {
			Map<String, Object> resMap = payv2StatisticsDayCompanyService.getCurrentStatisticsDayCompany(isToday(map), companyId, licenseType, app_id, paramMap
					.get("startTime").toString(), paramMap.get("endTime").toString());
			if (resMap == null || resMap.size() == 0) {
				resMap = new HashMap<>();
				resMap.put("companyId", companyId);
				resMap.put("orderType", licenseType);
				resMap.put("appId", app_id);
				resMap.put("allSuccessCount", 0);
				resMap.put("allSuccessMoney", 0);
				resMap.put("allFailCount", 0);
				resMap.put("allFailMoney", 0);
				resMap.put("allProcessingCount", 0);
				resMap.put("allProcessingMoney", 0);
			}
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resMap);
		} catch (Exception e) {
			logger.error("获取关键数据指标异常:" + map.toString(), e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 交易时刻趋势图
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("timeTrend")
	@ResponseBody
	public Map<String, Object> timeTrend(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		// 今天 昨天 前七天 前三十天 自定义日期
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> commonVali = commonVali(map);
		if (commonVali != null) {
			return commonVali;
		}
		Map<String, Object> paramMap = commonTimeParam(map);
		Long pay_way_id = null;
		if (map.containsKey("payWayId") && StringUtils.isNotBlank(map.get("payWayId").toString())) {
			pay_way_id = NumberUtils.createLong(map.get("payWayId").toString());
			paramMap.put("payWayId", pay_way_id);
		}
		Long app_id = null;
		if (map.containsKey("appId") && StringUtils.isNotBlank(map.get("appId").toString())) {
			app_id = NumberUtils.createLong(map.get("appId").toString());
			paramMap.put("appId", app_id);
		}
		try {
			Payv2BussCompany bussCompany = getAdmin();
			if (bussCompany == null || bussCompany.getCurrentUserStatus() == null || bussCompany.getId() == null) {
				return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN, null, "用户Session不存在或失效,请重新登录");
			}
			Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
			Long companyId = bussCompany.getId();
			paramMap.put("companyId", companyId);
			Integer licenseType = bussCompany.getCurrentUserStatus();
			paramMap.put("companyType", licenseType);
			List<Payv2StatisticsDayCompanyTime> resList = null;
			Map<String, Object> resMap = new HashMap<>();
			if (queryType == 2) {
				resList = payv2StatisticsDayCompanyTimeService.getYesterDayCompanyTime(companyId, licenseType, app_id, pay_way_id, paramMap.get("startTime")
						.toString(), paramMap.get("endTime").toString());
				resMap.put("resultType", 1);
				resMap.put("tempTime", DateUtil.addDay(new Date(), -1));
			} else if (queryType == 1) {
				resList = payv2StatisticsDayCompanyTimeService.getCurrentStatisticsDayCompanyTime(isToday(map), companyId, licenseType, app_id, pay_way_id,
						paramMap.get("startTime").toString(), paramMap.get("endTime").toString());
				resMap.put("resultType", 1);
				resMap.put("tempTime", new Date());
			} else {
				resList = payv2StatisticsDayCompanyTimeService.getStatisticsDayCompanyTime(paramMap);
				int size = resList.size();
				for (int i = 0; i < size; i++) {
					Payv2StatisticsDayCompanyTime payv2StatisticsDayCompanyTime = resList.get(i);
					Date statisticsTime = payv2StatisticsDayCompanyTime.getStatisticsTime();
					payv2StatisticsDayCompanyTime.setStatisticsStringTime(DateUtil.DateToString(statisticsTime, DateStyle.YYYY_MM_DD).trim());
				}
				resMap.put("resultType", 2);
			}
			if (resList == null || resList.size() == 0) {
				if (NumberUtils.createInteger(resMap.get("resultType").toString()) == 1) {
					Payv2StatisticsDayCompanyTime companyTime = null;
					Date st = (Date) resMap.get("tempTime");
					resMap.remove("tempTime");
					for (int i = 0; i < 24; i++) {
						resList.add(setComanyTime(companyTime, app_id, companyId, pay_way_id, 0, 0.0, i, licenseType, st, st));
					}
				} else {
					Payv2StatisticsDayCompanyTime companyTime = null;
					if (queryType == 3 || queryType == 4) {
						int fos = queryType == 3 ? 8 : 31;
						Date date = new Date();
						for (int i = 1; i < fos; i++) {
							BigInteger b = new BigInteger(String.valueOf(i));
							Date addDay = DateUtil.addDay(date, b.negate().intValue());
							resList.add(setComanyTime(companyTime, app_id, companyId, pay_way_id, 0, 0.0, null, licenseType, addDay, addDay, true));// 为真是为了设置字符串日期
						}
					} else {
						String startTime = paramMap.get("startTime").toString();
						String endTime = paramMap.get("endTime").toString();
						// 自定义时间
						Date[] dateArrays = getDateArrays(DateUtil.StringToDate(startTime, DateStyle.YYYY_MM_DD),
								DateUtil.StringToDate(endTime, DateStyle.YYYY_MM_DD));
						for (Date date : dateArrays) {
							resList.add(setComanyTime(companyTime, app_id, companyId, pay_way_id, 0, 0.0, null, licenseType, date, date, true));// 为真是为了设置字符串日期
						}
					}
				}
			} else {
				// 如果不为空 而记录条数不足七条 或者 不满足30天,需要添加对应日期的假数据
				int len = 0;
				Date[] dateArrays = null;
				if (queryType == 3 && resList.size() < 7) {
					len = 7;
				} else if (queryType == 4 && resList.size() < 30) {
					len = 30;
				} else if (queryType == 5) {
					String startTime = paramMap.get("startTime").toString();
					String endTime = paramMap.get("endTime").toString();
					// 自定义时间
					dateArrays = getDateArrays(DateUtil.StringToDate(startTime, DateStyle.YYYY_MM_DD), DateUtil.StringToDate(endTime, DateStyle.YYYY_MM_DD));
					if (resList.size() < dateArrays.length) {
						len = dateArrays.length;
					}
				}
				int resultSize = resList.size();
				// 先将res的所有日期存入map , 然后 就不用每次都去获取每个对象
				Map<String, String> tempMap = new HashMap<>();
				for (int i = 0; i < resultSize; i++) {
					String trim = resList.get(i).getStatisticsStringTime();
					tempMap.put(trim, "");
				}
				Payv2StatisticsDayCompanyTime companyTime = null;
				if (dateArrays != null && len > 0) {
					for (Date dates : dateArrays) {
						String oldDate = DateUtil.DateToString(dates, DateStyle.YYYY_MM_DD).trim();
						if (!tempMap.containsKey(oldDate)) {
							// 对不存在的日期 进行添加
							resList.add(setComanyTime(companyTime, app_id, companyId, pay_way_id, 0, 0.0, null, licenseType, dates, dates, true));// 为真是为了设置字符串日期
						}
					}
				} else {
					Date date = new Date();
					for (int i = 0; i < len; i++) {
						// 计算
						BigInteger b = new BigInteger(String.valueOf(i + 1));
						Date addDay = DateUtil.addDay(date, b.negate().intValue());
						String oldDate = DateUtil.DateToString(addDay, DateStyle.YYYY_MM_DD).trim();
						// 如果
						if (!tempMap.containsKey(oldDate)) {
							// 对不存在的日期 进行添加
							resList.add(setComanyTime(companyTime, app_id, companyId, pay_way_id, 0, 0.0, null, licenseType, addDay, addDay, true));// 为真是为了设置字符串日期
						}
					}
				}
			}
			if (queryType == 3 || queryType == 4 || queryType == 5) {
				// 对日期进行排序
				ListUtils.sort(resList, true, true, "statisticsTime");
			}
			// 如果支付方式为 全部的时候：计算各个总数
			System.out.println(map.get("payWayId"));
			if (resList.size() > 0 && (null == map.get("payWayId") || "" == map.get("payWayId")) && (queryType == 1 || queryType == 2)) {
				List<Payv2StatisticsDayCompanyTime> resList1 = new ArrayList<Payv2StatisticsDayCompanyTime>();
				for (int i = 0; i < 24; i++) {
					Payv2StatisticsDayCompanyTime payv2StatisticsDayCompanyTime1 = new Payv2StatisticsDayCompanyTime();
					// 成功订单总数
					int successOrderCount = 0;
					// 成功订单总额
					double successOrderMoney = 0.00;
					for (Payv2StatisticsDayCompanyTime payv2StatisticsDayCompanyTime : resList) {
						if (null != payv2StatisticsDayCompanyTime.getTimeType() && payv2StatisticsDayCompanyTime.getTimeType() == i) {
							successOrderCount = successOrderCount + payv2StatisticsDayCompanyTime.getSuccessOrderCount();
							successOrderMoney = successOrderMoney + payv2StatisticsDayCompanyTime.getSuccessOrderMoney();
						}
					}
					// 时间段
					payv2StatisticsDayCompanyTime1.setTimeType(i);
					payv2StatisticsDayCompanyTime1.setSuccessOrderCount(successOrderCount);
					payv2StatisticsDayCompanyTime1.setSuccessOrderMoney(successOrderMoney);
					resList1.add(payv2StatisticsDayCompanyTime1);
				}
				// 返回数据
				resMap.put("resultData", resList1);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resMap);
			} else {
				resMap.put("resultData", resList);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resMap);
			}
		} catch (Exception e) {
			logger.error("获取交易时刻趋势图异常:" + map.toString(), e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	public Payv2StatisticsDayCompanyTime setComanyTime(Payv2StatisticsDayCompanyTime companyTime, Long app_id, Long companyId, Long pay_way_id,
			int successOrderCount, double successOrderMoney, Integer timeType, Integer type, Date statisticsTime, Date createTime) {
		return setComanyTime(companyTime, app_id, companyId, pay_way_id, successOrderCount, successOrderMoney, timeType, type, statisticsTime, createTime,
				false);
	}

	public Payv2StatisticsDayCompanyTime setComanyTime(Payv2StatisticsDayCompanyTime companyTime, Long app_id, Long companyId, Long pay_way_id,
			int successOrderCount, double successOrderMoney, Integer timeType, Integer type, Date statisticsTime, Date createTime, boolean flag) {
		companyTime = new Payv2StatisticsDayCompanyTime();
		companyTime.setAppId(app_id);
		companyTime.setCompanyId(companyId);
		companyTime.setPayWayId(pay_way_id);
		companyTime.setSuccessOrderCount(successOrderCount);
		companyTime.setSuccessOrderMoney(successOrderMoney);
		if (timeType != null) {
			companyTime.setTimeType(timeType);
		}
		companyTime.setType(type);
		companyTime.setStatisticsTime(statisticsTime);
		companyTime.setCreateTime(createTime);
		if (flag) {
			String stringTime = DateUtil.DateToString(statisticsTime, DateStyle.YYYY_MM_DD).trim();
			companyTime.setStatisticsStringTime(stringTime);
		}
		return companyTime;
	}

	/**
	 * 交易分布图(通道)
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("tradeDistributedPayWay")
	@ResponseBody
	public Map<String, Object> tradeDistributedPayWay(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		// 今天 昨天 前七天 前三十天 自定义日期
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> commonVali = commonVali(map);
		if (commonVali != null) {
			return commonVali;
		}
		Map<String, Object> paramMap = commonTimeParam(map);
		Long app_id = null;
		if (map.containsKey("appId") && StringUtils.isNotBlank(map.get("appId").toString())) {
			app_id = NumberUtils.createLong(map.get("appId").toString());
			paramMap.put("appId", app_id);
		}
		Payv2BussCompany bussCompany = getAdmin();
		if (bussCompany == null || bussCompany.getCurrentUserStatus() == null || bussCompany.getId() == null) {
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN, null, "用户Session不存在或失效,请重新登录");
		}
		Long companyId = bussCompany.getId();
		Integer licenseType = bussCompany.getCurrentUserStatus();
		try {
			// 订单总金额类型
			Integer queryTypeMoney = 1;
			// 订单总数类型
			Integer queryTypeCount = 2;
			Map<String, Object> allResultMap = new HashMap<>();
			// 按支付通道：获取各个通道的总成功金额
			List<Map<String, Object>> moneyMap = payv2StatisticsDayCompanyClearService.getCurrentStatisticsDayCompanyClear(isToday(map), companyId, app_id,
					licenseType, paramMap.get("startTime").toString(), paramMap.get("endTime").toString(), queryTypeMoney);
			allResultMap.put("topMoney", moneyMap);
			// 按支付通道：获取各个通道的总成功笔数
			List<Map<String, Object>> countMap = payv2StatisticsDayCompanyClearService.getCurrentStatisticsDayCompanyClear(isToday(map), companyId, app_id,
					licenseType, paramMap.get("startTime").toString(), paramMap.get("endTime").toString(), queryTypeCount);
			allResultMap.put("topCount", countMap);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, allResultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}

	/**
	 * 交易分布图(商品)
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("tradeDistributedGoods")
	@ResponseBody
	public Map<String, Object> tradeDistributedGoods(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		// 今天 昨天 前七天 前三十天 自定义日期
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> commonVali = commonVali(map);
		if (commonVali != null) {
			return commonVali;
		}
		Map<String, Object> paramMap = commonTimeParam(map);
		Long app_id = null;
		if (map.containsKey("appId") && StringUtils.isNotBlank(map.get("appId").toString())) {
			app_id = NumberUtils.createLong(map.get("appId").toString());
			paramMap.put("appId", app_id);
		}
		// Long companyId =
		// NumberUtils.createLong(map.get("companyId").toString());
		Payv2BussCompany bussCompany = getAdmin();
		if (bussCompany == null || bussCompany.getCurrentUserStatus() == null || bussCompany.getId() == null) {
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN, null, "用户Session不存在或失效,请重新登录");
		}
		Integer licenseType = bussCompany.getCurrentUserStatus();
		Long companyId = bussCompany.getId();
		try {
			Integer queryTypeMoney = 1;
			Integer queryTypeCount = 2;
			Map<String, Object> allResultMap = new HashMap<>();
			Map<Long, String> tempMap = new HashMap<>();
			List<Map<String, Object>> goodsMoneyMap = payv2StatisticsDayCompanyGoodsService.getCurrentStatisticsDayCompanyGoods(isToday(map), companyId,
					app_id, licenseType, paramMap.get("startTime").toString(), paramMap.get("endTime").toString(), queryTypeMoney);
			goodsMapPut(tempMap, goodsMoneyMap, licenseType);
			allResultMap.put("goodsMoneyMap", goodsMoneyMap);
			List<Map<String, Object>> goodsCountMap = payv2StatisticsDayCompanyGoodsService.getCurrentStatisticsDayCompanyGoods(isToday(map), companyId,
					app_id, licenseType, paramMap.get("startTime").toString(), paramMap.get("endTime").toString(), queryTypeCount);
			goodsMapPut(tempMap, goodsCountMap, licenseType);
			allResultMap.put("goodsCountMap", goodsCountMap);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, allResultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}

	/**
	 * 引用中的map字段值更新
	 * 
	 * @param tempMap
	 * @param goodsResultMap
	 * @param licenseType
	 *            用于判断使用哪个service
	 */
	public void goodsMapPut(Map<Long, String> tempMap, List<Map<String, Object>> goodsResultMap, Integer licenseType) {
		if (goodsResultMap != null) {
			Map<String, Object> tm = new HashMap<>();
			for (Map<String, Object> maps : goodsResultMap) {
				Long app_ids = NumberUtils.createLong(maps.get("appId").toString());
				if (!tempMap.containsKey(app_ids)) {
					tm.put("id", app_ids);
					String appName = "";
					if (licenseType == 1) {
						Payv2BussCompanyApp detail = payv2BussCompanyAppService.detail(tm);
						appName = detail.getAppName();
					} else {
						Payv2BussCompanyShop detail = payv2BussCompanyShopService.detail(tm);
						appName = detail.getShopName();
					}
					tm.clear();
					String goodsName = String.valueOf(maps.get("goods_name"));
					maps.put("goods_name", appName + ":" + goodsName);
					tempMap.put(app_ids, appName);
				} else {
					String appName = tempMap.get(app_ids);
					String goodsName = String.valueOf(maps.get("goods_name"));
					maps.put("goods_name", appName + ":" + goodsName);
				}
			}
		}
	}

	public static Date[] getDateArrays(Date start, Date end) {
		return getDateArrays(start, end, Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取两个日期间隔的每一天(包括起始日期和结束日期)
	 * 
	 * @param start
	 *            "2017-03-10"
	 * @param end
	 *            "2017-03-20"
	 * @param calendarType
	 *            Calendar.DAY_OF_YEAR
	 * @return
	 */
	public static Date[] getDateArrays(Date start, Date end, int calendarType) {
		ArrayList<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		Date tmpDate = calendar.getTime();
		long endTime = end.getTime();
		while (tmpDate.before(end) || tmpDate.getTime() == endTime) {
			ret.add(calendar.getTime());
			calendar.add(calendarType, 1);
			tmpDate = calendar.getTime();
		}
		Date[] dates = new Date[ret.size()];
		return ret.toArray(dates);
	}

	public boolean isToday(Map<String, Object> map) {
		Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
		boolean isToday = true;
		if (queryType != 1) {
			isToday = false;
		}
		return isToday;
	}

	public Map<String, Object> commonVali(Map<String, Object> map) {
		Map<String, Object> resultMap = null;
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "dateType" }, map);
		if (!isNotNull) {
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数不允许为空");
		}
		Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
		if ((!(ObjectUtil.checkObject(new String[] { "startTime", "endTime" }, map))) && queryType == 5) {
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数不允许为空");
		}
		return resultMap;
	}

	public static Map<String, Object> commonTimeParam(Map<String, Object> map) {
		Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
		String startTimeSuffix = " 00:00:00";
		String endTimeSuffix = " 23:59:59";
		String start_time = "startTime";
		String end_time = "endTime";
		// 封装不同查询时间的参数
		Map<String, Object> paramMap = new HashMap<>();
		Date nowDate = new Date();
		Date yestoDate = DateUtil.addDay(nowDate, -1);
		// TODO 前七天和前三十天应该不包含今天
		if (queryType == 1) {// 今天
			paramMap.put(start_time, DateUtil.DateToString(nowDate, DateStyle.YYYY_MM_DD) + startTimeSuffix);
			paramMap.put(end_time, DateUtil.DateToString(nowDate, DateStyle.YYYY_MM_DD_HH_MM_SS));
		} else if (queryType == 2) {// 昨天
			paramMap.put(start_time, DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD) + startTimeSuffix);
			paramMap.put(end_time, DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD) + endTimeSuffix);
		} else if (queryType == 3) {// 前七天
			Date date = DateUtil.addDay(nowDate, -7);
			paramMap.put(start_time, DateUtil.DateToString(date, DateStyle.YYYY_MM_DD) + startTimeSuffix);
			paramMap.put(end_time, DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD) + endTimeSuffix);
		} else if (queryType == 4) {// 前三十天
			Date date = DateUtil.addDay(nowDate, -30);
			paramMap.put(start_time, DateUtil.DateToString(date, DateStyle.YYYY_MM_DD) + startTimeSuffix);
			paramMap.put(end_time, DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD) + endTimeSuffix);
		} else if (queryType == 5) {// 自定义时间
			String startTime = map.get("startTime").toString();
			String endTime = map.get("endTime").toString();
			paramMap.put(start_time, startTime + startTimeSuffix);
			paramMap.put(end_time, endTime + endTimeSuffix);
		}
		return paramMap;
	}
}
