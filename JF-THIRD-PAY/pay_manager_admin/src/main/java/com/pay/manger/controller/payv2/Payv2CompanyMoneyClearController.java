package com.pay.manger.controller.payv2;


import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.order.entity.Payv2PayOrderClear;
import com.pay.business.order.service.Payv2PayOrderClearService;
import com.pay.business.record.entity.Payv2BussWayDetail;
import com.pay.business.record.entity.Payv2DayCompanyClear;
import com.pay.business.record.mapper.Payv2DayCompanyClearMapper;
import com.pay.business.record.service.Payv2BussWayDetailService;
import com.pay.business.record.service.Payv2DayCompanyClearService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.ExportExcel.TestExportExcel2;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * @ClassName Payv2CompanyMoneyClearController
 * @Description:商户财务清算管理
 * @author zhangheng
 * @date 
 *
 */
@RequestMapping("/Payv2CompanyMoneyClear/*")
@Controller
public class Payv2CompanyMoneyClearController extends BaseManagerController<Payv2DayCompanyClear, Payv2DayCompanyClearMapper> {

	@Autowired
	private Payv2ChannelService payv2ChannelService;//渠道商服务
	
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;//商户服务
	
	@Autowired
	private Payv2DayCompanyClearService payv2DayCompanyClearService; //商户每天账单结算服务
	
	@Autowired
	private Payv2BussWayDetailService payv2BussWayDetailService;
	
	@Autowired
	private Payv2PayOrderClearService payv2PayOrderClearService; //对账单服务
	
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService; //商户的App服务
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Payv2CompanyMoneyClearController.class);
	
	
	/**
	 * 商户日账单管理
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("dayMoneyManager")
    public ModelAndView dayMoneyManager(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/companyMoneyClear/dayMoneyManager");
		
		//获取渠道商列表
    	Payv2Channel payv2Channel=new Payv2Channel();
    	payv2Channel.setIsDelete(2);
    	List<Payv2Channel>	payv2ChannelList=payv2ChannelService.selectByObject(payv2Channel);
    	mv.addObject("payv2ChannelList", payv2ChannelList);
    	return mv;
    }
	
	/**
	 * 商户月账单管理
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("mouthMoneyManager")
    public ModelAndView mouthMoneyManager(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/companyMoneyClear/mouthMoneyManager");
		
		//获取渠道商列表
    	Payv2Channel payv2Channel=new Payv2Channel();
    	payv2Channel.setIsDelete(2);
    	List<Payv2Channel>	payv2ChannelList=payv2ChannelService.selectByObject(payv2Channel);
    	mv.addObject("payv2ChannelList", payv2ChannelList);
    	return mv;
    }
	
	/**
	 * 根据渠道商id查询对应的商户
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("showCompany")
    public Map<String,Object> showCompany(@RequestParam Long channelId ){
		Map<String,Object> resultMap = new HashMap<>();
    	try {
    		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
    		payv2BussCompany.setIsDelete(2);
    		payv2BussCompany.setChannelId(channelId);
    		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectByObject(payv2BussCompany);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, companyList);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
	
	/**
	 * 商户日账单分页列表
	 * @param map
	 * @return resultMap
	 */
	@ResponseBody
    @RequestMapping("selectByDay")
    public Map<String,Object> selectByDay(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();	
		try {
			PageObject<Payv2DayCompanyClear> pageObject = payv2DayCompanyClearService.dayClearList(map);
			resultMap.put("clearList", pageObject);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
    }
	
	/**
	 * 商户日账单数值汇总：累计手续费、累计手续费利润、累计已支付金额（元）、累计已退款金额（元）、累计支付净额（元）、累计结算金额（元）
	 * @param map
	 * @return resultMap
	 */
	@ResponseBody
    @RequestMapping("selectByDayAllMoney")
    public Map<String,Object> selectByDayAllMoney(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();	
		Payv2DayCompanyClear allClear = new Payv2DayCompanyClear();
		try {
			allClear = payv2DayCompanyClearService.dayClearAllMoney(map);
			resultMap.put("allClear", allClear);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
    }
	
	/**
	 * 商户日账单结算
	 * @param map
	 * @return resultMap
	 */
	@ResponseBody
    @RequestMapping("moneyClear")
    public Map<String,Object> moneyClear(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();
		String companyCheckId[] = map.get("companyCheckId").toString().split(",");

		try {
			payv2DayCompanyClearService.moneyClear(companyCheckId);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}		
		return resultMap;		
    }
	
	/**
	 * 月账单
	 * @param map
	 * @return
	 */
	@ResponseBody	
    @RequestMapping("selectByMouth")
    public Map<String, Object> selectByMouth(@RequestParam Map<String, Object> map){		
		Map<String,Object> resultMap = new HashMap<>();	
		try {
			Map<Long, Payv2DayCompanyClear> dataMap=payv2DayCompanyClearService.mouthClearList(map);
			resultMap.put("dataMap", dataMap);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
    }
	
	/**
	 * 商户月账单数值汇总：累计手续费、累计手续费利润、累计已支付金额（元）、累计已退款金额（元）、累计支付净额（元）、累计结算金额（元）
	 * 
	 * @param map
	 * @return resultMap
	 */
	@ResponseBody
    @RequestMapping("selectByMouthAllMoney")
    public Map<String,Object> selectByMouthAllMoney(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();	
		Payv2DayCompanyClear allClear = new Payv2DayCompanyClear();
		try {
			allClear = payv2DayCompanyClearService.dayClearAllMoney(map);
			resultMap.put("allClear", allClear);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
    }
	
	
	/**
	 * 生成日账单
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("makeBill")
    public Map<String,Object> makeBill(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();
		try{
			resultMap = billFunc(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, resultMap);
		}
		return resultMap;
    }
	
	/**
	 * 批量生成日账单
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("makeBills")
    public Map<String,Object> makeBills(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();
		try{
			//　根据传来的搜索条件，搜索账单集合
			map.put("status", 3);
			map.put("curPage", 1);
			map.put("pageData", 99999);
			PageObject<Payv2DayCompanyClear> pageObject = payv2DayCompanyClearService.dayClearList(map);
			List<Payv2DayCompanyClear> dayClearList = pageObject.getDataList();
			
			// 将账单集合的的账单时间，商户id，账单编号取出，生成该账单
			int noBill = 0;
			if(dayClearList.size() == 0){
				noBill = -1;
			}else{
				for (Payv2DayCompanyClear payv2DayCompanyClear : dayClearList) {
					Map<String,Object> paramMap = new HashMap<>();
					paramMap.put("clearTime", DateUtil.DateToString(payv2DayCompanyClear.getClearTime(), DateStyle.YYYY_MM_DD));
					paramMap.put("companyId", payv2DayCompanyClear.getCompanyId());
					paramMap.put("companyCheckId", payv2DayCompanyClear.getCompanyCheckId());
					resultMap = billFunc(paramMap);					
					if("2".equals(resultMap.get("status").toString())){
						noBill++;
					}
				}
			}
			resultMap.put("noBill", noBill);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, resultMap);
		}
		return resultMap;
    }
	
	public Map<String,Object> billFunc(Map<String, Object> map) throws Exception{
		Map<String,Object> resultMap = new HashMap<>();
		//判断能否生成账单
		int status = payv2PayOrderClearService.isHaveBill(map);
		Payv2DayCompanyClear payv2DayCompanyClear = payv2DayCompanyClearService.selectObject(map);
		if(status == 1){
			// 根据账单编号查询出账单详情
			Object companyCheckId = map.get("companyCheckId");
			map.remove("companyCheckId");
			List<Payv2BussWayDetail> detailList = payv2BussWayDetailService.queryByCom(map);
			map.put("companyCheckId", companyCheckId);
			payv2DayCompanyClear.setStatus(2);
			//　将账单详情集合遍历相加，并将结果存放在日账单对象中
			for(Payv2BussWayDetail payv2BussWayDetail : detailList) {
				payv2DayCompanyClear.setSuccessCount(payv2BussWayDetail.getSuccessCount());
				payv2DayCompanyClear.setSuccessMoney(payv2BussWayDetail.getSuccessMoney());
				payv2DayCompanyClear.setRefundCount(payv2BussWayDetail.getRefundCount());
				payv2DayCompanyClear.setRefundMoney(payv2BussWayDetail.getRefundMoney());
				payv2DayCompanyClear.setRateMoney(payv2BussWayDetail.getBussWayRateMoney());
				payv2DayCompanyClear.setRateProfit(payv2BussWayDetail.getBussWayRateMoney()-payv2BussWayDetail.getCostRateMoney());				
			}
			Map<String,Object> orderParam = new HashMap<>();
			orderParam.put("companyId", map.get("companyId"));
			orderParam.put("clearTime", map.get("clearTime"));
			List<Payv2PayOrderClear> orderList = payv2PayOrderClearService.queryByApp(orderParam);
			
			String alipayTime = (String) map.get("clearTime");
//			Date as = new Date(alipayTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Date as = sdf.parse(map.get("clearTime").toString());			
			
			payv2PayOrderClearService.appIdDownBill(orderList,alipayTime, as,payv2DayCompanyClear,map);
			// 将这个对象更新到数据库中	
			resultMap.put("status", 1);
		}else if(status == 2){
			resultMap.put("status", 2);  //存在未出账的数据
		}else if(status == 3){
			//该商户无订单记录，只改变账单状态，不生成账单
			payv2DayCompanyClear.setStatus(1);
			payv2DayCompanyClearService.updateStatus(payv2DayCompanyClear);
			resultMap.put("status", 3); 
		}else{
			resultMap.put("status", -1); 
		}
		return resultMap;
	}
	
	/**
	 * @param map form表单提交的搜索条件
	 * @param workbook
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportExcelOrder")
	public Map<String, Object> exportExcelOrder(@RequestParam Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 搜索
		// 根据Map搜索条件查询出对应的月账单
		Map<Long, Payv2DayCompanyClear> dataMap=payv2DayCompanyClearService.mouthClearList(map);
		Map<String, Object> detailMap = new HashMap<String, Object>();
		
		// 根据Map搜索出对应日账单List,将搜索出的List遍历按商户Id分组，每组对应一个日账单List,同时查询出每个账单的账单详情，将该账单详情存放在账单对象中
		map.put("curPage", 1);
		map.put("pageData", 999999999);
		PageObject<Payv2DayCompanyClear> pageObject = payv2DayCompanyClearService.dayClearList(map);
		List<Payv2DayCompanyClear> dayCompanyClearList = pageObject.getDataList();
		
		Map<String, List<Payv2DayCompanyClear>> dayCompanyMap=new HashMap<String, List<Payv2DayCompanyClear>>();	//用来分组的Map
		
		for(Payv2DayCompanyClear payv2DayCompanyClear : dayCompanyClearList){
			//搜索日账单详情
			detailMap.clear();
			detailMap.put("companyCheckId",payv2DayCompanyClear.getCompanyCheckId());
//			detailMap.put("companyId",payv2DayCompanyClear.getCompanyId().toString());
			Map<String, Object> dayDetailMap = payv2BussWayDetailService.dayBussWayDetaiList(detailMap);
			payv2DayCompanyClear.setDetailMap(dayDetailMap);
			//将日账单List中的对象分组
			if(dayCompanyMap.containsKey(payv2DayCompanyClear.getCompanyName())){
				List<Payv2DayCompanyClear> dayCompanyClearList1 = dayCompanyMap.get(payv2DayCompanyClear.getCompanyName());
				dayCompanyClearList1.add(payv2DayCompanyClear);
			}else{
				List<Payv2DayCompanyClear> dayCompanyClearList1 =new ArrayList<Payv2DayCompanyClear>();
				dayCompanyClearList1.add(payv2DayCompanyClear);
				dayCompanyMap.put(payv2DayCompanyClear.getCompanyName(), dayCompanyClearList1);
			}
		}
		
		// 遍历月账单集合，每次遍历时搜索出对应的月账单详情。并将详情放在账单实体中		
		for (Entry<Long, Payv2DayCompanyClear> entry : dataMap.entrySet()) {
			map.put("companyId", entry.getKey());
		    Map<Long, Payv2BussWayDetail> mapData = payv2BussWayDetailService.mouthBussWayDetaiList(map);
		    entry.getValue().setDetailMap(mapData);
		}
		
		
		//　将月账单的集合传递给导出工具类	
		try {
			if (null != detailMap && detailMap.size() > 0) {	
				// 导出
				TestExportExcel2<Payv2DayCompanyClear> ex = new TestExportExcel2<Payv2DayCompanyClear>();
				
				
				OutputStream out = response.getOutputStream();
				String filename = "账单管理" + new Date().getTime() + ".xls";// 设置下载时客户端Excel的名称
				filename = URLEncoder.encode(filename, "UTF-8");// 处理中文文件名
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment;filename=" + filename);
				ex.exportExcel(map, dataMap, dayCompanyMap, out);
				out.close();
			} else {
				returnMap.put("status", -1);// 失败
			}
		} catch (Exception e) {
			LOGGER.error("导出订单列表.xls错误", e);
			e.printStackTrace();
		}
		return returnMap;
	}
	   public static String toString(Object object) {
	        String time;  
	        SimpleDateFormat formater = new SimpleDateFormat();  
	        formater.applyPattern("yyyy-MM-dd");  
	        time = formater.format(object);  
	        return time;  
	    }  
	
}
