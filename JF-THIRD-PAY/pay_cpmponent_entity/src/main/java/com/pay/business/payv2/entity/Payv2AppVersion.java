package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_app_version        
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //应用id，关联payv2_buss_company_app
app_type             Integer(10)                 1          //平台类型（1.安卓2.ios 3.网页）
app_file_url         String(255)                            //app链接或网页地址
app_store_url        String(255)                            //appstore地址
app_package_name     String(50)                             //包名
file_md5             String(255)                            //应用版本md5值
file_size            Integer(10)                            //文件大小
app_version          String(20)                             //版本号
app_version_code     Integer(10)                            //子版本号
update_explain       String(65535)                          //更新说明
pass_reason          String(1000)                           //不通过原因
is_delete            Integer(10)                 2          //是否删除（1是，2否）
status               Integer(10)                            //状态（1.待提交2.审核中3.使用中4已下线5.审核不通过）
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Payv2AppVersion implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long appId;
	private	Integer appType;
	private	String appFileUrl;
	private	String appStoreUrl;
	private	String appPackageName;
	private	String fileMd5;
	private	Integer fileSize;
	private	String appVersion;
	private	Integer appVersionCode;
	private	String updateExplain;
	private	String passReason;
	private	Integer isDelete;
	private	Integer status;
	private	Date createTime;
	private	Date updateTime;
	
	private	String appName;
	
	private String currentVersion;

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
	* app_type  Integer(10)  1  //平台类型（1.安卓2.ios  3.网页）    
	*/
	public Integer getAppType(){
		return appType;
	}
	
	/**
	* app_type  Integer(10)  1  //平台类型（1.安卓2.ios  3.网页）    
	*/
	public void setAppType(Integer appType){
		this.appType = appType;
	}
	
	/**
	* app_file_url  String(255)  //app链接或网页地址    
	*/
	public String getAppFileUrl(){
		return appFileUrl;
	}
	
	/**
	* app_file_url  String(255)  //app链接或网页地址    
	*/
	public void setAppFileUrl(String appFileUrl){
		this.appFileUrl = appFileUrl;
	}
	
	/**
	* app_store_url  String(255)  //appstore地址    
	*/
	public String getAppStoreUrl(){
		return appStoreUrl;
	}
	
	/**
	* app_store_url  String(255)  //appstore地址    
	*/
	public void setAppStoreUrl(String appStoreUrl){
		this.appStoreUrl = appStoreUrl;
	}
	
	/**
	* app_package_name  String(50)  //包名    
	*/
	public String getAppPackageName(){
		return appPackageName;
	}
	
	/**
	* app_package_name  String(50)  //包名    
	*/
	public void setAppPackageName(String appPackageName){
		this.appPackageName = appPackageName;
	}
	
	/**
	* file_md5  String(255)  //应用版本md5值    
	*/
	public String getFileMd5(){
		return fileMd5;
	}
	
	/**
	* file_md5  String(255)  //应用版本md5值    
	*/
	public void setFileMd5(String fileMd5){
		this.fileMd5 = fileMd5;
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
	* app_version  String(20)  //版本号    
	*/
	public String getAppVersion(){
		return appVersion;
	}
	
	/**
	* app_version  String(20)  //版本号    
	*/
	public void setAppVersion(String appVersion){
		this.appVersion = appVersion;
	}
	
	/**
	* app_version_code  Integer(10)  //子版本号    
	*/
	public Integer getAppVersionCode(){
		return appVersionCode;
	}
	
	/**
	* app_version_code  Integer(10)  //子版本号    
	*/
	public void setAppVersionCode(Integer appVersionCode){
		this.appVersionCode = appVersionCode;
	}
	
	/**
	* update_explain  String(65535)  //更新说明    
	*/
	public String getUpdateExplain(){
		return updateExplain;
	}
	
	/**
	* update_explain  String(65535)  //更新说明    
	*/
	public void setUpdateExplain(String updateExplain){
		this.updateExplain = updateExplain;
	}
	
	/**
	* pass_reason  String(1000)  //不通过原因    
	*/
	public String getPassReason(){
		return passReason;
	}
	
	/**
	* pass_reason  String(1000)  //不通过原因    
	*/
	public void setPassReason(String passReason){
		this.passReason = passReason;
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
	* status  Integer(10)  //状态（1.待提交2.审核中3.使用中4已下线5.审核不通过）    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  //状态（1.待提交2.审核中3.使用中4已下线5.审核不通过）    
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

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
	
}