package com.pay.business.order.entity;

import java.util.Date;

public class Payv2PayOrderVO {

	private String appKey;
	private	Long appId;
	private String alipayOrderType;//对账单业务类型
	private String payType;//支付类型
	private String refundNum;//退款订单号
	private	Double payMoney;
	private	String orderNum;
	private	String orderName;
	private	String merchantOrderNum;
	private	Date createTime;
	private	Date payTime;
	private Double payWayMoney;//手续费
	private Long companyId;//商户id
	private Long rateId;//通道id
	private Double rateValue;//官方费率
	private Double comRateValue;//商户费率
	private Long channelId;
	
	public String getAlipayOrderType() {
		return alipayOrderType;
	}
	public void setAlipayOrderType(String alipayOrderType) {
		this.alipayOrderType = alipayOrderType;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getMerchantOrderNum() {
		return merchantOrderNum;
	}
	public void setMerchantOrderNum(String merchantOrderNum) {
		this.merchantOrderNum = merchantOrderNum;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Double getPayWayMoney() {
		return payWayMoney;
	}
	public void setPayWayMoney(Double payWayMoney) {
		this.payWayMoney = payWayMoney;
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
	public Double getRateValue() {
		return rateValue;
	}
	public void setRateValue(Double rateValue) {
		this.rateValue = rateValue;
	}
	public Double getComRateValue() {
		return comRateValue;
	}
	public void setComRateValue(Double comRateValue) {
		this.comRateValue = comRateValue;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

}
