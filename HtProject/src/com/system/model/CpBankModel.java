package com.system.model;

public class CpBankModel {
	private int id;
	private int cpId;
	//结款类型
	private int type;
	private String typeName;
	private String bankName;
	private String userName;
	//银行帐号
	private String bankAccount;
	private String bankBranch;
	private int status;
	private String createDate;
	//cp信息
	private int comUserId;
	private String cpFullName;
	private String cpShortName;
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCpId() {
		return cpId;
	}
	public void setCpId(int cpId) {
		this.cpId = cpId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getComUserId() {
		return comUserId;
	}
	public void setComUserId(int comUserId) {
		this.comUserId = comUserId;
	}
	public String getCpFullName() {
		return cpFullName;
	}
	public void setCpFullName(String cpFullName) {
		this.cpFullName = cpFullName;
	}
	public String getCpShortName() {
		return cpShortName;
	}
	public void setCpShortName(String cpShortName) {
		this.cpShortName = cpShortName;
	}
	
	

}
