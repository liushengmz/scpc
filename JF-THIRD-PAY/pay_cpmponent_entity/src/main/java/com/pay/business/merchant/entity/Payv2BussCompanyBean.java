package com.pay.business.merchant.entity;

import java.io.Serializable;
import java.util.Date;
public class Payv2BussCompanyBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long channelId;
	private	String companyName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}