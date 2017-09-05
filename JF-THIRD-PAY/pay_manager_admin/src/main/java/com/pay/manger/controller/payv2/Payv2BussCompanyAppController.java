package com.pay.manger.controller.payv2;

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

import com.core.teamwork.base.util.IdUtils;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.payv2.entity.Payv2AppType;
import com.pay.business.payv2.service.Payv2AppTypeService;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.util.GenerateUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

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
	public ModelAndView payv2BussCompanyAppList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/app/pay_app_list");
		map.put("isDelete", "2");
		PageObject<Payv2BussCompanyApp> pageObject = payv2BussCompanyAppService.payv2BussCompanyAppList(map);
		mv.addObject("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectByObject(payv2BussCompany);
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
	public ModelAndView toPayv2BussCompanyAppList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/app/pay_from_company_app_list");
		map.put("isDelete", "2");
		PageObject<Payv2BussCompanyApp> pageObject = payv2BussCompanyAppService.payv2BussCompanyAppList(map);
		mv.addObject("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectByObject(payv2BussCompany);
		map.put("merchantType", 1);
		mv.addObject("companyList", companyList);
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 查看商户APP未通过原因
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toViewFailReason")
	public ModelAndView toViewFailReason(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/app/pay_app_view");
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
		ModelAndView mvc = new ModelAndView("payv2/app/pay_app_approve");
		mvc.addObject("map", map);
		return mvc;
	}

	/**
	 * 从商户管理进到的审核
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toFromCompanyApprove")
	public ModelAndView toFromCompanyApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/app/pay_from_company_app_approve");
		try {
			if (null != map.get("id")) {
				Payv2BussCompanyApp payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				mvc.addObject("payv2BussCompanyApp", payv2BussCompanyApp);

			}
		} catch (Exception e) {
			logger.error(" 跳转商户APP编辑页面报错", e);
		}
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
		ModelAndView mvc = new ModelAndView("payv2/app/pay_app_edit");
		Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyApp = payv2BussCompanyAppService.detail(map);
				mvc.addObject("payv2BussCompanyApp", payv2BussCompanyApp);
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				List<Payv2BussCompany> companyList = payv2BussCompanyService.selectByObject(payv2BussCompany);
				mvc.addObject("companyList", companyList);
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
		ModelAndView mvc = new ModelAndView("payv2/app/pay_app_download");
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

	/**
	 * 审核商户通过
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/approveAgreePayv2BussCompanyApp")
	public Map<String, Object> approveAgreePayv2BussCompanyApp(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			map.put("updateTime", new Date());
			map.put("appKey", GenerateUtil.getRandomString(64));
			//map.put("appSecret", GenerateUtil.getRandomString(64) + GenerateUtil.getRandomString(64));
			map.put("appSecret", IdUtils.createRandomString(40));//跟手机验证生成的秘钥一致
			payv2BussCompanyAppService.update(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			resultMap.put("companyId", map.get("companyId"));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "审核商户异常！");
		}
		return resultMap;
	}

	/**
	 * app详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/detail")
	public ModelAndView detail(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/app/pay_app_detail_new");
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
				String appImg = payv2BussCompanyApp.getAppImg();
				//应用截图
				if(appImg!=null&&!"".equals(appImg)){
					String appImgs [] = appImg.split(",");
					mvc.addObject("appImgs", appImgs);
				}
				String appDescFile = payv2BussCompanyApp.getAppDescFile();
				//应用说明文档
				if(appDescFile!=null&&!"".equals(appDescFile)){
					String appDescFiles [] = appDescFile.split(",");
					mvc.addObject("appDescFiles", appDescFiles);
				}
				
				String appCopyright = payv2BussCompanyApp.getAppCopyright();
				//应用说明文档
				if(appCopyright!=null&&!"".equals(appCopyright)){
					String appCopyrights [] = appCopyright.split(",");
					mvc.addObject("appCopyrights", appCopyrights);
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
