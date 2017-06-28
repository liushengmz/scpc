package com.system.model;

public class ExportDetailModel {
	private String productName; //业务线
	private String spTroneName; //业务名称
	private String amount;     //信息费
	private String rate; 	//结算率
	private String spTroneBillAmount; //帐单信息费
	private String reduceAmount; //核减金额
	private int reduceType;  //核减类型(0是信息费，1是结算款)
	private String actureAmount; //实际应收
	
	//Add By Andy 2016.10.27
	private String reduceDataAmount;
	private String reduceMoneyAmount;
	
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getSpTroneBillAmount() {
		return spTroneBillAmount;
	}
	public void setSpTroneBillAmount(String spTroneBillAmount) {
		this.spTroneBillAmount = spTroneBillAmount;
	}
	public String getReduceAmount() {
		return reduceAmount;
	}
	public void setReduceAmount(String reduceAmount) {
		this.reduceAmount = reduceAmount;
	}
	public int getReduceType() {
		return reduceType;
	}
	public void setReduceType(int reduceType) {
		this.reduceType = reduceType;
	}
	public String getActureAmount() {
		return actureAmount;
	}
	public void setActureAmount(String actureAmount) {
		this.actureAmount = actureAmount;
	}
	public String getReduceDataAmount()
	{
		return reduceDataAmount;
	}
	public void setReduceDataAmount(String reduceDataAmount)
	{
		this.reduceDataAmount = reduceDataAmount;
	}
	public String getReduceMoneyAmount()
	{
		return reduceMoneyAmount;
	}
	public void setReduceMoneyAmount(String reduceMoneyAmount)
	{
		this.reduceMoneyAmount = reduceMoneyAmount;
	}
	

}
