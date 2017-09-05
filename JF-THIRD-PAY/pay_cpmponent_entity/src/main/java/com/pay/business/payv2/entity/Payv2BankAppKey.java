package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_bank_app_key       
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //应用id，关联payv2_buss_company_app表
rate_id              Long(19)                               //通道路由id，关联payv2_pay_way_rate表
bank_key             String(128)                            //银行分配的key
bank_key1            String(128)                            //银行分配的数据字段1，预留字段
bank_key2            String(128)                            //银行分配的数据字段2，预留字段
bank_key3            String(128)                            //银行分配的数据字段3，预留字段
create_time          Date(19)                               //创建时间
*/
public class Payv2BankAppKey implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long appId;
	private	Long rateId;
	private	String bankKey;
	private	String bankKey1;
	private	String bankKey2;
	private	String bankKey3;
	private	Date createTime;
	
	private String appName;

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
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app表    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app表    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* rate_id  Long(19)  //通道路由id，关联payv2_pay_way_rate表    
	*/
	public Long getRateId(){
		return rateId;
	}
	
	/**
	* rate_id  Long(19)  //通道路由id，关联payv2_pay_way_rate表    
	*/
	public void setRateId(Long rateId){
		this.rateId = rateId;
	}
	
	/**
	* bank_key  String(128)  //银行分配的key    
	*/
	public String getBankKey(){
		return bankKey;
	}
	
	/**
	* bank_key  String(128)  //银行分配的key    
	*/
	public void setBankKey(String bankKey){
		this.bankKey = bankKey;
	}
	
	/**
	* bank_key1  String(128)  //银行分配的数据字段1，预留字段    
	*/
	public String getBankKey1(){
		return bankKey1;
	}
	
	/**
	* bank_key1  String(128)  //银行分配的数据字段1，预留字段    
	*/
	public void setBankKey1(String bankKey1){
		this.bankKey1 = bankKey1;
	}
	
	/**
	* bank_key2  String(128)  //银行分配的数据字段2，预留字段    
	*/
	public String getBankKey2(){
		return bankKey2;
	}
	
	/**
	* bank_key2  String(128)  //银行分配的数据字段2，预留字段    
	*/
	public void setBankKey2(String bankKey2){
		this.bankKey2 = bankKey2;
	}
	
	/**
	* bank_key3  String(128)  //银行分配的数据字段3，预留字段    
	*/
	public String getBankKey3(){
		return bankKey3;
	}
	
	/**
	* bank_key3  String(128)  //银行分配的数据字段3，预留字段    
	*/
	public void setBankKey3(String bankKey3){
		this.bankKey3 = bankKey3;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
}