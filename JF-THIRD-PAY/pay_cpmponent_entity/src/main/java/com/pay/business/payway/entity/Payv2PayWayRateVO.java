package com.pay.business.payway.entity;

public class Payv2PayWayRateVO {
	
	/**
	 * 支付通道
	 */
	private	String payWayName;
	
	private String rateName;
	/**
	 * 支付方式
	 */
	private	String payType;
	/**
	 * 上游通道
	 */
	private String upWayName;
	/**
	 * 主体公司
	 */
	private	String companyName;
	/**
	 * 录入人
	 */
	private	String adminName;
	/**
	 * 备注
	 */
	private	String remark;
	/**
	 * 
	 */
	private	String bank;
	/**
	 * 
	 */
	private	String bankType;
	/**
	 * 
	 */
	private	String bankName;
	/**
	 * 
	 */
	private	String bankCard;
	/**
	 * 
	 */
	private	String rate;
	/**
	 * 
	 */
	private	String max;
	/**
	 * 
	 */
	private	String dayMax;
	/**
	 * 
	 */
	private	String arrivalType;
	/**
	 * 
	 */
	private	String arrivalValue;
	/**
	 * 
	 */
	private	String status;
	
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
	public String getPayType() {
		if("1".equals(payType)){
			return "app支付";
		}else if ("2".equals(payType)) {
			return "web";
		}else if ("3".equals(payType)) {
			return "扫码";
		}
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getUpWayName() {
		return upWayName;
	}
	public void setUpWayName(String upWayName) {
		this.upWayName = upWayName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankType() {
		if("1".equals(bankType)){
			return "对公";
		}else if ("2".equals(bankType)) {
			return "对私";
		}
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getDayMax() {
		return dayMax;
	}
	public void setDayMax(String dayMax) {
		this.dayMax = dayMax;
	}
	public String getArrivalType() {
		if("1".equals(arrivalType)){
			return "T+日期（工作日）";
		}else if ("2".equals(arrivalType)) {
			return "实时到账";
		}else if ("3".equals(arrivalType)) {
			return "T+日期";
		}
		return arrivalType;
	}
	public void setArrivalType(String arrivalType) {
		this.arrivalType = arrivalType;
	}
	public String getArrivalValue() {
		return arrivalValue;
	}
	public void setArrivalValue(String arrivalValue) {
		this.arrivalValue = arrivalValue;
	}
	public String getStatus() {
		if("1".equals(status)){
			return "开启";
		}else if ("2".equals(status)) {
			return "关闭";
		}
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
