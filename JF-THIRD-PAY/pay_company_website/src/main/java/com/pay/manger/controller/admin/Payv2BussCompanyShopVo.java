package com.pay.manger.controller.admin;

import java.io.Serializable;

public class Payv2BussCompanyShopVo implements Serializable{
	private static final long serialVersionUID = 1L;
	//店铺ID
	private	Long id;
	//商户ID
	private	Long companyId;
	//店铺名字
	private	String shopName;
	//店铺门店号
	private	String shopCard;
	//店铺地址
	private	String shopAddress;
	//店铺图标
	private	String shopIcon;
	//联系人
	private	String shopContacts;
	//联系人手机号码
	private	String shopContactsPhone;
	//店铺邮箱
	private	String shopEmail;
	//每天营业开始时间
	private	String shopDayStartTime;
	//每天营业结束时间
	private	String shopDayEndTime;
	//店铺二维码
	private	String shopTwoCodeUrl;
	//店铺备注
	private	String shopRemarks;
	//店铺介绍
	private	String shopDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopCard() {
		return shopCard;
	}
	public void setShopCard(String shopCard) {
		this.shopCard = shopCard;
	}
	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	public String getShopIcon() {
		return shopIcon;
	}
	public void setShopIcon(String shopIcon) {
		this.shopIcon = shopIcon;
	}
	public String getShopContacts() {
		return shopContacts;
	}
	public void setShopContacts(String shopContacts) {
		this.shopContacts = shopContacts;
	}
	public String getShopContactsPhone() {
		return shopContactsPhone;
	}
	public void setShopContactsPhone(String shopContactsPhone) {
		this.shopContactsPhone = shopContactsPhone;
	}
	public String getShopEmail() {
		return shopEmail;
	}
	public void setShopEmail(String shopEmail) {
		this.shopEmail = shopEmail;
	}
	public String getShopDayStartTime() {
		return shopDayStartTime;
	}
	public void setShopDayStartTime(String shopDayStartTime) {
		this.shopDayStartTime = shopDayStartTime;
	}
	public String getShopDayEndTime() {
		return shopDayEndTime;
	}
	public void setShopDayEndTime(String shopDayEndTime) {
		this.shopDayEndTime = shopDayEndTime;
	}
	public String getShopTwoCodeUrl() {
		return shopTwoCodeUrl;
	}
	public void setShopTwoCodeUrl(String shopTwoCodeUrl) {
		this.shopTwoCodeUrl = shopTwoCodeUrl;
	}
	public String getShopRemarks() {
		return shopRemarks;
	}
	public void setShopRemarks(String shopRemarks) {
		this.shopRemarks = shopRemarks;
	}
	public String getShopDesc() {
		return shopDesc;
	}
	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}
}