package com.pay.manger.controller.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.service.Payv2PayOrderClearService;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;

/**
 * 查询支付宝对账单定时器 
 * 
 * @author Administrator
 * 
 */  
@Component
@Controller()
@RequestMapping("QueryJob")
public class AlipayBillQueryJob {
	
	@Autowired
	private Payv2PayOrderClearService payv2PayOrderClearService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;
	/**
	 * 查询支付宝对账单
	 * @throws Exception 
	 */
	@RequestMapping("/QueryJobgo")
	public void startUp(){  
			System.out.println("=========》对账开始《==========");
			Date as = new Date(new Date().getTime()-24*60*60*1000);
			String alipayTime = DateUtil.DateToString(as,"yyyy-MM-dd");
			String weixinTime = DateUtil.DateToString(as,"yyyyMMdd");
			//获取交易订单
			Map<String, Object> pav2PayOrderObjMap = getOrderDataByTime(alipayTime);
			//获取退款单
			Map<String, Object> payv2PayOrderRefundObjMap = getOrderRefundByTime(alipayTime);
			//获取通道
			List<Payv2PayWayRate> rateList = payv2PayWayRateService.queryByDicRate();
//			Set<String> setList = new HashSet<>();
			for(Payv2PayWayRate rate : rateList){
//				if(		!PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_GZH.equals(rate.getDictName())
//						&&!PayRateDictValue.PAY_TYPE_XYBANk_ALI_SCAN.equals(rate.getDictName())
//						&&!PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_SCAN.equals(rate.getDictName())
//						&&!PayRateDictValue.PAY_TYPE_MSBANk_ALI_SCAN.equals(rate.getDictName())
//						&&!PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_GZH.equals(rate.getDictName())
//						&&!PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_SCAN.equals(rate.getDictName())
//						){
//					if(setList.contains(rate.getRateKey1())){
//						continue;
//					}
//					setList.add(rate.getRateKey1());
//				}
				//不是自动对账的直接拜拜
				if(rate.getAutoRecord()==2){
					continue;
				}
				if(StringUtils.isEmpty(rate.getDictName())) continue;
				String payName = "";
				if (rate.getPayViewType() == 1) {
					payName = "支付宝";
				} else if (rate.getPayViewType() == 2) {
					payName = "微信";
				}
				try {
					payv2PayOrderClearService.job(alipayTime, weixinTime, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
//			try {
//				//根据appId分组生成对账文件
//				payv2PayOrderClearService.appIdDownBill(orderList, alipayTime, as);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			//商户+对账单
//			Map<Long,String> comMap = new HashMap<>();
//			
//			try {
//				//根据companyId分组生成对账文件
//				payv2PayOrderClearService.companyIdDownBill(orderList, alipayTime, as,comMap);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			try {
//				//根据companyId和rateId分组生成对账文件
//				payv2PayOrderClearService.comRateIdDownBill(orderList, alipayTime, as,comMap);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			System.out.println("=========》对账结束《==========");
	}
	/**
	 * 获取本地时间段的所有订单
	 * @param time
	 * @return
	 */
	private Map<String, Object> getOrderDataByTime(String time){
		Map<String, Object> orderMap = new HashMap<String, Object>();
		Map<String, Object> pav2PayOrderObjMap = new HashMap<String, Object>();
		orderMap.put("createTimeBegin", time);
		try{
			List<Payv2PayOrder> payv2PayOrderList = payv2PayOrderService.selectByObjectDate(orderMap);
			for(Payv2PayOrder order : payv2PayOrderList){
				pav2PayOrderObjMap.put(order.getOrderNum(), order);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return pav2PayOrderObjMap;
	}
	/**
	 * 获取本地时间段的所有退款单
	 * @param time
	 * @return
	 */
	private Map<String, Object> getOrderRefundByTime(String time) {
		// 退货订单
		Map<String, Object> payv2PayOrderRefundObjMap = new HashMap<String, Object>();
		Map<String, Object> refundMap = new HashMap<String, Object>();
		refundMap.put("refundTimeBegin", time);
		try {
			List<Payv2PayOrderRefund> payv2PayOrderRefundList = payv2PayOrderRefundService.selectByRefundTime(refundMap);
			for (Payv2PayOrderRefund refund : payv2PayOrderRefundList) {
				payv2PayOrderRefundObjMap.put(refund.getRefundNum(), refund);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payv2PayOrderRefundObjMap;
	}
	public static void main(String[] args) throws ParseException {
//		"transDate":1500480000000,"transTime":13552000,
			Long sss = Long.valueOf("1500480000000");
			Long sss1 = Long.valueOf("13552000");
		 	String res;
//			String s="1500480000000";
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
	        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
	        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        long lt = new Long(s);
	        Date date = new Date(sss+sss1);
	        Date date1=new Date(sss1);
	        res = simpleDateFormat.format(date)+simpleDateFormat1.format(date1);
	        Date date2=simpleDateFormat2.parse(res);
	        System.out.println(res);
	        System.out.println(date2);
	}
}
