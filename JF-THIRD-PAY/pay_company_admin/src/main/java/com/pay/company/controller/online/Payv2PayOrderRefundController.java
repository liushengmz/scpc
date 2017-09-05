package com.pay.company.controller.online;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.entity.Payv2PayOrderRefundVO;
import com.pay.business.order.mapper.Payv2PayOrderRefundMapper;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.ExportExcel.CsvWriter;
import com.pay.company.controller.admin.BaseManagerController;
import com.pay.company.controller.offline.Payv2BussCompanyDataController;

@Controller
@RequestMapping("/Payv2PayOrderRefund/*")
public class Payv2PayOrderRefundController extends BaseManagerController<Payv2PayOrderRefund, Payv2PayOrderRefundMapper> {

	@Autowired
	private Payv2ChannelService payv2ChannelService;//渠道商服务
	
	@Autowired
	private Payv2PayWayService payv2PayWayService;//支付渠道服务
	
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;//支付渠道服务
	
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;//退款订单服务
	
	private static final Logger logger = Logger.getLogger(Payv2PayOrderRefundController.class);
	
	/**
	 * 根据搜索条件查询退款订单分页列表
	 * 
	 * @param map
	 * @return List<Payv2PayOrderRefund>
	 */
	@ResponseBody
	@RequestMapping("queryRefunds")
	public Map<String, Object> queryRefunds(@RequestParam Map<String, Object> map) {
		Map<String,Object> resultMap = new HashMap<>();
		try {
			//获取当前商户
			Payv2BussCompany bussCompany = getAdmin();
			if (bussCompany == null || bussCompany.getCurrentUserStatus() == null || bussCompany.getId() == null) {
				return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN, null, "用户Session不存在或失效,请重新登录");
			}
			map.put("companyId", bussCompany.getId());
			Map<String, Object> paramMap = Payv2BussCompanyDataController.commonTimeParam(map);
			map.put("startTime", paramMap.get("startTime"));
			map.put("endTime", paramMap.get("endTime"));
			PageObject<Payv2PayOrderRefundVO> payOrderRefundVOList = payv2PayOrderRefundService.queryRefunds(map);
			resultMap.put("payOrderRefundVOList", payOrderRefundVOList);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
	}
	
	/**
	 * 退款订单详情页面
	 * 
	 * @param map
	 * @return ModelAndView
	 */
	@ResponseBody
	@RequestMapping("refundDetail")
	public Map<String, Object> refundDetail(@RequestParam Map<String, Object> map) {
		Map<String,Object> resultMap = new HashMap<>();
		try {
			Payv2PayOrderRefundVO refundDetail = payv2PayOrderRefundService.refundDetail(map);
			resultMap.put("refundDetail", refundDetail);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}
	/**
	 * @param map form页面搜索条件
	 * @param workbook
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("refundsExport")
	public Map<String, Object> refundsExport(@RequestParam Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) {

		PageObject<Payv2PayOrder> pageObject;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		OutputStream out = null;
		String displayColNames = null;
		String matchColNames = null;
		String fileName = null;
		String content = "";
		try {
			// 搜索
			map.put("curPage", 1);
			map.put("pageData", 999999999);
			Payv2BussCompany company = getAdmin();
			map.put("companyId", company.getId());
			Map<String, Object> paramMap = Payv2BussCompanyDataController.commonTimeParam(map);
			map.put("startTime", paramMap.get("startTime"));
			map.put("endTime", paramMap.get("endTime"));
			PageObject<Payv2PayOrderRefundVO> payOrderRefundVOList = payv2PayOrderRefundService.queryRefunds(map);
			// 获取LIST集合
			List<Map<String, Object>> dataList = fillData(payOrderRefundVOList.getDataList());
			// 完成数据csv文件的封装
			displayColNames = "退款时间,交易时间,退款订单号,支付集订单号,商户订单号,支付平台,商品名称,交易状态,订单金额（原交易金额）," + "退款金额,费率,手续费,结算金额,应用名称";
			matchColNames = "refundTime,payTime,refundNum,orderNum,merchantOrderNum,wayName,goodsName,refundType,payMoney,"
					+ "refundMoney,bussWayRate,payWayMoney,clearMoney,appName";
			fileName = "退款订单";
			content = Payv2PayOrderController.fillTheme(getAdmin(), map);
			content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
			content += "#------------------交易明细列表结束------------------#\r\n";
			CsvWriter.exportCsv(fileName, content, response);
			returnMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);// 201
																					// 标示没有查询到数据
		} catch (Exception e) {
			logger.error("导出退款订单.csv错误", e);
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnMap;
	
	}
	
	/**
	 * 数据封装
	 * 
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillData(List<Payv2PayOrderRefundVO> list) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Payv2PayOrderRefundVO refund : list) {
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("refundTime", refund.getRefundTime() == null ? "" : DateUtil.DateToString(refund.getRefundTime(), "yyyy-MM-dd HH:mm:ss") + "\t");
			orderMap.put("payTime", refund.getPayTime() == null ? "" : DateUtil.DateToString(refund.getPayTime(), "yyyy-MM-dd HH:mm:ss") + "\t");
			orderMap.put("refundNum", refund.getRefundNum() == null ? "" : refund.getRefundNum() + "\t");
			orderMap.put("orderNum", refund.getOrderNum() == null ? "" : refund.getOrderNum()+ "\t");
			orderMap.put("merchantOrderNum", refund.getMerchantOrderNum() == null ? "" : refund.getMerchantOrderNum()+ "\t");
			orderMap.put("wayName", refund.getWayName() == null ? "" : refund.getWayName()+ "\t");
			orderMap.put("goodsName", refund.getGoodsName() == null ? "" : refund.getGoodsName() + "\t");
			orderMap.put("refundType", refund.getRefundType() == 0 ? "" : refund.getRefundType() + "\t");
			orderMap.put("payMoney", refund.getPayMoney() == 0 ? "" : refund.getPayMoney() + "\t");			
			orderMap.put("refundMoney", refund.getRefundMoney() == null ? "" : refund.getRefundMoney() + "\t");
			orderMap.put("bussWayRate", refund.getBussWayRate() == null ? "" : refund.getBussWayRate() +"‰"+ "\t");
			orderMap.put("payWayMoney", refund.getPayWayMoney() == null ? "" : refund.getPayWayMoney()+ "\t");
			orderMap.put("clearMoney", (refund.getPayMoney()-refund.getRefundMoney())-refund.getPayWayMoney() + "\t");
			orderMap.put("appName", refund.getAppName() == null ? "" : refund.getAppName() + "\t");
			dataList.add(orderMap);			
		}
		return dataList;
	}
	
}
