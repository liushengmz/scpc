package com.system.model;

public class CommRightModel {
	private int id;
	private int type;
	private int userId;
	private String userName;
	private String rightList;
	private String rightListName;
	private String remark;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRightList() {
		return rightList;
	}
	public void setRightList(String rightList) {
		this.rightList = rightList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRightListName() {
		return rightListName;
	}
	public void setRightListName(String rightListName) {
		this.rightListName = rightListName;
	}
	

}
