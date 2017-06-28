package com.system.model;

public class CpChannelModel {
	private int id;                 //主键
	private int appid;
	private String appname;			//应用名
	private String appkey;			//应用KEY
	private String channel;			//渠道名
	private int channelid;		//渠道ID
	private String feeDate;			//计费日期
	private int newUserRows;		//新增用户数，真实数据
	private int activeRows;			//活跃用户数，真实数据
	private double amount;			//收入
	private int showNewUserRows;	//展示真实用户数
	private int showActiveRows;		//展示活跃用户数
	private double showAmount;		//展示收入
	private double scale;			//分成比例
	private int status;				//状态，0为未同步，1为已同步
	
	
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public int getChannelid() {
		return channelid;
	}
	public void setChannelid(int channelid) {
		this.channelid = channelid;
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
	public int getActiveRows() {
		return activeRows;
	}
	public void setActiveRows(int activeRows) {
		this.activeRows = activeRows;
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
	public int getShowActiveRows() {
		return showActiveRows;
	}
	public void setShowActiveRows(int showActiveRows) {
		this.showActiveRows = showActiveRows;
	}
	public double getShowAmount() {
		return showAmount;
	}
	public void setShowAmount(double showAmount) {
		this.showAmount = showAmount;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
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
