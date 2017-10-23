package com.pay.company.controller.online;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderClear;
import com.pay.business.order.mapper.Payv2PayOrderClearMapper;
import com.pay.business.order.service.Payv2PayOrderClearService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyClear;
import com.pay.business.record.service.Payv2StatisticsDayCompanyClearService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.ExportExcel.CsvWriter;
import com.pay.company.controller.admin.BaseManagerController;

/**
 * @ClassName: Payv2PayOrderClearController
 * @Description:账单管理
 * @author mofan
 * @date 2017年02月10日 16:53:42
 */
@Controller
@RequestMapping("/online/payv2PayOrderClear/*")
public class Payv2PayOrderClearController extends BaseManagerController<Payv2PayOrderClear, Payv2PayOrderClearMapper> {

	private static final Logger logger = Logger.getLogger(Payv2PayOrderClearController.class);

	@Autowired
	private Payv2PayOrderClearService payv2PayOrderClearService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private Payv2StatisticsDayCompanyClearService payv2StatisticsDayCompanyClearService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;

	/**
	 * @Title: getPayv2PayOrderClearList
	 * @Description:获取账单管理列表
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月7日 下午3:12:21
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("getPayv2PayOrderClearList")
	public Map<String, Object> getPayv2PayOrderClearList(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		map.put("type", 1);// 线上
		Payv2BussCompany company = getAdmin();
		map.put("companyId", company.getId());
		PageObject<Payv2StatisticsDayCompanyClear> pageList = null;
		resultMap.put("map", map);
		try {
			pageList = payv2StatisticsDayCompanyClearService.getPageQueryByGroupByTime(map);
			for (Payv2StatisticsDayCompanyClear clear : pageList.getDataList()) {
				if (null != clear.getSuccessOrderMoney() && null != clear.getRefundOrderMoney()) {
					BigDecimal bg = new BigDecimal(clear.getSuccessOrderMoney() - clear.getRefundOrderMoney());
					double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					clear.setActuallyPayMoney(f1);// 实收金额=成功订单总金额-退款
				}
				if (null != clear.getActuallyPayMoney()) {
					double payWayMoney = clear.getPayWayMoney() == null ? 0.00 : clear.getPayWayMoney();
					BigDecimal bg = new BigDecimal(clear.getActuallyPayMoney() - payWayMoney);
					double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					clear.setArrivalMoney(f1);// 到账金额=实收金额-手续费
				}

			}
			resultMap.put("list", pageList);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, resultMap);
		}
		return resultMap;
	}

	/**
	 * 导出
	 * 
	 * @param map
	 * @param workbook
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportExcelOrderClear")
	public Map<String, Object> exportExcelOrderClear(@RequestParam Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) {
		PageObject<Payv2PayOrder> pageObject = null;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		OutputStream out = null;
		String displayColNames = null;
		String matchColNames = null;
		String fileName = null;
		String content = "";
		try {
			// 搜索
			map.put("curPage", 1);
			map.put("pageData", 999999999);
			map.put("merchantType", 1);// 线上
			validateTimeType(map);
			Payv2BussCompany company = getAdmin();
			map.put("companyId", company.getId());
			pageObject = payv2PayOrderService.getPayOrderPageByObject(map);
			map.put("type", 1);// 线上
			map.put("groupType", 1);// 按支付同道分组
			List<Payv2StatisticsDayCompanyClear> pageList = payv2StatisticsDayCompanyClearService.getTimeZoneAggregateCompanyClear(map);
			// 获取LIST集合
			List<Map<String, Object>> dataList = fillData(pageList);
			// 完成数据csv文件的封装
			displayColNames = "收款通道,交易笔数,交易金额,退款笔数,退款金额,商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额";
			matchColNames = "wayName,tradeCount,payMoney,returnCount,returnMoney,merchantDiscount,discount,payWayDiscount,actuallyPayMoney,counterFee,settlementMoney";
			fileName = "对账单管理列表";
			String clearTimeBegin = "";
			String clearTimeEnd = "";
			if (map.get("reqType") != null && "2".equals(map.get("reqType").toString())) {
				clearTimeBegin = map.get("clearTimeBegin") == null ? "" : map.get("clearTimeBegin").toString();
				clearTimeEnd = map.get("clearTimeEnd") == null ? "" : map.get("clearTimeEnd").toString();
				clearTimeBegin = clearTimeBegin.substring(0, clearTimeBegin.lastIndexOf("-")) + "月";
				clearTimeEnd = clearTimeEnd.substring(0, clearTimeEnd.lastIndexOf("-")) + "月";
			} else {
				clearTimeBegin = map.get("clearTimeBegin") == null ? "" : map.get("clearTimeBegin").toString();
				clearTimeEnd = map.get("clearTimeEnd") == null ? "" : map.get("clearTimeEnd").toString();
			}
			content += "起始日期" + "," + (clearTimeBegin + "\t") + "," + "终止日期" + "," + (clearTimeEnd + "\t") + "\r\t";
			content += fillTheme(getAdmin());
			content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
			content += "\r\n";
			content += "应用汇总清单" + "," + "\r\n";
			displayColNames = "应用号,商户应用号,应用名称,交易笔数,交易金额,退款笔数,退款金额," + "商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额,支付宝结算金额,微信结算金额";
			matchColNames = "app_appNum,app_merchantName,app_appName,app_tradeCount,app_payMoney,app_returnCount,app_returnMoney,"
					+ "app_merchantDiscount,app_discount,app_payWayDiscount,app_actuallyPayMoney,app_counterFee,app_settlementMoney,app_alipayMoney,app_weixinMoney";
			map.put("groupType", 2);// 按应用分组
			pageList = payv2StatisticsDayCompanyClearService.getTimeZoneAggregateCompanyClear(map);
			dataList = fillAppData(pageList);
			content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
//			content += fillDetailTheme(getAdmin(), map);
//			displayColNames = "交易日期,时间,支付集订单号,支付方式,商品名称,商户内部订单号,商户订单号,收款通道订单号,交易类型," + "交易状态,付款账号,货币类型,交易金额,商户优惠,支付集优惠,收款通道优惠,消费者实付金额,"
//					+ "费率%,手续费,实收金额,结算金额,应用号,商户应用号,应用名称";
//			matchColNames = "payTime,createTime,orderNum,wayName,goodsName,merchantOrderSeftNum,merchantOrderNum,payWayOrderNum,tradeType,"
//					+ "payStatus,payUserName,currencyType,payMoney,merchantDiscount,discount,payWayDiscount,customerPayMoney,"
//					+ "rate,counterFee,actuallyPayMoney,settlementMoney,appNumber,merchantName,appName";
//			dataList = fillDetailData(pageObject.getDataList());
//			content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
//			content += "#------------------交易明细列表结束------------------#\r\n";
			// 完成数据csv文件的封装
			CsvWriter.exportCsv(fileName, content, response);
			returnMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("导出账单管理列表.csv错误", e);
			e.printStackTrace();
			returnMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);// 201
																				// 标示没有查询到数据
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnMap;
	}

	/**
	 * 支付集交易明细数据封装
	 * 
	 * @param clearMap
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillDetailData(List<Payv2PayOrder> list) {
		/*
		 * displayColNames =
		 * "交易日期,时间,支付集订单号,支付方式,商品名称,商户内部订单号,商户订单号,收款通道订单号,交易类型," +
		 * "交易状态,付款账号,货币类型,交易金额,商户优惠,支付集优惠,收款通道优惠,消费者实付金额," +
		 * "费率%,手续费,实收金额,结算金额,应用号,商户应用号,应用名称"; matchColNames =
		 * "payTime,createTime,orderNum,payType,goodsName,merchantOrderSeftNum,merchantOrderNum,payWayOrderNum,tradeType,"
		 * +
		 * "payStatus,payUserName,currencyType,payMoney,merchantDiscount,discount,payWayDiscount,customerPayMoney,"
		 * +
		 * "rate,counterFee,actuallyPayMoney,settlementMoney,appNumber,merchantName,appName"
		 * ;
		 */
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Payv2PayOrder clear : list) {
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("payTime", clear.getPayTime() == null ? "" : DateUtil.DateToString(clear.getPayTime(), "yyyy-MM-dd HH:mm:ss") + "\t");// 交易日期
			orderMap.put("createTime", clear.getCreateTime() == null ? "" : DateUtil.DateToString(clear.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "\t");// 时间
			orderMap.put("orderNum", clear.getOrderNum() == null ? "" : clear.getOrderNum() + "\t");// 支付集订单号
			orderMap.put("wayName", clear.getWayName() == null ? "" : clear.getWayName());// 支付方式
			orderMap.put("goodsName", clear.getGoodsName() == null ? "" : clear.getGoodsName());
			orderMap.put("merchantOrderSeftNum", "");// 商户内部订单号
			orderMap.put("merchantOrderNum", clear.getMerchantOrderNum() == null ? "" : clear.getMerchantOrderNum() + "\t");// 商户订单号
			orderMap.put("payWayOrderNum", "");// 商户订单号
			orderMap.put("tradeType", "线上");// 交易类型
			if (clear.getPayStatus().intValue() == 1) {// 1支付成功,2支付失败,3未支付,4超时,5.扣款成功回调失败',
				orderMap.put("payStatus", "支付成功");
			} else if (clear.getPayStatus().intValue() == 2) {
				orderMap.put("payStatus", "支付失败");
			} else if (clear.getPayStatus().intValue() == 3) {
				orderMap.put("payStatus", "未支付");
			} else if (clear.getPayStatus().intValue() == 4) {
				orderMap.put("payStatus", "超时");
			} else if (clear.getPayStatus().intValue() == 5) {
				orderMap.put("payStatus", "扣款成功回调失败");
			} else {
				orderMap.put("payStatus", "未知异常");// 交易状态
			}
			orderMap.put("payUserName", clear.getPayUserName() == null ? "" : clear.getPayUserName());// 付款账号
			orderMap.put("currencyType", "");// 货币类型
			orderMap.put("payMoney", clear.getPayMoney() == null ? "" : clear.getPayMoney());// 交易金额
			orderMap.put("merchantDiscount", clear.getDiscountMoney() == null ? "" : clear.getDiscountMoney());// 商户优惠
			orderMap.put("discount", "");// 支付集优惠
			orderMap.put("payWayDiscount", "");// 收款通道优惠
			orderMap.put("customerPayMoney", "");// 消费者实付金额
			orderMap.put("rate", clear.getPayWayRate() == null ? "" : clear.getPayWayRate());// 比率
			orderMap.put("counterFee", clear.getPayWayMoney() == null ? "" : clear.getPayWayMoney());// 手续费

			orderMap.put("actuallyPayMoney", "");// 实收金额 = 订单金额 - 退款
			orderMap.put("settlementMoney", "");// 结算金额 = 实收金额 - 手续费
			orderMap.put("appNumber", "");// 应用号
			orderMap.put("merchantName", "");// 商户应用号
			orderMap.put("appName", clear.getAppName() == null ? "" : clear.getAppName());// 应用名称
			dataList.add(orderMap);
		}

		return dataList;
	}

	/**
	 * 应用汇总清单数据封装
	 * 
	 * @param clearMap
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillAppData(List<Payv2StatisticsDayCompanyClear> list) {
		// displayColNames = "应用号,商户应用号,应用名称,交易笔数,交易金额,退款笔数,退款金额," +
		// "商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额,支付宝结算金额,微信结算金额";
		// matchColNames =
		// "app_appNum,app_merchantName,app_appName,app_tradeCount,app_payMoney,app_returnCount,app_returnMoney,"
		// +
		// "app_merchantDiscount,app_discount,app_payWayDiscount,app_actuallyPayMoney,app_counterFee,app_settlementMoney,app_alipayMoney,app_weixinMoney";
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 根据应用分类
		for (Payv2StatisticsDayCompanyClear clear : list) {
			String app_appNum = "";
			String app_merchantName = "";
			String app_appName = "";
			int app_tradeCount = 0;
			double app_payMoney = 0.00;
			int app_returnCount = 0;
			double app_returnMoney = 0.00;
			double app_merchantDiscount = 0.00;
			double app_discount = 0.00;
			double app_payWayDiscount = 0.00;
			double app_actuallyPayMoney = 0.00;
			double app_counterFee = 0.00;
			double app_settlementMoney = 0.00;
			double app_alipayMoney = 0.00;
			double app_weixinMoney = 0.00;
			double app_payWayMmoney = 0.00;
			Map<String, Object> reqMap = new HashMap<String, Object>();
			int type = clear.getType();
			if (type == 1) {// 1.线上
				reqMap.put("id", clear.getAppId());
				Payv2BussCompanyApp app = payv2BussCompanyAppService.detail(reqMap);
				if (app != null) {
					app_appName = app.getAppName();
					// app_appNum = app.getAppKey();
				}
			} else if (type == 2) {// 2.线下
				reqMap.put("id", clear.getAppId());
				Payv2BussCompanyShop shop = payv2BussCompanyShopService.detail(reqMap);
				if (shop != null) {
					app_appName = shop.getShopName();
					// app_appNum = shop.getShopKey();
				}

			}
			app_payMoney = clear.getSuccessOrderMoney() == null ? 0.00 : clear.getSuccessOrderMoney();
			app_returnMoney = clear.getRefundOrderMoney() == null ? 0.00 : clear.getRefundOrderMoney();
			app_counterFee = clear.getPayWayMoney() == null ? 0.00 : clear.getPayWayMoney();
			app_payWayMmoney = clear.getPayWayMoney() == null ? 0.00 : clear.getPayWayMoney();
			app_tradeCount = clear.getSuccessOrderCount();

			Map<String, Object> clearMap = new HashMap<String, Object>();
			clearMap.put("app_appNum", app_appNum);// 应用号
			clearMap.put("app_merchantName", app_merchantName);// 商户应用号
			clearMap.put("app_appName", app_appName);// 应用名称
			clearMap.put("app_tradeCount", app_tradeCount);// 交易笔数
			clearMap.put("app_payMoney", app_payMoney);// 交易金额
			clearMap.put("app_returnCount", app_returnCount);// 退款笔数
			clearMap.put("app_returnMoney", app_returnMoney);// 退款金额
			clearMap.put("app_merchantDiscount", app_merchantDiscount);// 商户优惠
			clearMap.put("app_discount", app_discount);// 支付集优惠
			clearMap.put("app_payWayDiscount", app_payWayDiscount);// 收款通道优惠
			app_actuallyPayMoney = app_payMoney - app_returnMoney;// 实收金额 = 订单金额
																	// - 退款
			clearMap.put("app_actuallyPayMoney", app_actuallyPayMoney);// 实收金额
			clearMap.put("app_counterFee", app_counterFee);// 费率
			app_settlementMoney = app_actuallyPayMoney - app_payWayMmoney;// 结算金额
																			// =
																			// 实收金额
																			// -
																			// 手续费
			clearMap.put("app_settlementMoney", app_settlementMoney);// 结算金额
			clearMap.put("app_alipayMoney", app_alipayMoney);// 支付宝结算金额
			clearMap.put("app_weixinMoney", app_weixinMoney);// 微信结算金额
			dataList.add(clearMap);
		}

		return dataList;
	}

	/**
	 * 商户交易汇总清单数据封装
	 * 
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillData(List<Payv2StatisticsDayCompanyClear> list) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// displayColNames =
		// "收款通道,交易笔数,交易金额,退款笔数,退款金额,商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额";
		// matchColNames =
		// "wayName,tradeCouont,payMoney,returnCount,returnMoney,merchantDiscount,
		// discount,payWayDiscount,actuallyPayMoney,counterFee,settlementMoney";
		// 根据支付分类
		Map<String, Object> clearMap = null;
		for (Payv2StatisticsDayCompanyClear clear : list) {
			String wayName = "";
			long tradeCount = 0;
			double payMoney = 0;
			long returnCount = 0;
			double returnMoney = 0.00;
			double merchantDiscount = 0;
			double discount = 0.00;
			double payWayDiscount = 0.00;
			double actuallyPayMoney = 0.00;
			double payWayMmoney = 0.00;
			double settlementMoney = 0.00;
			wayName = clear.getWayName() == null ? "" : clear.getWayName();
			payMoney = clear.getSuccessOrderMoney() == null ? 0.00 : clear.getSuccessOrderMoney();
			returnMoney = clear.getRefundOrderMoney() == null ? 0.00 : clear.getRefundOrderMoney();
			payWayMmoney = clear.getPayWayMoney() == null ? 0.00 : clear.getPayWayMoney();
			tradeCount = clear.getSuccessOrderCount();
			clearMap=new HashMap<String, Object>();
			clearMap.put("wayName", wayName);// 收款通道
			clearMap.put("tradeCount", tradeCount);// 交易笔数
			clearMap.put("payMoney", payMoney);// 交易金额
			clearMap.put("returnCount", returnCount);// 退款笔数
			clearMap.put("returnMoney", returnMoney);// 退款金额
			clearMap.put("merchantDiscount", merchantDiscount);// 商户优惠
			clearMap.put("discount", discount);// 支付集优惠
			clearMap.put("payWayDiscount", payWayDiscount);// 收款通道优惠
			actuallyPayMoney = payMoney - returnMoney;// 实收金额 = 订单金额 - 退款
			clearMap.put("actuallyPayMoney", actuallyPayMoney);// 实收金额 = 订单金额 -
																// 退款
			clearMap.put("counterFee", payWayMmoney);// 手续费
			settlementMoney = actuallyPayMoney - payWayMmoney;// 结算金额 = 实收金额 -
																// 手续费
			clearMap.put("settlementMoney", settlementMoney);// 结算金额
			dataList.add(clearMap);
		}

		return dataList;
	}

	/**
	 * 封装查询天数
	 * 
	 * @param map
	 */
	private void validateTimeType(Map<String, Object> map) {
		if (map.get("reqType") != null && (map.get("clearTimeBegin") == null && map.get("clearTimeEnd") == null)) {// 如果查询天数的订单，则要进行天数判断
			String dateType = map.get("type").toString();
			int days = 0;
			if ("1".equals(dateType)) {
				days = 1;
			} else if ("2".equals(dateType)) {
				days = 30;
			}

			Date startDate = new Date(new Date().getTime() - (long) days * 24 * 60 * 60 * 1000);
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
			String startTime = matter1.format(startDate);
			Date endDate = new Date();
			String endTime = matter1.format(endDate);
			map.put("clearTimeBegin", startTime);
			map.put("clearTimeEnd", endTime);

		} else if (map.get("reqType") != null && (map.get("clearTimeBegin") != null || map.get("clearTimeEnd") != null)) {

		} else {
			// 如果不输入查询日期类型，默认取一个月的订单数据
			Date startDate = new Date(new Date().getTime() - (long) 30 * 24 * 60 * 60 * 1000);
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
			String startTime = matter1.format(startDate);
			Date endDate = new Date();
			String endTime = matter1.format(endDate);
			map.put("clearTimeBegin", startTime);
			map.put("clearTimeEnd", endTime);
		}
	}

	/**
	 * 封装标题
	 * 
	 * @param company
	 * @return
	 */
	public String fillTheme(Payv2BussCompany company) {
		StringBuffer buf = new StringBuffer();
		buf.append("对账单编号").append(",").append("SQB" + System.currentTimeMillis()).append("\r\n");
		buf.append("商户号").append(",").append("商户名称").append(",").append("\r\n");
		buf.append(company.getUserName() + "\t").append(",").append(company.getCompanyName() + "\t").append(",").append("\r\n");
		buf.append("商户交易汇总清单").append(",").append("\r\n");
		return buf.toString();
	}

	/**
	 * 封装支付集交易明细标题
	 * 
	 * @param company
	 * @return
	 */
	public String fillDetailTheme(Payv2BussCompany company, Map<String, Object> map) {
		StringBuffer buf = new StringBuffer();
		buf.append("支付集交易明细").append(",").append("SQB" + System.currentTimeMillis()).append("\r\n");
		buf.append("商户号：").append(",").append(company.getUserName() + "\t").append(",").append("\r\n");
		buf.append("商户名称：").append(",").append(company.getCompanyName() + "\t").append(",").append("\r\n");
		buf.append("起始日期：").append(",").append(map.get("clearTimeBegin") == null ? "" : map.get("clearTimeBegin").toString()).append(",").append("终止日期：")
				.append(",").append(map.get("clearTimeEnd") == null ? "" : map.get("clearTimeEnd").toString()).append(",").append("\r\n");
		buf.append("#------------------交易明细列表开始------------------#").append(",").append("\r\n");
		return buf.toString();
	}

}
