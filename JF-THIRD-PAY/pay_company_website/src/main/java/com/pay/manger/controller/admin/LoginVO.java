package com.pay.manger.controller.admin;

import java.io.Serializable;

public class LoginVO implements Serializable{
	//用户ID
	private String userId;
	//账户余额
	private Double balance;
	//用户真实名字
	private String realName;
	//用户图像
	private String userIcon;
	//支付密码是否设置
	private boolean havePayPass;
  	//手机号码
	private String mobile;
	//session
	private String userCertificate;
	//收款账号收款账号类型
	private	Integer accountType;
//	开户银行
	private	String accountBank;
//	收款商户名（开户姓名）
	private	String accountName;
//	收款账号（卡号）
	private	String accountCard;
//	微信收款账户
	private	String wechatAccountCard;
//	支付宝收款账户
	private	String alipayAccountCard;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	public boolean isHavePayPass() {
		return havePayPass;
	}
	public void setHavePayPass(boolean havePayPass) {
		this.havePayPass = havePayPass;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUserCertificate() {
		return userCertificate;
	}
	public void setUserCertificate(String userCertificate) {
		this.userCertificate = userCertificate;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public String getAccountBank() {
		return accountBank;
	}
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountCard() {
		return accountCard;
	}
	public void setAccountCard(String accountCard) {
		this.accountCard = accountCard;
	}
	public String getWechatAccountCard() {
		return wechatAccountCard;
	}
	public void setWechatAccountCard(String wechatAccountCard) {
		this.wechatAccountCard = wechatAccountCard;
	}
	public String getAlipayAccountCard() {
		return alipayAccountCard;
	}
	public void setAlipayAccountCard(String alipayAccountCard) {
		this.alipayAccountCard = alipayAccountCard;
	}
}
