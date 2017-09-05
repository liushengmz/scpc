package com.pay.business.admin.entity;

import java.util.Date;

/**
TABLE:.sys_ucenter_apps         
--------------------------------------------------------
id                   Integer(10)        NOTNULL             //
app_name             String(120)        NOTNULL             //应用名称
app_type             Integer(10)        NOTNULL             //应用类型（1-app;2-pc;3-其他） 
app_key              String(64)         NOTNULL             //应用编码
app_logo             String(256)                            //应用logo
app_desc             String(256)                            //应用描述
app_contacts_name    String(20)                             //联系人姓名
app_contacts_phone   String(15)                             //联系电话
create_time          Date(19)           NOTNULL  0000-00-00 00:00:00 //创建时间
create_user_by       Long(19)           NOTNULL             //创建用户id
update_time          Date(19)                    CURRENT_TIMESTAMP //最后一次修改时间
update_user_by       Long(19)                               //最后一次修改的用户id
*/
public class SysUcenterApps {
	private	Integer id;
	private	String appName;
	private	Integer appType;
	private	String appKey;
	private	String appLogo;
	private	String appDesc;
	private	String appContactsName;
	private	String appContactsPhone;
	private	Date createTime;
	private	Long createUserBy;
	private	Date updateTime;
	private	Long updateUserBy;

	/**
	* id  Integer(10)  NOTNULL  //    
	*/
	public Integer getId(){
		return id;
	}
	
	/**
	* id  Integer(10)  NOTNULL  //    
	*/
	public void setId(Integer id){
		this.id = id;
	}
	
	/**
	* app_name  String(120)  NOTNULL  //应用名称    
	*/
	public String getAppName(){
		return appName;
	}
	
	/**
	* app_name  String(120)  NOTNULL  //应用名称    
	*/
	public void setAppName(String appName){
		this.appName = appName;
	}
	
	/**
	* app_type  Integer(10)  NOTNULL  //应用类型（1-app;2-pc;3-其他）    
	*/
	public Integer getAppType(){
		return appType;
	}
	
	/**
	* app_type  Integer(10)  NOTNULL  //应用类型（1-app;2-pc;3-其他）    
	*/
	public void setAppType(Integer appType){
		this.appType = appType;
	}
	
	/**
	* app_key  String(64)  NOTNULL  //应用编码    
	*/
	public String getAppKey(){
		return appKey;
	}
	
	/**
	* app_key  String(64)  NOTNULL  //应用编码    
	*/
	public void setAppKey(String appKey){
		this.appKey = appKey;
	}
	
	/**
	* app_logo  String(256)  //应用logo    
	*/
	public String getAppLogo(){
		return appLogo;
	}
	
	/**
	* app_logo  String(256)  //应用logo    
	*/
	public void setAppLogo(String appLogo){
		this.appLogo = appLogo;
	}
	
	/**
	* app_desc  String(256)  //应用描述    
	*/
	public String getAppDesc(){
		return appDesc;
	}
	
	/**
	* app_desc  String(256)  //应用描述    
	*/
	public void setAppDesc(String appDesc){
		this.appDesc = appDesc;
	}
	
	/**
	* app_contacts_name  String(20)  //联系人姓名    
	*/
	public String getAppContactsName(){
		return appContactsName;
	}
	
	/**
	* app_contacts_name  String(20)  //联系人姓名    
	*/
	public void setAppContactsName(String appContactsName){
		this.appContactsName = appContactsName;
	}
	
	/**
	* app_contacts_phone  String(15)  //联系电话    
	*/
	public String getAppContactsPhone(){
		return appContactsPhone;
	}
	
	/**
	* app_contacts_phone  String(15)  //联系电话    
	*/
	public void setAppContactsPhone(String appContactsPhone){
		this.appContactsPhone = appContactsPhone;
	}
	
	/**
	* create_time  Date(19)  NOTNULL  0000-00-00  00:00:00  //创建时间    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  NOTNULL  0000-00-00  00:00:00  //创建时间    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	/**
	* create_user_by  Long(19)  NOTNULL  //创建用户id    
	*/
	public Long getCreateUserBy(){
		return createUserBy;
	}
	
	/**
	* create_user_by  Long(19)  NOTNULL  //创建用户id    
	*/
	public void setCreateUserBy(Long createUserBy){
		this.createUserBy = createUserBy;
	}
	
	/**
	* update_time  Date(19)  CURRENT_TIMESTAMP  //最后一次修改时间    
	*/
	public Date getUpdateTime(){
		return updateTime;
	}
	
	/**
	* update_time  Date(19)  CURRENT_TIMESTAMP  //最后一次修改时间    
	*/
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	/**
	* update_user_by  Long(19)  //最后一次修改的用户id    
	*/
	public Long getUpdateUserBy(){
		return updateUserBy;
	}
	
	/**
	* update_user_by  Long(19)  //最后一次修改的用户id    
	*/
	public void setUpdateUserBy(Long updateUserBy){
		this.updateUserBy = updateUserBy;
	}
	
}