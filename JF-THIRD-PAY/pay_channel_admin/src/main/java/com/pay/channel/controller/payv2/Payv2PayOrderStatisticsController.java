package com.pay.channel.controller.payv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyBean;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.channel.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2PayOrderStatisticsController 
* @Description:渠道商订单数据统计
* @author zhoulibo
* @date 2017年2月9日 上午10:32:03
*/
@Controller
@RequestMapping("/Payv2PayOrderStatistics/*")
public class Payv2PayOrderStatisticsController extends BaseManagerController<Payv2PayOrder, Payv2PayOrderMapper> {
	private static final Logger logger = Logger.getLogger(Payv2PayOrderStatisticsController.class);
	//订单表
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	//商户表
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	/**
	* @Title: getOrderStatistics 
	* @Description:获取渠道商订单统计详情数据
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2017年2月9日 上午10:39:29 
	* @throws
	*/
	@ResponseBody
	@RequestMapping("getOrderStatistics")
	public Map<String,Object> getOrderStatistics(@RequestParam Map<String,Object> map){
		Map<String,Object> returnMap=new HashMap<String, Object>();
		//获取渠道商ID
		Long channelId=1L;
		//获取渠道商的接入商列表
		Payv2BussCompany payv2BussCompany=new Payv2BussCompany();
		payv2BussCompany.setChannelId(channelId);
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		List<Payv2BussCompany> companyList=	payv2BussCompanyService.selectByObject(payv2BussCompany);
		List<Payv2BussCompanyBean> companyBeanList=new ArrayList<Payv2BussCompanyBean>();
		for (Payv2BussCompany payv2BussCompany1 : companyList) {
			Payv2BussCompanyBean payv2BussCompanyBean=new Payv2BussCompanyBean();
			payv2BussCompanyBean.setChannelId(payv2BussCompany1.getChannelId());
			payv2BussCompanyBean.setCompanyName(payv2BussCompany1.getCompanyName());
			payv2BussCompanyBean.setId(payv2BussCompany1.getId());
			companyBeanList.add(payv2BussCompanyBean);
		}
		//获取各个维度详情数据
		try {
			map.put("channelId", channelId);
			returnMap=payv2PayOrderService.getDayStatistics(map);
		} catch (Exception e) {
			logger.error("查询各个维度数据出错！", e);
		}
		returnMap.put("companyList", companyBeanList);
		return returnMap;
	}
}
