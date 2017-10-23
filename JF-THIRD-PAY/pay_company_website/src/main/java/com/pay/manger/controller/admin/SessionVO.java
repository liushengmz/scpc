package com.pay.manger.controller.admin;

import java.io.Serializable;

import com.pay.business.payv2.entity.Payv2BussCompany;


public class SessionVO implements Serializable{
	
	// 用户对象
	private Payv2BussCompany user;
	public Payv2BussCompany getUser() {
		return user;
	}

	public void setUser(Payv2BussCompany user) {
		this.user = user;
	}
}
