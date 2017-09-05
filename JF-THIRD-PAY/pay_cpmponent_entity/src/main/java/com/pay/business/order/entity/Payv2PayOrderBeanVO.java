package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;
/**
* @ClassName: Payv2PayOrderBean 
* @Description:导出订单Bean
* @author zhoulibo
* @date 2017年2月10日 下午3:53:26
*/
public class Payv2PayOrderBeanVO implements Serializable {
	private static final long serialVersionUID = -8739536644313023879L;
	
	// 支付集订单号
	private String orderNum;
	// 商户订单号
	private String merchantOrderNum;
	//来源渠道
	private String channelName;
	// 来源商户
	private String companyName;
	// 来源应用
	private String appName;
	// 支付平台
	private String wayName;
	// 支付通道
	private String rateName;
	// 订单名称
	private String orderName;
	// 订单金额
	private Double payMoney;
	
	// 手续费
	private Double payWayMoney;
	// 实际支付金额
	//private Double payDiscountMoney;
	// 优惠金额
	//private Double discountMoney;
	// 退款金额
	//private double refundMoney;
	// 支付状态
	private String payStatusName;
	// 创建时间
	private Date createTime;
	// 回调时间
	//private Date callbackTime;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getMerchantOrderNum() {
		return merchantOrderNum;
	}

	public void setMerchantOrderNum(String merchantOrderNum) {
		this.merchantOrderNum = merchantOrderNum;
	}
	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}


	
	public String getPayStatusName() {
		return payStatusName;
	}

	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getWayName() {
		return wayName;
	}

	public void setWayName(String wayName) {
		this.wayName = wayName;
	}

	public Double getPayWayMoney() {
		return payWayMoney;
	}

	public void setPayWayMoney(Double payWayMoney) {
		this.payWayMoney = payWayMoney;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}
	
}