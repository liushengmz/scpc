package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_agent_platform     
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
channel_id           Long(19)                               //渠道id，关联payv2_channel表
platform_name        String(100)                            //代理平台名称
platform_comm        Double(9)                   0          //平台佣金
is_open              Integer(10)                            //是否开启1.是2.否
is_delete            Integer(10)                 2          //是否删除（1是，2否）
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Payv2AgentPlatform implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long channelId;
	private	String platformName;
	private	Double platformComm;
	private	Integer isOpen;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;

	private	int number;
	
	private String channelName;//渠道商名称
	private Integer supportPayWayNumber;//支持支付通道数量

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
	* channel_id  Long(19)  //渠道id，关联payv2_channel表    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道id，关联payv2_channel表    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
	}
	
	/**
	* platform_name  String(100)  //代理平台名称    
	*/
	public String getPlatformName(){
		return platformName;
	}
	
	/**
	* platform_name  String(100)  //代理平台名称    
	*/
	public void setPlatformName(String platformName){
		this.platformName = platformName;
	}
	
	/**
	* platform_comm  Double(9)  0  //平台佣金    
	*/
	public Double getPlatformComm(){
		return platformComm;
	}
	
	/**
	* platform_comm  Double(9)  0  //平台佣金    
	*/
	public void setPlatformComm(Double platformComm){
		this.platformComm = platformComm;
	}
	
	/**
	* is_open  Integer(10)  //是否开启1.是2.否    
	*/
	public Integer getIsOpen(){
		return isOpen;
	}
	
	/**
	* is_open  Integer(10)  //是否开启1.是2.否    
	*/
	public void setIsOpen(Integer isOpen){
		this.isOpen = isOpen;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除（1是，2否）    
	*/
	public Integer getIsDelete(){
		return isDelete;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除（1是，2否）    
	*/
	public void setIsDelete(Integer isDelete){
		this.isDelete = isDelete;
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Integer getSupportPayWayNumber() {
		return supportPayWayNumber;
	}

	public void setSupportPayWayNumber(Integer supportPayWayNumber) {
		this.supportPayWayNumber = supportPayWayNumber;
	}

}