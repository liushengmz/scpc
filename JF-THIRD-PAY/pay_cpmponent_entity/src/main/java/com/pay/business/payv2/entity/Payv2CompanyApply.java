package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_company_apply      
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_name         String(50)                             //公司名称
contacts_name        String(50)                             //联系人
contacts_phone       String(20)                             //联系人手机号
email                String(50)                             //邮箱
source_type          Integer(10)                 1          //来源类型，1.商户app申请账号2.官网申请账号3.官网联系我们
remarks              String(1000)                           //备注
status               Integer(10)                            //状态1.处理，2.未处理
create_time          Date(19)                               //创建时间
*/
public class Payv2CompanyApply implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String companyName;
	private	String contactsName;
	private	String contactsPhone;
	private	String email;
	private	Integer sourceType;
	private	String remarks;
	private	Integer status;
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
	* company_name  String(50)  //公司名称    
	*/
	public String getCompanyName(){
		return companyName;
	}
	
	/**
	* company_name  String(50)  //公司名称    
	*/
	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}
	
	/**
	* contacts_name  String(50)  //联系人    
	*/
	public String getContactsName(){
		return contactsName;
	}
	
	/**
	* contacts_name  String(50)  //联系人    
	*/
	public void setContactsName(String contactsName){
		this.contactsName = contactsName;
	}
	
	/**
	* contacts_phone  String(20)  //联系人手机号    
	*/
	public String getContactsPhone(){
		return contactsPhone;
	}
	
	/**
	* contacts_phone  String(20)  //联系人手机号    
	*/
	public void setContactsPhone(String contactsPhone){
		this.contactsPhone = contactsPhone;
	}
	
	/**
	* email  String(50)  //邮箱    
	*/
	public String getEmail(){
		return email;
	}
	
	/**
	* email  String(50)  //邮箱    
	*/
	public void setEmail(String email){
		this.email = email;
	}
	
	/**
	* source_type  Integer(10)  1  //来源类型，1.商户app申请账号2.官网申请账号3.官网联系我们    
	*/
	public Integer getSourceType(){
		return sourceType;
	}
	
	/**
	* source_type  Integer(10)  1  //来源类型，1.商户app申请账号2.官网申请账号3.官网联系我们    
	*/
	public void setSourceType(Integer sourceType){
		this.sourceType = sourceType;
	}
	
	/**
	* remarks  String(1000)  //备注    
	*/
	public String getRemarks(){
		return remarks;
	}
	
	/**
	* remarks  String(1000)  //备注    
	*/
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	
	/**
	* status  Integer(10)  //状态1.处理，2.未处理    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  //状态1.处理，2.未处理    
	*/
	public void setStatus(Integer status){
		this.status = status;
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