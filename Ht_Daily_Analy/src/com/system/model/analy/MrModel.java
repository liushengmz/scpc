package com.system.model.analy;

public class MrModel 
{
	private int id;
	private String mobile;
	private int provinceId;
	private int cityId;
	private int troneId;
	private int troneOrderId;
	private String oriTrone;
	private String oriOrder;
	private String linkId;
	private String cpParams;
	private String serviceCode;
	private int price;
	private String ip;
	private String status;
	private String mrDate;
	private boolean isSynToCp = false;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getTroneId() {
		return troneId;
	}
	public void setTroneId(int troneId) {
		this.troneId = troneId;
	}
	public int getTroneOrderId() {
		return troneOrderId;
	}
	public void setTroneOrderId(int troneOrderId) {
		this.troneOrderId = troneOrderId;
	}
	public String getOriTrone() {
		return oriTrone;
	}
	public void setOriTrone(String oriTrone) {
		this.oriTrone = oriTrone;
	}
	public String getOriOrder() {
		return oriOrder;
	}
	public void setOriOrder(String oriOrder) {
		this.oriOrder = oriOrder;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getCpParams() {
		return cpParams;
	}
	public void setCpParams(String cpParams) {
		this.cpParams = cpParams;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMrDate() {
		return mrDate;
	}
	public void setMrDate(String mrDate) {
		this.mrDate = mrDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isSynToCp() {
		return isSynToCp;
	}
	public void setSynToCp(boolean isSynToCp) {
		this.isSynToCp = isSynToCp;
	}
	
	
	
}
