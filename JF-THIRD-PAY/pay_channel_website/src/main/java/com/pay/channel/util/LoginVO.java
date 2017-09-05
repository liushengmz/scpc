package com.pay.channel.util;

import java.io.Serializable;

public class LoginVO implements Serializable{
	//用户ID
	private String userId;
	//账户余额
	private Double balance;
	//身份证
	private String idCard;
	//手机token
	private String phoneToken;
	//电子邮箱
	private String email;
	//个人电子账号
	private String userCard;
	//用户名字
	private String userName;
	//用户真实名字
	private String realName;
	//用户图像
	private String userIcon;
	//已提现金额额
	private Double cashBalance;
	//支付密码是否设置
	private boolean havePayPass;
	//用户昵称
  	private	String userNick;
  	//用户二维码
  	private	String userQrcode;
  	//手机号码
	private String mobile;
	//累计收益
	private Double incomeBalance;
	//session
	private String userCertificate;
	
	private boolean haveBankCard;
	public boolean isHaveBankCard() {
		return haveBankCard;
	}
	public void setHaveBankCard(boolean haveBankCard) {
		this.haveBankCard = haveBankCard;
	}
	public boolean isHavePayPass() {
		return havePayPass;
	}
	public void setHavePayPass(boolean havePayPass) {
		this.havePayPass = havePayPass;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getPhoneToken() {
		return phoneToken;
	}
	public void setPhoneToken(String phoneToken) {
		this.phoneToken = phoneToken;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserCard() {
		return userCard;
	}
	public void setUserCard(String userCard) {
		this.userCard = userCard;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getUserNick() {
		return userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	public String getUserQrcode() {
		return userQrcode;
	}
	public void setUserQrcode(String userQrcode) {
		this.userQrcode = userQrcode;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getCashBalance() {
		return cashBalance;
	}
	public void setCashBalance(Double cashBalance) {
		this.cashBalance = cashBalance;
	}
	public Double getIncomeBalance() {
		return incomeBalance;
	}
	public void setIncomeBalance(Double incomeBalance) {
		this.incomeBalance = incomeBalance;
	}
}
