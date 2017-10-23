package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_user           
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //应用ID,标注该用户是从哪个应用来的
user_mobile          String(11)                             //用户手机号
user_device_token    String(50)                             //用户手机唯一标识
create_time          Date(19)                               //
*/
public class Payv2PayUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long appId;
	private	String userMobile;
	private	String userDeviceToken;
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
	* app_id  Long(19)  //应用ID,标注该用户是从哪个应用来的    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //应用ID,标注该用户是从哪个应用来的    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* user_mobile  String(11)  //用户手机号    
	*/
	public String getUserMobile(){
		return userMobile;
	}
	
	/**
	* user_mobile  String(11)  //用户手机号    
	*/
	public void setUserMobile(String userMobile){
		this.userMobile = userMobile;
	}
	
	/**
	* user_device_token  String(50)  //用户手机唯一标识    
	*/
	public String getUserDeviceToken(){
		return userDeviceToken;
	}
	
	/**
	* user_device_token  String(50)  //用户手机唯一标识    
	*/
	public void setUserDeviceToken(String userDeviceToken){
		this.userDeviceToken = userDeviceToken;
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