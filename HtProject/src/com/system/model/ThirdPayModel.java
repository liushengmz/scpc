package com.system.model;

public class ThirdPayModel
{
	
	private Long id;
	private String timeId;
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	private int price; // 价格，单位人民币，分
	private String payChannel;// 支付通道
	private String ip;// 来源ip
	private String payInfo;// 从支付通道获取的原始内容
	private String releaseChannel;// 发行通道ID，一般从payInfo中解析出
	private String appKey;// CP方ID，一般从payInfo中解析出
	private String payChannelOrderId;// 支付通道的订单号，一般从payInfo中解析出
	private String ownUserId;// 付费用户ID，待用
	private String ownItemId;// 购买道具ID，待用
	private String ownOrderId;// 原始订单号ID，待用
	private int testStatus;// 是否是测试信息
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	public String getReleaseChannel() {
		return releaseChannel;
	}
	public void setReleaseChannel(String releaseChannel) {
		this.releaseChannel = releaseChannel;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getPayChannelOrderId() {
		return payChannelOrderId;
	}
	public void setPayChannelOrderId(String payChannelOrderId) {
		this.payChannelOrderId = payChannelOrderId;
	}
	public String getOwnUserId() {
		return ownUserId;
	}
	public void setOwnUserId(String ownUserId) {
		this.ownUserId = ownUserId;
	}
	public String getOwnItemId() {
		return ownItemId;
	}
	public void setOwnItemId(String ownItemId) {
		this.ownItemId = ownItemId;
	}
	public String getOwnOrderId() {
		return ownOrderId;
	}
	public void setOwnOrderId(String ownOrderId) {
		this.ownOrderId = ownOrderId;
	}
	public int getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(int testStatus) {
		this.testStatus = testStatus;
	}
	
	
}
