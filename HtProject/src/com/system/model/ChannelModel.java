package com.system.model;

public class ChannelModel {
	private int id;
	private int appid;
	private String appname;
	private String appkey;
	private String channel;
	private int settleType;
	private String hold_percent; 
	private int syn_type;
	private int userid;
	private String userName;
	private String remark;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getHold_percent() {
		return hold_percent;
	}
	public void setHold_percent(String hold_percent) {
		this.hold_percent = hold_percent;
	}
	public int getSyn_type() {
		return syn_type;
	}
	public void setSyn_type(int syn_type) {
		this.syn_type = syn_type;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public int getSettleType() {
		return settleType;
	}
	public void setSettleType(int settleType) {
		this.settleType = settleType;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
}
