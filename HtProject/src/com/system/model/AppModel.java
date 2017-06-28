package com.system.model;

public class AppModel {
	private int id;
	private String appkey;
	private String appname;
	private int hold_percent;
	private int user_id;
	private String remark;
	private String userName;
	private int appType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public int getHold_percent() {
		return hold_percent;
	}
	public void setHold_percent(int hold_percent) {
		this.hold_percent = hold_percent;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public int getAppType()
	{
		return appType;
	}
	public void setAppType(int appType)
	{
		this.appType = appType;
	}
	
}
