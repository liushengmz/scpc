package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.*;

/**
TABLE:.payv2_buss_way_detail    
--------------------------------------------------------
id                   Long(19)           NOTNULL             //账单明细id
company_check_id     String(30)                             //账单号
company_id           Long(19)                               //商户Id,表payv2_buss_company
pay_way_id           Long(19)                               //支付通道Id,表payv2_pay_way_rate
buss_money           Double(16,2)                0.00       //交易支付净额
cost_rate            Double(9,2)                 0.00       //成本费率
cost_rate_money      Double(16,2)                0.00       //成本手续费
buss_way_rate        Double(9,2)                 0.00       //商户签约费率
buss_way_rate_money  Double(16,2)                0.00       //商户手续费
clear_time           Date(19)                               //
create_time          Date(19)                               //创建时间
*/
public class Payv2BussWayDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String companyCheckId;
	private	Long channelId;
	private	Long companyId;
	private	Long appId;
	private	Long payWayId;
	private	Integer successCount;
	private	Double successMoney;
	private	Integer refundCount;
	private	Double refundMoney;
	private	Double bussMoney;
	private	Double costRate;
	private	Double costRateMoney;
	private	Double bussWayRate;
	private	Double bussWayRateMoney;
	private	Date clearTime;
	private	Date createTime;	 
	private String rateName;
	private Double accountsMonry;//结算金额
	private Double rateProfit;//手续费利润
	
	

	public Double getRateProfit() {
		return rateProfit;
	}

	public void setRateProfit(Double rateProfit) {
		this.rateProfit = rateProfit;
	}

	public Double getAccountsMonry() {
		return accountsMonry;
	}

	public void setAccountsMonry(Double accountsMonry) {
		this.accountsMonry = accountsMonry;
	}
	

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	/**
	* id  Long(19)  NOTNULL  //账单明细id    
	*/
	public Long getId(){
		return id;
	}
	
	/**
	* id  Long(19)  NOTNULL  //账单明细id    
	*/
	public void setId(Long id){
		this.id = id;
	}
	
	/**
	* company_check_id  String(30)  //账单号    
	*/
	public String getCompanyCheckId(){
		return companyCheckId;
	}
	
	/**
	* company_check_id  String(30)  //账单号    
	*/
	public void setCompanyCheckId(String companyCheckId){
		this.companyCheckId = companyCheckId;
	}
	
	/**
	* company_id  Long(19)  //商户Id,表payv2_buss_company    
	*/
	public Long getCompanyId(){
		return companyId;
	}
	
	/**
	* company_id  Long(19)  //商户Id,表payv2_buss_company    
	*/
	public void setCompanyId(Long companyId){
		this.companyId = companyId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付通道Id,表payv2_pay_way_rate    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付通道Id,表payv2_pay_way_rate    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* buss_money  Double(16,2)  0.00  //交易支付净额    
	*/
	public Double getBussMoney(){
		return bussMoney;
	}
	
	/**
	* buss_money  Double(16,2)  0.00  //交易支付净额    
	*/
	public void setBussMoney(Double bussMoney){
		this.bussMoney = bussMoney;
	}
	
	/**
	* cost_rate  Double(9,2)  0.00  //成本费率    
	*/
	public Double getCostRate(){
		return costRate;
	}
	
	/**
	* cost_rate  Double(9,2)  0.00  //成本费率    
	*/
	public void setCostRate(Double costRate){
		this.costRate = costRate;
	}
	
	/**
	* cost_rate_money  Double(16,2)  0.00  //成本手续费    
	*/
	public Double getCostRateMoney(){
		return costRateMoney;
	}
	
	/**
	* cost_rate_money  Double(16,2)  0.00  //成本手续费    
	*/
	public void setCostRateMoney(Double costRateMoney){
		this.costRateMoney = costRateMoney;
	}
	
	/**
	* buss_way_rate  Double(9,2)  0.00  //商户签约费率    
	*/
	public Double getBussWayRate(){
		return bussWayRate;
	}
	
	/**
	* buss_way_rate  Double(9,2)  0.00  //商户签约费率    
	*/
	public void setBussWayRate(Double bussWayRate){
		this.bussWayRate = bussWayRate;
	}
	
	/**
	* buss_way_rate_money  Double(16,2)  0.00  //商户手续费    
	*/
	public Double getBussWayRateMoney(){
		return bussWayRateMoney;
	}
	
	/**
	* buss_way_rate_money  Double(16,2)  0.00  //商户手续费    
	*/
	public void setBussWayRateMoney(Double bussWayRateMoney){
		this.bussWayRateMoney = bussWayRateMoney;
	}
	
	/**
	* clear_time  Date(19)  //    
	*/
	public Date getClearTime(){
		return clearTime;
	}
	
	/**
	* clear_time  Date(19)  //    
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

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Double getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(Double successMoney) {
		this.successMoney = successMoney;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}

	public Double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}
	
}