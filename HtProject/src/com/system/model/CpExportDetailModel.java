package com.system.model;

public class CpExportDetailModel {
	private String productName; //业务线
	private String spTroneName; //业务名称
	private float amount;     //信息费
	private float rate; 	//结算率
	private float spTroneBillAmount; //帐单信息费
	private float reduceAmount; //核减金额
	private int reduceType;  //核减类型(0是信息费，1是结算款)
	private float actureAmount; //实际应收
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSpTroneName() {
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName) {
		this.spTroneName = spTroneName;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getSpTroneBillAmount() {
		return spTroneBillAmount;
	}
	public void setSpTroneBillAmount(float spTroneBillAmount) {
		this.spTroneBillAmount = spTroneBillAmount;
	}
	public float getReduceAmount() {
		return reduceAmount;
	}
	public void setReduceAmount(float reduceAmount) {
		this.reduceAmount = reduceAmount;
	}
	public int getReduceType() {
		return reduceType;
	}
	public void setReduceType(int reduceType) {
		this.reduceType = reduceType;
	}
	public float getActureAmount() {
		return actureAmount;
	}
	public void setActureAmount(float actureAmount) {
		this.actureAmount = actureAmount;
	}

}
