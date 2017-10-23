package com.pay.company.ExportExcel;

import java.util.Date;
public class Payv2PayOrderBean{
	private	String orderNum;
	private	Double payMoney;
	private	Date payTime;
	private	String appName;
	private	String wayName;
	private String goodsName;
	private Double actuallyPayMoney;
	private Double payBackMoney;
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
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
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Double getActuallyPayMoney() {
		return actuallyPayMoney;
	}
	public void setActuallyPayMoney(Double actuallyPayMoney) {
		this.actuallyPayMoney = actuallyPayMoney;
	}
	public Double getPayBackMoney() {
		return payBackMoney;
	}
	public void setPayBackMoney(Double payBackMoney) {
		this.payBackMoney = payBackMoney;
	}

}