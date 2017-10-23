package com.pay.business.order.entity;

import java.io.Serializable;

public class OrderVo implements Serializable {
	private static final long serialVersionUID = 1L;
//	今日总订单数
	private int dayCount;
//	今日成功订单数
	private int daySuccessCount;
//	今日失败订单数
	private int dayFailCount;
//	今日成功交易总金额数
	private double daySuccessMoney;
//	 今日失败金额
	private double dayFailMoney;
//	 今日订单成功率
	private double daySuccessRateOrder;
//	今日累积与昨日累积百分比
	private double dayCountPercentage;
//	今日成功订单数与昨日成功订单数百分比
	private double dayStatisticsPercentage;
//	今日成功交易金额与昨日成功交易金额百分比
	private double dayMoneyPercentage;
//	今日失败订单数与昨日订单数的百分比
	private double dayFailCountPercentage;
//	今日失败金额与昨日失败金额的百分比
	private double dayFailMonePercentage;
//	昨日成功百分比
	private double dayYesterdayStatisticsPercentage;
	public int getDayCount() {
		return dayCount;
	}
	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}
	public int getDaySuccessCount() {
		return daySuccessCount;
	}
	public double getDaySuccessMoney() {
		return daySuccessMoney;
	}
	public void setDaySuccessMoney(double daySuccessMoney) {
		this.daySuccessMoney = daySuccessMoney;
	}
	public void setDaySuccessCount(int daySuccessCount) {
		this.daySuccessCount = daySuccessCount;
	}
	public int getDayFailCount() {
		return dayFailCount;
	}
	public void setDayFailCount(int dayFailCount) {
		this.dayFailCount = dayFailCount;
	}
	public double getDaySuccessRateOrder() {
		return daySuccessRateOrder;
	}
	public void setDaySuccessRateOrder(double daySuccessRateOrder) {
		this.daySuccessRateOrder = daySuccessRateOrder;
	}
	public double getDayCountPercentage() {
		return dayCountPercentage;
	}
	public void setDayCountPercentage(double dayCountPercentage) {
		this.dayCountPercentage = dayCountPercentage;
	}
	public double getDayStatisticsPercentage() {
		return dayStatisticsPercentage;
	}
	public void setDayStatisticsPercentage(double dayStatisticsPercentage) {
		this.dayStatisticsPercentage = dayStatisticsPercentage;
	}
	public double getDayMoneyPercentage() {
		return dayMoneyPercentage;
	}
	public void setDayMoneyPercentage(double dayMoneyPercentage) {
		this.dayMoneyPercentage = dayMoneyPercentage;
	}
	public double getDayFailCountPercentage() {
		return dayFailCountPercentage;
	}
	public void setDayFailCountPercentage(double dayFailCountPercentage) {
		this.dayFailCountPercentage = dayFailCountPercentage;
	}
	public double getDayFailMonePercentage() {
		return dayFailMonePercentage;
	}
	public void setDayFailMonePercentage(double dayFailMonePercentage) {
		this.dayFailMonePercentage = dayFailMonePercentage;
	}
	public double getDayYesterdayStatisticsPercentage() {
		return dayYesterdayStatisticsPercentage;
	}
	public void setDayYesterdayStatisticsPercentage(double dayYesterdayStatisticsPercentage) {
		this.dayYesterdayStatisticsPercentage = dayYesterdayStatisticsPercentage;
	}
	public double getDayFailMoney() {
		return dayFailMoney;
	}
	public void setDayFailMoney(double dayFailMoney) {
		this.dayFailMoney = dayFailMoney;
	}
}
