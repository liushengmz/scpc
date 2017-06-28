package com.system.model;

public class ProductModel {
	private int opratorId;	//运营商ID
	private int flag;		//运营商标志
	private String enName;	//运营商英文名
	private String cnName;	//运营商中文名
	private int bjflag;		//北京的标志
	
	private int productLineId;   //产品线ID
	private int OperFlag;//产品线运营商标志
	private String productLineName;//产品线名称
	
	private int childProductId; //子产品ID
	private int childLineId;     //子产品对应产品线ID
	private String childProductName;//子产品名称
	public int getOpratorId() {
		return opratorId;
	}
	public void setOpratorId(int opratorId) {
		this.opratorId = opratorId;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public int getBjflag() {
		return bjflag;
	}
	public void setBjflag(int bjflag) {
		this.bjflag = bjflag;
	}
	public int getProductLineId() {
		return productLineId;
	}
	public void setProductLineId(int productLineId) {
		this.productLineId = productLineId;
	}
	public int getOperFlag() {
		return OperFlag;
	}
	public void setOperFlag(int operFlag) {
		OperFlag = operFlag;
	}
	public String getProductLineName() {
		return productLineName;
	}
	public void setProductLineName(String productLineName) {
		this.productLineName = productLineName;
	}
	public int getChildProductId() {
		return childProductId;
	}
	public void setChildProductId(int childProductId) {
		this.childProductId = childProductId;
	}
	public int getChildLineId() {
		return childLineId;
	}
	public void setChildLineId(int childLineId) {
		this.childLineId = childLineId;
	}
	public String getChildProductName() {
		return childProductName;
	}
	public void setChildProductName(String childProductName) {
		this.childProductName = childProductName;
	}
	
	

}
