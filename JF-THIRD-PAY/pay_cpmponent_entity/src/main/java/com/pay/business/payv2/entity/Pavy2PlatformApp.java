package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.pavy2_platform_app       
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
channel_id           Long(19)                               //渠道id,关联payv2_channel
app_id               Long(19)                               //应用id，关联payv2_buss_company_app
platform_id          Long(19)                               //平台id,关联payv2_agent_platform
status               Integer(10)                 1          //状态1未添加2.已添加3.已取消
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Pavy2PlatformApp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long channelId;
	private	Long appId;
	private	Long platformId;
	private	Integer status;
	private	Date createTime;
	private	Date updateTime;

	private	String appName;
	
	private	Integer isIos;
	private	Integer isAndroid;
	private	Integer isWeb;

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
	* channel_id  Long(19)  //渠道id,关联payv2_channel    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道id,关联payv2_channel    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
	}
	
	/**
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* platform_id  Long(19)  //平台id,关联payv2_agent_platform    
	*/
	public Long getPlatformId(){
		return platformId;
	}
	
	/**
	* platform_id  Long(19)  //平台id,关联payv2_agent_platform    
	*/
	public void setPlatformId(Long platformId){
		this.platformId = platformId;
	}
	
	/**
	* status  Integer(10)  1  //状态1未添加2.已添加3.已取消    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  1  //状态1未添加2.已添加3.已取消    
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getIsIos() {
		return isIos;
	}

	public void setIsIos(Integer isIos) {
		this.isIos = isIos;
	}

	public Integer getIsAndroid() {
		return isAndroid;
	}

	public void setIsAndroid(Integer isAndroid) {
		this.isAndroid = isAndroid;
	}

	public Integer getIsWeb() {
		return isWeb;
	}

	public void setIsWeb(Integer isWeb) {
		this.isWeb = isWeb;
	}
	
}