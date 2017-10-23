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

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.payv2.entity.Payv2AgentPlatform;
import com.pay.business.payv2.mapper.Payv2AgentPlatformMapper;
import com.pay.business.payv2.service.Pavy2PlatformAppService;
import com.pay.business.payv2.service.Payv2AgentPlatformService;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payv2.service.Payv2PlatformWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2AgentPlatformController
 * @Description: 代理平台表
 * @author mofan
 * @date 2016年12月28日 10:13:11
 */
@Controller
@RequestMapping("/payv2AgentPlatform")
public class Payv2AgentPlatformController extends BaseManagerController<Payv2AgentPlatform, Payv2AgentPlatformMapper> {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2AgentPlatformService payv2AgentPlatformService;
	@Autowired
	private Pavy2PlatformAppService pavy2PlatformAppService;
	@Autowired
	private Payv2ChannelService payv2ChannelService;
	@Autowired
	private Payv2PlatformWayService payv2PlatformWayService;
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	
	/**
	 * 代理平台列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2AgentPlatformList")
	public ModelAndView payv2AgentPlatformList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/agentPlat/pay_agent_platform_list");
		map.put("isDelete", 2);// 未删除
		PageObject<Payv2AgentPlatform> pageObject = payv2AgentPlatformService.getPayv2AgentPlatformList(map);
		mv.addObject("list", pageObject);
		mv.addObject("map", map);
		//获取所有渠道商
    	map = new HashMap<String, Object>();
    	map.put("isDelete", "2");//未删除
    	map.put("isAddCompany", "1");//是允许添加商户
    	map.put("isAddPlatform", "1");//1是允许代理平台
		List<Payv2Channel> payv2ChannelList = payv2ChannelService.query(map);
		mv.addObject("payv2ChannelList", payv2ChannelList);
		return mv;
	}
    
	/**
	 * 编辑代理平台 启用停用 
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2AgentPlatform", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2BussCompanyApp(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				map.put("updateTime", new Date());
				payv2AgentPlatformService.update(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				logger.error("修改代理平台提交失败", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "修改代理平台出错!");
			}
		}
		return resultMap;
	}
	
	/**
	 * 代理平台店铺列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2AgentPlatformShopList")
	public ModelAndView payv2AgentPlatformShopList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/agentPlat/pay_agent_platform_shop_list");
		if (map.get("platformId") != null) {
			try {
				mv.addObject("list",payv2BussCompanyShopService.selectByPayWayIds(map));
			} catch (Exception e) {
				logger.error("跳转代理平台店铺列表失败", e);
				e.printStackTrace();
			}
		} else {
			logger.error("代理平台线上列表查询参数出错！");
		}
		mv.addObject("map", map);
		return mv;
	}
	
	/**
	 * 代理平台App列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2AgentPlatformAppList")
	public ModelAndView payv2AgentPlatformAppList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/agentPlat/pay_agent_platform_app_list");
    	if(map.get("platformAppType")==null){
    		map.put("platformAppType", 1);
    	}
		if (map.get("platformId") != null) {
			mv.addObject("list", payv2BussCompanyAppService.selectByAppIds(map));
		} else {
			logger.error("代理平台线上列表查询参数出错！");
		}
		mv.addObject("map", map);
		return mv;
	}

	
	/**
	 * 把app绑定到代理平台
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toJoinPlatform", method = RequestMethod.POST)
	public Map<String, Object> toJoinPlatform(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "channelId","platformId","appId"}, map);
		if (isNotNull) {
			try {
				map.put("createTime", new Date());
				map.put("status", 2);//1未添加2.已添加3.已取消
				pavy2PlatformAppService.add(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				logger.error("app绑定平台提交失败", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "app绑定平台提交失败!");
			}
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "app绑定平台提交参数出错!");
		}
		resultMap.put("channelId", map.get("channelId").toString());
		resultMap.put("platformId", map.get("platformId").toString());
		return resultMap;
	}
	
}
