package com.pay.business.util.mail;


public class MailRun{
	
	public static void send(String dictName,String errorMsg,String orderNum
			,String rateId,String appName,String payMoney,String companyName
			,String rateCompanyName,String payWayName){
		
		MailThread mt = new MailThread(errorMsg, dictName, orderNum, rateId, appName
				, payMoney, companyName, rateCompanyName, payWayName);
		mt.start();
	}
	
	public static void main(String[] args) {
		//send("测试", "测试","dfg",null,"电饭锅");
	}
}
