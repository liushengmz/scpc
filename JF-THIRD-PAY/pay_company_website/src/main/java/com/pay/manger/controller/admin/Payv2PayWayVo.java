package com.pay.manger.controller.admin;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;

public class Payv2PayWayVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	String wayName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
}