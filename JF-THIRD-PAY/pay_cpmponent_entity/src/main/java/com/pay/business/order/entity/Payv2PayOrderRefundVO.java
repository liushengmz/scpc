package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

public class Payv2PayOrderRefundVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long orderId;
	private	Long channelId;
	private	Long companyId;
	private Long rateId;
	private	Long payWayId;
	private	Long appId;	
	private int rateType;
	private String channelName;
	private String companyName;
	private String appName;
	private String wayName;
	private String payWayName;
	private String rateName;
	private	String refundNum;
	private	String orderNum;
	private	String merchantOrderNum;//商户订单号
	private String bankTransaction;//银行流水号	
	private	Date refundTime;
	private	Date payTime;//交易时间
	private Double payMoney;//订单金额
	private	Double refundMoney;
	private	Double payDiscountMoney;//支付金额
	private Double payWayMoney;//商户手续费	
	private String goodsName;
	private int refundType;//退款类型，1为全部退款，2为部分退款
	private Double discountMoney;//商家优惠金额
	private Double bussWayRate;//商户费率‰
	public String getBussWayRate() {
		return bussWayRate+"";
	}
	public void setBussWayRate(Double bussWayRate) {
		this.bussWayRate = bussWayRate;
	}
	public Double getDiscountMoney() {
		return discountMoney;
	}
	public void setDiscountMoney(Double discountMoney) {
		this.discountMoney = discountMoney;
	}
	public int getRefundType() {
		return refundType;
	}
	public void setRefundType(int refundType) {
		this.refundType = refundType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getRateId() {
		return rateId;
	}
	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}
	public Long getPayWayId() {
		return payWayId;
	}
	public void setPayWayId(Long payWayId) {
		this.payWayId = payWayId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public int getRateType() {
		return rateType;
	}
	public void setRateType(int rateType) {
		this.rateType = rateType;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public String getPayWayName() {
		return payWayName;
	}
	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}
	public String getRateName() {
		return rateName;
	}
	public void setRateName(String rateName) {
		this.rateName = rateName;
	}
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
	public String getBankTransaction() {
		return bankTransaction;
	}
	public void setBankTransaction(String bankTransaction) {
		this.bankTransaction = bankTransaction;
	}
	public Date getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
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
	public Double getPayDiscountMoney() {
		return payDiscountMoney;
	}
	public void setPayDiscountMoney(Double payDiscountMoney) {
		this.payDiscountMoney = payDiscountMoney;
	}
	public Double getPayWayMoney() {
		return payWayMoney;
	}
	public void setPayWayMoney(Double payWayMoney) {
		this.payWayMoney = payWayMoney;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	
}