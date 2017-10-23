package com.pay.business.admin.entity;


import java.io.Serializable;
import java.util.Date;

/**
TABLE:.sys_ucenter_role         
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Integer(10)        NOTNULL             //应用ID（可以理解为那个系统）
rol_code             String(64)         NOTNULL             //角色编码
rol_name             String(128)        NOTNULL             //角色名称
rol_status           Integer(10)        NOTNULL  0          //用户当前状态 0-有效；1-无效 
create_time          Date(19)           NOTNULL  0000-00-00 00:00:00 //创建时间
create_user_by       Long(19)                               //创建用户id
update_time          Date(19)           NOTNULL  CURRENT_TIMESTAMP //最后一次修改时间
update_user_by       Long(19)                               //最后一次修改的用户id
*/
public class SysUcenterRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5303824100023527202L;
	private	Long id;
	private	Integer appId;
	private	String rolCode;
	private	String rolName;
	private	Integer rolStatus;
	private	Date createTime;
	private	Long createUserBy;
	private	Date updateTime;
	private	Long updateUserBy;

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
	* app_id  Integer(10)  NOTNULL  //应用ID（可以理解为那个系统）    
	*/
	public Integer getAppId(){
		return appId;
	}
	
	/**
	* app_id  Integer(10)  NOTNULL  //应用ID（可以理解为那个系统）    
	*/
	public void setAppId(Integer appId){
		this.appId = appId;
	}
	
	/**
	* rol_code  String(64)  NOTNULL  //角色编码    
	*/
	public String getRolCode(){
		return rolCode;
	}
	
	/**
	* rol_code  String(64)  NOTNULL  //角色编码    
	*/
	public void setRolCode(String rolCode){
		this.rolCode = rolCode;
	}
	
	/**
	* rol_name  String(128)  NOTNULL  //角色名称    
	*/
	public String getRolName(){
		return rolName;
	}
	
	/**
	* rol_name  String(128)  NOTNULL  //角色名称    
	*/
	public void setRolName(String rolName){
		this.rolName = rolName;
	}
	
	/**
	* rol_status  Integer(10)  NOTNULL  0  //用户当前状态  0-有效；1-无效    
	*/
	public Integer getRolStatus(){
		return rolStatus;
	}
	
	/**
	* rol_status  Integer(10)  NOTNULL  0  //用户当前状态  0-有效；1-无效    
	*/
	public void setRolStatus(Integer rolStatus){
		this.rolStatus = rolStatus;
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
	* create_user_by  Long(19)  //创建用户id    
	*/
	public Long getCreateUserBy(){
		return createUserBy;
	}
	
	/**
	* create_user_by  Long(19)  //创建用户id    
	*/
	public void setCreateUserBy(Long createUserBy){
		this.createUserBy = createUserBy;
	}
	
	/**
	* update_time  Date(19)  NOTNULL  CURRENT_TIMESTAMP  //最后一次修改时间    
	*/
	public Date getUpdateTime(){
		return updateTime;
	}
	
	/**
	* update_time  Date(19)  NOTNULL  CURRENT_TIMESTAMP  //最后一次修改时间    
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