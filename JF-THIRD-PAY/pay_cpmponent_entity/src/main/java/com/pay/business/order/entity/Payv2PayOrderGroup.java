package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_order_group    
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //应用id，关联payv2_buss_company_app表
group_value          String(100)                            //应用分组的值
create_time          Date(19)                               //创建时间
*/
public class Payv2PayOrderGroup implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long appId;
	private	String groupValue;
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
	* group_value  String(100)  //应用分组的值    
	*/
	public String getGroupValue(){
		return groupValue;
	}
	
	/**
	* group_value  String(100)  //应用分组的值    
	*/
	public void setGroupValue(String groupValue){
		this.groupValue = groupValue;
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