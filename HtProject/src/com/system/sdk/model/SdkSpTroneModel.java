package com.system.sdk.model;

public class SdkSpTroneModel {
	private int id;
	private int spId;
	private int spTroneId;
	private String name;
	private int operatorId;
	private String CteateDate;
	//sdksp的字段
	private String fullName;
	private String shortName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSpId() {
		return spId;
	}
	public void setSpId(int spId) {
		this.spId = spId;
	}
	public int getSpTroneId() {
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId) {
		this.spTroneId = spTroneId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	public String getCteateDate() {
		return CteateDate;
	}
	public void setCteateDate(String cteateDate) {
		CteateDate = cteateDate;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	

}
