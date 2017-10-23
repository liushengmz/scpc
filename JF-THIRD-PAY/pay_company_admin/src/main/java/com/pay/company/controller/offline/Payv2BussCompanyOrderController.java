package com.pay.company.controller.offline;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.ExportExcel.CsvWriter;
import com.pay.company.controller.admin.BaseManagerController;

@Controller
@RequestMapping("/offline/bussCompanyOrder/*")
public class Payv2BussCompanyOrderController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper>{

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
	
	/**
	 * 订单列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("orderList")
	@ResponseBody
	public Map<String,Object> orderList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		/*Map<String, Object> commonVali = commonVali(map);
		if(commonVali!=null){
			return commonVali;
		}*/
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"curPage","pageData","dateType"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		Map<String, Object> paramMap = commonTimeParam(map);
		paramMap.put("orderType", admin.getCurrentUserStatus());
		paramMap.put("companyId", admin.getId());
		if(map.containsKey("appId") && StringUtils.isNotBlank(map.get("appId").toString())){
			Long appId = NumberUtils.createLong(map.get("appId").toString());
			paramMap.put("appId",appId);
		}
		if(map.containsKey("orderNum") && StringUtils.isNotBlank(map.get("orderNum").toString())){
			paramMap.put("orderNum",map.get("orderNum").toString());
		}
		if(map.containsKey("payWayTypeId") && StringUtils.isNotBlank(map.get("payWayTypeId").toString())){
			Long payWayId = NumberUtils.createLong(map.get("payWayTypeId").toString());
			paramMap.put("payWayId",payWayId);
		}
		if(map.containsKey("payStatusType") && StringUtils.isNotBlank(map.get("payStatusType").toString())){
			Integer payStatus = NumberUtils.createInteger(map.get("payStatusType").toString());
			paramMap.put("payStatus",payStatus);
		}
		if(map.containsKey("pageData") && map.containsKey("curPage") && map.get("pageData") != null && map.get("curPage") != null){
			paramMap.put("pageData", NumberUtils.createInteger(map.get("pageData").toString()));
			paramMap.put("curPage", NumberUtils.createInteger(map.get("curPage").toString()));
		}
		try {
			int count = payv2PayOrderService.getCount(paramMap);
			PageHelper pageHelper = new PageHelper(count, paramMap);
			List<Payv2PayOrder> query = payv2PayOrderService.selectDetailList(pageHelper.getMap());
			PageObject<Payv2PayOrder> pageObject = pageHelper.getPageObject();
			pageObject.setDataList(query);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, pageObject);
		} catch (Exception e) {
			logger.error("获取线下订单列表异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 订单详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("orderDetail")
	@ResponseBody
	public Map<String,Object> orderDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"orderId"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		try {
			String orderId = map.get("orderId").toString();
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("id", orderId);
			List<Payv2PayOrder> detail = payv2PayOrderService.selectDetailList(paramMap);
			if(detail!=null && detail.size()==0){
				//支付状态 交易时间 商户订单交易号 所属应用 商品名称
				//订单金额 商家优惠 实收金额 退款金额 手续费 到帐金额
				Payv2PayOrder payv2PayOrder = detail.get(0);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, payv2PayOrder);
			}else{
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不合法或者数据不存在");
			}
		} catch (Exception e) {
			logger.error("线下获取订单详情异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 获取交易渠道
	 * @param map
	 * @param request
	 */
	@RequestMapping("getPayWayList")
	@ResponseBody
	public Map<String,Object> getPayWayList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		Payv2BussCompany admin = getAdmin();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		try {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("parentId", admin.getId());
			List<Map<String,Object>> maps = payv2BussSupportPayWayService.queryPayWayIdAndNameByCompanyId(admin.getId());
			//List<Payv2BussSupportPayWay> query = payv2BussSupportPayWayService.query(paramMap);
			//List<Payv2PayWay> query = payv2PayWayService.query(null);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, maps);
		} catch (Exception e) {
			logger.error("", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
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
	@RequestMapping("/exportExcelOrder")
	public Map<String, Object> exportExcelOrder(
			@RequestParam Map<String, Object> map, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response) {
		PageObject<Payv2PayOrder> pageObject;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return returnMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		OutputStream out = null;
        String displayColNames = null;  
        String matchColNames = null; 
        String fileName = null;  
        String content = "";  
		try {
			// 搜索
			map.put("curPage", 1);
			map.put("pageData", 999999999);
			//获取当前用户登录状态 然后下载线上还是线下的
			Integer currentUserStatus = admin.getCurrentUserStatus();//1线上 2线下
			map.put("orderType", currentUserStatus);//线下
			validateTimeType(map);
			pageObject = payv2PayOrderService.getPayOrderPageByObject(map);
			// 获取LIST集合
			List<Map<String, Object>> dataList = fillData(pageObject.getDataList());
	        // 完成数据csv文件的封装  
	        displayColNames = "交易日期,时间,支付集订单号,支付方式,商品名称,商户内部订单号,商户订单号,收款通道订单号,交易类型," +
	        		"交易状态,付款账号,货币类型,交易金额,商户优惠,支付集优惠,收款通道优惠,消费者实付金额," +
	        		"费率%,手续费,实收金额,结算金额,应用号,商户应用号,应用名称"; 
	        matchColNames = "payTime,createTime,orderNum,wayName,goodsName,merchantOrderSeftNum,merchantOrderNum,payWayOrderNum,tradeType," +
	        		"payStatus,payUserName,currencyType,payMoney,merchantDiscount,discount,payWayDiscount,customerPayMoney," +
	        		"rate,counterFee,actuallyPayMoney,settlementMoney,appNumber,merchantName,appName";  
	        fileName = "订单管理列表";  
	        content = fillTheme(getAdmin(),map);
	        content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
	        content += "#------------------交易明细列表结束------------------#\r\n";
	        CsvWriter.exportCsv(fileName, content, response);  
			returnMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,
						null);// 201 标示没有查询到数据
		} catch (Exception e) {
			logger.error("导出订单管理列表.csv错误", e);
			e.printStackTrace();
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
	 * 数据封装
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillData(List<Payv2PayOrder> list){
/*        displayColNames = "交易日期,时间,支付集订单号,支付方式,商品名称,商户内部订单号,商户订单号,收款通道订单号,交易类型," +
        		"交易状态,付款账号,货币类型,交易金额,商户优惠,支付集优惠,收款通道优惠,消费者实付金额," +
        		"费率%,手续费,实收金额,结算金额,应用号,商户应用号,应用名称"; 
        matchColNames = "payTime,createTime,orderNum,payType,goodsName,merchantOrderSeftNum,merchantOrderNum,payWayOrderNum,tradeType," +
        		"payStatus,payUserName,currencyType,payMoney,merchantDiscount,discount,payWayDiscount,customerPayMoney," +
        		"rate,counterFee,actuallyPayMoney,settlementMoney,appNumber,merchantName,appName";  */
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();  
		for(Payv2PayOrder order : list){
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("payTime", order.getPayTime() == null ? "" : 
				DateUtil.DateToString(order.getPayTime(), "yyyy-MM-dd HH:mm:ss") + "\t");//交易日期
			orderMap.put("createTime", order.getCreateTime() == null ? "" : 
				DateUtil.DateToString(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "\t");//时间
			orderMap.put("orderNum", order.getOrderNum() == null ? "" : order.getOrderNum() + "\t");//支付集订单号
			orderMap.put("wayName", order.getWayName() == null ? "" : order.getWayName());//支付方式
			orderMap.put("goodsName", order.getGoodsName() == null ? "" : order.getGoodsName());
			orderMap.put("merchantOrderSeftNum","");//商户内部订单号
			orderMap.put("merchantOrderNum", order.getMerchantOrderNum() == null ? "" : order.getMerchantOrderNum() + "\t");//商户订单号
			orderMap.put("payWayOrderNum","" );//商户订单号
			orderMap.put("tradeType","线上" );//交易类型
			if(order.getPayStatus().intValue() == 1){
				orderMap.put("payStatus","支付成功" );//交易状态
			}else if(order.getPayStatus().intValue() == 2){
				orderMap.put("payStatus","支付失败" );//交易状态
			}else if(order.getPayStatus().intValue() == 3){
				orderMap.put("payStatus","未支付" );//交易状态
			}else if(order.getPayStatus().intValue() == 4){
				orderMap.put("payStatus","超时" );//交易状态
			}else {
				orderMap.put("payStatus","扣款成功回调失败" );//交易状态
			}
			orderMap.put("payUserName", order.getPayUserName() == null ? "" : order.getPayUserName() + "\t");//付款账号
			orderMap.put("currencyType", "");//货币类型
			orderMap.put("payMoney", order.getPayMoney() == null ? "" : order.getPayMoney());//交易金额
			orderMap.put("merchantDiscount", order.getMerchantOrderNum() == null ? "" : order.getMerchantOrderNum() + "\t");//商户优惠
			orderMap.put("discount", "");//收款通道优惠
			orderMap.put("payWayDiscount", "");//收款通道优惠
			orderMap.put("customerPayMoney", "");//消费者实付金额
			orderMap.put("rate", "");//比率
			orderMap.put("counterFee", "");//手续费
			orderMap.put("actuallyPayMoney", "");//实收金额
			orderMap.put("settlementMoney", "");//结算金额
			orderMap.put("appNumber", "");//应用号
			orderMap.put("merchantName", "");//商户应用号
			orderMap.put("appName", order.getAppName() == null ? "" : order.getAppName());//应用名称
			dataList.add(orderMap);
		}
		return dataList;
	}
	
	/**
	 * 封装查询日期
	 * @param map
	 */
	private void validateTimeType(Map<String, Object> map){
		if(map.get("dateType") != null){//如果查询天数的订单，则要进行天数判断
			String dateType = map.get("dateType").toString();
			int days = 1;
			if("1".equals(dateType)){
				days = 1;
			}else if("2".equals(dateType)){
				days = 2;
			}else if("3".equals(dateType)){
				days = 7;
			}else if("4".equals(dateType)){
				days = 30;
			}
			
			Date startDate = new Date(new Date().getTime()- (long) days * 24*60*60*1000);
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
			String startTime = matter1.format(startDate);
			Date endDate = new Date();
			String endTime = matter1.format(endDate);
			map.put("payTimeBegin", startTime);
			map.put("payTimeEnd", endTime);
			
		}else if(map.get("payTimeBegin") != null || map.get("payTimeEnd") != null){
			
		}else{
			//如果不输入查询日期类型，默认取一个月的订单数据
			Date startDate = new Date(new Date().getTime()- (long)30*24*60*60*1000);
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
			String startTime = matter1.format(startDate);
			Date endDate = new Date();
			String endTime = matter1.format(endDate);
			map.put("payTimeBegin", startTime);
			map.put("payTimeEnd", endTime);
		}
	}
	
	/**
	 * 标题封装
	 * @param company
	 * @param map
	 * @return
	 */
	public String fillTheme(Payv2BussCompany company,Map<String, Object> map){
		StringBuffer buf = new StringBuffer(); 
		buf.append("支付集交易明细").append(",").append("\r\n");
		buf.append("商户号：").append(",").append(company.getUserName()+"\t").append(",").append("\r\n");
		buf.append("商户名称：").append(",").append(company.getCompanyName()).append(",").append("\r\n");
		buf.append("起始日期：").append(",").append(map.get("payTimeBegin") == null ? "": map.get("payTimeBegin").toString()).append(",")
		   .append("终止日期：").append(",").append(map.get("payTimeEnd") == null ? "" : map.get("payTimeEnd").toString()).append(",").append("\r\n");
		buf.append("#------------------交易明细列表开始------------------#").append(",").append("\r\n");
		return buf.toString();
	}
	public Map<String,Object> commonVali(Map<String,Object> map){
		Map<String,Object> resultMap = null;
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"payTimeType","payWayType","payStatusType"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Integer queryType = NumberUtils.createInteger(map.get("payTimeType").toString());
		if((!(ObjectUtil.checkObject(new String[]{"startTime","endTime"}, map))) && queryType==5){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		return resultMap;
	}
	
	public boolean isToday(Map<String,Object> map){
		Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
		boolean isToday = true;
		if(queryType!=1){
			isToday = false;
		}
		return isToday;
	}
	
	public Map<String,Object> commonTimeParam(Map<String,Object> map){
		Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
		String startTimeSuffix = " 00:00:00";
		String endTimeSuffix = " 23:59:59";
		String start_time = "payTimeBegin";
		String end_time = "payTimeEnd";
		//封装不同查询时间的参数
		Map<String,Object> paramMap = new HashMap<>();
		Date nowDate = new Date();
		Date yestoDate = DateUtil.addDay(nowDate, -1);
		//TODO 前七天和前三十天应该不包含今天
		if(queryType==1){//今天
			paramMap.put(start_time,DateUtil.DateToString(nowDate, DateStyle.YYYY_MM_DD)+startTimeSuffix);
			paramMap.put(end_time,DateUtil.DateToString(nowDate, DateStyle.YYYY_MM_DD_HH_MM_SS));
		}else if(queryType==2){//昨天
			paramMap.put(start_time,DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD)+startTimeSuffix);
			paramMap.put(end_time,DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD)+endTimeSuffix);
		}else if(queryType==3){//前七天
			Date date = DateUtil.addDay(nowDate, -7);
			paramMap.put(start_time,DateUtil.DateToString(date, DateStyle.YYYY_MM_DD)+startTimeSuffix);
			paramMap.put(end_time,DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD)+endTimeSuffix);
		}else if(queryType==4){//前三十天
			Date date = DateUtil.addDay(nowDate, -30);
			paramMap.put(start_time,DateUtil.DateToString(date, DateStyle.YYYY_MM_DD)+startTimeSuffix);
			paramMap.put(end_time,DateUtil.DateToString(yestoDate, DateStyle.YYYY_MM_DD)+endTimeSuffix);
		}else if(queryType==5){//自定义时间
			String startTime = map.get("startTime").toString();
			String endTime = map.get("endTime").toString();
			paramMap.put(startTime,startTime+startTimeSuffix);
			paramMap.put(endTime,endTime+endTimeSuffix);
		}
		return paramMap;
	}
}
