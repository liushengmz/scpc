package com.pay.manger.controller.payv2;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.order.entity.Payv2PayOrderClear;
import com.pay.business.order.mapper.Payv2PayOrderClearMapper;
import com.pay.business.order.service.Payv2PayOrderClearService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.manger.controller.admin.BaseManagerController;

/**
 * @ClassName: Payv2PayOrderClearController
 * @Description:清算订单管理
 * @author mofan
 * @date 2017年02月10日 16:53:42
 */
@Controller
@RequestMapping("/payv2PayOrderClear/*")
public class Payv2PayOrderClearController extends BaseManagerController<Payv2PayOrderClear, Payv2PayOrderClearMapper> {

	private static final Logger logger = Logger.getLogger(Payv2PayOrderClearController.class);
	
	@Autowired
	private Payv2PayOrderClearService payv2PayOrderClearService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;


	/**
	 * @Title: getPayv2PayOrderList
	 * @Description:获取订单管理列表
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月7日 下午3:12:21
	 * @throws
	 */
	@RequestMapping("getPayv2PayOrderClearList")
	public ModelAndView getPayv2PayOrderList(@RequestParam Map<String, Object> map) {
		ModelAndView av = new ModelAndView("payv2/pay_order_clear_list");
		PageObject<Payv2PayOrderClear> pageList = payv2PayOrderClearService.payv2PayOrderClearList(map);
		av.addObject("list", pageList);
		// 获取商户 app 支付渠道
		Payv2BussCompany company = new Payv2BussCompany();
		company.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService
				.selectByObject(company);
		Payv2BussCompanyApp app = new Payv2BussCompanyApp();
		app.setIsDelete(2);
		List<Payv2BussCompanyApp> appList = payv2BussCompanyAppService
				.selectByObject(app);
		Payv2PayWay pay = new Payv2PayWay();
		pay.setIsDelete(2);
		List<Payv2PayWay> payList = payv2PayWayService.selectByObject(pay);
		av.addObject("companyList", companyList);
		av.addObject("appList", appList);
		av.addObject("payList", payList);
		av.addObject("map", map);
		return av;
	}
	
}
