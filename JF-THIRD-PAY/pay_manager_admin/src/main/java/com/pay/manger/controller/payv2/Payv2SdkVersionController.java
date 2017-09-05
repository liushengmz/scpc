
package com.pay.manger.controller.payv2;

import java.util.Date;
import java.util.HashMap;
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
import com.pay.business.payv2.entity.Payv2SdkVersion;
import com.pay.business.payv2.mapper.Payv2SdkVersionMapper;
import com.pay.business.payv2.service.Payv2SdkVersionService;
import com.pay.manger.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2SdkVersionController 
* @Description:SDK管理
* @author zhoulibo
* @date 2016年12月8日 上午11:32:42
*/
@RequestMapping("/Payv2SdkVersion/*")
@Controller
public class Payv2SdkVersionController extends BaseManagerController<Payv2SdkVersion,Payv2SdkVersionMapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Payv2SdkVersionController.class);
    @Autowired
    private Payv2SdkVersionService payv2SdkVersionService;
    /**
    * @Title: payv2SdkVersionList 
    * @Description:sdk版本管理列表
    * @param map
    * @param request
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月8日 上午11:15:05 
    * @throws
    */
    @RequestMapping("/payv2SdkVersionList")
    public ModelAndView payv2SdkVersionList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/sdk/sdkList");
		map.put("sortName", "create_time");
		map.put("sortOrder", "desc");
    	PageObject<Payv2SdkVersion> pageObject = payv2SdkVersionService.Pagequery(map);
    	mv.addObject("list", pageObject);
    	mv.addObject("map", map);
    	return mv;
    }
    
    /**
    * @Title: addSdkVersionGo 
    * @Description:添加SDK弹窗
    * @param map
    * @param request
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月8日 上午11:15:15 
    * @throws
    */
    @RequestMapping("/addPayv2SdkVersionTc")
    public ModelAndView addPayv2SdkVersionTc(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/sdk/sdk-add");
    	return mv;
    }
    /**
	 * 上线下线
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2SdkVersion", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2SdkVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id","isOnline" }, map);
		if (isNotNull) {
			try {
				payv2SdkVersionService.update(map);
				resultMap.put("resultCode", 200);
			} catch (Exception e) {
				LOGGER.error("上线下线提交失败", e);
			}
		}
		return resultMap;
	}
    
	/**
	 * 删除sdk版本
	 */
	@ResponseBody
	@RequestMapping(value = "/delPayv2SdkVersion", method = RequestMethod.POST)
	public Map<String, Object> delPayv2SdkVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			try {
				payv2SdkVersionService.delete(map);
				resultMap.put("resultCode", 200);
			} catch (Exception e) {
				LOGGER.error("删除sdk版本提交失败", e);
			}
		}
		return resultMap;
	}
	
	/**
	 * 上传sdk
	 */
	@ResponseBody
	@RequestMapping(value = "/addPayv2SdkVersion", method = RequestMethod.POST)
	public Map<String, Object> addPayv2SdkVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			payv2SdkVersionService.add(map);
			resultMap.put("resultCode", 200);
		} catch (Exception e) {
			LOGGER.error("添加sdk版本提交失败", e);
		}	
		return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @param map
	 * @param request
	 * @return
	 */
    @RequestMapping("/editPayv2SdkVersionTc")
    public ModelAndView editPayv2SdkVersionTc(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/sdk/sdk-edit");
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			Payv2SdkVersion payv2SdkVersion = payv2SdkVersionService.detail(map);
			mv.addObject("obj", payv2SdkVersion);
		}
    	return mv;
    }
    
    /**
	 * 编辑sdk
	 */
	@ResponseBody
	@RequestMapping(value = "/editPayv2SdkVersion")
	public Map<String, Object> editPayv2SdkVersion(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			try {
				map.put("updateTime", new Date());
				payv2SdkVersionService.update(map);
				resultMap.put("resultCode", 200);
			} catch (Exception e) {
				LOGGER.error("编辑sdk版本提交失败", e);
			}
		}
		return resultMap;
	}
}
