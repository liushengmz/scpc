package com.pay.business.merchant.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
TABLE:.payv2_buss_company_app   
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
company_id           Long(19)                               //商户ID
app_name             String(255)                            //app名称
app_type_id          Integer(10)                            //应用类别id，关联payv2_app_type
app_icon             String(255)                            //应用图标url
app_img              String(255)                            //应用截图url
app_desc             String(1000)                           //应用说明
app_desc_file        String(255)                            //应用说明文档url
app_copyright        String(255)                            //著作权验证url
is_android           Integer(10)                 1          //是否支持Android,1是,2否
android_apk_url      String(255)                            //Android包下载地址
android_apk_md5      String(64)                             //Android包的md5值
android_apk_package  String(100)                            //Android包名,备用
android_apk_logo     String(255)                            //Android应用的logo地址,备用
is_ios               Integer(10)                 1          //是否支持ios,1是,2否
ios_ipa_url          String(255)                            //ios包下载地址
ios_ipa_md5          String(64)                             //ios包md5值
is_web               Integer(10)                 2          //是否支持网页1.是2.否
web_url              String(255)                            //网页商品（url链接）
app_key              String(64)                             //应用的key
app_secret           String(64)                             //app密钥
app_status           Integer(10)                 1          //当前状态,1未审核,2通过,3未通过,4终止合作,5已生成SDK
app_pass_reason      String(1000)                           //app未通过原因
callback_url         String(255)                            //回调地址
is_delete            Integer(10)                 2          //是否删除,1是,2否
create_time          Date(19)                               //
update_time          Date(19)                               //
*/
public class Payv2BussCompanyApp implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long companyId;
	private	String appName;
	private	Long appTypeId;
	private	String appIcon;
	private	String appImg;
	private	String appDesc;
	private	String appDescFile;
	private	String appCopyright;
	private	Integer isAndroid;
	private	String androidAppUrl;
	private	String androidAppMd5;
	private	String androidAppPackage;
	private	String androidApkUrl;
	private	String androidApkMd5;
	private	String androidApkPackage;
	private	String androidApkLogo;
	private	Integer isIos;
	private	String iosIpaUrl;
	private	String iosIpaMd5;
	private	String iosIpaTestId;
	private	String iosIphoneUrl;
	private	String iosIphoneId;
	private	String iosIphoneTestId;
	private	Integer isWeb;
	private	String webUrl;
	private	String appKey;
	private	String appSecret;
	private	Integer appStatus;
	private	String appPassReason;
	private	String callbackUrl;
	private	Integer isNew;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;

	private String companyName;//所属商户
	private Long channelId;//渠道商ID
	private	Integer appSupportPayWayNumber;//支付方式个数
	
	private String typeName;//应用类型
	private List<Long> appIds;//id字符串
	private Integer isAddPlatform;//是否已经添加到代理平台 状态1未添加2.已添加3.已取消

	private String wayIcon;
	private	String wayName;
	
	List<Payv2BussSupportPayWay> supportList;
	private Integer isRandom;//是否通道随机路由1是2否（顺序）
	
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
	* company_id  Long(19)  //商户ID    
	*/
	public Long getCompanyId(){
		return companyId;
	}
	
	/**
	* company_id  Long(19)  //商户ID    
	*/
	public void setCompanyId(Long companyId){
		this.companyId = companyId;
	}
	
	/**
	* app_name  String(255)  //app名称    
	*/
	public String getAppName(){
		return appName;
	}
	
	/**
	* app_name  String(255)  //app名称    
	*/
	public void setAppName(String appName){
		this.appName = appName;
	}
	
	
	
	public Long getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(Long appTypeId) {
		this.appTypeId = appTypeId;
	}

	/**
	* app_icon  String(255)  //应用图标url    
	*/
	public String getAppIcon(){
		return appIcon;
	}
	
	/**
	* app_icon  String(255)  //应用图标url    
	*/
	public void setAppIcon(String appIcon){
		this.appIcon = appIcon;
	}
	
	/**
	* app_img  String(255)  //应用截图url    
	*/
	public String getAppImg(){
		return appImg;
	}
	
	/**
	* app_img  String(255)  //应用截图url    
	*/
	public void setAppImg(String appImg){
		this.appImg = appImg;
	}
	
	/**
	* app_desc  String(1000)  //应用说明    
	*/
	public String getAppDesc(){
		return appDesc;
	}
	
	/**
	* app_desc  String(1000)  //应用说明    
	*/
	public void setAppDesc(String appDesc){
		this.appDesc = appDesc;
	}
	
	/**
	* app_desc_file  String(255)  //应用说明文档url    
	*/
	public String getAppDescFile(){
		return appDescFile;
	}
	
	/**
	* app_desc_file  String(255)  //应用说明文档url    
	*/
	public void setAppDescFile(String appDescFile){
		this.appDescFile = appDescFile;
	}
	
	/**
	* app_copyright  String(255)  //著作权验证url    
	*/
	public String getAppCopyright(){
		return appCopyright;
	}
	
	/**
	* app_copyright  String(255)  //著作权验证url    
	*/
	public void setAppCopyright(String appCopyright){
		this.appCopyright = appCopyright;
	}
	
	/**
	* is_android  Integer(10)  1  //是否支持Android,1是,2否    
	*/
	public Integer getIsAndroid(){
		return isAndroid;
	}
	
	/**
	* is_android  Integer(10)  1  //是否支持Android,1是,2否    
	*/
	public void setIsAndroid(Integer isAndroid){
		this.isAndroid = isAndroid;
	}
	
	/**
	* android_apk_url  String(255)  //Android包下载地址    
	*/
	public String getAndroidApkUrl(){
		return androidApkUrl;
	}
	
	/**
	* android_apk_url  String(255)  //Android包下载地址    
	*/
	public void setAndroidApkUrl(String androidApkUrl){
		this.androidApkUrl = androidApkUrl;
	}
	
	/**
	* android_apk_md5  String(64)  //Android包的md5值    
	*/
	public String getAndroidApkMd5(){
		return androidApkMd5;
	}
	
	/**
	* android_apk_md5  String(64)  //Android包的md5值    
	*/
	public void setAndroidApkMd5(String androidApkMd5){
		this.androidApkMd5 = androidApkMd5;
	}
	
	/**
	* android_apk_package  String(100)  //Android包名,备用    
	*/
	public String getAndroidApkPackage(){
		return androidApkPackage;
	}
	
	/**
	* android_apk_package  String(100)  //Android包名,备用    
	*/
	public void setAndroidApkPackage(String androidApkPackage){
		this.androidApkPackage = androidApkPackage;
	}
	
	/**
	* android_apk_logo  String(255)  //Android应用的logo地址,备用    
	*/
	public String getAndroidApkLogo(){
		return androidApkLogo;
	}
	
	/**
	* android_apk_logo  String(255)  //Android应用的logo地址,备用    
	*/
	public void setAndroidApkLogo(String androidApkLogo){
		this.androidApkLogo = androidApkLogo;
	}
	
	/**
	* is_ios  Integer(10)  1  //是否支持ios,1是,2否    
	*/
	public Integer getIsIos(){
		return isIos;
	}
	
	/**
	* is_ios  Integer(10)  1  //是否支持ios,1是,2否    
	*/
	public void setIsIos(Integer isIos){
		this.isIos = isIos;
	}
	
	/**
	* ios_ipa_url  String(255)  //ios包下载地址    
	*/
	public String getIosIpaUrl(){
		return iosIpaUrl;
	}
	
	/**
	* ios_ipa_url  String(255)  //ios包下载地址    
	*/
	public void setIosIpaUrl(String iosIpaUrl){
		this.iosIpaUrl = iosIpaUrl;
	}
	
	/**
	* ios_ipa_md5  String(64)  //ios包md5值    
	*/
	public String getIosIpaMd5(){
		return iosIpaMd5;
	}
	
	/**
	* ios_ipa_md5  String(64)  //ios包md5值    
	*/
	public void setIosIpaMd5(String iosIpaMd5){
		this.iosIpaMd5 = iosIpaMd5;
	}
	
	/**
	* is_web  Integer(10)  2  //是否支持网页1.是2.否    
	*/
	public Integer getIsWeb(){
		return isWeb;
	}
	
	/**
	* is_web  Integer(10)  2  //是否支持网页1.是2.否    
	*/
	public void setIsWeb(Integer isWeb){
		this.isWeb = isWeb;
	}
	
	/**
	* web_url  String(255)  //网页商品（url链接）    
	*/
	public String getWebUrl(){
		return webUrl;
	}
	
	/**
	* web_url  String(255)  //网页商品（url链接）    
	*/
	public void setWebUrl(String webUrl){
		this.webUrl = webUrl;
	}
	
	/**
	* app_key  String(64)  //应用的key    
	*/
	public String getAppKey(){
		return appKey;
	}
	
	/**
	* app_key  String(64)  //应用的key    
	*/
	public void setAppKey(String appKey){
		this.appKey = appKey;
	}
	
	/**
	* app_secret  String(64)  //app密钥    
	*/
	public String getAppSecret(){
		return appSecret;
	}
	
	/**
	* app_secret  String(64)  //app密钥    
	*/
	public void setAppSecret(String appSecret){
		this.appSecret = appSecret;
	}
	
	/**
	* app_status  Integer(10)  1  //当前状态,1未审核,2通过,3未通过,4终止合作,5已生成SDK    
	*/
	public Integer getAppStatus(){
		return appStatus;
	}
	
	/**
	* app_status  Integer(10)  1  //当前状态,1未审核,2通过,3未通过,4终止合作,5已生成SDK    
	*/
	public void setAppStatus(Integer appStatus){
		this.appStatus = appStatus;
	}
	
	/**
	* app_pass_reason  String(1000)  //app未通过原因    
	*/
	public String getAppPassReason(){
		return appPassReason;
	}
	
	/**
	* app_pass_reason  String(1000)  //app未通过原因    
	*/
	public void setAppPassReason(String appPassReason){
		this.appPassReason = appPassReason;
	}
	
	/**
	* callback_url  String(255)  //回调地址    
	*/
	public String getCallbackUrl(){
		return callbackUrl;
	}
	
	/**
	* callback_url  String(255)  //回调地址    
	*/
	public void setCallbackUrl(String callbackUrl){
		this.callbackUrl = callbackUrl;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除,1是,2否    
	*/
	public Integer getIsDelete(){
		return isDelete;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除,1是,2否    
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getAppSupportPayWayNumber() {
		return appSupportPayWayNumber;
	}

	public void setAppSupportPayWayNumber(Integer appSupportPayWayNumber) {
		this.appSupportPayWayNumber = appSupportPayWayNumber;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<Long> getAppIds() {
		return appIds;
	}

	public void setAppIds(List<Long> appIds) {
		this.appIds = appIds;
	}

	public Integer getIsAddPlatform() {
		return isAddPlatform;
	}

	public void setIsAddPlatform(Integer isAddPlatform) {
		this.isAddPlatform = isAddPlatform;
	}

	public String getAndroidAppUrl() {
		return androidAppUrl;
	}

	public void setAndroidAppUrl(String androidAppUrl) {
		this.androidAppUrl = androidAppUrl;
	}

	public String getAndroidAppMd5() {
		return androidAppMd5;
	}

	public void setAndroidAppMd5(String androidAppMd5) {
		this.androidAppMd5 = androidAppMd5;
	}

	public String getAndroidAppPackage() {
		return androidAppPackage;
	}

	public void setAndroidAppPackage(String androidAppPackage) {
		this.androidAppPackage = androidAppPackage;
	}

	public String getIosIpaTestId() {
		return iosIpaTestId;
	}

	public void setIosIpaTestId(String iosIpaTestId) {
		this.iosIpaTestId = iosIpaTestId;
	}

	public String getIosIphoneUrl() {
		return iosIphoneUrl;
	}

	public void setIosIphoneUrl(String iosIphoneUrl) {
		this.iosIphoneUrl = iosIphoneUrl;
	}

	public String getIosIphoneId() {
		return iosIphoneId;
	}

	public void setIosIphoneId(String iosIphoneId) {
		this.iosIphoneId = iosIphoneId;
	}

	public String getIosIphoneTestId() {
		return iosIphoneTestId;
	}

	public void setIosIphoneTestId(String iosIphoneTestId) {
		this.iosIphoneTestId = iosIphoneTestId;
	}

	public List<Payv2BussSupportPayWay> getSupportList() {
		return supportList;
	}

	public void setSupportList(List<Payv2BussSupportPayWay> supportList) {
		this.supportList = supportList;
	}

	public String getWayIcon() {
		return wayIcon;
	}

	public void setWayIcon(String wayIcon) {
		this.wayIcon = wayIcon;
	}

	public String getWayName() {
		return wayName;
	}

	public void setWayName(String wayName) {
		this.wayName = wayName;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getIsRandom() {
		return isRandom;
	}

	public void setIsRandom(Integer isRandom) {
		this.isRandom = isRandom;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

}