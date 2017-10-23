package com.pay.manger.controller.payv2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.entity.Payv2BankAppKey;
import com.pay.business.payv2.mapper.Payv2BankAppKeyMapper;
import com.pay.business.payv2.service.Payv2BankAppKeyService;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2BankAppKeyController
 * @Description: 应用绑定银行key等数据
 * @author qiuguojie
 * @date 2017年04月10日 15:45:11
 */
@Controller
@RequestMapping("/payv2BankAppKey")
public class Payv2BankAppKeyController extends BaseManagerController<Payv2BankAppKey, Payv2BankAppKeyMapper> {
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private Payv2BankAppKeyService payv2BankAppKeyService;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
	/**
	 * 应用绑定银行key等数据列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2BankAppKeyList")
	public ModelAndView payv2BankAppKeyList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/bankappkey/pay_bank_app_key_list");
		PageObject<Payv2BankAppKey> list = payv2BankAppKeyService.payv2BankAppKeyList(map);
		mv.addObject("list", list);
		mv.addObject("map", map);
		return mv;
	}
	
	/**
	 * 去新增页面
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAddPayv2BankAppKey")
	public ModelAndView toAddPayv2BankAppKey(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/bankappkey/pay_bank_app_key_add");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id", map.get("appId"));
		Payv2BussCompanyApp company = payv2BussCompanyAppService.detail(param);
		if(company==null){
			return mv;
		}
		
		param.clear();
		param.put("parentId", company.getCompanyId());
		param.put("isDelete", 2);
		param.put("payWayStatus", 1);
		List<Payv2BussSupportPayWay> rateList = payv2BussSupportPayWayService.query(param);
		List<Payv2PayWayRate> rateList2 = new ArrayList<Payv2PayWayRate>();
		Set<Integer> rateIdSet = new HashSet<Integer>();
		
		//查询已经绑定过银行key的通道
		List<Payv2BankAppKey> payv2BankAppKeyList = payv2BankAppKeyService.query(map);
		for(Payv2BankAppKey payv2BankAppKey : payv2BankAppKeyList){
			if(payv2BankAppKey.getRateId() != null && !rateIdSet.contains(Integer.valueOf(payv2BankAppKey.getRateId().toString()))){
				rateIdSet.add(Integer.valueOf(payv2BankAppKey.getRateId().toString()));
			}
		}
		
		param.clear();
		//过滤掉已经绑定过银行key的通道
		for(Payv2BussSupportPayWay payv2BussSupportPayWay : rateList){
			if(null!=payv2BussSupportPayWay.getRateId()&&!rateIdSet.contains(Integer.valueOf(payv2BussSupportPayWay.getRateId().toString()))){
				param.put("id", payv2BussSupportPayWay.getRateId());
				Payv2PayWayRate payv2PayWayRate = payv2PayWayRateService.detail(param);
				rateList2.add(payv2PayWayRate);
			}
		}
		mv.addObject("rateList", rateList2);
		mv.addObject("map", map);
		return mv;
	}
	
	/**
	 * 添加银行绑定
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addPayv2BankAppKey", method = RequestMethod.POST)
	public Map<String, Object> AddPayv2BankAppKey(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			payv2BankAppKeyService.add(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "添加绑定错误!");
		}	
		return resultMap;
	}
}
