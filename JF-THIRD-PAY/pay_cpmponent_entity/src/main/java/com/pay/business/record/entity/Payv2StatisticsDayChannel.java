package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_statistics_day_channel  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
channel_id           Long(19)                               //渠道商id
company_id           Long(19)                               //商户id,关联payv2_buss_company表
order_count          Integer(10)                            //累计订单
success_order_count  Integer(10)                            //成功订单总数
success_order_money  Double(16,2)                           //成功订单总金额
fail_order_count     Integer(10)                            //失败订单总数
fail_order_money     Double(16,2)                           //失败订单总金额
statistics_time      Date(19)                               //统计时间
create_time          Date(19)                               //创建时间
*/
public class Payv2StatisticsDayChannel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long channelId;
	private	Long companyId;
	private	Integer orderCount;
	private	Integer successOrderCount;
	private	Double successOrderMoney;
	private	Integer failOrderCount;
	private	Double failOrderMoney;
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
	* channel_id  Long(19)  //渠道商id    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道商id    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
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
	* order_count  Integer(10)  //累计订单    
	*/
	public Integer getOrderCount(){
		return orderCount;
	}
	
	/**
	* order_count  Integer(10)  //累计订单    
	*/
	public void setOrderCount(Integer orderCount){
		this.orderCount = orderCount;
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
	* fail_order_count  Integer(10)  //失败订单总数    
	*/
	public Integer getFailOrderCount(){
		return failOrderCount;
	}
	
	/**
	* fail_order_count  Integer(10)  //失败订单总数    
	*/
	public void setFailOrderCount(Integer failOrderCount){
		this.failOrderCount = failOrderCount;
	}
	
	/**
	* fail_order_money  Double(16,2)  //失败订单总金额    
	*/
	public Double getFailOrderMoney(){
		return failOrderMoney;
	}
	
	/**
	* fail_order_money  Double(16,2)  //失败订单总金额    
	*/
	public void setFailOrderMoney(Double failOrderMoney){
		this.failOrderMoney = failOrderMoney;
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