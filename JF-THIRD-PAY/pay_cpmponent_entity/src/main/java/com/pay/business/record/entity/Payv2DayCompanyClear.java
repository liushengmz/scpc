package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

/**
TABLE:.payv2_day_company_clear  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_check_id     String(30)         NOTNULL             //商户账单号
channel_id           Long(19)                               //渠道商编号,表payv2_channel
company_id           Long(19)                               //商户Id,表payv2_buss_company
success_count        Integer(10)                 0          //交易笔数
success_money        Double(16,2)                0.00       //已支付订单金额
refund_count         Integer(10)                 0          //退款笔数
refund_money         Double(16,2)                0.00       //已退款订单金额
rate_money           Double(16,2)                0.00       //手续费
rate_profit          Double(16,2)                0.00       //手续费利润
status               Integer(10)                 2          //结算状态,'1'是结算，‘2’是未结算
clear_time           Date(19)                               //结算时间
create_time          Date(19)                               //创建时间
 * @param <T>
*/

public class Payv2DayCompanyClear<T> implements Serializable{

	public Map<T, T> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(Map<T, T> detailMap) {
		this.detailMap = detailMap;
	}

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String companyCheckId;
	private	Long channelId;
	private	Long companyId;
	private	Integer successCount;
	private	Double successMoney;
	private	Integer refundCount;
	private	Double refundMoney;
	private	Double rateMoney;
	private	Double rateProfit;
	private	Integer status;
	private	Date settleTime;
	private	Date clearTime;	
	private	Date createTime;	
	private String channelName;
	private String companyName;	
	private Double cleanpayMoney;
	private Double accountsMonry;
	private Map<T,T> detailMap;

	public Double getCleanpayMoney() {
		return cleanpayMoney;
	}

	public void setCleanpayMoney(Double cleanpayMoney) {
		this.cleanpayMoney = cleanpayMoney;
	}

	public Double getAccountsMonry() {
		return accountsMonry;
	}

	public void setAccountsMonry(Double accountsMonry) {
		this.accountsMonry = accountsMonry;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	* company_check_id  String(30)  NOTNULL  //商户账单号    
	*/
	public String getCompanyCheckId(){
		return companyCheckId;
	}
	
	/**
	* company_check_id  String(30)  NOTNULL  //商户账单号    
	*/
	public void setCompanyCheckId(String companyCheckId){
		this.companyCheckId = companyCheckId;
	}
	
	/**
	* channel_id  Long(19)  //渠道商编号,表payv2_channel    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道商编号,表payv2_channel    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
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
	* success_count  Integer(10)  0  //交易笔数    
	*/
	public Integer getSuccessCount(){
		return successCount;
	}
	
	/**
	* success_count  Integer(10)  0  //交易笔数    
	*/
	public void setSuccessCount(Integer successCount){
		this.successCount = successCount;
	}
	
	/**
	* success_money  Double(16,2)  0.00  //已支付订单金额    
	*/
	public Double getSuccessMoney(){
		return successMoney;
	}
	
	/**
	* success_money  Double(16,2)  0.00  //已支付订单金额    
	*/
	public void setSuccessMoney(Double successMoney){
		this.successMoney = successMoney;
	}
	
	/**
	* refund_count  Integer(10)  0  //退款笔数    
	*/
	public Integer getRefundCount(){
		return refundCount;
	}
	
	/**
	* refund_count  Integer(10)  0  //退款笔数    
	*/
	public void setRefundCount(Integer refundCount){
		this.refundCount = refundCount;
	}
	
	/**
	* refund_money  Double(16,2)  0.00  //已退款订单金额    
	*/
	public Double getRefundMoney(){
		return refundMoney;
	}
	
	/**
	* refund_money  Double(16,2)  0.00  //已退款订单金额    
	*/
	public void setRefundMoney(Double refundMoney){
		this.refundMoney = refundMoney;
	}
	
	/**
	* rate_money  Double(16,2)  0.00  //手续费    
	*/
	public Double getRateMoney(){
		return rateMoney;
	}
	
	/**
	* rate_money  Double(16,2)  0.00  //手续费    
	*/
	public void setRateMoney(Double rateMoney){
		this.rateMoney = rateMoney;
	}
	
	/**
	* rate_profit  Double(16,2)  0.00  //手续费利润    
	*/
	public Double getRateProfit(){
		return rateProfit;
	}
	
	/**
	* rate_profit  Double(16,2)  0.00  //手续费利润    
	*/
	public void setRateProfit(Double rateProfit){
		this.rateProfit = rateProfit;
	}
	
	/**
	* status  Integer(10)  2  //结算状态,'1'是结算，‘2’是未结算    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  2  //结算状态,'1'是结算，‘2’是未结算    
	*/
	public void setStatus(Integer status){
		this.status = status;
	}
	
	/**
	* clear_time  Date(19)  //结算时间    
	*/
	public Date getClearTime(){
		return clearTime;
	}
	
	/**
	* clear_time  Date(19)  //结算时间    
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

	public Date getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}
	
}