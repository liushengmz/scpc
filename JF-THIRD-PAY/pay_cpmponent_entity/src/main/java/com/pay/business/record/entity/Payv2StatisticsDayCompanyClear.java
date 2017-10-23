package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_statistics_day_company_clear  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_id           Long(19)                               //商户id,关联payv2_buss_company表
app_id               Long(19)                               //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
pay_way_id           Long(19)                               //支付渠道id,关联payv2_pay_way表
type                 Integer(10)                            //类型1.线上2线下
success_order_count  Integer(10)                            //成功交易笔数
success_order_money  Double(16,2)                           //成功订单总金额
refund_order_money   Double(16,2)                           //退款订单金额
pay_way_money        Double(16,2)                           //通道费用
statistics_time      Date(19)                               //统计时间
create_time          Date(19)                               //创建时间
*/
public class Payv2StatisticsDayCompanyClear implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long companyId;
	private	Long appId;
	private	Long payWayId;
	private	Integer type;
	private	Integer successOrderCount;
	private	Double successOrderMoney;
	private	Double refundOrderMoney;
	private	Double payWayMoney;
	private	Date statisticsTime;
	private	Date createTime;
	private Double actuallyPayMoney;//实收金额
	private Double arrivalMoney;//到账金额
	private String wayName;//支付通道名称
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
	* pay_way_id  Long(19)  //支付渠道id,关联payv2_pay_way表    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付渠道id,关联payv2_pay_way表    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* type  Integer(10)  //类型1.线上2线下    
	*/
	public Integer getType(){
		return type;
	}
	
	/**
	* type  Integer(10)  //类型1.线上2线下    
	*/
	public void setType(Integer type){
		this.type = type;
	}
	
	/**
	* success_order_count  Integer(10)  //成功交易笔数    
	*/
	public Integer getSuccessOrderCount(){
		return successOrderCount;
	}
	
	/**
	* success_order_count  Integer(10)  //成功交易笔数    
	*/
	public void setSuccessOrderCount(Integer successOrderCount){
		this.successOrderCount = successOrderCount;
	}
	
	/**
	* success_order_money  Double(16,2)  //成功订单总金额    
	*/
	public Double getSuccessOrderMoney(){
		return successOrderMoney;
	}
	
	/**
	* success_order_money  Double(16,2)  //成功订单总金额    
	*/
	public void setSuccessOrderMoney(Double successOrderMoney){
		this.successOrderMoney = successOrderMoney;
	}
	
	/**
	* refund_order_money  Double(16,2)  //退款订单金额    
	*/
	public Double getRefundOrderMoney(){
		return refundOrderMoney;
	}
	
	/**
	* refund_order_money  Double(16,2)  //退款订单金额    
	*/
	public void setRefundOrderMoney(Double refundOrderMoney){
		this.refundOrderMoney = refundOrderMoney;
	}
	
	/**
	* pay_way_money  Double(16,2)  //通道费用    
	*/
	public Double getPayWayMoney(){
		return payWayMoney;
	}
	
	/**
	* pay_way_money  Double(16,2)  //通道费用    
	*/
	public void setPayWayMoney(Double payWayMoney){
		this.payWayMoney = payWayMoney;
	}
	
	/**
	* statistics_time  Date(19)  //统计时间    
	*/
	public Date getStatisticsTime(){
		return statisticsTime;
	}
	
	/**
	* statistics_time  Date(19)  //统计时间    
	*/
	public void setStatisticsTime(Date statisticsTime){
		this.statisticsTime = statisticsTime;
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

	public String getWayName() {
		return wayName;
	}

	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	
}