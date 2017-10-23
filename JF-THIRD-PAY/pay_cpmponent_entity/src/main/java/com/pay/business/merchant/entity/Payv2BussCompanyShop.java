package com.pay.business.merchant.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
TABLE:.payv2_buss_company_shop  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
shop_key             String(64)                             //店铺key（唯一）
company_id           Long(19)                               //所属公司
shop_name            String(50)                             //店铺名称
shop_card            String(50)                             //门店号
shop_range_province  Long(19)                               //店铺地址，省，关联payv2_provices_city表
shop_range_city      Long(19)                               //店铺地址，市，关联payv2_provices_city表
lat                  Double(10,7)                           //纬度
lon                  Double(10,7)                           //经度
shop_address         String(100)                            //店铺详细地址
shop_icon            String(255)                            //店铺小图标url
shop_background      String(255)                            //店铺背景图片url
shop_desc            String(1000)                           //店铺详情介绍
shop_week_time       String(200)                            //店铺营业时间（周），如（星期一，星期二）
shop_day_start_time  String(20)                             //店铺营业开始时间段（日），如（09:00:00）
shop_day_end_time    String(20)                             //店铺营业结束时间段（日），如（22:00:00）
shop_remarks         String(100)                            //营业备注
shop_per_ave_money   Double(9,2)                            //
shop_contacts        String(50)                             //店铺联系人
shop_contacts_phone  String(20)                             //联系人电话
shop_email           String(50)                             //邮箱
pay_way_id           Long(19)                               //钱包id，关联payv2_pay_way表
code_way_id          String(50)                             //二维码支付通道，逗号隔开如：1,14,13,
shop_status          Integer(10)                 1          //当前状态,1未审核,2通过,3未通过,4终止合作
shop_pass_reason     String(1000)                           //未通过原因
shop_two_code_url    String(255)                            //二维码地址
is_delete            Integer(10)                 2          //是否删除1.是2.否
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Payv2BussCompanyShop implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String shopKey;
	private	Long companyId;
	private	String shopName;
	private	String shopCard;
	private	Long shopRangeProvince;
	private	Long shopRangeCity;
	private	Double lat;
	private	Double lon;
	private	String shopAddress;
	private	String shopIcon;
	private	String shopBackground;
	private	String shopDesc;
	private	String shopWeekTime;
	private	String shopDayStartTime;
	private	String shopDayEndTime;
	private	String shopRemarks;
	private	Double shopPerAveMoney;
	private	String shopContacts;
	private	String shopContactsPhone;
	private	String shopEmail;
	private	Long payWayId;
	private String payWayName;
	private	String codeWayId;
	private	Integer shopStatus;
	private	String shopPassReason;
	private	String shopTwoCodeUrl;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;

	private String companyName;//所属商户
	private String channelId;//渠道商ID
	private	Integer appSupportPayWayNumber;//支付方式个数
	
	//支付通道名字
	private String wayName;
	
	private	List<Long> payWayIds;
	
	private String shopWeekTimeStart;//店铺开始营业时间
	private String shopWeekTimeEnd;//店铺结束营业时间
	
	private JSONArray inlineObj;//
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
	* shop_key  String(64)  //店铺key（唯一）    
	*/
	public String getShopKey(){
		return shopKey;
	}
	
	/**
	* shop_key  String(64)  //店铺key（唯一）    
	*/
	public void setShopKey(String shopKey){
		this.shopKey = shopKey;
	}
	
	/**
	* company_id  Long(19)  //所属公司    
	*/
	public Long getCompanyId(){
		return companyId;
	}
	
	/**
	* company_id  Long(19)  //所属公司    
	*/
	public void setCompanyId(Long companyId){
		this.companyId = companyId;
	}
	
	/**
	* shop_name  String(50)  //店铺名称    
	*/
	public String getShopName(){
		return shopName;
	}
	
	/**
	* shop_name  String(50)  //店铺名称    
	*/
	public void setShopName(String shopName){
		this.shopName = shopName;
	}
	
	/**
	* shop_range_province  Long(19)  //店铺地址，省，关联payv2_provices_city表    
	*/
	public Long getShopRangeProvince(){
		return shopRangeProvince;
	}
	
	/**
	* shop_range_province  Long(19)  //店铺地址，省，关联payv2_provices_city表    
	*/
	public void setShopRangeProvince(Long shopRangeProvince){
		this.shopRangeProvince = shopRangeProvince;
	}
	
	/**
	* shop_range_city  Long(19)  //店铺地址，市，关联payv2_provices_city表    
	*/
	public Long getShopRangeCity(){
		return shopRangeCity;
	}
	
	/**
	* shop_range_city  Long(19)  //店铺地址，市，关联payv2_provices_city表    
	*/
	public void setShopRangeCity(Long shopRangeCity){
		this.shopRangeCity = shopRangeCity;
	}
	
	/**
	* lat  Double(10,7)  //纬度    
	*/
	public Double getLat(){
		return lat;
	}
	
	/**
	* lat  Double(10,7)  //纬度    
	*/
	public void setLat(Double lat){
		this.lat = lat;
	}
	
	/**
	* lon  Double(10,7)  //经度    
	*/
	public Double getLon(){
		return lon;
	}
	
	/**
	* lon  Double(10,7)  //经度    
	*/
	public void setLon(Double lon){
		this.lon = lon;
	}
	
	/**
	* shop_address  String(100)  //店铺详细地址    
	*/
	public String getShopAddress(){
		return shopAddress;
	}
	
	/**
	* shop_address  String(100)  //店铺详细地址    
	*/
	public void setShopAddress(String shopAddress){
		this.shopAddress = shopAddress;
	}
	
	/**
	* shop_icon  String(255)  //店铺小图标url    
	*/
	public String getShopIcon(){
		return shopIcon;
	}
	
	/**
	* shop_icon  String(255)  //店铺小图标url    
	*/
	public void setShopIcon(String shopIcon){
		this.shopIcon = shopIcon;
	}
	
	/**
	* shop_background  String(255)  //店铺背景图片url    
	*/
	public String getShopBackground(){
		return shopBackground;
	}
	
	/**
	* shop_background  String(255)  //店铺背景图片url    
	*/
	public void setShopBackground(String shopBackground){
		this.shopBackground = shopBackground;
	}
	
	/**
	* shop_desc  String(1000)  //店铺详情介绍    
	*/
	public String getShopDesc(){
		return shopDesc;
	}
	
	/**
	* shop_desc  String(1000)  //店铺详情介绍    
	*/
	public void setShopDesc(String shopDesc){
		this.shopDesc = shopDesc;
	}
	
	/**
	* shop_week_time  String(200)  //店铺营业时间（周），如（星期一，星期二）    
	*/
	public String getShopWeekTime(){
		return shopWeekTime;
	}
	
	/**
	* shop_week_time  String(200)  //店铺营业时间（周），如（星期一，星期二）    
	*/
	public void setShopWeekTime(String shopWeekTime){
		this.shopWeekTime = shopWeekTime;
	}
	
	/**
	* shop_day_start_time  String(20)  //店铺营业开始时间段（日），如（09:00:00）    
	*/
	public String getShopDayStartTime(){
		return shopDayStartTime;
	}
	
	/**
	* shop_day_start_time  String(20)  //店铺营业开始时间段（日），如（09:00:00）    
	*/
	public void setShopDayStartTime(String shopDayStartTime){
		this.shopDayStartTime = shopDayStartTime;
	}
	
	/**
	* shop_day_end_time  String(20)  //店铺营业结束时间段（日），如（22:00:00）    
	*/
	public String getShopDayEndTime(){
		return shopDayEndTime;
	}
	
	/**
	* shop_day_end_time  String(20)  //店铺营业结束时间段（日），如（22:00:00）    
	*/
	public void setShopDayEndTime(String shopDayEndTime){
		this.shopDayEndTime = shopDayEndTime;
	}
	
	/**
	* shop_per_ave_money  Double(9,2)  //    
	*/
	public Double getShopPerAveMoney(){
		return shopPerAveMoney;
	}
	
	/**
	* shop_per_ave_money  Double(9,2)  //    
	*/
	public void setShopPerAveMoney(Double shopPerAveMoney){
		this.shopPerAveMoney = shopPerAveMoney;
	}
	
	/**
	* shop_contacts  String(50)  //店铺联系人    
	*/
	public String getShopContacts(){
		return shopContacts;
	}
	
	/**
	* shop_contacts  String(50)  //店铺联系人    
	*/
	public void setShopContacts(String shopContacts){
		this.shopContacts = shopContacts;
	}
	
	/**
	* shop_contacts_phone  String(20)  //联系人电话    
	*/
	public String getShopContactsPhone(){
		return shopContactsPhone;
	}
	
	/**
	* shop_contacts_phone  String(20)  //联系人电话    
	*/
	public void setShopContactsPhone(String shopContactsPhone){
		this.shopContactsPhone = shopContactsPhone;
	}
	
	/**
	* pay_way_id  Long(19)  //钱包id，关联payv2_pay_way表    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //钱包id，关联payv2_pay_way表    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* code_way_id  String(50)  //二维码支付通道，逗号隔开如：1,14,13,    
	*/
	public String getCodeWayId(){
		return codeWayId;
	}
	
	/**
	* code_way_id  String(50)  //二维码支付通道，逗号隔开如：1,14,13,    
	*/
	public void setCodeWayId(String codeWayId){
		this.codeWayId = codeWayId;
	}
	
	/**
	* shop_status  Integer(10)  1  //当前状态,1未审核,2通过,3未通过,4终止合作    
	*/
	public Integer getShopStatus(){
		return shopStatus;
	}
	
	/**
	* shop_status  Integer(10)  1  //当前状态,1未审核,2通过,3未通过,4终止合作    
	*/
	public void setShopStatus(Integer shopStatus){
		this.shopStatus = shopStatus;
	}
	
	/**
	* shop_pass_reason  String(1000)  //未通过原因    
	*/
	public String getShopPassReason(){
		return shopPassReason;
	}
	
	/**
	* shop_pass_reason  String(1000)  //未通过原因    
	*/
	public void setShopPassReason(String shopPassReason){
		this.shopPassReason = shopPassReason;
	}
	
	/**
	* shop_two_code_url  String(255)  //二维码地址    
	*/
	public String getShopTwoCodeUrl(){
		return shopTwoCodeUrl;
	}
	
	/**
	* shop_two_code_url  String(255)  //二维码地址    
	*/
	public void setShopTwoCodeUrl(String shopTwoCodeUrl){
		this.shopTwoCodeUrl = shopTwoCodeUrl;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getAppSupportPayWayNumber() {
		return appSupportPayWayNumber;
	}

	public void setAppSupportPayWayNumber(Integer appSupportPayWayNumber) {
		this.appSupportPayWayNumber = appSupportPayWayNumber;
	}

	public String getWayName() {
		return wayName;
	}

	public void setWayName(String wayName) {
		this.wayName = wayName;
	}

	public List<Long> getPayWayIds() {
		return payWayIds;
	}

	public void setPayWayIds(List<Long> payWayIds) {
		this.payWayIds = payWayIds;
	}

	public String getShopWeekTimeStart() {
		return shopWeekTimeStart;
	}

	public void setShopWeekTimeStart(String shopWeekTimeStart) {
		this.shopWeekTimeStart = shopWeekTimeStart;
	}

	public String getShopWeekTimeEnd() {
		return shopWeekTimeEnd;
	}

	public void setShopWeekTimeEnd(String shopWeekTimeEnd) {
		this.shopWeekTimeEnd = shopWeekTimeEnd;
	}

	public JSONArray getInlineObj() {
		return inlineObj;
	}

	public void setInlineObj(JSONArray inlineObj) {
		this.inlineObj = inlineObj;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public String getShopCard() {
		return shopCard;
	}

	public void setShopCard(String shopCard) {
		this.shopCard = shopCard;
	}

	public String getShopRemarks() {
		return shopRemarks;
	}

	public void setShopRemarks(String shopRemarks) {
		this.shopRemarks = shopRemarks;
	}

	public String getShopEmail() {
		return shopEmail;
	}

	public void setShopEmail(String shopEmail) {
		this.shopEmail = shopEmail;
	}
}