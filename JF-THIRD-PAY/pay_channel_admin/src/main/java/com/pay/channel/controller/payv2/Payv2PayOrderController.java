package com.pay.channel.controller.payv2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderBean;
import com.pay.business.order.entity.Payv2PayOrderBeanVO;
import com.pay.business.order.entity.Payv2PayOrderGroup;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.service.Payv2PayOrderGroupService;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.CSVUtils;
import com.pay.channel.controller.admin.BaseManagerController;

/**
 * @ClassName: Payv2PayOrderController
 * @Description:订单管理
 * @author zhoulibo
 * @date 2016年12月7日 下午3:10:42
 */
@Controller
@RequestMapping("/Payv2PayOrder/*")
public class Payv2PayOrderController extends BaseManagerController<Payv2PayOrder, Payv2PayOrderMapper> {

	private static final Logger logger = Logger.getLogger(Payv2PayOrderController.class);
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;
	@Autowired
	private Payv2PayOrderGroupService payv2PayOrderGroupService;

	/**
	 * @Title: getPayv2PayOrderList
	 * @Description:获取订单管理列表
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月7日 下午3:12:21
	 * @throws
	 */
	@RequestMapping("getPayv2PayOrderList")
	public ModelAndView getPayv2PayOrderList(@RequestParam Map<String, Object> map) {
		ModelAndView av = new ModelAndView("payv2/payv2PayOrder-list");
		// 渠道商ID
		Long channelId = getAdmin().getId();
		// 根据渠道商ID 获取渠道下面的商户
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(channelId);
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		// 商户列表
		List<Payv2BussCompany> list = payv2BussCompanyService.selectByObject(payv2BussCompany);
		List<Long> pList = new ArrayList<Long>();
		for (Payv2BussCompany payv2BussCompany2 : list) {
			pList.add(payv2BussCompany2.getId());
		}
		if (pList.size() > 0) {
			// 查询订单
			map.put("companyIdList", pList);
			map.put("type", 1);
			PageObject<Payv2PayOrder> pageList = payv2PayOrderService.getPageObject(map, 1);
			List<Payv2PayOrder> orderList = pageList.getDataList();
			for (Payv2PayOrder payv2PayOrder : orderList) {
				// 获取商户名字
				/*Long companyId = payv2PayOrder.getCompanyId();
				Payv2BussCompany company = new Payv2BussCompany();
				company.setId(companyId);
				company = payv2BussCompanyService.selectSingle(company);
				if (company != null) {
					payv2PayOrder.setCompanyName(company.getCompanyName());
				}
				// 获取APP名字
				Long appId = payv2PayOrder.getAppId();
				Payv2BussCompanyApp app = new Payv2BussCompanyApp();
				app.setId(appId);
				app = payv2BussCompanyAppService.selectSingle(app);
				if (app != null) {
					payv2PayOrder.setAppName(app.getAppName());
				}
				// 获取支付渠道
				// 获取APP名字
				Long payWayId = payv2PayOrder.getPayWayId();
				if (payWayId != null) {
					Payv2PayWay pay = new Payv2PayWay();
					pay.setId(payWayId);
					pay = payv2PayWayService.selectSingle(pay);
					if (pay != null) {
						payv2PayOrder.setWayName(pay.getWayName());
					}
				}
				// 獲取退款金額
				Payv2PayOrderRefund payv2PayOrderRefund = new Payv2PayOrderRefund();
				payv2PayOrderRefund.setOrderId(payv2PayOrder.getId());
				// payv2PayOrderRefund.setChannelId(channelId);
				List<Payv2PayOrderRefund> list2 = payv2PayOrderRefundService.selectByObject(payv2PayOrderRefund);
				double refundMoney = 0.00;
				for (Payv2PayOrderRefund payv2PayOrderRefund2 : list2) {
					refundMoney = refundMoney + payv2PayOrderRefund2.getRefundMoney();
				}
				payv2PayOrder.setRefundMoney(refundMoney);*/
				//查询所属分类
				if(payv2PayOrder.getGroupId()!=null){
					Map<String,Object> map2=new HashMap<String, Object>();
					map2.put("id", payv2PayOrder.getGroupId());
					Payv2PayOrderGroup payv2PayOrderGroup=payv2PayOrderGroupService.detail(map2);
					if(payv2PayOrderGroup!=null){
						payv2PayOrder.setGroupValue(payv2PayOrderGroup.getGroupValue());
					}
				}
			}
//			// 获取商户 app 支付渠道
//			Payv2BussCompany company = new Payv2BussCompany();
//			company.setIsDelete(2);
//			List<Payv2BussCompany> companyList = payv2BussCompanyService.selectByObject(company);
			//APP列表
			map.put("appStatus", 2);
			map.put("isDelete", 2);
			List<Payv2BussCompanyApp> appList = payv2BussCompanyAppService.selectByObject2(map);
			//支付通道列表
			Payv2PayWay pay = new Payv2PayWay();
			pay.setIsDelete(2);
			pay.setStatus(1);
			List<Payv2PayWay> payList = payv2PayWayService.selectByObject(pay);
			//查询分类列表
			if(appList.size()>0){
				List<Long> appIdList = new ArrayList<Long>();
				for (Payv2BussCompanyApp Payv2BussCompanyApp : appList) {
					appIdList.add(Payv2BussCompanyApp.getId());
				}
				map.put("appIdList", appIdList);
				List<Payv2PayOrderGroup> groupList=payv2PayOrderGroupService.getGroupList(map);
				av.addObject("groupList", groupList);
			}
			av.addObject("companyList", list);
			av.addObject("appList", appList);
			av.addObject("payList", payList);
			av.addObject("map", map);
			av.addObject("list", pageList);
			
		}
		return av;
	}

	/**
	 * @Title: getRefundOrder
	 * @Description:获取退款订单列表
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月27日 上午9:27:21
	 * @throws
	 */
	@RequestMapping()
	public ModelAndView getRefundOrder(@RequestParam Map<String, Object> map) {
		ModelAndView mav = new ModelAndView("payv2/payv2PayRefundOrder-list");
		Payv2PayOrderRefund payv2PayOrderRefund = new Payv2PayOrderRefund();
		// Long channelId=getAdmin().getId();
		payv2PayOrderRefund.setOrderId(Long.valueOf(map.get("orderId").toString()));
		// payv2PayOrderRefund.setChannelId(channelId);
		List<Payv2PayOrderRefund> list = payv2PayOrderRefundService.selectByObject(payv2PayOrderRefund);
		double payDiscountMoney = Double.valueOf(map.get("payDiscountMoney").toString());
		for (Payv2PayOrderRefund payv2PayOrderRefund2 : list) {
			double surplusMoney = payDiscountMoney - payv2PayOrderRefund2.getRefundMoney();
			payv2PayOrderRefund2.setSurplusMoney(surplusMoney);
			payv2PayOrderRefund2.setPayDiscountMoney(payDiscountMoney);
		}
		mav.addObject("dataList", list);
		return mav;
	}
	
	/**
	 * @Title: getOrderDetail
	 * @Description:获取订单详情
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月27日 上午9:27:21
	 * @throws
	 */
	@RequestMapping("getOrderDetail")
	public ModelAndView getOrderDetail(@RequestParam Map<String, Object> map) {
		ModelAndView mav = new ModelAndView("payv2/payv2PayOrder_detail");
		Payv2PayOrder payv2PayOrder = new Payv2PayOrder();
		payv2PayOrder.setId(Long.valueOf(map.get("orderId").toString()));
		payv2PayOrder = payv2PayOrderService.selectSingleDetail(payv2PayOrder);
		mav.addObject("payv2PayOrder", payv2PayOrder);
		return mav;
	}
	
	/**
	* @Title: exportExcelOrder 
	* @Description:订单导出
	* @param map
	* @param workbook
	* @param request
	* @param response
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2017年2月10日 下午5:29:47 
	* @throws
	*/
	@ResponseBody
	@RequestMapping("/exportExcelOrder")
	public Map<String, Object> exportExcelOrder(@RequestParam Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		/*Assert.notNull(map.get("createTime"), "订单时间不能为空！");*/
		// 渠道商ID
		Long channelId = getAdmin().getId();
		// 根据渠道商ID 获取渠道下面的商户
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(channelId);
		payv2BussCompany.setIsDelete(2);
		// 商户列表
		List<Payv2BussCompany> list = payv2BussCompanyService.selectByObject(payv2BussCompany);
		List<Long> pList = new ArrayList<Long>();
		for (Payv2BussCompany payv2BussCompany2 : list) {
			pList.add(payv2BussCompany2.getId());
		}
		if (pList.size() > 0) {
			// 查询订单
			map.put("companyIdList", pList);
			map.put("type", 1);
			map.put("curPage", 1);
			map.put("pageData", 999999999);
			PageObject<Payv2PayOrder> pageList = payv2PayOrderService.getPageObject(map, 1);
			List<Payv2PayOrder> orderList = pageList.getDataList();
			try {
				if (null != orderList && orderList.size() > 0) {
					// 导出
					CSVUtils<Object> csv = new CSVUtils<Object>();
					//TestExportExcel<Payv2PayOrderBean> ex = new TestExportExcel<Payv2PayOrderBean>();
					//Object[] headers = { "支付集订单号", "商户订单号", "来源商户", "来源应用", "支付渠道", "订单金额(元)", "实际支付金额(元)", "优惠金额(元)", "退款金额(元)", "订单状态", "创建时间", "订单支付时间", "订单回调时间" };
					
					Object[] headers = { "平台订单号", "商户订单号", "来源商户", "来源应用", "支付渠道","订单名词","订单金额(元)","服务费", "订单状态", "交易时间"};
					//List<Payv2PayOrderBean> dataset = new ArrayList<Payv2PayOrderBean>();
					List<Object> dataset = new ArrayList<Object>();
					for (Payv2PayOrder payv2PayOrder : orderList) {
						Payv2PayOrderBeanVO bte = new Payv2PayOrderBeanVO();
						// 平台订单号
						bte.setOrderNum(payv2PayOrder.getOrderNum());
						// 商户订单号
						bte.setMerchantOrderNum(payv2PayOrder.getMerchantOrderNum());
						// 来源商户
						bte.setCompanyName(payv2PayOrder.getCompanyName());
						// 来源应用
						bte.setAppName(payv2PayOrder.getAppName());
						// 支付渠道
						bte.setWayName(payv2PayOrder.getWayName());
						//订单名称
						bte.setOrderName(payv2PayOrder.getOrderName());
						// 订单金额(元)
						bte.setPayMoney(payv2PayOrder.getPayMoney());
						//手续费
						bte.setPayWayMoney(payv2PayOrder.getPayWayMoney());
						// 订单状态
						if (payv2PayOrder.getPayStatus() == 1) {
							bte.setPayStatusName("支付成功");
						} else if (payv2PayOrder.getPayStatus() == 2) {
							bte.setPayStatusName("支付失败");
						} else if (payv2PayOrder.getPayStatus() == 3) {
							bte.setPayStatusName("未支付");
						} else if (payv2PayOrder.getPayStatus() == 4) {
							bte.setPayStatusName("超时");
						} else if (payv2PayOrder.getPayStatus() == 5) {
							bte.setPayStatusName("扣款成功回调失败");
						}
						// 交易时间
						bte.setCreateTime(payv2PayOrder.getCreateTime());

						dataset.add(bte);
					}
					OutputStream out = response.getOutputStream();
					//String filename = "订单列表" + new Date().getTime() + ".xlsx";// 设置下载时客户端Excel的名称
					String filename = "订单列表" + new Date().getTime() + ".csv";
					filename = URLEncoder.encode(filename, "UTF-8");// 处理中文文件名
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-disposition", "attachment;filename=" + filename);
					List<Object> headList = Arrays.asList(headers);
					File csvFile = csv.commonCSV(headList, dataset, null, filename);
					InputStream in = new FileInputStream(csvFile);
					int b;  
		            while((b=in.read())!= -1)  
		            {  
		                out.write(b);  
		            }  
		            in.close();
					//ex.exportExcel(headers, dataset, out);
					out.close();
				} else {
					returnMap.put("status", -1);// 失败
				}
			} catch (Exception e) {
				logger.error("导出订单列表.xls错误", e);
				e.printStackTrace();
			}
		}
		return returnMap;
	}
}
