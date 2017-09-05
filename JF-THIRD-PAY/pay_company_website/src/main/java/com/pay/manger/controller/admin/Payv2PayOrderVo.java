package com.pay.manger.controller.admin;

import java.io.Serializable;

public class Payv2PayOrderVo implements Serializable {
	private static final long serialVersionUID = 1L;
	//交易总条数
	private	Integer number;
	//实收金额
	private	Double payDiscountMoney;
	//支付通道ID
	private	Long payWayId;
	//到账金额
	private	Double amountOfMoney;
	//通道名字
	private	String wayName;
	//通道图标URL
	private	String wayIcon;
	public String getWayIcon() {
		return wayIcon;
	}
	public void setWayIcon(String wayIcon) {
		this.wayIcon = wayIcon;
	}
	public Double getPayDiscountMoney() {
		return payDiscountMoney;
	}
	public void setPayDiscountMoney(Double payDiscountMoney) {
		this.payDiscountMoney = payDiscountMoney;
	}
	public Long getPayWayId() {
		return payWayId;
	}
	public void setPayWayId(Long payWayId) {
		this.payWayId = payWayId;
	}
	
	public Double getAmountOfMoney() {
		return amountOfMoney;
	}
	public void setAmountOfMoney(Double amountOfMoney) {
		this.amountOfMoney = amountOfMoney;
	}
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}