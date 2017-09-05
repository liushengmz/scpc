package com.pay.manger.controller.payv2;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.record.entity.Payv2BussWayDetail;
import com.pay.business.record.mapper.Payv2BussWayDetailMapper;
import com.pay.business.record.service.Payv2BussWayDetailService;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * @ClassName Payv2BussWayDetailController
 * @Description:商户支付通道明细
 * @author zhangheng
 * @date
 * 
 */
@RequestMapping("/Payv2BussWayDetail/*")
@Controller
public class Payv2BussWayDetailController extends
		BaseManagerController<Payv2BussWayDetail, Payv2BussWayDetailMapper> {

	@Autowired
	private Payv2BussWayDetailService payv2BussWayDetailService;// 商户账单明细

	private static final Logger LOGGER = LoggerFactory.getLogger(Payv2BussWayDetailController.class);

	/**
	 * 根据当前商户账单号查询当前账单明细
	 * 
	 * @param map
	 * @return
	 */
	
	@RequestMapping("showDetailDay")
	public ModelAndView showDetailDay(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("payv2/companyMoneyClear/dayMoneyDetail");
		System.out.println(map.get("clearTime"));
		Map<String, Object> dataMap = payv2BussWayDetailService.dayBussWayDetaiList(map);
		
		List<Payv2BussWayDetail> bussDetailList =(List<Payv2BussWayDetail>) dataMap.get("bussDetailList");
		Payv2BussCompany payv2BussCompany = (Payv2BussCompany) dataMap.get("payv2BussCompany");
		
		mv.addObject("bussDetailList", bussDetailList);
		mv.addObject("payv2BussCompany", payv2BussCompany);
		mv.addObject("map", map);
		return mv;
	}
	
	

	/**
	 * 月账单明细
	 * 
	 * @param map
	 * @return
	 */	
	@RequestMapping("showDetailMouth")
	public ModelAndView showDetailMouth(@RequestParam  Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("payv2/companyMoneyClear/mouthMoneyDetail");	
		Map<Long, Payv2BussWayDetail> mapData = payv2BussWayDetailService.mouthBussWayDetaiList(map);
		
		mv.addObject("mapData", mapData);
		mv.addObject("map", map);
		
		return mv;
	}

}
