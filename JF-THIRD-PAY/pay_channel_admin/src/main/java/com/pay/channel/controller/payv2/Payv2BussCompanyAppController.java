package com.pay.channel.controller.payv2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.payv2.entity.Payv2AppType;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.service.Payv2AppTypeService;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.channel.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2BussCompanyAppController
 * @Description: 商户APP表
 * @author mofan
 * @date 2016年12月1日 15:13:11
 */
@Controller
@RequestMapping("/payv2BussCompanyApp")
public class Payv2BussCompanyAppController extends BaseManagerController<Payv2BussCompanyApp, Payv2BussCompanyAppMapper> {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;
	@Autowired
	private Payv2AppTypeService payv2AppTypeService;

	/**
	 * 商户APP列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2BussCompanyAppList")
	public ModelAndView payv2BussCompanyAppList(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("companyApp/pay_app_list");
		Payv2Channel pc = getAdmin();
		map.put("channelId", pc.getId());
		map.put("isDelete", "2");
		if (map.get("platform") != null) {
			String platform = map.get("platform").toString();
			if ("1".equals(platform)) {
				map.put("isIos", "1");
			} else if ("2".equals(platform)) {
				map.put("isAndroid", "1");
			} else if ("3".equals(platform)) {
				map.put("isWeb", "1");
			}
		}
		PageObject<Payv2BussCompanyApp> pageObject = payv2BussCompanyAppService.payv2BussCompanyAppList(map);
		List<Payv2BussCompanyApp> appList = pageObject.getDataList();
		for (Payv2BussCompanyApp payv2BussCompanyApp : appList) {
			Long appId = payv2BussCompanyApp.getId();
			Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
			payv2BussAppSupportPayWay.setAppId(appId);
			payv2BussAppSupportPayWay.setMerchantType(1);
			payv2BussAppSupportPayWay.setIsDelete(2);
			List<Payv2BussAppSupportPayWay> list = payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
			payv2BussCompanyApp.setAppSupportPayWayNumber(list.size());
			if(payv2BussCompanyApp.getAppTypeId()!=null){
				Payv2AppType payv2AppType=new Payv2AppType();
				payv2AppType.setId(payv2BussCompanyApp.getAppTypeId());
				payv2AppType=payv2AppTypeService.selectSingle(payv2AppType);
				if(payv2AppType!=null){
					payv2BussCompanyApp.setTypeName(payv2AppType.getTypeName());
				}
			}
			
		}
		mv.addObject("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyApp(payv2BussCompany);
		mv.addObject("companyList", companyList);
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 从商户管理进到的商户APP列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toPayv2BussCompanyAppList")
	public ModelAndView toPayv2BussCompanyAppList(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("companyApp/pay_from_company_app_list");
		Payv2Channel pc = getAdmin();
		map.put("channelId", pc.getId());
		map.put("isDelete", "2");
		PageObject<Payv2BussCompanyApp> pageObject = payv2BussCompanyAppService.payv2BussCompanyAppList(map);
		List<Payv2BussCompanyApp> appList = pageObject.getDataList();
		for (Payv2BussCompanyApp payv2BussCompanyApp : appList) {
			Long appId = payv2BussCompanyApp.getId();
			Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
			payv2BussAppSupportPayWay.setAppId(appId);
			payv2BussAppSupportPayWay.setMerchantType(1);
			payv2BussAppSupportPayWay.setIsDelete(2);
			List<Payv2BussAppSupportPayWay> list = payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
			payv2BussCompanyApp.setAppSupportPayWayNumber(list.size());
			if(payv2BussCompanyApp.getAppTypeId()!=null){
				Payv2AppType payv2AppType=new Payv2AppType();
				payv2AppType.setId(payv2BussCompanyApp.getAppTypeId());
				payv2AppType=payv2AppTypeService.selectSingle(payv2AppType);
				if(payv2AppType!=null){
					payv2BussCompanyApp.setTypeName(payv2AppType.getTypeName());
				}
			}
		}
		mv.addObject("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyApp(payv2BussCompany);
		mv.addObject("companyList", companyList);
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 新增商户APP
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAddPayv2BussCompanyApp")
	public ModelAndView toAddPayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		ModelAndView view = new ModelAndView("companyApp/pay_app_add");
		map.put("fromType", 1);
		view.addObject("map", map);
		
		Payv2Channel pc = getAdmin();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		// 获取商户
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyApp(payv2BussCompany);
		// 获取类型
		Payv2AppType payv2AppType = new Payv2AppType();
		List<Payv2AppType> appTypeList = payv2AppTypeService.selectByObject(payv2AppType);
		view.addObject("companyList", companyList);
		view.addObject("appTypeList", appTypeList);
		return view;
	}

	/**
	 * 新增商户APP
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toCompanyPayv2BussApp")
	public ModelAndView toCompanyPayv2BussApp(@RequestParam Map<String, Object> map) {
		/**
		 * ModelAndView view = new ModelAndView("payv2/pay_from_company_app_add"); 2017.2.6注释掉，原因因为最新版本改了，可以与payv2/pay_app_add页面 复用，
		 */
//		ModelAndView view = new ModelAndView("payv2/pay_from_company_app_add");
		ModelAndView view = new ModelAndView("companyApp/pay_app_add");
		map.put("fromType",2);
		view.addObject("map", map);
		Payv2Channel pc = getAdmin();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyApp(payv2BussCompany);
		// 获取类型
		Payv2AppType payv2AppType = new Payv2AppType();
		List<Payv2AppType> appTypeList = payv2AppTypeService.selectByObject(payv2AppType);
		view.addObject("companyList", companyList);
		view.addObject("appTypeList", appTypeList);
		return view;
	}

	/**
	 * 添加商户APP
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addPayv2BussCompanyApp")
	public Map<String, Object> addPayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			payv2BussCompanyAppService.add(map);
//			resultMap.put("resultCode", "200");
			resultMap.put("companyId", map.get("companyId"));
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCode", "-1");
			resultMap.put("message", "添加商户APP出错！");
		}
		return resultMap;
	}

	/**
	 * 查看商户APP未通过原因
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toViewFailReason")
	public ModelAndView toViewFailReason(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyApp/pay_app_view");
		Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				mvc.addObject("payv2BussCompanyApp", payv2BussCompanyApp);
			}
		} catch (Exception e) {
			logger.error(" 查看商户APP页面报错", e);
		}
		return mvc;
	}

	/**
	 * 审核
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toApprove")
	public ModelAndView toApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyApp/pay_app_approve");
		mvc.addObject("map", map);
		return mvc;
	}

	/**
	 * 编辑商户APP
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toEditPayv2BussCompanyApp")
	public ModelAndView toEditPayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyApp/pay_app_edit");
		Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				mvc.addObject("payv2BussCompanyApp", payv2BussCompanyApp);
				Payv2Channel pc = getAdmin();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setChannelId(pc.getId());
				payv2BussCompany.setIsDelete(2);
				List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyApp(payv2BussCompany);
				// 获取类型
				Payv2AppType payv2AppType = new Payv2AppType();
				List<Payv2AppType> appTypeList = payv2AppTypeService.selectByObject(payv2AppType);
				mvc.addObject("companyList", companyList);
				mvc.addObject("appTypeList", appTypeList);
			}
		} catch (Exception e) {
			logger.error(" 跳转商户APP编辑页面报错", e);
		}
		return mvc;
	}

	/**
	 * 从商户管理进到的商户APP修改
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toEditFromCompanyPayv2BussCompanyApp")
	public ModelAndView toEditFromCompanyPayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyApp/pay_from_company_app_edit");
		Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				mvc.addObject("payv2BussCompanyApp", payv2BussCompanyApp);
				Payv2Channel pc = getAdmin();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setChannelId(pc.getId());
				payv2BussCompany.setIsDelete(2);
				List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyApp(payv2BussCompany);
				// 获取类型
				Payv2AppType payv2AppType = new Payv2AppType();
				List<Payv2AppType> appTypeList = payv2AppTypeService.selectByObject(payv2AppType);
				mvc.addObject("companyList", companyList);
				mvc.addObject("appTypeList", appTypeList);
			}
		} catch (Exception e) {
			logger.error(" 跳转商户APP编辑页面报错", e);
		}
		return mvc;
	}

	/**
	 * 查看商户APP下载
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toDownload")
	public ModelAndView toDownload(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyApp/pay_app_download");
		Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				mvc.addObject("payv2BussCompanyApp", payv2BussCompanyApp);
			}
		} catch (Exception e) {
			logger.error(" 查看商户APP页面报错", e);
		}
		return mvc;
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
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			try {
				map.put("updateTime", new Date());
				if (map.get("appStatus") == null) {
					map.put("appStatus", 1);// 修改的时候 同时改变状态为：未审核；让管理再次审核
				}
				payv2BussCompanyAppService.update(map);
				resultMap.put("resultCode", "200");
			} catch (Exception e) {
				logger.error("修改商户APP提交失败", e);
				e.printStackTrace();
				resultMap.put("resultCode", "-1");
				resultMap.put("message", "修改商户APP出错！");
			}
		}
		resultMap.put("companyId", map.get("companyId"));
		return resultMap;
	}

	/**
	 * @Title: updatePayv2BussCompanyAppYes
	 * @Description:恢复合作APP
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月20日 下午5:03:13
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2BussCompanyAppYes")
	public Map<String, Object> updatePayv2BussCompanyAppYes(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		resultMap.put("companyId", map.get("companyId"));
		if (isNotNull) {
			try {
				// 首先查询APP上级：商户是否启动正常状态
				Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
				payv2BussCompanyApp.setId(Long.valueOf(map.get("id").toString()));
				payv2BussCompanyApp.setIsDelete(2);
				payv2BussCompanyApp = payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
				if (payv2BussCompanyApp != null) {
					Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
					payv2BussCompany.setId(payv2BussCompanyApp.getCompanyId());
					payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
					if (payv2BussCompany != null) {
						int isDelete = payv2BussCompany.getIsDelete();
						int status = payv2BussCompany.getCompanyStatus();
						if (isDelete == 1) {
							resultMap.put("resultCode", 201);// 已经删除不能启动
							return resultMap;
						}
						if (status == 4) {
							resultMap.put("resultCode", 202);// 已经关闭不能启动
							return resultMap;
						}
					}
				}
				map.put("updateTime", new Date());
				payv2BussCompanyAppService.update(map);
				resultMap.put("resultCode", "200");
			} catch (Exception e) {
				logger.error("恢复合作APP失败", e);
				e.printStackTrace();
				resultMap.put("resultCode", "-1");
				resultMap.put("message", "修改商户APP出错！");
			}
		}
		return resultMap;
	}

	/**
	 * @Title: updatePayv2BussCompanyAppNo
	 * @Description:APP终止合作
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月21日 上午10:14:38
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2BussCompanyAppNo")
	public Map<String, Object> updatePayv2BussCompanyAppNo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			try {
				map.put("updateTime", new Date());
				// 需要终止旗下的APP支付方式
				Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
				payv2BussCompanyApp.setId(Long.valueOf(map.get("id").toString()));
				payv2BussCompanyApp.setIsDelete(2);
				payv2BussCompanyApp = payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
				if (payv2BussCompanyApp != null) {
					// APP支付方式列表
					Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
					payv2BussAppSupportPayWay.setAppId(payv2BussCompanyApp.getId());
					payv2BussAppSupportPayWay.setMerchantType(1);
					payv2BussAppSupportPayWay.setIsDelete(2);
					List<Payv2BussAppSupportPayWay> list = payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
					for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : list) {
						payv2BussAppSupportPayWay2.setUpdateTime(new Date());
						payv2BussAppSupportPayWay2.setStatus(2);
						payv2BussAppSupportPayWayService.update(payv2BussAppSupportPayWay2);
					}
				}
				payv2BussCompanyAppService.update(map);
				resultMap.put("resultCode", "200");
			} catch (Exception e) {
				logger.error("APP终止合作失败", e);
				e.printStackTrace();
				resultMap.put("resultCode", "-1");
				resultMap.put("message", "APP终止合作出错！");
			}
		}
		resultMap.put("companyId", map.get("companyId"));
		return resultMap;
	}

	/**
	 * 查看订单
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toViewOrders")
	public ModelAndView toViewOrders(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/pay_app_view_order");
		try {
			if (null != map.get("id")) {
				Payv2BussCompanyApp payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
			}
		} catch (Exception e) {
			logger.error(" 查看商户APP对应的订单出错", e);
		}
		return mvc;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 商户APP列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/interfaceCompanyAppList")
	public Map<String, Object> interfaceCompanyAppList(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Payv2Channel pc = getAdmin();
		map.put("channelId", 1);
		map.put("isDelete", "2");
		if (map.get("platform") != null) {
			String platform = map.get("platform").toString();
			if ("1".equals(platform)) {
				map.put("isIos", "1");
			} else if ("2".equals(platform)) {
				map.put("isAndroid", "1");
			} else if ("3".equals(platform)) {
				map.put("isWeb", "1");
			}
		}
		PageObject<Payv2BussCompanyApp> pageObject = payv2BussCompanyAppService.payv2BussCompanyAppList(map);
		List<Payv2BussCompanyApp> appList = pageObject.getDataList();
		for (Payv2BussCompanyApp payv2BussCompanyApp : appList) {
			Long appId = payv2BussCompanyApp.getId();
			Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
			payv2BussAppSupportPayWay.setAppId(appId);
			payv2BussAppSupportPayWay.setMerchantType(1);
			payv2BussAppSupportPayWay.setIsDelete(2);
			List<Payv2BussAppSupportPayWay> list = payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
			payv2BussCompanyApp.setAppSupportPayWayNumber(list.size());
			if(payv2BussCompanyApp.getAppTypeId()!=null){
				Payv2AppType payv2AppType=new Payv2AppType();
				payv2AppType.setId(payv2BussCompanyApp.getAppTypeId());
				payv2AppType=payv2AppTypeService.selectSingle(payv2AppType);
				if(payv2AppType!=null){
					payv2BussCompanyApp.setTypeName(payv2AppType.getTypeName());
				}
			}
			
		}
		resultMap.put("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyApp(payv2BussCompany);
		resultMap.put("companyList", companyList);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}
	
	/**
	 * app详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/detail")
	public ModelAndView detail(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyApp/pay_app_detail");
		try {
			if (null != map.get("id")) {
				Payv2BussCompanyApp payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				Map<String,Object> param =new HashMap<>();
				param.put("id", payv2BussCompanyApp.getCompanyId());
				Payv2BussCompany payv2BussCompany = payv2BussCompanyService.detail(param);
				if(payv2BussCompanyApp.getAppTypeId()!=null){
					param.put("id", payv2BussCompanyApp.getAppTypeId());
					Payv2AppType payv2AppType = payv2AppTypeService.detail(param);
					mvc.addObject("typeName", payv2AppType.getTypeName());
				}
				mvc.addObject("obj", payv2BussCompanyApp);
				mvc.addObject("companyName", payv2BussCompany.getCompanyName());
			}
		} catch (Exception e) {
			logger.error(" 查看商户APP对应的订单出错", e);
		}
		return mvc;
	}
}
