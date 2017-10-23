package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_company_quota      
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_id           Long(19)                               //商户id,关联payv2_buss_company表
rate_id              Long(19)                               //支付通道id,关联payv2_pay_way_rate表
total_money          Double(16,2)                0.00       //当天支付总金额
create_time          Date(19)                               //创建时间（当天时间）
*/
public class Payv2CompanyQuota implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long companyId;
	private	Long rateId;
	private	Double totalMoney;
	private	Date recordTime;
	private	Date createTime;
	private Integer payType;

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
	* rate_id  Long(19)  //支付通道id,关联payv2_pay_way_rate表    
	*/
	public Long getRateId(){
		return rateId;
	}
	
	/**
	* rate_id  Long(19)  //支付通道id,关联payv2_pay_way_rate表    
	*/
	public void setRateId(Long rateId){
		this.rateId = rateId;
	}
	
	/**
	* total_money  Double(16,2)  0.00  //当天支付总金额    
	*/
	public Double getTotalMoney(){
		return totalMoney;
	}
	
	/**
	* total_money  Double(16,2)  0.00  //当天支付总金额    
	*/
	public void setTotalMoney(Double totalMoney){
		this.totalMoney = totalMoney;
	}
	
	/**
	* create_time  Date(19)  //创建时间（当天时间）    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  //创建时间（当天时间）    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	
}