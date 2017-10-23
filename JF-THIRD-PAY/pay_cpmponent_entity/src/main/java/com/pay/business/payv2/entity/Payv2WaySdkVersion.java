package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_way_sdk_version    
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
pay_way_id           Long(19)                               //钱包id,关联payv2_pay_way
sdk_file_url         String(255)                            //sdk下载链接
sdk_md5              String(64)                             //md5值
file_size            Integer(10)                            //文件大小
sdk_type             Integer(10)                 1          //1Android,2iOS
sdk_version          String(20)                             //版本号
sdk_version_code     Integer(10)                            //子版本号
is_online            Integer(10)                 2          //是否上线,1是,2否
is_delete            Integer(10)                 2          //是否删除1.是2.否
create_time          Date(19)                               //
update_time          Date(19)                               //
*/
public class Payv2WaySdkVersion implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long payWayId;
	private	String sdkFileUrl;
	private	String sdkMd5;
	private	Integer fileSize;
	private	Integer sdkType;
	private	String sdkVersion;
	private	Integer sdkVersionCode;
	private	Integer isOnline;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;
	private String payWayName;//所属钱包

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
	* pay_way_id  Long(19)  //钱包id,关联payv2_pay_way    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //钱包id,关联payv2_pay_way    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* sdk_file_url  String(255)  //sdk下载链接    
	*/
	public String getSdkFileUrl(){
		return sdkFileUrl;
	}
	
	/**
	* sdk_file_url  String(255)  //sdk下载链接    
	*/
	public void setSdkFileUrl(String sdkFileUrl){
		this.sdkFileUrl = sdkFileUrl;
	}
	
	/**
	* sdk_md5  String(64)  //md5值    
	*/
	public String getSdkMd5(){
		return sdkMd5;
	}
	
	/**
	* sdk_md5  String(64)  //md5值    
	*/
	public void setSdkMd5(String sdkMd5){
		this.sdkMd5 = sdkMd5;
	}
	
	/**
	* file_size  Integer(10)  //文件大小    
	*/
	public Integer getFileSize(){
		return fileSize;
	}
	
	/**
	* file_size  Integer(10)  //文件大小    
	*/
	public void setFileSize(Integer fileSize){
		this.fileSize = fileSize;
	}
	
	/**
	* sdk_type  Integer(10)  1  //1Android,2iOS    
	*/
	public Integer getSdkType(){
		return sdkType;
	}
	
	/**
	* sdk_type  Integer(10)  1  //1Android,2iOS    
	*/
	public void setSdkType(Integer sdkType){
		this.sdkType = sdkType;
	}
	
	/**
	* sdk_version  String(20)  //版本号    
	*/
	public String getSdkVersion(){
		return sdkVersion;
	}
	
	/**
	* sdk_version  String(20)  //版本号    
	*/
	public void setSdkVersion(String sdkVersion){
		this.sdkVersion = sdkVersion;
	}
	
	/**
	* sdk_version_code  Integer(10)  //子版本号    
	*/
	public Integer getSdkVersionCode(){
		return sdkVersionCode;
	}
	
	/**
	* sdk_version_code  Integer(10)  //子版本号    
	*/
	public void setSdkVersionCode(Integer sdkVersionCode){
		this.sdkVersionCode = sdkVersionCode;
	}
	
	/**
	* is_online  Integer(10)  2  //是否上线,1是,2否    
	*/
	public Integer getIsOnline(){
		return isOnline;
	}
	
	/**
	* is_online  Integer(10)  2  //是否上线,1是,2否    
	*/
	public void setIsOnline(Integer isOnline){
		this.isOnline = isOnline;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除1.是2.否    
	*/
	public Integer getIsDelete(){
		return isDelete;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除1.是2.否    
	*/
	public void setIsDelete(Integer isDelete){
		this.isDelete = isDelete;
	}
	
	/**
	* create_time  Date(19)  //    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  //    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	/**
	* update_time  Date(19)  //    
	*/
	public Date getUpdateTime(){
		return updateTime;
	}
	
	/**
	* update_time  Date(19)  //    
	*/
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}
	
}