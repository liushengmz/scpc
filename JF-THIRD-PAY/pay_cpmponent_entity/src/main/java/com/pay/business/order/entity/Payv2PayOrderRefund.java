package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_order_refund   
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
order_id             Long(19)                               //订单id，关联payv2_pay_order
app_id               Long(19)                               //
company_id           Long(19)                               //商户id
pay_way_id           Long(19)                               //支付通道id
order_type           Integer(10)                            //类型1线上2线下
refund_num           String(64)                             //退款单号
channel_id           Long(19)                               //渠道id，关联payv2_channel表
refund_type          Integer(10)                            //退款类型1.全部退款2.部分退款
order_num            String(64)                             //订单号
merchant_order_num   String(64)                             //商户订单号
refund_money         Double(9,2)                 0.00       //退款金额
refund_reason        String(255)                            //退款理由
refund_status        Integer(10)                 1          //退款状态1.退款成功2.退款失败3.退款中4.退款成功回调失败
refund_time          Date(19)                               //退款时间
liquidation_time     Date(19)                               //清算时间
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Payv2PayOrderRefund implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long orderId;
	private	Long appId;
	private	Long companyId;
	private	Long payWayId;
	private	Integer orderType;
	private	String refundNum;
	private	Long channelId;
	private	Integer refundType;
	private	String orderNum;
	private	String merchantOrderNum;
	private	Double refundMoney;
	private	String refundReason;
	private	Integer refundStatus;
	private	Date refundTime;
	private	Date liquidationTime;
	private	Date createTime;
	private	Date updateTime;

	private	Double payDiscountMoney;//支付金额
	private	Double surplusMoney;//剩余金额
	
	private String refundTimeBegin;
	private String refundTimeEnd;
	
	private Long rateId;
	private Integer refundCount;
	
	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}

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
	* order_id  Long(19)  //订单id，关联payv2_pay_order    
	*/
	public Long getOrderId(){
		return orderId;
	}
	
	/**
	* order_id  Long(19)  //订单id，关联payv2_pay_order    
	*/
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	/**
	* app_id  Long(19)  //    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* refund_num  String(64)  //退款单号    
	*/
	public String getRefundNum(){
		return refundNum;
	}
	
	/**
	* refund_num  String(64)  //退款单号    
	*/
	public void setRefundNum(String refundNum){
		this.refundNum = refundNum;
	}
	
	/**
	* channel_id  Long(19)  //渠道id，关联payv2_channel表    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道id，关联payv2_channel表    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
	}
	
	/**
	* refund_type  Integer(10)  //退款类型1.全部退款2.部分退款    
	*/
	public Integer getRefundType(){
		return refundType;
	}
	
	/**
	* refund_type  Integer(10)  //退款类型1.全部退款2.部分退款    
	*/
	public void setRefundType(Integer refundType){
		this.refundType = refundType;
	}
	
	/**
	* order_num  String(64)  //订单号    
	*/
	public String getOrderNum(){
		return orderNum;
	}
	
	/**
	* order_num  String(64)  //订单号    
	*/
	public void setOrderNum(String orderNum){
		this.orderNum = orderNum;
	}
	
	/**
	* merchant_order_num  String(64)  //商户订单号    
	*/
	public String getMerchantOrderNum(){
		return merchantOrderNum;
	}
	
	/**
	* merchant_order_num  String(64)  //商户订单号    
	*/
	public void setMerchantOrderNum(String merchantOrderNum){
		this.merchantOrderNum = merchantOrderNum;
	}
	
	/**
	* refund_money  Double(9,2)  0.00  //退款金额    
	*/
	public Double getRefundMoney(){
		return refundMoney;
	}
	
	/**
	* refund_money  Double(9,2)  0.00  //退款金额    
	*/
	public void setRefundMoney(Double refundMoney){
		this.refundMoney = refundMoney;
	}
	
	/**
	* refund_reason  String(255)  //退款理由    
	*/
	public String getRefundReason(){
		return refundReason;
	}
	
	/**
	* refund_reason  String(255)  //退款理由    
	*/
	public void setRefundReason(String refundReason){
		this.refundReason = refundReason;
	}
	
	/**
	* refund_status  Integer(10)  1  //退款状态1.退款成功2.退款失败3.退款中4.退款成功回调失败    
	*/
	public Integer getRefundStatus(){
		return refundStatus;
	}
	
	/**
	* refund_status  Integer(10)  1  //退款状态1.退款成功2.退款失败3.退款中4.退款成功回调失败    
	*/
	public void setRefundStatus(Integer refundStatus){
		this.refundStatus = refundStatus;
	}
	
	/**
	* refund_time  Date(19)  //退款时间    
	*/
	public Date getRefundTime(){
		return refundTime;
	}
	
	/**
	* refund_time  Date(19)  //退款时间    
	*/
	public void setRefundTime(Date refundTime){
		this.refundTime = refundTime;
	}
	
	/**
	* liquidation_time  Date(19)  //清算时间    
	*/
	public Date getLiquidationTime(){
		return liquidationTime;
	}
	
	/**
	* liquidation_time  Date(19)  //清算时间    
	*/
	public void setLiquidationTime(Date liquidationTime){
		this.liquidationTime = liquidationTime;
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
	
	/**
	* update_time  Date(19)  //修改时间    
	*/
	public Date getUpdateTime(){
		return updateTime;
	}
	
	/**
	* update_time  Date(19)  //修改时间    
	*/
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public Double getPayDiscountMoney() {
		return payDiscountMoney;
	}

	public void setPayDiscountMoney(Double payDiscountMoney) {
		this.payDiscountMoney = payDiscountMoney;
	}

	public Double getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(Double surplusMoney) {
		this.surplusMoney = surplusMoney;
	}

	public String getRefundTimeBegin() {
		return refundTimeBegin;
	}

	public void setRefundTimeBegin(String refundTimeBegin) {
		this.refundTimeBegin = refundTimeBegin;
	}

	public String getRefundTimeEnd() {
		return refundTimeEnd;
	}

	public void setRefundTimeEnd(String refundTimeEnd) {
		this.refundTimeEnd = refundTimeEnd;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getPayWayId() {
		return payWayId;
	}

	public void setPayWayId(Long payWayId) {
		this.payWayId = payWayId;
	}

}