package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;
public class Payv2PayOrderCountBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Double payDiscountMoney;
	private	Date createTime;
	private	String companyName;
	private Integer orderCount;
	public Double getPayDiscountMoney() {
		return payDiscountMoney;
	}
	public void setPayDiscountMoney(Double payDiscountMoney) {
		this.payDiscountMoney = payDiscountMoney;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	
}