package com.system.model;

public class LvMrModel {
	//tbl_mr_201609
	private int id;
	private String orderId;//cp方订单号
	private int payType;//支付类型
	private int price;//价格(分)
	private String appkey;
	private String channel;
	private String payOrderId;//第三方支付订单号
	private int status;
	private int levelId;
	private String createDate;
	//tbl_lv_channel
	private int lvId;
	private int holdPercent;//扣量
	private String lvappkey;
	private String lvchannel;
	private int userId;
	private String lvCreateDate;
	//计算
	private float dayAmount;//（元）
	private String dateDay;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPayOrderId() {
		return payOrderId;
	}
	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getLvId() {
		return lvId;
	}
	public void setLvId(int lvId) {
		this.lvId = lvId;
	}
	public int getHoldPercent() {
		return holdPercent;
	}
	public void setHoldPercent(int holdPercent) {
		this.holdPercent = holdPercent;
	}
	public String getLvappkey() {
		return lvappkey;
	}
	public void setLvappkey(String lvappkey) {
		this.lvappkey = lvappkey;
	}
	public String getLvchannel() {
		return lvchannel;
	}
	public void setLvchannel(String lvchannel) {
		this.lvchannel = lvchannel;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLvCreateDate() {
		return lvCreateDate;
	}
	public void setLvCreateDate(String lvCreateDate) {
		this.lvCreateDate = lvCreateDate;
	}
	public String getDateDay() {
		return dateDay;
	}
	public void setDateDay(String dateDay) {
		this.dateDay = dateDay;
	}
	public float getDayAmount() {
		return dayAmount;
	}
	public void setDayAmount(float dayAmount) {
		this.dayAmount = dayAmount;
	}
	
	
	
	
}
