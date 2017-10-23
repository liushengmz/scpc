package com.pay.business.admin.entity;


import java.io.Serializable;
import java.util.Date;

/**
TABLE:.sys_ucenter_admin_role   
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Integer(10)        NOTNULL             //应用ID（可以理解为那个系统）
adm_id               Long(19)           NOTNULL             //管理员用户id（表 sys_ucenter__admin中id）
rol_id               Long(19)           NOTNULL             //角色id
exp_date             Date(10)           NOTNULL             //到期时间 
create_time          Date(19)           NOTNULL  0000-00-00 00:00:00 //创建时间
create_user_by       Long(19)                               //创建用户id
update_time          Date(19)           NOTNULL  CURRENT_TIMESTAMP //最后一次修改时间
update_user_by       Long(19)                               //最后一次修改的用户id
*/
public class SysUcenterAdminRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3572627847833052695L;
	private	Long id;
	private	Integer appId;
	private	Long admId;
	private	Long rolId;
	private	Date expDate;
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
	* adm_id  Long(19)  NOTNULL  //管理员用户id（表  sys_ucenter__admin中id）    
	*/
	public Long getAdmId(){
		return admId;
	}
	
	/**
	* adm_id  Long(19)  NOTNULL  //管理员用户id（表  sys_ucenter__admin中id）    
	*/
	public void setAdmId(Long admId){
		this.admId = admId;
	}
	
	/**
	* rol_id  Long(19)  NOTNULL  //角色id    
	*/
	public Long getRolId(){
		return rolId;
	}
	
	/**
	* rol_id  Long(19)  NOTNULL  //角色id    
	*/
	public void setRolId(Long rolId){
		this.rolId = rolId;
	}
	
	/**
	* exp_date  Date(10)  NOTNULL  //到期时间    
	*/
	public Date getExpDate(){
		return expDate;
	}
	
	/**
	* exp_date  Date(10)  NOTNULL  //到期时间    
	*/
	public void setExpDate(Date expDate){
		this.expDate = expDate;
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