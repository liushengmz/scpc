package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_statistics_day_company  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_id           Long(19)                               //商户id,关联payv2_buss_company表
app_id               Long(19)                               //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
type                 Integer(10)                 1          //类型1线上2线下
success_order_count  Integer(10)                            //成功订单笔数
success_order_money  Double(16,2)                           //成功订单金额
fail_order_count     Integer(10)                            //失败订单笔数
fail_order_money     Double(16,2)                           //失败订单金额
handle_order_count   Integer(10)                            //处理中订单笔数
handle_order_money   Double(16,2)                           //处理中订单金额
statistics_time      Date(19)                               //统计时间
create_time          Date(19)                               //创建时间
*/
public class Payv2StatisticsDayCompany implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long companyId;
	private	Long appId;
	private	Integer type;
	private	Integer successOrderCount;
	private	Double successOrderMoney;
	private	Integer failOrderCount;
	private	Double failOrderMoney;
	private	Integer handleOrderCount;
	private	Double handleOrderMoney;
	private	Date statisticsTime;
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
	* type  Integer(10)  1  //类型1线上2线下    
	*/
	public Integer getType(){
		return type;
	}
	
	/**
	* type  Integer(10)  1  //类型1线上2线下    
	*/
	public void setType(Integer type){
		this.type = type;
	}
	
	/**
	* success_order_count  Integer(10)  //成功订单笔数    
	*/
	public Integer getSuccessOrderCount(){
		return successOrderCount;
	}
	
	/**
	* success_order_count  Integer(10)  //成功订单笔数    
	*/
	public void setSuccessOrderCount(Integer successOrderCount){
		this.successOrderCount = successOrderCount;
	}
	
	/**
	* success_order_money  Double(16,2)  //成功订单金额    
	*/
	public Double getSuccessOrderMoney(){
		return successOrderMoney;
	}
	
	/**
	* success_order_money  Double(16,2)  //成功订单金额    
	*/
	public void setSuccessOrderMoney(Double successOrderMoney){
		this.successOrderMoney = successOrderMoney;
	}
	
	/**
	* fail_order_count  Integer(10)  //失败订单笔数    
	*/
	public Integer getFailOrderCount(){
		return failOrderCount;
	}
	
	/**
	* fail_order_count  Integer(10)  //失败订单笔数    
	*/
	public void setFailOrderCount(Integer failOrderCount){
		this.failOrderCount = failOrderCount;
	}
	
	/**
	* fail_order_money  Double(16,2)  //失败订单金额    
	*/
	public Double getFailOrderMoney(){
		return failOrderMoney;
	}
	
	/**
	* fail_order_money  Double(16,2)  //失败订单金额    
	*/
	public void setFailOrderMoney(Double failOrderMoney){
		this.failOrderMoney = failOrderMoney;
	}
	
	/**
	* handle_order_count  Integer(10)  //处理中订单笔数    
	*/
	public Integer getHandleOrderCount(){
		return handleOrderCount;
	}
	
	/**
	* handle_order_count  Integer(10)  //处理中订单笔数    
	*/
	public void setHandleOrderCount(Integer handleOrderCount){
		this.handleOrderCount = handleOrderCount;
	}
	
	/**
	* handle_order_money  Double(16,2)  //处理中订单金额    
	*/
	public Double getHandleOrderMoney(){
		return handleOrderMoney;
	}
	
	/**
	* handle_order_money  Double(16,2)  //处理中订单金额    
	*/
	public void setHandleOrderMoney(Double handleOrderMoney){
		this.handleOrderMoney = handleOrderMoney;
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
	
}