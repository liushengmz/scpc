package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_bank               
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
bank_name            String(50)                             //
create_time          Date(19)                               //
*/
public class Payv2Bank implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String bankName;
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
	* bank_name  String(50)  //    
	*/
	public String getBankName(){
		return bankName;
	}
	
	/**
	* bank_name  String(50)  //    
	*/
	public void setBankName(String bankName){
		this.bankName = bankName;
	}
	
	/**
	* create_time  Date(19)  //    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  //    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
}