package com.system.model;

public class CpAppModel {
	private int id;                 //主键
	private String appname;			//应用名
	private String appkey;			//应用KEY
	private int appid;              //应用ID
	private String feeDate;			//计费日期
	private int newUserRows;		//新增用户-真实数据
	private double amount;			//每日收入-真实数据
	private int showNewUserRows;	//展示的新增用户
	private double showAmount;		//展示的每日收入
	private double extendFee;		//推广费用
	private double profit;			//利润
	private int status;				//状态，0未同步，1为已同步
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(String feeDate) {
		this.feeDate = feeDate;
	}
	public int getNewUserRows() {
		return newUserRows;
	}
	public void setNewUserRows(int newUserRows) {
		this.newUserRows = newUserRows;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getShowNewUserRows() {
		return showNewUserRows;
	}
	public void setShowNewUserRows(int showNewUserRows) {
		this.showNewUserRows = showNewUserRows;
	}
	public double getShowAmount() {
		return showAmount;
	}
	public void setShowAmount(double showAmount) {
		this.showAmount = showAmount;
	}
	public double getExtendFee() {
		return extendFee;
	}
	public void setExtendFee(double extendFee) {
		this.extendFee = extendFee;
	}
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	
}
