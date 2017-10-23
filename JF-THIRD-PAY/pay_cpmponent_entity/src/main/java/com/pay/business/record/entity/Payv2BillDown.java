package com.pay.business.record.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_bill_down          
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //应用id
url                  String(255)                            //账单地址
clear_time           Date(19)                               //账单时间
create_time          Date(19)                               //创建时间
*/
public class Payv2BillDown implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long appId;
	private	String url;
	private	Date clearTime;
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
	* app_id  Long(19)  //应用id    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //应用id    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* url  String(255)  //账单地址    
	*/
	public String getUrl(){
		return url;
	}
	
	/**
	* url  String(255)  //账单地址    
	*/
	public void setUrl(String url){
		this.url = url;
	}
	
	/**
	* clear_time  Date(19)  //账单时间    
	*/
	public Date getClearTime(){
		return clearTime;
	}
	
	/**
	* clear_time  Date(19)  //账单时间    
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
	
}