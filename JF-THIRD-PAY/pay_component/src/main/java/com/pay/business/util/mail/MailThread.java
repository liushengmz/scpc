package com.pay.business.util.mail;

import com.core.teamwork.base.util.ReadProChange;

public class MailThread extends Thread{
	private String msgError;	//错误信息
	private String title;		//标题
	private String payMoney; 	//支付金额
	private String orderNum;	//订单号
	private String rateId;		//通道id
	private String appName;		//应用名称
	private String companyName;	//商户名称
	private String rateCompanyName;//通道机构
	private String payWayName;//通道名称
	private String mailName = ReadProChange.getValue("mail_name");
	private String mailForm = ReadProChange.getValue("mail_form");
	
	public MailThread(String msgError,String title,String orderNum, String rateId,String appName
			,String payMoney,String companyName,String rateCompanyName,String payWayName){
		this.msgError = msgError; 
		this.title = title; 
		this.orderNum=orderNum;
		this.rateId=rateId;
		this.appName=appName;
		this.payMoney=payMoney;
		this.companyName=companyName;
		this.rateCompanyName=rateCompanyName;
		this.payWayName=payWayName;
	}
	
	
	public void run(){

		SslSmtpMailUtil ms = new SslSmtpMailUtil();
        ms.setSubject(mailName+"通道错误【"+title+"】");
        StringBuffer buffer = new StringBuffer();
        if(msgError!=null){
        	buffer.append("\r\n通道错误【"+msgError==null?"":msgError);
        }
        if(orderNum!=null){
        	buffer.append("】\r\n订单号："+orderNum);
        }
        if(rateId!=null){
        	buffer.append("\r\n通道id:"+rateId);
        }
        if(appName!=null){
        	buffer.append("\r\n应用名称:"+appName);
        }
        if(companyName!=null){
        	buffer.append("\r\n商户名称:"+companyName);
        }
        if(rateCompanyName!=null){
        	buffer.append("\r\n通道机构:"+rateCompanyName);
        }
        if(payWayName!=null){
        	buffer.append("\r\n通道名称:"+payWayName);
        }
        if(payMoney!=null){
        	buffer.append("\r\n支付金额:"+payMoney);
        }
        
        ms.setText(buffer.toString());
        ms.setFrom("support@aijinfu.cn");
        String [] mailFroms = mailForm.split("-");
        ms.setRecipients(mailFroms, "TO");
        ms.setSentDate();
        ms.sendMail();
	}

	public String getMsgError() {
		return msgError;
	}


	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}


	public String getRateId() {
		return rateId;
	}


	public void setRateId(String rateId) {
		this.rateId = rateId;
	}



	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getMailName() {
		return mailName;
	}


	public void setMailName(String mailName) {
		this.mailName = mailName;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getRateCompanyName() {
		return rateCompanyName;
	}


	public void setRateCompanyName(String rateCompanyName) {
		this.rateCompanyName = rateCompanyName;
	}


	public String getPayWayName() {
		return payWayName;
	}


	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}


	public String getPayMoney() {
		return payMoney;
	}


	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}


	public String getMailForm() {
		return mailForm;
	}


	public void setMailForm(String mailForm) {
		this.mailForm = mailForm;
	}
}
