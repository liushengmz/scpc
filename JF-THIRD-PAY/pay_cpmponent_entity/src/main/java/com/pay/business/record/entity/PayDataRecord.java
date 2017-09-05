package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.pay_data_record          
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //应用id,表payv2_buss_company_app
company_id           Long(19)                               //商户id，表payv2_buss_company
channel_id           Long(19)                               //渠道商id，表payv2_channel
rate_id              Long(19)                               //通道id，表payv2_pay_way_rate
way_id               Long(19)                               //支付平台id，表payv2_pay_way
no_pay_amount        Double(16,2)                0.00       //未支付金额
no_pay_count         Integer(10)                 0          //未支付笔数
pay_amount           Double(16,2)                0.00       //支付成功金额
pay_count            Integer(10)                 0          //支付成功笔数
refund_amount        Double(16,2)                0.00       //退款金额
refund_count         Integer(10)                 0          //退款笔数
pay_net              Double(16,2)                0.00       //交易净额
rate_amount          Double(16,2)                0.00       //手续费
rate_cost            Double(16,2)                0.00       //成本手续费
rate_profit          Double(16,2)                0.00       //手续费利润
record_time          Date(19)                               //记录时间,保留到小时.如（YYYY-MM-DD HH:00:00）
create_time          Date(19)                               //创建时间
*/
public class PayDataRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long appId;
	private	Long companyId;
	private	Long channelId;
	private	Long rateId;
	private	Long wayId;
	private	Double noPayAmount;
	private	Integer noPayCount;
	private	Double payAmount;
	private	Integer payCount;
	private	Double refundAmount;
	private	Integer refundCount;
	private	Double payNet;
	private	Double rateAmount;
	private	Double rateCost;
	private	Double rateProfit;
	private	Date recordTime;
	private	Date createTime;

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
	* app_id  Long(19)  //应用id,表payv2_buss_company_app    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //应用id,表payv2_buss_company_app    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* company_id  Long(19)  //商户id，表payv2_buss_company    
	*/
	public Long getCompanyId(){
		return companyId;
	}
	
	/**
	* company_id  Long(19)  //商户id，表payv2_buss_company    
	*/
	public void setCompanyId(Long companyId){
		this.companyId = companyId;
	}
	
	/**
	* channel_id  Long(19)  //渠道商id，表payv2_channel    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道商id，表payv2_channel    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
	}
	
	/**
	* rate_id  Long(19)  //通道id，表payv2_pay_way_rate    
	*/
	public Long getRateId(){
		return rateId;
	}
	
	/**
	* rate_id  Long(19)  //通道id，表payv2_pay_way_rate    
	*/
	public void setRateId(Long rateId){
		this.rateId = rateId;
	}
	
	/**
	* way_id  Long(19)  //支付平台id，表payv2_pay_way    
	*/
	public Long getWayId(){
		return wayId;
	}
	
	/**
	* way_id  Long(19)  //支付平台id，表payv2_pay_way    
	*/
	public void setWayId(Long wayId){
		this.wayId = wayId;
	}
	
	/**
	* pay_amount  Double(16,2)  0.00  //支付成功金额    
	*/
	public Double getPayAmount(){
		return payAmount;
	}
	
	/**
	* pay_amount  Double(16,2)  0.00  //支付成功金额    
	*/
	public void setPayAmount(Double payAmount){
		this.payAmount = payAmount;
	}
	
	/**
	* pay_count  Integer(10)  0  //支付成功笔数    
	*/
	public Integer getPayCount(){
		return payCount;
	}
	
	/**
	* pay_count  Integer(10)  0  //支付成功笔数    
	*/
	public void setPayCount(Integer payCount){
		this.payCount = payCount;
	}
	
	/**
	* refund_amount  Double(16,2)  0.00  //退款金额    
	*/
	public Double getRefundAmount(){
		return refundAmount;
	}
	
	/**
	* refund_amount  Double(16,2)  0.00  //退款金额    
	*/
	public void setRefundAmount(Double refundAmount){
		this.refundAmount = refundAmount;
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
	* pay_net  Double(16,2)  0.00  //交易净额    
	*/
	public Double getPayNet(){
		return payNet;
	}
	
	/**
	* pay_net  Double(16,2)  0.00  //交易净额    
	*/
	public void setPayNet(Double payNet){
		this.payNet = payNet;
	}
	
	/**
	* rate_amount  Double(16,2)  0.00  //手续费    
	*/
	public Double getRateAmount(){
		return rateAmount;
	}
	
	/**
	* rate_amount  Double(16,2)  0.00  //手续费    
	*/
	public void setRateAmount(Double rateAmount){
		this.rateAmount = rateAmount;
	}
	
	/**
	* rate_cost  Double(16,2)  0.00  //成本手续费    
	*/
	public Double getRateCost(){
		return rateCost;
	}
	
	/**
	* rate_cost  Double(16,2)  0.00  //成本手续费    
	*/
	public void setRateCost(Double rateCost){
		this.rateCost = rateCost;
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
	* record_time  Date(19)  //记录时间,保留到小时.如（YYYY-MM-DD  HH:00:00）    
	*/
	public Date getRecordTime(){
		return recordTime;
	}
	
	/**
	* record_time  Date(19)  //记录时间,保留到小时.如（YYYY-MM-DD  HH:00:00）    
	*/
	public void setRecordTime(Date recordTime){
		this.recordTime = recordTime;
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

	public Double getNoPayAmount() {
		return noPayAmount;
	}

	public void setNoPayAmount(Double noPayAmount) {
		this.noPayAmount = noPayAmount;
	}

	public Integer getNoPayCount() {
		return noPayCount;
	}

	public void setNoPayCount(Integer noPayCount) {
		this.noPayCount = noPayCount;
	}
	
}