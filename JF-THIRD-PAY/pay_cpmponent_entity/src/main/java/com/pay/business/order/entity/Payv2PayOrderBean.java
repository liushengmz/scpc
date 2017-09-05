package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;
/**
* @ClassName: Payv2PayOrderBean 
* @Description:导出订单Bean
* @author zhoulibo
* @date 2017年2月10日 下午3:53:26
*/
public class Payv2PayOrderBean implements Serializable {
	// 支付集订单号
	private String orderNum;
	// 商户订单号
	private String merchantOrderNum;
	// 来源商户
	private String companyName;
	// 来源应用
	private String appName;
	// 支付渠道
	private String wayName;
	// 订单金额
	private Double payMoney;
	// 实际支付金额
	private Double payDiscountMoney;
	//优惠金额
	private Double discountMoney;
	//退款金额
	private double refundMoney;
	// 支付状态
	private String payStatusName;
	// 创建时间
	private Date createTime;
	// 订单支付时间
	private Date payTime;
	// 回调时间
	private Date callbackTime;

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
	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}


	
	public String getPayStatusName() {
		return payStatusName;
	}

	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
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

	public Double getPayDiscountMoney() {
		return payDiscountMoney;
	}

	public void setPayDiscountMoney(Double payDiscountMoney) {
		this.payDiscountMoney = payDiscountMoney;
	}

	public Double getDiscountMoney() {
		return discountMoney;
	}

	public void setDiscountMoney(Double discountMoney) {
		this.discountMoney = discountMoney;
	}

	public double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public Date getCallbackTime() {
		return callbackTime;
	}

	public void setCallbackTime(Date callbackTime) {
		this.callbackTime = callbackTime;
	}
}