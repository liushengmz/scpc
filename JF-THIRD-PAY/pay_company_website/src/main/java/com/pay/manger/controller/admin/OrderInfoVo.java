package com.pay.manger.controller.admin;

import java.io.Serializable;
import java.util.Date;

public class OrderInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;
	//订单金额
	private	Double payMoney;
	//实收金额
	private	Double payDiscountMoney;
	//退款金额
	private	Double refundMoney;
	//交易时间
	private	Date payTime;
	//退款时间
	private Date refundTime;
	//通道名字
	private	String wayName;
	//付款人
	private	String payUserName;
	//收款账户
	private	String payWayUserName;
	//交易订单号
	private	String orderNum;
	//交易门店名称
	private	String shopName;
	//到账金额
	private	Double amountOfMoney;
	//手续费
	private	Double payWayMoney;
	//状态
	private Integer payStatus;
	//是否退过款 1是 2：否
	private Integer yesOrNoRefund;
	
	public Integer getYesOrNoRefund() {
		return yesOrNoRefund;
	}
	public void setYesOrNoRefund(Integer yesOrNoRefund) {
		this.yesOrNoRefund = yesOrNoRefund;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public Double getPayDiscountMoney() {
		return payDiscountMoney;
	}
	public void setPayDiscountMoney(Double payDiscountMoney) {
		this.payDiscountMoney = payDiscountMoney;
	}
	public Double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
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
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public String getPayUserName() {
		return payUserName;
	}
	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}
	public String getPayWayUserName() {
		return payWayUserName;
	}
	public void setPayWayUserName(String payWayUserName) {
		this.payWayUserName = payWayUserName;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Double getAmountOfMoney() {
		return amountOfMoney;
	}
	public void setAmountOfMoney(Double amountOfMoney) {
		this.amountOfMoney = amountOfMoney;
	}
	public Double getPayWayMoney() {
		return payWayMoney;
	}
	public void setPayWayMoney(Double payWayMoney) {
		this.payWayMoney = payWayMoney;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
}