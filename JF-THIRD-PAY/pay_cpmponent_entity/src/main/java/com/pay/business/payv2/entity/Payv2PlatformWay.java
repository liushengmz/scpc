package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_platform_way       
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
platform_id          Long(19)                               //代理平台id，关联payv2_agent_platform
pay_way_id           Long(19)                               //支付通道id，关联payv2_pay_way
status               Integer(10)                 1          //状态1开启2关闭
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Payv2PlatformWay implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long platformId;
	private	Long payWayId;
	private	Integer status;
	private	Date createTime;
	private	Date updateTime;
	
	private	String wayName;

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
	* platform_id  Long(19)  //代理平台id，关联payv2_agent_platform    
	*/
	public Long getPlatformId(){
		return platformId;
	}
	
	/**
	* platform_id  Long(19)  //代理平台id，关联payv2_agent_platform    
	*/
	public void setPlatformId(Long platformId){
		this.platformId = platformId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付通道id，关联payv2_pay_way    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付通道id，关联payv2_pay_way    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* status  Integer(10)  1  //状态1开启2关闭    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  1  //状态1开启2关闭    
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

	public String getWayName() {
		return wayName;
	}

	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
}