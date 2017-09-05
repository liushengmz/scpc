package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_statistics_day_company_goods  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_id           Long(19)                               //商户id，关联payv2_buss_company表
app_id               Long(19)                               //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
type                 Integer(10)                            //类型1线上2线下
goods_id             Long(19)                               //商品id,关联payv2_pay_goods表
success_order_count  Integer(10)                            //成功订单笔数
success_order_money  Double(16,2)                           //成功订单金额
statistics_time      Date(19)                               //统计时间
create_time          Date(19)                               //创建时间
*/
public class Payv2StatisticsDayCompanyGoods implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long companyId;
	private	Long appId;
	private	Integer type;
	private	Long goodsId;
	private	Integer successOrderCount;
	private	Double successOrderMoney;
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
	* type  Integer(10)  //类型1线上2线下    
	*/
	public Integer getType(){
		return type;
	}
	
	/**
	* type  Integer(10)  //类型1线上2线下    
	*/
	public void setType(Integer type){
		this.type = type;
	}
	
	/**
	* goods_id  Long(19)  //商品id,关联payv2_pay_goods表    
	*/
	public Long getGoodsId(){
		return goodsId;
	}
	
	/**
	* goods_id  Long(19)  //商品id,关联payv2_pay_goods表    
	*/
	public void setGoodsId(Long goodsId){
		this.goodsId = goodsId;
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