package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_order_clear    
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
order_num            String(64)                             //支付集订单号
merchant_order_num   String(64)                             //商家订单号
company_id           Long(19)                               //商户id,关联payv2_buss_company表
app_id               Long(19)                               //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
pay_way_id           Long(19)                               //支付渠道，关联payv2_pay_way表
merchant_type        Integer(10)                            //商户类型1.线上2.线下
type                 Integer(10)                 1          //类型1.交易2.退款
order_name           String(255)                            //订单名称
order_money          Double(9,2)                            //订单金额
discount_money       Double(9,2)                            //优惠金额
status               Integer(10)                 1          //1.正常2.订单异常3.正常回调失败
clear_time           Date(19)                               //对账时间
create_time          Date(19)                               //创建时间
*/
public class Payv2PayOrderClear implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String orderNum;
	private	String transactionId;
	private	String refundNum;
	private	String merchantOrderNum;
	private	Long channelId;
	private	Long companyId;
	private	Long appId;
	private	Long payWayId;
	private	Long rateId;
	private	Integer merchantType;
	private	Integer type;
	private	String orderName;
	private	Double orderMoney;
	private	Double upstreamAmount;
	private	Integer payStatus;
	private	Integer upstreamStatus;
	private	Double discountMoney;
	private	Double bussWayRateMoney;
	private	Double bussWayRate;
	private	Double costRate;
	private	Double costRateMoney;
	private	Date payTime;
	private	Date upstreamTime;
	private	Integer status;
	private	Date clearTime;
	private	Date createTime;
	private String companyName;//商户
	private String appName;//应用
	private String wayName;//支付通道
	
	private String clearTimeBegin;
	private String clearTimeEnd;
	private Double incomeMoney;//收入
	private Double returnMoney;//退款金额
	private Double actuallyPayMoney;//实收金额
	private Double arrivalMoney;//到账金额
	private Double rate;//手续费

	/**
	* id  Long(19)  NOTNULL  //    
	*/
	public Long getId(){
		return id;
	}
	
	/**
	* id  Long(19)  NOTNULL  //    
	*/
	public void setId(Long id){
		this.id = id;
	}
	
	/**
	* order_num  String(64)  //支付集订单号    
	*/
	public String getOrderNum(){
		return orderNum;
	}
	
	/**
	* order_num  String(64)  //支付集订单号    
	*/
	public void setOrderNum(String orderNum){
		this.orderNum = orderNum;
	}
	
	/**
	* merchant_order_num  String(64)  //商家订单号    
	*/
	public String getMerchantOrderNum(){
		return merchantOrderNum;
	}
	
	/**
	* merchant_order_num  String(64)  //商家订单号    
	*/
	public void setMerchantOrderNum(String merchantOrderNum){
		this.merchantOrderNum = merchantOrderNum;
	}
	
	/**
	* company_id  Long(19)  //商户id,关联payv2_buss_company表    
	*/
	public Long getCompanyId(){
		return companyId;
	}
	
	/**
	* company_id  Long(19)  //商户id,关联payv2_buss_company表    
	*/
	public void setCompanyId(Long companyId){
		this.companyId = companyId;
	}
	
	/**
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付渠道，关联payv2_pay_way表    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付渠道，关联payv2_pay_way表    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* merchant_type  Integer(10)  //商户类型1.线上2.线下    
	*/
	public Integer getMerchantType(){
		return merchantType;
	}
	
	/**
	* merchant_type  Integer(10)  //商户类型1.线上2.线下    
	*/
	public void setMerchantType(Integer merchantType){
		this.merchantType = merchantType;
	}
	
	/**
	* type  Integer(10)  1  //类型1.交易2.退款    
	*/
	public Integer getType(){
		return type;
	}
	
	/**
	* type  Integer(10)  1  //类型1.交易2.退款    
	*/
	public void setType(Integer type){
		this.type = type;
	}
	
	/**
	* order_name  String(255)  //订单名称    
	*/
	public String getOrderName(){
		return orderName;
	}
	
	/**
	* order_name  String(255)  //订单名称    
	*/
	public void setOrderName(String orderName){
		this.orderName = orderName;
	}
	
	/**
	* order_money  Double(9,2)  //订单金额    
	*/
	public Double getOrderMoney(){
		return orderMoney;
	}
	
	/**
	* order_money  Double(9,2)  //订单金额    
	*/
	public void setOrderMoney(Double orderMoney){
		this.orderMoney = orderMoney;
	}
	
	/**
	* discount_money  Double(9,2)  //优惠金额    
	*/
	public Double getDiscountMoney(){
		return discountMoney;
	}
	
	/**
	* discount_money  Double(9,2)  //优惠金额    
	*/
	public void setDiscountMoney(Double discountMoney){
		this.discountMoney = discountMoney;
	}
	
	/**
	* status  Integer(10)  1  //1.正常2.订单异常3.正常回调失败    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  1  //1.正常2.订单异常3.正常回调失败    
	*/
	public void setStatus(Integer status){
		this.status = status;
	}
	
	/**
	* clear_time  Date(19)  //对账时间    
	*/
	public Date getClearTime(){
		return clearTime;
	}
	
	/**
	* clear_time  Date(19)  //对账时间    
	*/
	public void setClearTime(Date clearTime){
		this.clearTime = clearTime;
	}
	
	/**
	* create_time  Date(19)  //创建时间    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  //创建时间    
	*/
	public void setCreateTime(Date createTime){
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

	public String getClearTimeBegin() {
		return clearTimeBegin;
	}

	public void setClearTimeBegin(String clearTimeBegin) {
		this.clearTimeBegin = clearTimeBegin;
	}

	public String getClearTimeEnd() {
		return clearTimeEnd;
	}

	public void setClearTimeEnd(String clearTimeEnd) {
		this.clearTimeEnd = clearTimeEnd;
	}

	public Double getIncomeMoney() {
		return incomeMoney;
	}

	public void setIncomeMoney(Double incomeMoney) {
		this.incomeMoney = incomeMoney;
	}

	public Double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}

	public Double getActuallyPayMoney() {
		return actuallyPayMoney;
	}

	public void setActuallyPayMoney(Double actuallyPayMoney) {
		this.actuallyPayMoney = actuallyPayMoney;
	}

	public Double getArrivalMoney() {
		return arrivalMoney;
	}

	public void setArrivalMoney(Double arrivalMoney) {
		this.arrivalMoney = arrivalMoney;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getRefundNum() {
		return refundNum;
	}

	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}

	public Double getUpstreamAmount() {
		return upstreamAmount;
	}

	public void setUpstreamAmount(Double upstreamAmount) {
		this.upstreamAmount = upstreamAmount;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getUpstreamStatus() {
		return upstreamStatus;
	}

	public void setUpstreamStatus(Integer upstreamStatus) {
		this.upstreamStatus = upstreamStatus;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getUpstreamTime() {
		return upstreamTime;
	}

	public void setUpstreamTime(Date upstreamTime) {
		this.upstreamTime = upstreamTime;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Double getBussWayRateMoney() {
		return bussWayRateMoney;
	}

	public void setBussWayRateMoney(Double bussWayRateMoney) {
		this.bussWayRateMoney = bussWayRateMoney;
	}

	public Double getBussWayRate() {
		return bussWayRate;
	}

	public void setBussWayRate(Double bussWayRate) {
		this.bussWayRate = bussWayRate;
	}

	public Double getCostRate() {
		return costRate;
	}

	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}

	public Double getCostRateMoney() {
		return costRateMoney;
	}

	public void setCostRateMoney(Double costRateMoney) {
		this.costRateMoney = costRateMoney;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	
}