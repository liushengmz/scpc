package com.system.model.xy;
/**
 * 翔云数据tbl_xypay_summer表实体类
 * @author Administrator
 *
 */
public class XyUserCpsModel {
	private int id;               //主键
	private String fee_date;   
	private String appKey;
	private String appName;
	private String channelKey;
	private int dataRows;
	private double amount;
	private double showAmount;
	private int status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFee_date() {
		return fee_date;
	}
	public void setFee_date(String fee_date) {
		this.fee_date = fee_date;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getChannelKey() {
		return channelKey;
	}
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	public int getDataRows() {
		return dataRows;
	}
	public void setDataRows(int dataRows) {
		this.dataRows = dataRows;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getShowAmount() {
		return showAmount;
	}
	public void setShowAmount(double showAmount) {
		this.showAmount = showAmount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
