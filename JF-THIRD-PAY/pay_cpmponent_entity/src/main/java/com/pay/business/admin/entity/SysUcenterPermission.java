package com.pay.business.admin.entity;


import java.io.Serializable;
import java.util.Date;

/**
TABLE:.sys_ucenter_permission   
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
rol_id               Long(19)           NOTNULL             //角色id
fun_id               Long(19)           NOTNULL             //功能id
prm_params           String(2048)                           //参数-预留
prm_remark           String(512)                            //备注
create_time          Date(19)           NOTNULL  0000-00-00 00:00:00 //创建时间
create_user_by       Long(19)                               //创建用户id
update_time          Date(19)           NOTNULL  CURRENT_TIMESTAMP //最后一次修改时间
update_user_by       Long(19)                               //最后一次修改的用户id
*/
public class SysUcenterPermission implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6993871571084076704L;
	private	Long id;
	private	Long rolId;
	private	Long funId;
	private	String prmParams;
	private	String prmRemark;
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
	* fun_id  Long(19)  NOTNULL  //功能id    
	*/
	public Long getFunId(){
		return funId;
	}
	
	/**
	* fun_id  Long(19)  NOTNULL  //功能id    
	*/
	public void setFunId(Long funId){
		this.funId = funId;
	}
	
	/**
	* prm_params  String(2048)  //参数-预留    
	*/
	public String getPrmParams(){
		return prmParams;
	}
	
	/**
	* prm_params  String(2048)  //参数-预留    
	*/
	public void setPrmParams(String prmParams){
		this.prmParams = prmParams;
	}
	
	/**
	* prm_remark  String(512)  //备注    
	*/
	public String getPrmRemark(){
		return prmRemark;
	}
	
	/**
	* prm_remark  String(512)  //备注    
	*/
	public void setPrmRemark(String prmRemark){
		this.prmRemark = prmRemark;
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