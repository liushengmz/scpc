package com.pay.manger.controller.payv2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.record.entity.PayDataRecord;
import com.pay.business.record.mapper.PayDataRecordMapper;
import com.pay.business.record.service.PayDataRecordService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

@Controller
@RequestMapping("/Payv2BussiManagerAlldata/*")
public class Payv2BussiManagerAlldataController extends BaseManagerController<PayDataRecord, PayDataRecordMapper> {


	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PayDataRecordService payDataRecordService;
	
	@Autowired
	private Payv2ChannelService payv2ChannelService;//渠道商服务
	
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;//商户App服务
	
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;//支付渠道服务
	
	/**
	 * 交易汇总默认加载页面
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("alldata")
	public ModelAndView showDetailDay(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("payv2/businessmanage/payv2_bussiness_manage_alldata");	
		//获取渠道商列表
    	Payv2Channel payv2Channel=new Payv2Channel();
    	payv2Channel.setIsDelete(2);
    	List<Payv2Channel>	payv2ChannelList=payv2ChannelService.selectByObject(payv2Channel);
    	
    	List<Payv2PayWayRate> payWayList =payv2PayWayRateService.queryByChannelWayId(map);
    	mv.addObject("payv2ChannelList", payv2ChannelList);
    	mv.addObject("payWayList", payWayList);
		return mv;
	}
	
	/**
	 * 根据商户id查询对应的应用
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("getApps")
    public Map<String,Object> getApps(@RequestParam Long companyId){
		Map<String,Object> resultMap = new HashMap<>();
    	try {
    		Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
    		payv2BussCompanyApp.setCompanyId(companyId);
    		payv2BussCompanyApp.setIsDelete(2);    		
    		List<Payv2BussCompanyApp> appList = payv2BussCompanyAppService.selectByObject(payv2BussCompanyApp);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, appList);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
	
	/**
	 * 根据channelID获取其支持的支付渠道
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("getPayWaysByChannel")
    public Map<String,Object> getPayWaysByChannel(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();
    	try {
    		List<Payv2PayWayRate> payWayList =payv2PayWayRateService.queryByChannelWayId(map);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, payWayList);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
	
	
	/**
	 * 根据companyID获取其支持的支付渠道
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("getPayWays")
    public Map<String,Object> getPayWays(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();
    	try {
    		map.put("isDelete", 2);
    		List<Payv2PayWayRate> payWayList =payv2PayWayRateService.getPayWaysByCom(map);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, payWayList);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
	
	/**
	 * 关键数据获取
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("curxData")
    public Map<String,Object> curxData(@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();
		Integer dateType = NumberUtils.createInteger(map.get("dateType").toString());
    	try {
    		PayDataRecord curxData = new PayDataRecord();
    		// 判断dateType，如果为1，查实时
    		if(dateType ==1){
    			curxData = payDataRecordService.curxDataNow(map);
    		}else if(dateType ==2 ||dateType ==3||dateType ==4){
    			// 如果为2，3，4，查统计
    			curxData = payDataRecordService.curxData(map);
    		}else if(dateType == 5){
    			// 如果为5，判断时间的结束日期是否小于当前日期(精确到小时)，如果小于查统计 ,否则查实时
    			Boolean bool = comparDate(map.get("endTime").toString());
    			if(bool){
    				curxData = payDataRecordService.curxData(map);
    			}else{
    				curxData = payDataRecordService.curxDataNow(map);
    			}
    		}
    		resultMap.put("curxData", curxData);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, curxData);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
	 /**
	  * 判断一个日期与当前时间的先后，为true参数日期在前，false在后
	  * 
	 * @param date1
	 * @return boolean
	 */
	public static boolean comparDate(String date1) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Boolean bool = false;
	        try {
	            Date dt1 = df.parse(date1);
	            Date dt2 = new Date();
	            if (dt1.getTime() > dt2.getTime()) {
	            	bool =  false;
	            } else if (dt1.getTime() <= dt2.getTime()) {
	            	bool =  true;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return bool;
	    }
	
	/**
	 * 交易时刻趋势图
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
    @RequestMapping("timeTrend")
    public Map<String,Object> timeTrend(@RequestParam Map<String, Object> map){
		// 今天 昨天 前七天 前三十天 自定义日期
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//　根据搜索条件判断，如果只查一天，以小时为单位查询	
			//　如果大于一天，以天为单位查询
			Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
			List<PayDataRecord> resList = null;
			if(queryType == 1 || queryType == 2){
				try {
					resList = payDataRecordService.groupByHour(map);
				} catch (ParseException e) {
					logger.error("日期格式化异常:" , e);
					e.printStackTrace();
				}
				resultMap.put("resultType", 1);
			}else{
				resList = payDataRecordService.groupByDay(map);
				resultMap.put("resultType", 2);
			}			
    		resultMap.put("resultData", resList);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}
	
}
