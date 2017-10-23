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
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.payv2.entity.Payv2AppVersion;
import com.pay.business.payv2.mapper.Payv2AppVersionMapper;
import com.pay.business.payv2.service.Payv2AppVersionService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2AppVersionController 
 * @Description: 线上应用版本管理表
 * @author mofan
 * @date 2016年12月29日 15:13:11
 */
@Controller
@RequestMapping("/payv2AppVersion")
public class Payv2AppVersionController extends BaseManagerController<Payv2AppVersion, Payv2AppVersionMapper>{
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2AppVersionService payv2AppVersionService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	
	/**
	 * 线上应用版本列表
	 * @param map
	 * @param request
	 * @return
	 */
    @RequestMapping("/payv2AppVersionList")
    public ModelAndView payv2WaySdkVersionList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/pay_app_version_List");
		map.put("isDelete", "2");
    	PageObject<Payv2AppVersion> pageObject = payv2AppVersionService.payv2AppVersionList(map);
    	mv.addObject("list", pageObject);
    	mv.addObject("map", map);
    	return mv;
    }
    
    /**
	 * 上线下线
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2AppVersion", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2WaySdkVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				map.put("updateTime", new Date());
				payv2AppVersionService.update(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				logger.error("上线下线提交失败", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "上线下线提交出错!");
			}
		}
		resultMap.put("appId", map.get("appId"));
		return resultMap;
	}
	
    /**
     * 跳转上架
     * @param map
     * @return
     */
    @RequestMapping("/toUpOnSale")
    public ModelAndView toUpOnSale(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/pay_app_version_put_on_sale");
		mvc.addObject("map", map);
		try {
			if (null != map.get("id")) {
				Payv2AppVersion payv2AppVersion = payv2AppVersionService.toUpOnSale(map);
				mvc.addObject("payv2AppVersion", payv2AppVersion);				
			}
		} catch (Exception e) {
			logger.error("跳转上架页面报错", e);
		}
		return mvc;
    }
	
    
    /**
     * 上架
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/upOnSale")
    public Map<String,Object> upOnSale(@RequestParam Map<String, Object> map) {
    	Map<String,Object> resultMap = new HashMap<>();
    	try {
    		map.remove("status");
    		payv2AppVersionService.upOnSale(map);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	resultMap.put("appId", map.get("appId"));
    	return resultMap;
    }
	
    /**
     * 跳转下载
     * @param map
     * @return
     */
    @RequestMapping("/toDownload")
    public ModelAndView toDownload(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/pay_app_version_download");
		try {
			if (null != map.get("id")) {
				Payv2AppVersion payv2AppVersion = payv2AppVersionService.toLoadDetailForDownload(map);
				mvc.addObject("payv2AppVersion", payv2AppVersion);
			}
		} catch (Exception e) {
			logger.error("跳转下载页面报错", e);
		}
		mvc.addObject("map", map);
		return mvc;
    }
    
    /**
     * 审核
     * @param map
     * @return
     */
    @RequestMapping("/toApprove")
    public ModelAndView toApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/pay_app_version_approve");
		mvc.addObject("map", map);
		return mvc;
    }
	
    /**
     * 审核通过
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/approveAgreePayv2AppVersion")
    public Map<String,Object> approveAgreePayv2AppVersion(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<>();
    	try {
    		map.put("updateTime", new Date());
    		payv2AppVersionService.update(map);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	resultMap.put("appId", map.get("appId"));
    	return resultMap;
    }
}
