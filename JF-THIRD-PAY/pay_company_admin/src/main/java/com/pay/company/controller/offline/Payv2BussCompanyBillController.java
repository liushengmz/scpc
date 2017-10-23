package com.pay.company.controller.offline;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.order.entity.Payv2PayOrderClear;
import com.pay.business.order.service.Payv2PayOrderClearService;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyClear;
import com.pay.business.record.service.Payv2StatisticsDayCompanyClearService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.ExportExcel.CsvWriter;
import com.pay.company.controller.admin.BaseManagerController;

@Controller
@RequestMapping("/offline/bussCompanyBill/*")
public class Payv2BussCompanyBillController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper>{

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;
	
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;
	
	@Autowired
	private Payv2PayOrderClearService payv2OrderClearService;
	
	@Autowired
	private Payv2StatisticsDayCompanyClearService payv2StatisticsDayCompanyClearService;
	
	/**
	 * 账单列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("billList")
	@ResponseBody
	public Map<String,Object> billList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"curPage","pageData","type"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		map.put("companyId",admin.getId());
		//validateTimeType(map);
		Integer type = NumberUtils.createInteger(map.get("type").toString());
		commonDate(map, type);
		map.remove("type");//移除这个key 
		map.put("type", admin.getCurrentUserStatus());
		PageObject<Payv2StatisticsDayCompanyClear> pageList = null;
		try {
			int count = payv2StatisticsDayCompanyClearService.getCount(map);
			PageHelper pageHelper = new PageHelper(count, map);
			List<Payv2StatisticsDayCompanyClear> list = null;
			if(type == 1){//日汇总 对两个日期进行按照日期排序 会得到每天的
				list = payv2StatisticsDayCompanyClearService.getDayAggregateCompanyClear(pageHelper.getMap());
				/*Integer count = payv2StatisticsDayCompanyClearService.getCount(map);
				PageHelper pageHelper = new PageHelper(count, map);*/
				/*PageObject<Payv2StatisticsDayCompanyClear> pagequery = payv2StatisticsDayCompanyClearService.Pagequery(map);
				List<Payv2StatisticsDayCompanyClear> list = pagequery.getDataList();
				pagequery.setDataList(null);
				//List<Payv2StatisticsDayCompanyClear> list = payv2StatisticsDayCompanyClearService.pageQueryByObject(pageHelper.getMap());
				for(Payv2StatisticsDayCompanyClear clear : list){
					Double successOrderMoney = clear.getSuccessOrderMoney();
					Double refundOrderMoney = clear.getRefundOrderMoney();
					if(refundOrderMoney==null){
						refundOrderMoney=0.0;
						clear.setRefundOrderMoney(refundOrderMoney);
					}
					if(successOrderMoney==null){
						successOrderMoney=0.0;
						clear.setSuccessOrderMoney(successOrderMoney);
					}
					clear.setActuallyPayMoney(successOrderMoney - refundOrderMoney);//实收金额=成功订单总金额-退款
					if(clear.getPayWayMoney()==null){
						clear.setPayWayMoney(0.0);
					}
					clear.setArrivalMoney(clear.getActuallyPayMoney() - clear.getPayWayMoney());//到账金额=实收金额-手续费
				}
				//pageList = pageHelper.getPageObject();
				pageList.setDataList(list);*/
			}else{
				//对月汇总 会对两个日期的所有交易进行累加
				list = payv2StatisticsDayCompanyClearService.getMonthAggregateCompanyClear(pageHelper.getMap());
			}
			for(Payv2StatisticsDayCompanyClear clear : list){
				Double successOrderMoney = clear.getSuccessOrderMoney();
				Double refundOrderMoney = clear.getRefundOrderMoney();
				if(refundOrderMoney==null){
					refundOrderMoney=0.0;
					clear.setRefundOrderMoney(refundOrderMoney);
				}
				if(successOrderMoney==null){
					successOrderMoney=0.0;
					clear.setSuccessOrderMoney(successOrderMoney);
				}
				clear.setActuallyPayMoney(successOrderMoney - refundOrderMoney);//实收金额=成功订单总金额-退款
				if(clear.getPayWayMoney()==null){
					clear.setPayWayMoney(0.0);
				}
				clear.setArrivalMoney(clear.getActuallyPayMoney() - clear.getPayWayMoney());//到账金额=实收金额-手续费
			}
			pageList = pageHelper.getPageObject();
			pageList.setDataList(list);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, pageList);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, resultMap);
		}
		return resultMap;
	/*	Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"dateType","startTime"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		Map<String,Object> paramMap = commonParam(map);
		if(paramMap==null){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数数据错误");
		}
		//如果是日汇总那么没有 endTime
		if(map.containsKey("appId") && StringUtils.isNotBlank(map.get("appId").toString())){
			Long appId = NumberUtils.createLong(map.get("appId").toString());
			paramMap.put("appId",appId);
		}
		paramMap.put("companyId", admin.getId());
		//TODO
		List<Map<String,Object>> resultListMap = new ArrayList<>();
		try {
			//查询通道费
			List<Payv2BussAppSupportPayWay> resList = payv2BussAppSupportPayWayService.queryPayWayRate(paramMap);
			Integer dateType = NumberUtils.createInteger(map.get("dateType").toString());
			//查询交易金额
			if(dateType==1){
				Map<String, Object> resultHandler = resultHandler(resList, paramMap);
				if(resultHandler.size()>0){
					resultListMap.add(resultHandler);
				}
			}else{
				String startTime = paramMap.get("startTime").toString();
				String endTime = paramMap.get("endTime").toString();
				Date[] dateArrays = getDateArrays(DateUtil.StringToDate(startTime,DateStyle.YYYY_MM_DD), DateUtil.StringToDate(endTime,DateStyle.YYYY_MM_DD));
				for(Date date : dateArrays){
					String dateToString = DateUtil.DateToString(date, DateStyle.YYYY_MM_DD);
					String[] timeHandler = timeHandler(dateToString,dateToString);
					paramMap.put("startTime", timeHandler[0]);
					paramMap.put("endTime", timeHandler[1]);
					Map<String, Object> resultHandler = resultHandler(resList, paramMap);
					if(resultHandler.size()>0){
						resultListMap.add(resultHandler);
					}
				}
			}
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,resultListMap);
		} catch (Exception e) {
			logger.error("获取线下账单列表异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}*/
		/*List<Long> appIdList = new ArrayList<>();
		if(paramMap.containsKey("appId")){
			appIdList.add(NumberUtils.createLong(paramMap.get("appId").toString()));
		}else{
			//获取该商家有多少个应用
			List<Payv2BussCompanyShop> query = payv2BussCompanyShopService.query(paramMap);
			if(query!=null){
				int size = query.size();
				for(int i=0;i<size;i++){
					Payv2BussCompanyShop payv2BussCompanyShop = query.get(i);
					appIdList.add(payv2BussCompanyShop.getId());
				}
			}
		}
		if(appIdList.size()>0){
			payv2PayOrderRefundService.queryAllByAppIdList(appIdList);
		}else{
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"该商户暂无店铺");
		}*/
		//查询退款
		
		//计算每天的实收金额 退款金额 收入是(实收加退款),到账金额是扣除手续费后的,费率是和商户签订的
		
	}
	
	public void commonDate(Map<String,Object> map,Integer type){
		String st = " 00:00:00";
		String ed = " 23:59:59";
		Date date = new Date();
		if(type==2){
			if(map.containsKey("clearTimeBegin")){
				String clearTimeBegin = map.get("clearTimeBegin").toString();
				map.put("statisticsTimeBegin", clearTimeBegin + st);
			}else{
				map.put("statisticsTimeBegin", DateUtil.DateToString(DateUtil.addDay(date, -30), DateStyle.YYYY_MM_DD) + st);
			}
		}else{
			if(map.containsKey("clearTimeBegin")){
				String clearTimeBegin = map.get("clearTimeBegin").toString();
				map.put("statisticsTimeBegin", clearTimeBegin + st);
			}else{
				map.put("statisticsTimeBegin", DateUtil.DateToString(date, DateStyle.YYYY_MM_DD) + st);
			}
		}
		if(!map.containsKey("clearTimeEnd")){
			map.put("statisticsTimeEnd", DateUtil.DateToString(date, DateStyle.YYYY_MM_DD) + ed);
		}else{
			String clearTimeEnd = map.get("clearTimeEnd").toString();
			map.put("statisticsTimeEnd", clearTimeEnd + ed);
		}
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
	public Map<String, Object> exportExcelOrderClear(
			@RequestParam Map<String, Object> map, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response) {
		PageObject<Payv2PayOrderClear> pageObject;
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
			map.put("merchantType", 2);//线下
			validateTimeType(map);
			pageObject = payv2OrderClearService.payv2PayOrderClearList(map);
			// 获取LIST集合
			List<Map<String, Object>> dataList = fillData(pageObject.getDataList());
	        // 完成数据csv文件的封装  
	        displayColNames = "收款通道,交易笔数,交易金额,退款笔数,退款金额,商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额"; 
	        matchColNames = "wayName,tradeCount,payMoney,returnCount,returnMoney,merchantDiscount,discount,payWayDiscount,actuallyPayMoney,counterFee,settlementMoney";  
	        fileName = "对账单管理列表";  
	        content += "起始日期" +","+ (map.get("clearTimeBegin") == null ? "" : map.get("clearTimeBegin").toString() + "\t") + ","+
	        		   "终止日期" + "," + (map.get("clearTimeEnd") == null ? "" : map.get("clearTimeEnd").toString() + "\t") + "\r\t";
	        content += fillTheme(getAdmin());
	        content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
	        content += "\r\n";
	        content += "应用汇总清单" + "," + "\r\n";
	        displayColNames = "应用号,商户应用号,应用名称,交易笔数,交易金额,退款笔数,退款金额," +
	        		"商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额,支付宝结算金额,微信结算金额"; 
	        matchColNames = "app_appNum,app_merchantName,app_appName,app_tradeCount,app_payMoney,app_returnCount,app_returnMoney," +
	        		"app_merchantDiscount,app_discount,app_payWayDiscount,app_actuallyPayMoney,app_counterFee,app_settlementMoney,app_alipayMoney,app_weixinMoney";  
	        dataList = fillAppData(pageObject.getDataList());
	        content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
	        content += fillDetailTheme(getAdmin(),map);
	        displayColNames = "交易日期,时间,支付集订单号,支付方式,商品名称,商户内部订单号,商户订单号,收款通道订单号,交易类型," +
	        		"交易状态,付款账号,货币类型,交易金额,商户优惠,支付集优惠,收款通道优惠,消费者实付金额," +
	        		"费率%,手续费,实收金额,结算金额,应用号,商户应用号,应用名称"; 
	        matchColNames = "payTime,createTime,orderNum,wayName,goodsName,merchantOrderSeftNum,merchantOrderNum,payWayOrderNum,tradeType," +
	        		"payStatus,payUserName,currencyType,payMoney,merchantDiscount,discount,payWayDiscount,customerPayMoney," +
	        		"rate,counterFee,actuallyPayMoney,settlementMoney,appNumber,merchantName,appName";  
	        dataList = fillDetailData(pageObject.getDataList());
	        content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
	        content += "#------------------交易明细列表结束------------------#\r\n";
	        // 完成数据csv文件的封装  
	        CsvWriter.exportCsv(fileName, content, response);  
			returnMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,
						null);
		} catch (Exception e) {
			logger.error("导出账单管理列表.csv错误", e);
			e.printStackTrace();
			returnMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,
					null);// 201 标示没有查询到数据
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
	 * @param clearMap
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillDetailData(List<Payv2PayOrderClear> list){
		/*        displayColNames = "交易日期,时间,支付集订单号,支付方式,商品名称,商户内部订单号,商户订单号,收款通道订单号,交易类型," +
		"交易状态,付款账号,货币类型,交易金额,商户优惠,支付集优惠,收款通道优惠,消费者实付金额," +
		"费率%,手续费,实收金额,结算金额,应用号,商户应用号,应用名称"; 
		matchColNames = "payTime,createTime,orderNum,payType,goodsName,merchantOrderSeftNum,merchantOrderNum,payWayOrderNum,tradeType," +
		"payStatus,payUserName,currencyType,payMoney,merchantDiscount,discount,payWayDiscount,customerPayMoney," +
		"rate,counterFee,actuallyPayMoney,settlementMoney,appNumber,merchantName,appName";  */
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(); 
		for(Payv2PayOrderClear clear : list){
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("payTime", clear.getClearTime() == null ? "" : 
				DateUtil.DateToString(clear.getClearTime(), "yyyy-MM-dd HH:mm:ss") + "\t");//交易日期
			orderMap.put("createTime", clear.getCreateTime() == null ? "" : 
				DateUtil.DateToString(clear.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "\t");//时间
			orderMap.put("orderNum", clear.getOrderNum() == null ? "" : clear.getOrderNum() + "\t");//支付集订单号
			orderMap.put("wayName", clear.getWayName() == null ? "" : clear.getWayName());//支付方式
			orderMap.put("goodsName", "");
			orderMap.put("merchantOrderSeftNum","");//商户内部订单号
			orderMap.put("merchantOrderNum", clear.getMerchantOrderNum() == null ? "" : clear.getMerchantOrderNum() + "\t");//商户订单号
			orderMap.put("payWayOrderNum","" );//商户订单号
			orderMap.put("tradeType","线上" );//交易类型
			if(clear.getStatus().intValue() == 1){
				orderMap.put("payStatus","正常" );//交易状态
			}else if(clear.getStatus().intValue() == 2){
				orderMap.put("payStatus","订单异常" );//交易状态
			}else if(clear.getStatus().intValue() == 3){
				orderMap.put("payStatus","正常回调失败" );//交易状态
			}else {
				orderMap.put("payStatus","未知异常" );//交易状态
			}
			orderMap.put("payUserName", "");//付款账号
			orderMap.put("currencyType", "");//货币类型
			orderMap.put("payMoney", "");//交易金额
			orderMap.put("merchantDiscount", clear.getMerchantOrderNum() == null ? "" : clear.getMerchantOrderNum() + "\t");//商户优惠
			orderMap.put("discount", "");//收款通道优惠
			orderMap.put("payWayDiscount", "");//收款通道优惠
			orderMap.put("customerPayMoney", "");//消费者实付金额
			orderMap.put("rate", "");//比率
			orderMap.put("counterFee", "");//手续费
			orderMap.put("actuallyPayMoney", "");//实收金额
			orderMap.put("settlementMoney", "");//结算金额
			orderMap.put("appNumber", "");//应用号
			orderMap.put("merchantName", "");//商户应用号
			orderMap.put("appName", clear.getAppName() == null ? "" : clear.getAppName());//应用名称
			dataList.add(orderMap);
		}
		
		return dataList;
	}
	
	/**
	 * 应用汇总清单数据封装
	 * @param clearMap
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillAppData(List<Payv2PayOrderClear> list){
		//displayColNames = "应用号,商户应用号,应用名称,交易笔数,交易金额,退款笔数,退款金额," +
	    //  "商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额,支付宝结算金额,微信结算金额"; 
	    //  matchColNames = "app_appNum,app_merchantName,app_appName,app_tradeCount,app_payMoney,app_returnCount,app_returnMoney," +
	    //  "app_merchantDiscount,app_discount,app_payWayDiscount,app_actuallyPayMoney,app_counterFee,app_settlementMoney,app_alipayMoney,app_weixinMoney";  
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(); 
		Set<Long> paywayIds = new HashSet<Long>();
		Map<Long,List<Payv2PayOrderClear>> paywayMap = new HashMap<Long, List<Payv2PayOrderClear>>();
		//根据应用分类
		for(Payv2PayOrderClear clear : list){
			if(!paywayIds.contains(clear.getAppId())){
				List<Payv2PayOrderClear> list2 = new ArrayList<Payv2PayOrderClear>();
				list2.add(clear);
				paywayMap.put(clear.getAppId(), list2);
				paywayIds.add(clear.getAppId());
			}else{
				paywayMap.get(clear.getAppId()).add(clear);
			}
		}
		//分组后遍历
		for (Long appId : paywayIds) {  
			List<Payv2PayOrderClear> tempList = paywayMap.get(appId);
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
			for(Payv2PayOrderClear clear : tempList){
				app_appName = clear.getAppName();

				int type = clear.getType();
				if(type == 1){//交易
					app_tradeCount++;
					app_payMoney += clear.getOrderMoney() == null ? 0.00 : clear.getOrderMoney();
				}else if(type == 2){//退款
					app_returnCount++;
					app_returnMoney += clear.getOrderMoney() == null ? 0.00 : clear.getOrderMoney();
				}
				app_discount += clear.getDiscountMoney() == null ? 0.00 : clear.getDiscountMoney();
			}
			
			Map<String, Object> clearMap = new HashMap<String, Object>();
			clearMap.put("app_appNum", app_appNum);//应用号
			clearMap.put("app_merchantName", app_merchantName);//商户应用号
			clearMap.put("app_appName", app_appName);//应用名称
			clearMap.put("app_tradeCount", app_tradeCount);//交易笔数
			clearMap.put("app_payMoney", app_payMoney);//交易金额
			clearMap.put("app_returnCount", app_returnCount);//退款笔数
			clearMap.put("app_returnMoney", app_returnMoney);//退款金额
			clearMap.put("app_merchantDiscount", app_merchantDiscount);//商户优惠
			clearMap.put("app_discount", app_discount);//支付集优惠
			clearMap.put("app_payWayDiscount", app_payWayDiscount);//收款通道优惠
			clearMap.put("app_actuallyPayMoney", app_actuallyPayMoney);//实收金额
			clearMap.put("app_counterFee", app_counterFee);//费率
			clearMap.put("app_settlementMoney", app_settlementMoney);//结算金额
			clearMap.put("app_alipayMoney", app_alipayMoney);//支付宝结算金额
			clearMap.put("app_weixinMoney", app_weixinMoney);//微信结算金额
			dataList.add(clearMap);
		}
		return dataList;
	}
	
	/**
	 * 商户交易汇总清单数据封装
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillData(List<Payv2PayOrderClear> list){
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(); 
        //displayColNames = "收款通道,交易笔数,交易金额,退款笔数,退款金额,商户优惠,支付集优惠,收款通道优惠,实收金额,手续费,结算金额"; 
        //matchColNames = "wayName,tradeCouont,payMoney,returnCount,returnMoney,merchantDiscount,
		//					discount,payWayDiscount,actuallyPayMoney,counterFee,settlementMoney"; 
		Set<Long> paywayIds = new HashSet<Long>();
		Map<Long,List<Payv2PayOrderClear>> paywayMap = new HashMap<Long, List<Payv2PayOrderClear>>();
		//根据支付分类
		for(Payv2PayOrderClear clear : list){
			if(!paywayIds.contains(clear.getPayWayId())){
				List<Payv2PayOrderClear> list2 = new ArrayList<Payv2PayOrderClear>();
				list2.add(clear);
				paywayMap.put(clear.getPayWayId(), list2);
				paywayIds.add(clear.getPayWayId());
			}else{
				paywayMap.get(clear.getPayWayId()).add(clear);
			}
		}
		//分组后遍历
		for (Long payWayId : paywayIds) {  
			List<Payv2PayOrderClear> tempList = paywayMap.get(payWayId);
			String wayName = "";
			long tradeCount = 0;
			long payMoney = 0;
			long returnCount = 0;
			double returnMoney = 0.00;
			double merchantDiscount = 0;
			double discount = 0.00;
			double payWayDiscount = 0.00;
			double actuallyPayMoney = 0.00;
			double counterFee = 0.00;
			double settlementMoney = 0.00;
			for(Payv2PayOrderClear clear : tempList){
				wayName = clear.getWayName() == null ? "" : clear.getWayName();
				int type = clear.getType();
				if(type == 1){//交易
					tradeCount++;
					payMoney += clear.getOrderMoney() == null ? 0.00 : clear.getOrderMoney();
				}else if(type == 2){//退款
					returnCount++;
					returnMoney += clear.getOrderMoney() == null ? 0.00 : clear.getOrderMoney();
				}
				discount += clear.getDiscountMoney() == null ? 0.00 : clear.getDiscountMoney();
			}
			Map<String, Object> clearMap = new HashMap<String, Object>();
			clearMap.put("wayName", wayName);//收款通道
			clearMap.put("tradeCount", tradeCount);//交易笔数
			clearMap.put("payMoney", payMoney);//交易金额
			clearMap.put("returnCount", returnCount);//退款笔数
			clearMap.put("returnMoney", returnMoney);//退款金额
			clearMap.put("merchantDiscount", merchantDiscount);//商户优惠
			clearMap.put("discount", discount);//支付集优惠
			clearMap.put("payWayDiscount", payWayDiscount);//收款通道优惠
			clearMap.put("actuallyPayMoney", actuallyPayMoney);//实收金额
			clearMap.put("counterFee", counterFee);//手续费
			clearMap.put("settlementMoney", settlementMoney);//结算金额
			dataList.add(clearMap);
		} 
		return dataList;
	}
	
	/**
	 * 封装查询天数
	 * @param map
	 */
	private void validateTimeType(Map<String, Object> map){
		if(map.get("type") != null){//如果查询天数的订单，则要进行天数判断
			String dateType = map.get("type").toString();
			int days = 0;
			if("1".equals(dateType)){
				days = 1;
			}else if("2".equals(dateType)){
				days = 30;
			}
			
			Date startDate = new Date(new Date().getTime()- (long) days * 24*60*60*1000);
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
			String startTime = matter1.format(startDate);
			Date endDate = new Date();
			String endTime = matter1.format(endDate);
			map.put("clearTimeBegin", startTime);
			map.put("clearTimeEnd", endTime);
			
		}else if(map.get("clearTimeBegin") != null || map.get("clearTimeEnd") != null){
			
		}else{
			//如果不输入查询日期类型，默认取一个月的订单数据
			Date startDate = new Date(new Date().getTime()- (long)30*24*60*60*1000);
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
	 * @param company
	 * @return
	 */
	public String fillTheme(Payv2BussCompany company){
		StringBuffer buf = new StringBuffer(); 
		buf.append("对账单编号").append(",").append("SQB"+System.currentTimeMillis()).append("\r\n");
		buf.append("商户号").append(",").append("商户名称").append(",").append("\r\n");
		buf.append(company.getUserName()+"\t").append(",").append(company.getCompanyName()+"\t").append(",").append("\r\n");
		buf.append("商户交易汇总清单").append(",").append("\r\n");
		return buf.toString();
	}
	
	/**
	 * 封装支付集交易明细标题
	 * @param company
	 * @return
	 */
	public String fillDetailTheme(Payv2BussCompany company,Map<String, Object> map){
		StringBuffer buf = new StringBuffer(); 
		buf.append("支付集交易明细").append(",").append("SQB"+System.currentTimeMillis()).append("\r\n");
		buf.append("商户号：").append(",").append(company.getUserName()+"\t").append(",").append("\r\n");
		buf.append("商户名称：").append(",").append(company.getCompanyName()+"\t").append(",").append("\r\n");
		buf.append("起始日期：").append(",").append(map.get("clearTimeBegin") == null ? "": map.get("clearTimeBegin").toString()).append(",")
		   .append("终止日期：").append(",").append(map.get("clearTimeEnd") == null ? "" : map.get("clearTimeEnd").toString()).append(",").append("\r\n");
		buf.append("#------------------交易明细列表开始------------------#").append(",").append("\r\n");
		return buf.toString();
	}
	
	public Map<String,Object> resultHandler(List<Payv2BussAppSupportPayWay> resList,Map<String,Object> paramMap){
		List<Map<String, Object>> sumMoney = payv2PayOrderService.sumMoney(paramMap);//返回多个代表有多种支付方式\
		Map<String,Object> ms = new HashMap<>();
		if(sumMoney!=null && sumMoney.size()==1){
			Map<String, Object> map2 = sumMoney.get(0);
			Double allPayDiscountMoney = NumberUtils.createDouble(map2.get("allPayDiscountMoney").toString());
			Double allRefundMoney = NumberUtils.createDouble(map2.get("allRefundMoney").toString());
			Double payWayRate = 0.00;
			for(int i=0;i<resList.size();i++){
				Double payWayRate2 = resList.get(i).getPayWayRate();
				payWayRate = payWayRate2 + payWayRate;
			}
			ms.put("allPayDiscountMoney", allPayDiscountMoney);
			ms.put("allRefundMoney", allRefundMoney);
			ms.put("incomeMoney", allPayDiscountMoney+allRefundMoney);
			Double shouxufei = 0.00;
			if(payWayRate>0){
				//有通道费
				ms.put("payWayRate", payWayRate);
				shouxufei = allPayDiscountMoney * payWayRate;
				ms.put("arrivalMoney", shouxufei);
			}else{
				//没有通道费,到账金额就是支付金额
				ms.put("payWayRate", payWayRate);
				ms.put("arrivalMoney", shouxufei);
			}
		}
		return ms;
	}
	public String[] timeHandler(String startTime,String endTime){
		String[] arr = new String[2];
		arr[1] = startTime + " 00:00:00";
		arr[2] = endTime + " 23:59:59";
		return arr;
	}
	public Map<String,Object> commonParam(Map<String,Object> map){
		Integer dateType = NumberUtils.createInteger(map.get("dateType").toString());
		String startTime = map.get("startTime").toString();
		String startTimeSuffix = " 00:00:00";
		String endTimeSuffix = " 23:59:59";
		String startTimeValue = startTime + startTimeSuffix;
		String endTimeValue = null;
		if(dateType!=1 || dateType!=2){
			//今日
			return null;
		}else if(dateType==2){
			//月汇总
			String endTime = map.get("endTime").toString();
			endTimeValue = endTime+endTimeSuffix;
		}else{
			endTimeValue = startTime+endTimeSuffix;
		}
		map.clear();
		map.put("startTime",startTimeValue);
		map.put("endTime",endTimeValue);
		return map;
	}
	
	public static Date[] getDateArrays(Date start,Date end){
		return getDateArrays(start,end,Calendar.DAY_OF_YEAR);
	}
	/**
	 * 获取两个日期间隔的每一天(包括起始日期和结束日期)
	 * @param start "2017-03-10"
	 * @param end  "2017-03-20"
	 * @param calendarType Calendar.DAY_OF_YEAR
	 * @return
	 */
	public static Date[] getDateArrays(Date start,Date end,int calendarType){  
        ArrayList<Date> ret = new ArrayList<Date>();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(start);  
        Date tmpDate = calendar.getTime();  
        long endTime = end.getTime();  
        while(tmpDate.before(end)||tmpDate.getTime() == endTime){  
            ret.add(calendar.getTime());  
            calendar.add(calendarType, 1);  
            tmpDate = calendar.getTime();  
        }         
        Date[] dates = new Date[ret.size()];  
        return ret.toArray(dates);        
    }  
}
