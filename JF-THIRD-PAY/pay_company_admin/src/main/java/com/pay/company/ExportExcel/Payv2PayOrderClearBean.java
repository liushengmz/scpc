package com.pay.company.ExportExcel;

import java.util.Date;
public class Payv2PayOrderClearBean{

	private	Long id;
	private	String orderNum;//支付集订单号
	private	String merchantOrderNum;//商家订单号
	private	Long companyId;
	private	Long appId;
	private	Long payWayId;
	private	Integer merchantType;//商户类型1.线上2.线下
	private	Integer type;//类型1.交易2.退款
	private	String orderName;
	private	Double orderMoney;
	private	Double discountMoney;
	private	Integer status;//1.正常2.订单异常3.正常回调失败
	private	Date clearTime;
	private	Date createTime;
	private String companyName;//商户
	private String appName;//应用
	private String wayName;//支付通道
	
	private String clearTimeBegin;
	private String clearTimeEnd;

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
	
}