package com.pay.business.record.entity;

import java.io.Serializable;
public class Payv2StatisticsDayTimeBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer successCount;
	private Double successMoney;
	private Integer timeType;
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public Double getSuccessMoney() {
		return successMoney;
	}
	public void setSuccessMoney(Double successMoney) {
		this.successMoney = successMoney;
	}
	public Integer getTimeType() {
		return timeType;
	}
	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}
	
}