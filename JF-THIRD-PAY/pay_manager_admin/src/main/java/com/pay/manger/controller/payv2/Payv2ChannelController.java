package com.pay.manger.controller.payv2;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.core.teamwork.base.util.encrypt.MD5;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.entity.Payv2ChannelRate;
import com.pay.business.merchant.mapper.Payv2ChannelMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.merchant.service.Payv2ChannelRateService;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.service.Payv2AgentPlatformService;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payv2.service.Payv2PlatformWayService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2ChannelController
 * @Description: 渠道商表
 * @author mofan
 * @date 2016年12月10日 15:13:11
 */
@Controller
@RequestMapping("/payv2Channel")
public class Payv2ChannelController extends BaseManagerController<Payv2Channel, Payv2ChannelMapper> {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2ChannelService payv2ChannelService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;// APP
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;// 支付通道
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;// 支付方式
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;// 商铺
    @Autowired
    private Payv2PayWayService payv2PayWayService;
    @Autowired 
    private Payv2AgentPlatformService payv2AgentPlatformService;
    @Autowired
    private Payv2PlatformWayService payv2PlatformWayService;
    @Autowired
    private Payv2PayWayRateService payv2PayWayRateService;
    @Autowired
    private Payv2ChannelRateService payv2ChannelRateService;
	/**
	 * 渠道商列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2ChannelList")
	public ModelAndView payv2BussCompanyAppList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/channel/pay_channel_list");
		map.put("isDelete", 2);// 未删除
		PageObject<Payv2Channel> pageObject = payv2ChannelService.payv2ChannelList(map);
		mv.addObject("list", pageObject);
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 跳转新增渠道商
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAddPayv2Channel")
	public ModelAndView toAddPayv2Channel(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("payv2/channel/pay_channel_add_new");
		view.addObject("map", map);
    	//获取所有第三方支付钱包
    	map = new HashMap<String, Object>();
    	map.put("isDelete", "2");//删除
    	map.put("status", "1");//1启用
    	Payv2PayWay payv2PayWay = new Payv2PayWay();
		payv2PayWay.setIsDelete(2);
		payv2PayWay.setStatus(1);
		List<Payv2PayWay> payList = payv2PayWayService.selectByObject(payv2PayWay);
    	List<Payv2PayWay> payv2PayWayList = payv2PayWayService.query(map);
    	view.addObject("payv2PayWayList", payv2PayWayList);
    	view.addObject("payList", payList);
		return view;
	}

	/**
	 * 添加渠道商
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addPayv2Channel")
	public Map<String, Object> addPayv2Channel(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			if (map.get("channelLoginPwd") != null && !"".equals(map.get("channelLoginPwd").toString())) {
				String password =MD5.GetMD5Code(map.get("channelLoginPwd").toString());
				map.put("channelLoginPwd", password);
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "添加渠道商出错,请输入登录密码！");
				return resultMap;
			}
			resultMap = payv2ChannelService.addChannel(map);;
		} catch (Exception e) {
			logger.error("添加渠道商出错！", e);
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "添加渠道商出错！");
		}
		return resultMap;
	}

	/**
	 * 编辑渠道商
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toEditPayv2Channel")
	public ModelAndView toEditPayv2Channel(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/channel/pay_channel_edit_new");
		try {
			if (null != map.get("id")) {
				Payv2Channel payv2Channel = payv2ChannelService.detail(map);
				mvc.addObject("payv2Channel", payv2Channel);
				
				map.put("channelId", payv2Channel.getId());
				
				List<Payv2ChannelRate> wayList = payv2ChannelRateService.queryByChannelId(map);
				// 路由列表
				Map<String, Object> param = new HashMap<>();
				String payWayList = "";
				for (Payv2ChannelRate payv2ChannelRate : wayList) {
					if (payv2ChannelRate.getRateId() != null) {
						payWayList += payv2ChannelRate.getPayWayId() + "-" + payv2ChannelRate.getRateId() + "-"
								+ payv2ChannelRate.getPayWayRate() + ",";
					} else {
						payWayList += payv2ChannelRate.getPayWayId() + "-0-" + payv2ChannelRate.getPayWayRate() + ",";
					}
					param.put("payWayId", payv2ChannelRate.getPayWayId());
					param.put("isDelete", 2);
					param.put("status", 1);
					List<Payv2PayWayRate> rateList = payv2PayWayRateService.query(param);
					payv2ChannelRate.setRateList(rateList);
				}
				if (payWayList != "") {
					payWayList = payWayList.substring(0, payWayList.length() - 1);
				}
				// 支付通道列表
				Payv2PayWay payv2PayWay = new Payv2PayWay();
				payv2PayWay.setIsDelete(2);
				payv2PayWay.setStatus(1);
				List<Payv2PayWay> payList = payv2PayWayService.selectByObject(payv2PayWay);
				mvc.addObject("payList", payList);
				mvc.addObject("wayList", wayList);
				mvc.addObject("obj", payv2Channel);
				mvc.addObject("map", map);
				mvc.addObject("payWayList", payWayList);
			}
		} catch (Exception e) {
			logger.error(" 跳转渠道商编辑页面报错", e);
		}
		return mvc;
	}

	/**
	 * 
	 * @Title: updatePayv2Channel
	 * @Description:恢復、終止、删除
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月12日 下午4:31:32
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2Channel", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2Channel(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			try {
				Map<String, Object> param = new HashMap<>();
				param.put("id", map.get("id"));
				Payv2Channel payv2Channel = payv2ChannelService.detail(param);
				if (map.get("channelLoginPwd") != null && !"".equals(map.get("channelLoginPwd").toString())) {
					//如果前端传的加密值与预设的加密值一样；证明用户没有修改密码；故设为null;
					if (!"4c6e33e575da86adc6df9d85790c75ce".equals(String.valueOf(map.get("channelLoginPwd").toString()))) {
						if (!payv2Channel.getChannelLoginPwd().equals(map.get("channelLoginPwd").toString())) {
							String password =MD5.GetMD5Code(map.get("channelLoginPwd").toString());
							map.put("channelLoginPwd", password);
						}
					} else {
						map.put("channelLoginPwd", null);
					}
				}
				// 終止旗下的商戶
				if(map.get("channelStatus")!=null){
					int channelStatus = Integer.valueOf(map.get("channelStatus").toString());
					if (channelStatus == 2) {// 终止合作
						map.put("type", 1);
						updateAndDel(map);
					}
				}
				//删除
				if(map.get("isDelete")!=null){
					map.put("type",2);
					updateAndDel(map);
				}
				map.put("updateTime", new Date());
				// 启动/终止/删除：渠道商
				
				resultMap = payv2ChannelService.updateChannel(map, payv2Channel);
			} catch (Exception e) {
				logger.error("修改渠道商提交失败", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "修改渠道商出错！");
			}
		}
		return resultMap;
	}

	// 公共方法
	public void updateAndDel(Map<String, Object> map) throws Exception {
		// 获取商户列表
		int type = Integer.valueOf(map.get("type").toString());
		Long channelId = Long.valueOf(map.get("id").toString());
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setIsDelete(2);
		if(type==1){
			payv2BussCompany.setCompanyStatus(2);
		}
		payv2BussCompany.setChannelId(channelId);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectByObject(payv2BussCompany);
		for (Payv2BussCompany payv2BussCompany2 : companyList) {
			// 获取了商户就去修改商户支持的支付通道
			Payv2BussSupportPayWay payv2BussSupportPayWay = new Payv2BussSupportPayWay();
			payv2BussSupportPayWay.setParentId(payv2BussCompany2.getId());// 商户ID
			payv2BussSupportPayWay.setIsDelete(2);
			if(type==1){
				payv2BussSupportPayWay.setPayWayStatus(1);
			}
			List<Payv2BussSupportPayWay> supportPayWayList = payv2BussSupportPayWayService.selectByObject(payv2BussSupportPayWay);
			for (Payv2BussSupportPayWay payv2BussSupportPayWay2 : supportPayWayList) {
				// 获取支付方式列表
				Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
				payv2BussAppSupportPayWay.setPayWayId(payv2BussSupportPayWay2.getId());
				payv2BussAppSupportPayWay.setIsDelete(2);
				if(type==1){
					payv2BussAppSupportPayWay.setStatus(1);
				}
				List<Payv2BussAppSupportPayWay> appSupportPayWayList = payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
				for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : appSupportPayWayList) {
					payv2BussAppSupportPayWay2.setUpdateTime(new Date());
					// 终止
					if (type == 1) {
						payv2BussAppSupportPayWay2.setStatus(2);
					}// 删除
					if (type == 2) {
						payv2BussAppSupportPayWay2.setIsDelete(1);
					}
					payv2BussAppSupportPayWay2.setStatus(2);
					payv2BussAppSupportPayWayService.update(payv2BussAppSupportPayWay2);
				}
				// 终止
				if (type == 1) {
					payv2BussSupportPayWay2.setPayWayStatus(2);
				}
				// 删除
				if (type == 2) {
					payv2BussSupportPayWay2.setIsDelete(1);
				}
				payv2BussSupportPayWay2.setUpdateTime(new Date());
				payv2BussSupportPayWayService.update(payv2BussSupportPayWay2);
			}
			// 获取商户APP：app不启动
			Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
			payv2BussCompanyApp.setCompanyId(payv2BussCompany2.getId());
			payv2BussCompanyApp.setIsDelete(2);
			if(type==1){
				payv2BussCompanyApp.setAppStatus(2);
			}
			List<Payv2BussCompanyApp> appList = payv2BussCompanyAppService.selectByObject(payv2BussCompanyApp);
			for (Payv2BussCompanyApp payv2BussCompanyApp2 : appList) {
				// 终止
				if (type == 1) {
					payv2BussCompanyApp2.setAppStatus(4);
				}
				// 删除
				if (type == 2) {
					payv2BussCompanyApp2.setIsDelete(1);
				}
				payv2BussCompanyApp2.setUpdateTime(new Date());
				payv2BussCompanyAppService.update(payv2BussCompanyApp2);

			}
			// 商铺列表:修改商铺为不启动
			Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
			payv2BussCompanyShop.setCompanyId(payv2BussCompany2.getId());
			payv2BussCompanyShop.setIsDelete(2);
			if(type==1){
				payv2BussCompanyShop.setShopStatus(2);
			}
			List<Payv2BussCompanyShop> ShopList = payv2BussCompanyShopService.selectByObject(payv2BussCompanyShop);
			for (Payv2BussCompanyShop payv2BussCompanyShop2 : ShopList) {
				payv2BussCompanyShop2.setUpdateTime(new Date());
				// 终止
				if (type == 1) {
					payv2BussCompanyShop2.setShopStatus(4);
				}
				// 删除
				if (type == 2) {
					payv2BussCompanyShop2.setIsDelete(1);
				}

				payv2BussCompanyShopService.update(payv2BussCompanyShop2);
			}
			// 终止
			if (type == 1) {
				payv2BussCompany2.setCompanyStatus(4);
			}
			// 删除
			if (type == 2) {
				payv2BussCompany2.setIsDelete(1);
			}
			payv2BussCompany2.setUpdateTime(new Date());
			payv2BussCompanyService.update(payv2BussCompany2);
		}
	}
	

	/**
	 * 验证后台登录帐号是否存在
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validateChannelLoginName")
	public Map<String, Object> validateChannelLoginName(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(map.get("channelLoginName") != null){
				map.put("isDelete", 2);
				Payv2Channel payv2Channel = payv2ChannelService.detail(map);
				if(payv2Channel != null){
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
				}else{
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
				}
			}else{
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
			}
			
		} catch (Exception e) {
			logger.error("验证后台登录帐号是否存在出错！", e);
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "验证后台登录帐号是否存在出错！");
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping("/getPayWayRate")
	public Map<String, Object> getPayWayRate(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		map.put("isDelete", 2);
		map.put("status", 1);
		List<Payv2PayWayRate> payList = payv2PayWayRateService.query(map);
		List<Payv2PayWayRate> list = new ArrayList<Payv2PayWayRate>();
		String existIds = map.get("existIds").toString();
		if(existIds.equals("")) {
			list = payList;
		} else {
			List<String> existIdList = Arrays.asList(existIds.split(","));
			for (Payv2PayWayRate rate : payList) {
				if(!existIdList.contains(String.valueOf(rate.getId()))) {
					list.add(rate);
				}
			}
		}
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, list);
		return resultMap;
	}
	
	/**
	 * 跳转修改渠道商密码
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
//	@RequestMapping("/toUpdatePayv2ChannelPwd")
//	public ModelAndView toUpdatePayv2ChannelPwd(@RequestParam Map<String, Object> map, HttpServletRequest request) {
//		ModelAndView view = new ModelAndView("payv2/channel/pay_channel_add_new");
//		view.addObject("map", map);
//    	//获取所有第三方支付钱包
//    	map = new HashMap<String, Object>();
//    	map.put("isDelete", "2");//删除
//    	map.put("status", "1");//1启用
//    	Payv2PayWay payv2PayWay = new Payv2PayWay();
//		payv2PayWay.setIsDelete(2);
//		payv2PayWay.setStatus(1);
//		List<Payv2PayWay> payList = payv2PayWayService.selectByObject(payv2PayWay);
//    	List<Payv2PayWay> payv2PayWayList = payv2PayWayService.query(map);
//    	view.addObject("payv2PayWayList", payv2PayWayList);
//    	view.addObject("payList", payList);
//		return view;
//	}
}
