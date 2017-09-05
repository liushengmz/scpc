package com.pay.company.controller.online;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.entity.Payv2AppType;
import com.pay.business.payv2.service.Payv2AppTypeService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2BussCompanyAppController
 * @Description: 应用管理
 * @author mofan
 * @date 2016年12月1日 15:13:11
 */
@Controller
@RequestMapping("/online/payv2BussCompanyApp/*")
public class Payv2BussCompanyAppController extends BaseManagerController<Payv2BussCompanyApp, Payv2BussCompanyAppMapper> {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2AppTypeService payv2AppTypeService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;

	
	/**
	 * 商户APP列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/payv2BussCompanyAppList")
	public Map<String,Object> payv2BussCompanyAppList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		filterMap(map);
		resultMap.put("map", map);
		map.put("isDelete", "2");
		Payv2BussCompany company = getAdmin();
		map.put("companyId", company.getId());
		PageObject<Payv2BussCompanyApp> pageList = null;
		try {
			pageList = payv2BussCompanyAppService.payv2BussCompanyAppList(map);
			resultMap.put("list", pageList);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, resultMap);
		}
		return resultMap;
	}

	/**
	 * 获取APP列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAppList")
	public Map<String,Object> getAppList(){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany company = getAdmin();
		Payv2BussCompanyApp app = new Payv2BussCompanyApp();
		app.setCompanyId(company.getId());
		app.setIsDelete(2);
		// 获取应用
		List<Payv2BussCompanyApp> appList = payv2BussCompanyAppService
				.selectByObject(app);
		resultMap.put("appList", appList);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}
	
	/**
	 * 获取APP类别列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAppTypeList")
	public Map<String,Object> getAppTypeList(){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		// 获取应用
		List<Payv2AppType> appTypeList = payv2AppTypeService.selectByObject(new Payv2AppType());
		resultMap.put("appTypeList", appTypeList);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}
	
	public void filterMap(Map<String, Object> map){
		if(map.get("type") != null){
			String type = map.get("type").toString();
			if(type.length() > 0){
				String[] types = type.split(",");
				for(String ty:types){
					
					if("IOS".equals(ty)){
						map.put("isIos", "1");
					}else if("ANDROID".equals(ty)){
						map.put("isAndroid", "1");
					}else if("WEB".equals(ty)){
						map.put("isWeb", "1");
					}
				}
			}
		}
	}
	
	/**
	 * 跳转添加商户应用页面
	 * 
	 * @param map
	 *            参数集
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAddJinfuBussApp")
	public ModelAndView addJinfuBussApp(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jinfu/jinfu_buss_app_add");
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 添加商户应用
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addPayv2BussCompanyApp")
	public Map<String, Object> addPayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Payv2BussCompany company = getAdmin();
			map.put("createTime", new Date());
			map.put("companyId", company.getId());
			payv2BussCompanyAppService.add(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("添加商户应用报错： ", e);
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
		}
		return resultMap;
	}


	/**
	 * 跳转编辑商户APP页面
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/toEditPayv2BussCompanyApp")
	public Map<String, Object> toEditPayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				// 获取合作商户公司列表
				Payv2BussCompanyApp payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				if(null != payv2BussCompanyApp){
					//得到商户支持的支付方式
					Payv2BussCompany company = getAdmin();
					Payv2BussSupportPayWay payv2BussSupportPayWay = new Payv2BussSupportPayWay();
					payv2BussSupportPayWay.setParentId(company.getId());
					payv2BussSupportPayWay.setIsDelete(2);
					List<Payv2BussSupportPayWay> supportList = payv2BussSupportPayWayService.selectPayWayIdByGroup(payv2BussSupportPayWay);
//					for (Payv2BussSupportPayWay payv2BussSupportPayWay2 : supportList) {
//						String iocUrl=payv2BussSupportPayWay2.getWayIcon();
//						if(null!=iocUrl&&!iocUrl.equals("")){
//							iocUrl.replace("\\", "/");
//							payv2BussSupportPayWay2.setWayIcon(iocUrl);
//						}
//					}
					payv2BussCompanyApp.setSupportList(supportList);
					if(payv2BussCompanyApp.getAppTypeId() != null && payv2BussCompanyApp.getAppTypeId() > 0){
						map.put("id", payv2BussCompanyApp.getAppTypeId());
						Payv2AppType payv2AppType = payv2AppTypeService.detail(map);
						if(payv2AppType != null){
							payv2BussCompanyApp.setTypeName(payv2AppType.getTypeName());
						}
					}
				}
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, payv2BussCompanyApp);
			} catch (Exception e) {
				logger.error("跳转商户应用添加页面报错：", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
			}
		}
		return resultMap;
	}



	/**
	 * 编辑商户APP
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2BussCompanyApp", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				map.put("updateTime", new Date());
				payv2BussCompanyAppService.update(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				logger.error("修改商户APP提交失败", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "修改商户APP提交失败!");
			}
		}
		return resultMap;
	}

	
}
