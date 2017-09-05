package com.pay.business.admin.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.sys_ucenter_admin        
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
adm_name             String(64)         NOTNULL             //用户登录名
adm_type             String(16)         NOTNULL  NORMAL     //用户类型: “SUPER”, “NORMAL”
adm_password         String(128)        NOTNULL             //用户登录密码
adm_display_name     String(64)         NOTNULL             //用户显示名称
adm_tel              String(64)         NOTNULL             //用户电话号码
adm_status           Integer(10)        NOTNULL  0          //用户当前状态 0-有效；1-无效 
create_time          Date(19)           NOTNULL  CURRENT_TIMESTAMP //创建时间
update_time          Date(19)                               //修改时间
*/
public class SysUcenterAdmin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8067797871524092296L;
	private	Long id;
	private	String admName;
	private	String admType;
	private	String admPassword;
	private	String admDisplayName;
	private	String admTel;
	private	Integer admStatus;
	private	Date createTime;
	private	Date updateTime;

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
	* adm_name  String(64)  NOTNULL  //用户登录名    
	*/
	public String getAdmName(){
		return admName;
	}
	
	/**
	* adm_name  String(64)  NOTNULL  //用户登录名    
	*/
	public void setAdmName(String admName){
		this.admName = admName;
	}
	
	/**
	* adm_type  String(16)  NOTNULL  NORMAL  //用户类型:  “SUPER”,  “NORMAL”    
	*/
	public String getAdmType(){
		return admType;
	}
	
	/**
	* adm_type  String(16)  NOTNULL  NORMAL  //用户类型:  “SUPER”,  “NORMAL”    
	*/
	public void setAdmType(String admType){
		this.admType = admType;
	}
	
	/**
	* adm_password  String(128)  NOTNULL  //用户登录密码    
	*/
	public String getAdmPassword(){
		return admPassword;
	}
	
	/**
	* adm_password  String(128)  NOTNULL  //用户登录密码    
	*/
	public void setAdmPassword(String admPassword){
		this.admPassword = admPassword;
	}
	
	/**
	* adm_display_name  String(64)  NOTNULL  //用户显示名称    
	*/
	public String getAdmDisplayName(){
		return admDisplayName;
	}
	
	/**
	* adm_display_name  String(64)  NOTNULL  //用户显示名称    
	*/
	public void setAdmDisplayName(String admDisplayName){
		this.admDisplayName = admDisplayName;
	}
	
	/**
	* adm_tel  String(64)  NOTNULL  //用户电话号码    
	*/
	public String getAdmTel(){
		return admTel;
	}
	
	/**
	* adm_tel  String(64)  NOTNULL  //用户电话号码    
	*/
	public void setAdmTel(String admTel){
		this.admTel = admTel;
	}
	
	/**
	* adm_status  Integer(10)  NOTNULL  0  //用户当前状态  0-有效；1-无效    
	*/
	public Integer getAdmStatus(){
		return admStatus;
	}
	
	/**
	* adm_status  Integer(10)  NOTNULL  0  //用户当前状态  0-有效；1-无效    
	*/
	public void setAdmStatus(Integer admStatus){
		this.admStatus = admStatus;
	}
	
	/**
	* create_time  Date(19)  NOTNULL  CURRENT_TIMESTAMP  //创建时间    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  NOTNULL  CURRENT_TIMESTAMP  //创建时间    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	/**
	* update_time  Date(19)  //修改时间    
	*/
	public Date getUpdateTime(){
		return updateTime;
	}
	
	/**
	* update_time  Date(19)  //修改时间    
	*/
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
}