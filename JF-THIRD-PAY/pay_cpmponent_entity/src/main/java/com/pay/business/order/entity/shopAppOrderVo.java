package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

public class shopAppOrderVo implements Serializable {
	private static final long serialVersionUID = 1L;
	//订单ID
	private	Long id;
	//交易时间
	private	Date tradingTime;
	//订单号
	private	String orderNum;
	//支付通道ID
	private	Long payWayId;
	//状态：tradingType为3 的时候：支付状态,1支付成功,2支付失败,3未支付,4超时,5.扣款成功回调失败',   其余为退款表中：退款状态1.退款成功2.退款失败3.退款中4.退款成功回调失败', 
	private	Integer tradingStatus;
	//交易金额
	private	Double tradingMoney;
	//类型 3为总订单   其余为退款表中的状态:1.全部退款2.部分退款',
	private	Integer tradingType;
	//退款金额
	private	Double refundMoney;
	//支付通道图标
	private	String wayIcon;
	//支付通道名字
	private	String wayName;
	//这个是退款表中的订单ID
	private	Long orderId;
	//是否发起过退款
	private int yesOrNoRefund;
	
	public int getYesOrNoRefund() {
		return yesOrNoRefund;
	}
	public void setYesOrNoRefund(int yesOrNoRefund) {
		this.yesOrNoRefund = yesOrNoRefund;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getTradingTime() {
		return tradingTime;
	}
	public void setTradingTime(Date tradingTime) {
		this.tradingTime = tradingTime;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Long getPayWayId() {
		return payWayId;
	}
	public void setPayWayId(Long payWayId) {
		this.payWayId = payWayId;
	}
	public Integer getTradingStatus() {
		return tradingStatus;
	}
	public void setTradingStatus(Integer tradingStatus) {
		this.tradingStatus = tradingStatus;
	}
	public Double getTradingMoney() {
		return tradingMoney;
	}
	public void setTradingMoney(Double tradingMoney) {
		this.tradingMoney = tradingMoney;
	}
	public Integer getTradingType() {
		return tradingType;
	}
	public void setTradingType(Integer tradingType) {
		this.tradingType = tradingType;
	}
	public Double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public String getWayIcon() {
		return wayIcon;
	}
	public void setWayIcon(String wayIcon) {
		this.wayIcon = wayIcon;
	}
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
}
