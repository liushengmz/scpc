package com.pay.manger.controller.payv2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2WaySdkVersion;
import com.pay.business.payv2.mapper.Payv2WaySdkVersionMapper;
import com.pay.business.payv2.service.Payv2WaySdkVersionService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2WaySdkVersionController 
 * @Description: 钱包支付sdk表
 * @author mofan
 * @date 2016年12月10日 09:13:11
 */
@Controller
@RequestMapping("/payv2WaySdkVersion")
public class Payv2WaySdkVersionController extends BaseManagerController<Payv2WaySdkVersion, Payv2WaySdkVersionMapper>{
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2WaySdkVersionService payv2WaySdkVersionService;
    @Autowired
    private Payv2PayWayService payv2PayWayService;
	
	/**
	 * 钱包支付sdk列表
	 * @param map
	 * @param request
	 * @return
	 */
    @RequestMapping("/payv2WaySdkVersionList")
    public ModelAndView payv2WaySdkVersionList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/pay_moneybag_sdk_list");
		map.put("isDelete", "2");
    	PageObject<Payv2WaySdkVersion> pageObject = payv2WaySdkVersionService.payv2WaySdkVersionList(map);
    	mv.addObject("list", pageObject);
    	mv.addObject("map", map);
    	return mv;
    }
    
    /**
    * @Title: addPayv2WaySdkVersionTc 
    * @Description:添加钱包SDK弹窗
    * @param map
    * @param request
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月10日 09:15:15 
    * @throws
    */
    @RequestMapping("/addPayv2WaySdkVersionTc")
    public ModelAndView addPayv2WaySdkVersionTc(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/pay-moneybag_sdk_add");
		Payv2PayWay payv2PayWay = new Payv2PayWay();
		try {
			if (null != map.get("payWayId")) {
				map.put("id", map.get("payWayId"));
				map.remove("payWayId");
				payv2PayWay = payv2PayWayService.detail(map);
				mv.addObject("payv2PayWay", payv2PayWay);
			}
		} catch (Exception e) {
			logger.error(" 跳转钱包SDK弹窗报错", e);
		}
    	return mv;
    }
    
	
	/**
	 * 上传sdk
	 */
	@ResponseBody
	@RequestMapping(value = "/addPayv2WaySdkVersion", method = RequestMethod.POST)
	public Map<String, Object> addPayv2WaySdkVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			payv2WaySdkVersionService.add(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("上传新版本提交失败", e);
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "上传新版本提交失败!");
		}
		resultMap.put("payWayId", map.get("payWayId"));
		return resultMap;
	}
	
    /**
	 * 上线下线
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2WaySdkVersion", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2WaySdkVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				payv2WaySdkVersionService.update(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				logger.error("上线下线提交失败", e);
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "上线下线提交失败!");
			}
		}
		resultMap.put("payWayId", map.get("payWayId"));
		return resultMap;
	}
}
