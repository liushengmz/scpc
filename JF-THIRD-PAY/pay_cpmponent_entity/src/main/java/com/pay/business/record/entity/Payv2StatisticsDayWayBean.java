package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;
public class Payv2StatisticsDayWayBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer successCount;
	private Double successMoney;
	private Long payId;
	private String payName;
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
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public Long getPayId() {
		return payId;
	}
	public void setPayId(Long payId) {
		this.payId = payId;
	}
}