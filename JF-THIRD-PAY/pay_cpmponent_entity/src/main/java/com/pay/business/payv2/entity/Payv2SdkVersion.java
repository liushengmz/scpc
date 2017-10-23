package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_sdk_version        
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
sdk_file_name        String(100)                            //核心包文件名
sdk_file_url         String(255)                            //核心包sdk下载链接
sdk_document_name    String(100)                            //文档名称
sdk_document_url     String(255)                            //文档下载地址
sdk_type             Integer(10)                 1          //1Android,2iOS
sdk_version          String(20)                             //核心包版本号
sdk_version_code     Integer(10)                            //核心包子版本号
sdk_describe         String(65535)                          //更新说明
sdk_md5              String(128)                            //核心包md5值
resource_file_name   String(100)                            //资源包名称
resource_file_url    String(255)                            //资源包地址
resource_version     String(20)                             //资源包版本号
resource_version_code Integer(10)                            //资源包子版本
resource_md5         String(128)                            //资源包md5
is_online            Integer(10)                 2          //是否上线,1是,2否
create_time          Date(19)                               //
update_time          Date(19)                               //
*/
public class Payv2SdkVersion implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String sdkFileName;
	private	String sdkFileUrl;
	private	String sdkDocumentName;
	private	String sdkDocumentUrl;
	private	Integer sdkType;
	private	String sdkVersion;
	private	Integer sdkVersionCode;
	private	String sdkDescribe;
	private	String sdkMd5;
	private	String resourceFileName;
	private	String resourceFileUrl;
	private	String resourceVersion;
	private	Integer resourceVersionCode;
	private	String resourceMd5;
	private	Integer isOnline;
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
	* sdk_file_name  String(100)  //核心包文件名    
	*/
	public String getSdkFileName(){
		return sdkFileName;
	}
	
	/**
	* sdk_file_name  String(100)  //核心包文件名    
	*/
	public void setSdkFileName(String sdkFileName){
		this.sdkFileName = sdkFileName;
	}
	
	/**
	* sdk_file_url  String(255)  //核心包sdk下载链接    
	*/
	public String getSdkFileUrl(){
		return sdkFileUrl;
	}
	
	/**
	* sdk_file_url  String(255)  //核心包sdk下载链接    
	*/
	public void setSdkFileUrl(String sdkFileUrl){
		this.sdkFileUrl = sdkFileUrl;
	}
	
	/**
	* sdk_document_name  String(100)  //文档名称    
	*/
	public String getSdkDocumentName(){
		return sdkDocumentName;
	}
	
	/**
	* sdk_document_name  String(100)  //文档名称    
	*/
	public void setSdkDocumentName(String sdkDocumentName){
		this.sdkDocumentName = sdkDocumentName;
	}
	
	/**
	* sdk_document_url  String(255)  //文档下载地址    
	*/
	public String getSdkDocumentUrl(){
		return sdkDocumentUrl;
	}
	
	/**
	* sdk_document_url  String(255)  //文档下载地址    
	*/
	public void setSdkDocumentUrl(String sdkDocumentUrl){
		this.sdkDocumentUrl = sdkDocumentUrl;
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
	* sdk_version  String(20)  //核心包版本号    
	*/
	public String getSdkVersion(){
		return sdkVersion;
	}
	
	/**
	* sdk_version  String(20)  //核心包版本号    
	*/
	public void setSdkVersion(String sdkVersion){
		this.sdkVersion = sdkVersion;
	}
	
	/**
	* sdk_version_code  Integer(10)  //核心包子版本号    
	*/
	public Integer getSdkVersionCode(){
		return sdkVersionCode;
	}
	
	/**
	* sdk_version_code  Integer(10)  //核心包子版本号    
	*/
	public void setSdkVersionCode(Integer sdkVersionCode){
		this.sdkVersionCode = sdkVersionCode;
	}
	
	/**
	* sdk_describe  String(65535)  //更新说明    
	*/
	public String getSdkDescribe(){
		return sdkDescribe;
	}
	
	/**
	* sdk_describe  String(65535)  //更新说明    
	*/
	public void setSdkDescribe(String sdkDescribe){
		this.sdkDescribe = sdkDescribe;
	}
	
	/**
	* sdk_md5  String(128)  //核心包md5值    
	*/
	public String getSdkMd5(){
		return sdkMd5;
	}
	
	/**
	* sdk_md5  String(128)  //核心包md5值    
	*/
	public void setSdkMd5(String sdkMd5){
		this.sdkMd5 = sdkMd5;
	}
	
	/**
	* resource_file_name  String(100)  //资源包名称    
	*/
	public String getResourceFileName(){
		return resourceFileName;
	}
	
	/**
	* resource_file_name  String(100)  //资源包名称    
	*/
	public void setResourceFileName(String resourceFileName){
		this.resourceFileName = resourceFileName;
	}
	
	/**
	* resource_file_url  String(255)  //资源包地址    
	*/
	public String getResourceFileUrl(){
		return resourceFileUrl;
	}
	
	/**
	* resource_file_url  String(255)  //资源包地址    
	*/
	public void setResourceFileUrl(String resourceFileUrl){
		this.resourceFileUrl = resourceFileUrl;
	}
	
	/**
	* resource_version  String(20)  //资源包版本号    
	*/
	public String getResourceVersion(){
		return resourceVersion;
	}
	
	/**
	* resource_version  String(20)  //资源包版本号    
	*/
	public void setResourceVersion(String resourceVersion){
		this.resourceVersion = resourceVersion;
	}
	
	/**
	* resource_version_code  Integer(10)  //资源包子版本    
	*/
	public Integer getResourceVersionCode(){
		return resourceVersionCode;
	}
	
	/**
	* resource_version_code  Integer(10)  //资源包子版本    
	*/
	public void setResourceVersionCode(Integer resourceVersionCode){
		this.resourceVersionCode = resourceVersionCode;
	}
	
	/**
	* resource_md5  String(128)  //资源包md5    
	*/
	public String getResourceMd5(){
		return resourceMd5;
	}
	
	/**
	* resource_md5  String(128)  //资源包md5    
	*/
	public void setResourceMd5(String resourceMd5){
		this.resourceMd5 = resourceMd5;
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
	
}