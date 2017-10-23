package com.pay.manger.controller.payv2;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.util.hfbpay.WeiXinHelper;

@Controller
@RequestMapping("/callback/*")
public class HfbCallBackController {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	
	@ResponseBody
	@RequestMapping("/getHfbNotify")
	public void getNotifyOfAlipay(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		if(ObjectUtil.checkObjectFileIsEmpty(new String[] {"pay_amt","result","jnet_bill_no","agent_bill_id"},map)){
			String orderNum = map.get("agent_bill_id").toString();
			Payv2PayOrder payOrder = payv2PayOrderService.getOrderInfo(orderNum);
			if(null != payOrder){
				if (WeiXinHelper.checkSign(payOrder.getRateKey2(),map)) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("out_trade_no", orderNum);
					if("1".equals(map.get("result").toString())){
						params.put("trade_status", "TRADE_SUCCESS");
					}
					params.put("trade_no", map.get("jnet_bill_no").toString());
					params.put("gmt_payment",DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
					String moeny = map.get("pay_amt").toString();
					params.put("total_amount", moeny);
					boolean bool = payv2PayOrderService.aliPayCallBack(params, payOrder);
					if(bool){
						out.write("ok");
					}else{
						out.write("error");
					}
				}else{
					System.out.println("汇付宝验签失败");
					out.write("error");
				}
			}else{
				out.write("error");
			}
		}else{
			out.write("error");
		}
	}
	
	private String DateStr(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}
}
