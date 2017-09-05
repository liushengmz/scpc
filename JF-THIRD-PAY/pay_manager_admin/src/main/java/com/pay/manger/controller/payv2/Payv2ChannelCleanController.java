package com.pay.manger.controller.payv2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.order.entity.OrderClearVO;
import com.pay.business.order.service.Payv2PayOrderClearService;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.manger.controller.base.BaseController;

/**
 * <p>Title:渠道对账</p>
 * <p>Description: </p>
 * @author yy
 * 2017年8月3日
 */
@Controller
@RequestMapping("/channelClean")
public class Payv2ChannelCleanController extends BaseController {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private Payv2PayOrderClearService orderService;
	
	@Autowired
	private Payv2PayWayRateService wayService;
	
	/**
	 * @return	跳转用
	 */
	@RequestMapping("/channelClean")
	public ModelAndView showDetailDay() {
		ModelAndView mv = new ModelAndView("payv2/companyMoneyClear/channelClean");	
		return mv;
	}
	
	/**
	 * @return	跳转用
	 */
	@RequestMapping("/differDetail")
	public ModelAndView differDetail(String date, String rateid) {
		ModelAndView mv = new ModelAndView("payv2/companyMoneyClear/differDetail");	
		mv.addObject("date", date);
		mv.addObject("rateid", rateid);
		return mv;
	}
	
	/**
	 * @return	获取待对账账单时间
	 */
	@RequestMapping(value = "/getBills", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getBills() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<String> bills = orderService.getBills();
			result.put("code", 200);
			result.put("data", bills);
		} catch (Exception e) {
			result.put("code", 101);
			result.put("msg", e.getMessage());
			logger.error("查询待对账账单", e);
		}
		return result;
	}
	
	/**
	 * @return	根据时间获取对账列表
	 */
	@RequestMapping(value = "/getBillList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getBillList(String date) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Assert.notNull(date, "日期不能为空！");
			List<Map<String, Object>> billList = orderService.getBillList(date);
			result.put("code", 200);
			result.put("data", billList);
		} catch (Exception e) {
			result.put("code", 101);
			result.put("msg", e.getMessage());
			logger.error("查询待对账账单", e);
		}
		return result;
	}
	
	/**
	 * @return	获取差错列表
	 */
	@RequestMapping(value = "/getDifferOrder", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDifferOrder(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			PageObject<OrderClearVO> differOrder = orderService.getDifferOrder(map);
			result.put("code", 200);
			result.put("data", differOrder);
		} catch (Exception e) {
			result.put("code", 101);
			result.put("msg", e.getMessage());
			logger.error("获取差错订单", e);
		}
		return result;
	}
	
	/**
	 * 平帐
	 */
	@RequestMapping(value = "/updateClear", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> updateClear(String ids){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			orderService.updateClear(ids);
			result.put("code", 200);
		} catch (Exception e) {
			result.put("code", 101);
			result.put("msg", e.getMessage());
			logger.error("平帐", e);
		}
		return result;
	}
	
	/**
	 * 对账
	 */
	@RequestMapping(value = "/toBills", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> toBills(String date, String rateid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> oderAndRefundByTime = orderService.getOderAndRefundByTime(date, Long.valueOf(rateid));
			Payv2PayWayRate rate = wayService.queryByid(Long.valueOf(rateid));
			String payName = "";
			if (rate.getPayViewType() == 1) {
				payName = "支付宝";
			} else if (rate.getPayViewType() == 2) {
				payName = "微信";
			}
			orderService.job(date, date.replace("-", ""), oderAndRefundByTime.get(1), oderAndRefundByTime.get(0), payName, rate);
			result.put("code", 200);
		} catch (Exception e) {
			result.put("code", 101);
			result.put("msg", e.getMessage());
			logger.error("平帐", e);
		}
		return result;
	}
	
	/**
	 * 出账
	 */
	@RequestMapping(value = "/outOrder", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> outOrder(String date, String rateid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Assert.notNull(date, "日期不能为空！");
			Assert.notNull(rateid, "渠道不能为空！");
			orderService.updateOutOrder(date, rateid);
			result.put("code", 200);
		} catch (Exception e) {
			result.put("code", 101);
			result.put("msg", e.getMessage());
			logger.error("出账", e);
		}
		return result;
	}
}
