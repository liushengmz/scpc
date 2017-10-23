package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

public class LineShopVo implements Serializable {
	private static final long serialVersionUID = 1L;
	//线下商铺ID
	private	Long id;
	//支付通道ID
	private	Long payWayId;
	//商铺名字
	private String shopName;
	//商户ID
	private	Long companyId;
	//店铺状态：1未审核,2通过,3未通过,4终止合作',
	private Integer shopStatus;
	//创建时间
	private	Date createTime;
	//商户名字
	private String companyName;
	//支付通道名字
	private String wayName;
	//地址
	private	String shopAddress;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPayWayId() {
		return payWayId;
	}
	public void setPayWayId(Long payWayId) {
		this.payWayId = payWayId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Integer getShopStatus() {
		return shopStatus;
	}
	public void setShopStatus(Integer shopStatus) {
		this.shopStatus = shopStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	
}
