package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_statistics_day_company_time  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_id           Long(19)                               //商户id，关联payv2_buss_company表
app_id               Long(19)                               //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
pay_way_id           Long(19)                               //支付渠道id,关联payv2_pay_way表
type                 Integer(10)                            //类型1.线上2线下
time_type            Integer(10)                            //时间区间0.0点-1点1.1点-2点。。。
success_order_count  Integer(10)                            //成功订单总数
success_order_money  Double(16,2)                           //成功订单总金额
statistics_time      Date(19)                               //统计时间
create_time          Date(19)                               //创建时间
*/
public class Payv2StatisticsDayCompanyTime implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long companyId;
	private	Long appId;
	private	Long payWayId;
	private	Integer type;
	private	Integer timeType;
	private	Integer successOrderCount;
	private	Double successOrderMoney;
	private	Date statisticsTime;
	private	Date createTime;

	private String payWayName;
	private String statisticsStringTime; //字符串类型日期 YYYY-MM-DD
	
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
	* company_id  Long(19)  //商户id，关联payv2_buss_company表    
	*/
	public Long getCompanyId(){
		return companyId;
	}
	
	/**
	* company_id  Long(19)  //商户id，关联payv2_buss_company表    
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
	* time_type  Integer(10)  //时间区间0.0点-1点1.1点-2点。。。    
	*/
	public Integer getTimeType(){
		return timeType;
	}
	
	/**
	* time_type  Integer(10)  //时间区间0.0点-1点1.1点-2点。。。    
	*/
	public void setTimeType(Integer timeType){
		this.timeType = timeType;
	}
	
	/**
	* success_order_count  Integer(10)  //成功订单总数    
	*/
	public Integer getSuccessOrderCount(){
		return successOrderCount;
	}
	
	/**
	* success_order_count  Integer(10)  //成功订单总数    
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

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public String getStatisticsStringTime() {
		return statisticsStringTime;
	}

	public void setStatisticsStringTime(String statisticsStringTime) {
		this.statisticsStringTime = statisticsStringTime;
	}
	
}