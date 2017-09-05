
package com.pay.channel.controller.payv2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.payv2.entity.Payv2AppVersion;
import com.pay.business.payv2.mapper.Payv2AppVersionMapper;
import com.pay.business.payv2.service.Payv2AppVersionService;
import com.pay.channel.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2AppVersionController 
* @Description:APP版本管理
* @author zhoulibo
* @date 2016年12月29日 上午9:59:21
*/
@RequestMapping("/Payv2AppVersion/*")
@Controller
public class Payv2AppVersionController extends BaseManagerController<Payv2AppVersion,Payv2AppVersionMapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Payv2AppVersionController.class);
    @Autowired
    private Payv2AppVersionService payv2AppVersionService;
    @Autowired
    private Payv2BussCompanyAppService payv2BussCompanyAppService;
    /**
    * @Title: payv2SdkVersionList 
    * @Description:获取APP版本管理列表
    * @param map
    * @param request
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月29日 上午9:58:22 
    * @throws
    */
    @RequestMapping("/Payv2AppVersionList")
    public ModelAndView Payv2AppVersionList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("version/payv2AppVersion_list");
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "appId" }, map);
		if(isNotNull){
			map.put("sortName", "create_time");
			map.put("sortOrder", "desc");
			map.put("isDelete", 2);
			PageObject<Payv2AppVersion> pageObject = payv2AppVersionService.Pagequery(map);
			List<Payv2AppVersion> appVersionList=pageObject.getDataList();
			for (Payv2AppVersion payv2AppVersion : appVersionList) {
				//获取所属应用名字
				Payv2BussCompanyApp payv2BussCompanyApp=new Payv2BussCompanyApp();
				payv2BussCompanyApp.setId(payv2AppVersion.getAppId());
				payv2BussCompanyApp=payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
				if(payv2BussCompanyApp!=null){
					payv2AppVersion.setAppName(payv2BussCompanyApp.getAppName());
				}
				
			}
			mv.addObject("list", pageObject);
			mv.addObject("map", map);
		}else{
			LOGGER.error("参数错误");
		}
    	return mv;
    }
    /**
    * @Title: addPayv2AppVersionTc 
    * @Description:添加APP弹窗 
    * @param map
    * @param request
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月29日 上午11:30:00 
    * @throws
     */
    @RequestMapping("/addPayv2AppVersionTc")
    public ModelAndView addPayv2AppVersionTc(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("version/payv2AppVersion_add");
		//获取所属应用名字
		Payv2BussCompanyApp payv2BussCompanyApp=new Payv2BussCompanyApp();
		payv2BussCompanyApp.setId(Long.valueOf(map.get("appId").toString()));
		payv2BussCompanyApp=payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
		if(payv2BussCompanyApp!=null){
			map.put("appName", payv2BussCompanyApp.getAppName());
		}
		mv.addObject("map",map);
    	return mv;
    }
    
	/**
	* @Title: addPayv2AppVersion 
	* @Description:添加APP提交
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年12月29日 上午11:29:41 
	* @throws
	*/
	@ResponseBody
	@RequestMapping(value = "/addPayv2AppVersion")
	public Map<String, Object> addPayv2AppVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			map.put("status", 2);
			map.put("isDelete", 2);
			if(Integer.valueOf(map.get("appType").toString())==3){
				map.put("appVersion", map.get("appVersion1"));
				map.put("appVersionCode", map.get("appVersionCode1"));
				map.put("appFileUrl", map.get("appFileUrl1"));
				map.remove("appVersion1");
				map.remove("appVersionCode1");
				map.remove("appFileUrl1");
				
			}
			payv2AppVersionService.add(map);
			resultMap.put("resultCode", 200);
		} catch (Exception e) {
			LOGGER.error("添加sdk版本提交失败", e);
		}	
		return resultMap;
	} 
	
	/**
	 * 删除sdk版本
	 */
	@ResponseBody
	@RequestMapping(value = "/delPayv2AppVersion", method = RequestMethod.POST)
	public Map<String, Object> delPayv2AppVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			try {
				map.put("updateTime", new Date());
				map.put("isDelete",1);//删除
				payv2AppVersionService.update(map);
				resultMap.put("resultCode", 200);
			} catch (Exception e) {
				LOGGER.error("删除sdk版本提交失败", e);
			}
		}
		return resultMap;
	}	
   /**
   * @Title: updatePayv2AppVersion 
   * @Description:上线下线
   * @param map
   * @return    设定文件 
   * @return Map<String,Object>    返回类型 
   * @date 2016年12月29日 下午2:38:59 
   * @throws
    */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2AppVersion", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2AppVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id","status" }, map);
		if (isNotNull) {
			try {
				map.put("updateTime", new Date());
				payv2AppVersionService.update(map);
				resultMap.put("resultCode", 200);
			} catch (Exception e) {
				LOGGER.error("上线下线提交失败", e);
			}
		}
		return resultMap;
	}
	/**
	* @Title: getPassReason 
	* @Description:获取没有通过的原因
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月29日 下午2:42:37 
	* @throws
	*/
	@RequestMapping(value = "/getPassReason")
	public ModelAndView getPassReason(@RequestParam Map<String, Object> map){
		ModelAndView mv = new ModelAndView("version/payv2AppVersion_view");
		Payv2AppVersion payv2AppVersion=new Payv2AppVersion();
		payv2AppVersion.setId(Long.valueOf(map.get("id").toString()));
		payv2AppVersion=payv2AppVersionService.selectSingle(payv2AppVersion);
		if(payv2AppVersion!=null){
			mv.addObject("payv2AppVersion", payv2AppVersion);
		}
		return mv;
	}
	/**
	 * 查看商户APP下载
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toDownload")
	public ModelAndView toDownload(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("version/payv2AppVersion_download");
		Payv2AppVersion payv2AppVersion = new Payv2AppVersion();
		try {
			if (null != map.get("id")) {
				payv2AppVersion = payv2AppVersionService.detail(map);
				mvc.addObject("payv2AppVersion", payv2AppVersion);
			}
		} catch (Exception e) {
			LOGGER.error(" 查看商户APP页面报错", e);
		}
		return mvc;
	}
}
