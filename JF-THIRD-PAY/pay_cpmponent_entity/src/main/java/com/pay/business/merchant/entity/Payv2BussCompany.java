package com.pay.business.merchant.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_buss_company       
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
channel_id           Long(19)                               //渠道商ID
user_name            String(64)                             //商户账号
pass_word            String(64)                             //商户登陆密码
pay_pass_word        String(64)                             //支付密码
company_key          String(64)                             //商户key,由系统生成
company_secret       String(64)                             //商户密钥,系统生成,备用
company_name         String(50)                             //合作商户名称
company_photo        String(255)                            //公司头像
company_scale        Integer(10)                 1          //公司规模1.大型2.中型3.小型4.微型5.个体户
company_type         Integer(10)                 1          //公司类型,预留,先默认1
company_trade        Integer(10)                            //商户行业,对应行业表的ID
company_trade_var    String(100)                            //行业字符串
company_range_province Long(19)                               //营业范围省
company_range_city   Long(19)                               //营业范围市
company_addr         String(255)                            //公司所在区域地址
license_num          String(100)                            //营业执照号码
license_addr         String(100)                            //营业执照注册地址(省,市,区,街道用小写逗号分割)
license_pic          String(255)                            //营业执照图片
organization_code    String(64)                             //组织机构代码
organization_code_url String(255)                            //组织机构代码照
other_information    String(255)                            //其他资料
legal_name           String(20)                             //公司法人名称
legal_id_card        String(18)                             //法人身份证号码
account_type         Integer(10)                 1          //收款账号类型
account_bank         String(50)                             //开户银行
account_name         String(50)                             //收款商户名（开户姓名）
account_card         String(50)                             //收款账号（卡号）
way_arrival_type     Integer(10)                 1          //到账类型1.T+日期（工作日）2.实时到账3.T+日期
way_arrival_value    Integer(10)                 0          //到账时间
wechat_account_card  String(50)                             //微信账号
alipay_account_card  String(50)                             //支付宝收款账号
license_type         Integer(10)                 1          //营业类型1.线上（互联网）2.线下（实体店铺）3.线上+线下
contacts_name        String(50)                             //联系人姓名
contacts_phone       String(20)                             //联系人电话
contacts_mail        String(50)                             //联系人邮件
company_status       Integer(10)                 1          //商户状态,1未审核,2通过(合作中),3未通过,4终止合作
company_pass_reason  String(1000)                           //审核未通过原因
is_random            Integer(10)                 1          //支付通道是否随机1是2否（按顺序）
is_delete            Integer(10)                 2          //是否删除,1是,2否
create_time          Date(19)                               //
update_time          Date(19)                               //
*/
public class Payv2BussCompany implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long channelId;
	private	String userName;
	private	String passWord;
	private	String payPassWord;
	private	String companyKey;
	private	String companySecret;
	private	String companyName;
	private	String companyPhoto;
	private	Integer companyScale;
	private	Integer companyType;
	private	Integer companyTrade;
	private	String companyTradeVar;
	private	Long companyRangeProvince;
	private	Long companyRangeCity;
	private	String companyAddr;
	private	String licenseNum;
	private	String licenseAddr;
	private	String licensePic;
	private	String organizationCode;
	private	String organizationCodeUrl;
	private	String otherInformation;
	private	String legalName;
	private	String legalIdCard;
	private	Integer accountType;
	private	String accountBank;
	private	String accountName;
	private	String accountCard;
	private	Integer wayArrivalType;
	private	Integer wayArrivalValue;
	private	String wechatAccountCard;
	private	String alipayAccountCard;
	private	Integer licenseType;
	private	String contactsName;
	private	String contactsPhone;
	private	String contactsMail;
	private	Integer companyStatus;
	private	String companyPassReason;
	private	Integer isRandom;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;
	
	
	private String bussRange;  //营业范围
	private String channelName;//渠道商名称
	
	private Integer supportPayWayNum;//支付通道个数
	
	private Integer currentUserStatus;//当前用户登录状态(商户处于线上应用还是线下)
	
	private	String companyRangeProvinceName;
	private	String companyRangeCityName;
	
	public Integer getSupportPayWayNum() {
		return supportPayWayNum;
	}

	public void setSupportPayWayNum(Integer supportPayWayNum) {
		this.supportPayWayNum = supportPayWayNum;
	}

	public String getBussRange() {
		return bussRange;
	}

	public void setBussRange(String bussRange) {
		this.bussRange = bussRange;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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
	* channel_id  Long(19)  //渠道商ID    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道商ID    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
	}
	
	/**
	* company_key  String(64)  //商户key,由系统生成    
	*/
	public String getCompanyKey(){
		return companyKey;
	}
	
	/**
	* company_key  String(64)  //商户key,由系统生成    
	*/
	public void setCompanyKey(String companyKey){
		this.companyKey = companyKey;
	}
	
	/**
	* company_secret  String(64)  //商户密钥,系统生成,备用    
	*/
	public String getCompanySecret(){
		return companySecret;
	}
	
	/**
	* company_secret  String(64)  //商户密钥,系统生成,备用    
	*/
	public void setCompanySecret(String companySecret){
		this.companySecret = companySecret;
	}
	
	/**
	* company_name  String(50)  //合作商户名称    
	*/
	public String getCompanyName(){
		return companyName;
	}
	
	/**
	* company_name  String(50)  //合作商户名称    
	*/
	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}
	
	/**
	* company_type  Integer(10)  1  //公司类型,预留,先默认1    
	*/
	public Integer getCompanyType(){
		return companyType;
	}
	
	/**
	* company_type  Integer(10)  1  //公司类型,预留,先默认1    
	*/
	public void setCompanyType(Integer companyType){
		this.companyType = companyType;
	}
	
	/**
	* company_trade  Integer(10)  //商户行业,对应行业表的ID    
	*/
	public Integer getCompanyTrade(){
		return companyTrade;
	}
	
	/**
	* company_trade  Integer(10)  //商户行业,对应行业表的ID    
	*/
	public void setCompanyTrade(Integer companyTrade){
		this.companyTrade = companyTrade;
	}
	
	/**
	* company_range_province  Long(19)  //营业范围省    
	*/
	public Long getCompanyRangeProvince(){
		return companyRangeProvince;
	}
	
	/**
	* company_range_province  Long(19)  //营业范围省    
	*/
	public void setCompanyRangeProvince(Long companyRangeProvince){
		this.companyRangeProvince = companyRangeProvince;
	}
	
	/**
	* company_range_city  Long(19)  //营业范围市    
	*/
	public Long getCompanyRangeCity(){
		return companyRangeCity;
	}
	
	/**
	* company_range_city  Long(19)  //营业范围市    
	*/
	public void setCompanyRangeCity(Long companyRangeCity){
		this.companyRangeCity = companyRangeCity;
	}
	
	/**
	* license_num  String(100)  //营业执照号码    
	*/
	public String getLicenseNum(){
		return licenseNum;
	}
	
	/**
	* license_num  String(100)  //营业执照号码    
	*/
	public void setLicenseNum(String licenseNum){
		this.licenseNum = licenseNum;
	}
	
	/**
	* license_addr  String(100)  //营业执照注册地址(省,市,区,街道用小写逗号分割)    
	*/
	public String getLicenseAddr(){
		return licenseAddr;
	}
	
	/**
	* license_addr  String(100)  //营业执照注册地址(省,市,区,街道用小写逗号分割)    
	*/
	public void setLicenseAddr(String licenseAddr){
		this.licenseAddr = licenseAddr;
	}
	
	/**
	* license_pic  String(255)  //营业执照图片    
	*/
	public String getLicensePic(){
		return licensePic;
	}
	
	/**
	* license_pic  String(255)  //营业执照图片    
	*/
	public void setLicensePic(String licensePic){
		this.licensePic = licensePic;
	}
	
	/**
	* contacts_name  String(50)  //联系人姓名    
	*/
	public String getContactsName(){
		return contactsName;
	}
	
	/**
	* contacts_name  String(50)  //联系人姓名    
	*/
	public void setContactsName(String contactsName){
		this.contactsName = contactsName;
	}
	
	/**
	* contacts_phone  String(12)  //联系人电话    
	*/
	public String getContactsPhone(){
		return contactsPhone;
	}
	
	/**
	* contacts_phone  String(12)  //联系人电话    
	*/
	public void setContactsPhone(String contactsPhone){
		this.contactsPhone = contactsPhone;
	}
	
	/**
	* company_status  Integer(10)  1  //商户状态,1未审核,2通过(合作中),3未通过,4终止合作    
	*/
	public Integer getCompanyStatus(){
		return companyStatus;
	}
	
	/**
	* company_status  Integer(10)  1  //商户状态,1未审核,2通过(合作中),3未通过,4终止合作    
	*/
	public void setCompanyStatus(Integer companyStatus){
		this.companyStatus = companyStatus;
	}
	
	/**
	* company_pass_reason  String(1000)  //审核未通过原因    
	*/
	public String getCompanyPassReason(){
		return companyPassReason;
	}
	
	/**
	* company_pass_reason  String(1000)  //审核未通过原因    
	*/
	public void setCompanyPassReason(String companyPassReason){
		this.companyPassReason = companyPassReason;
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

	public Integer getCompanyScale() {
		return companyScale;
	}

	public void setCompanyScale(Integer companyScale) {
		this.companyScale = companyScale;
	}

	public Integer getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(Integer licenseType) {
		this.licenseType = licenseType;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationCodeUrl() {
		return organizationCodeUrl;
	}

	public void setOrganizationCodeUrl(String organizationCodeUrl) {
		this.organizationCodeUrl = organizationCodeUrl;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getLegalIdCard() {
		return legalIdCard;
	}

	public void setLegalIdCard(String legalIdCard) {
		this.legalIdCard = legalIdCard;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountCard() {
		return accountCard;
	}

	public void setAccountCard(String accountCard) {
		this.accountCard = accountCard;
	}

	public String getOtherInformation() {
		return otherInformation;
	}

	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}

	public String getAlipayAccountCard() {
		return alipayAccountCard;
	}

	public void setAlipayAccountCard(String alipayAccountCard) {
		this.alipayAccountCard = alipayAccountCard;
	}

	public String getWechatAccountCard() {
		return wechatAccountCard;
	}

	public void setWechatAccountCard(String wechatAccountCard) {
		this.wechatAccountCard = wechatAccountCard;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getCompanyTradeVar() {
		return companyTradeVar;
	}

	public void setCompanyTradeVar(String companyTradeVar) {
		this.companyTradeVar = companyTradeVar;
	}

	public Integer getCurrentUserStatus() {
		return currentUserStatus;
	}

	public void setCurrentUserStatus(Integer currentUserStatus) {
		this.currentUserStatus = currentUserStatus;
	}

	public String getCompanyRangeProvinceName() {
		return companyRangeProvinceName;
	}

	public void setCompanyRangeProvinceName(String companyRangeProvinceName) {
		this.companyRangeProvinceName = companyRangeProvinceName;
	}

	public String getCompanyRangeCityName() {
		return companyRangeCityName;
	}

	public void setCompanyRangeCityName(String companyRangeCityName) {
		this.companyRangeCityName = companyRangeCityName;
	}

	public String getPayPassWord() {
		return payPassWord;
	}

	public void setPayPassWord(String payPassWord) {
		this.payPassWord = payPassWord;
	}

	public String getCompanyPhoto() {
		return companyPhoto;
	}

	public void setCompanyPhoto(String companyPhoto) {
		this.companyPhoto = companyPhoto;
	}

	public Integer getWayArrivalType() {
		return wayArrivalType;
	}

	public void setWayArrivalType(Integer wayArrivalType) {
		this.wayArrivalType = wayArrivalType;
	}

	public Integer getWayArrivalValue() {
		return wayArrivalValue;
	}

	public void setWayArrivalValue(Integer wayArrivalValue) {
		this.wayArrivalValue = wayArrivalValue;
	}

	public Integer getIsRandom() {
		return isRandom;
	}

	public void setIsRandom(Integer isRandom) {
		this.isRandom = isRandom;
	}

	public String getContactsMail() {
		return contactsMail;
	}

	public void setContactsMail(String contactsMail) {
		this.contactsMail = contactsMail;
	}
	
}