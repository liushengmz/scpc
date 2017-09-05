package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

public class OrderRefundExportVO implements Serializable {
	private	String refundNum;
	private	String orderNum;
	private	String merchantOrderNum;//商户订单号
	private String companyName;
	private String appName;
	private String payWayName;	
	private String rateTypeName;
	private String rateName;	
	private String goodsName;
	private Double payMoney;//订单金额
	private	Double refundMoney;
	private Double payWayMoney;//手续费
	private String refundStatus;
	private	Date payTime;//交易时间
	private	Date refundTime;
	
	public String getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getMerchantOrderNum() {
		return merchantOrderNum;
	}
	public void setMerchantOrderNum(String merchantOrderNum) {
		this.merchantOrderNum = merchantOrderNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPayWayName() {
		return payWayName;
	}
	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}
	public String getRateTypeName() {
		return rateTypeName;
	}
	public void setRateTypeName(String rateTypeName) {
		this.rateTypeName = rateTypeName;
	}
	public String getRateName() {
		return rateName;
	}
	public void setRateName(String rateName) {
		this.rateName = rateName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public Double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public Double getPayWayMoney() {
		return payWayMoney;
	}
	public void setPayWayMoney(Double payWayMoney) {
		this.payWayMoney = payWayMoney;
	}
	public String getRefundStatus() {
		return "退款成功";
	}	
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	
	
}