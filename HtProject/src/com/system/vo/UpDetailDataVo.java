package com.system.vo;

public class UpDetailDataVo
{
	//上量VO
	private int id;
	private String imei;
	private String imsi;
	private String mobile;
	private String provinceName;
	private String spName;
	private String spTroneName;
	private float price;
	private String cpName;
	private String firstDate;
	private String order;
	private String troneNum;
	private String payCode;
	private int apiId;
	private int status;
	private String statusName;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getOrder()
	{
		return order;
	}
	public void setOrder(String order)
	{
		this.order = order;
	}
	public String getTroneNum()
	{
		return troneNum;
	}
	public void setTroneNum(String troneNum)
	{
		this.troneNum = troneNum;
	}
	
	public String getImei()
	{
		return imei;
	}
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	public String getImsi()
	{
		return imsi;
	}
	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getProvinceName()
	{
		return provinceName;
	}
	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}

	public String getSpName()
	{
		return spName;
	}
	public void setSpName(String spName)
	{
		this.spName = spName;
	}
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public float getPrice()
	{
		return price;
	}
	public void setPrice(float price)
	{
		this.price = price;
	}
	public String getCpName()
	{
		return cpName;
	}
	public void setCpName(String cpName)
	{
		this.cpName = cpName;
	}

	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public int getApiId() {
		return apiId;
	}
	public void setApiId(int apiId) {
		this.apiId = apiId;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
	
}
