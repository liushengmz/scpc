package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_app_discount       
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //支付通道id,关联payv2_pay_way表主键
merchant_type        Integer(10)                 1          //商户类型1.app2.店铺
scheme_name          String(50)                             //方案名称
user_type            Integer(10)                 1          //用户类型（1.新用户2.老用户）
startup_start_time   Date(19)                               //启动周期开始时间
startup_end_time     Date(19)                               //启动周期结束时间
discount_start_time  String(100)                            //优惠时间开始时间hhmmss
discount_end_time    String(100)                            //优惠时间结束时间hhmmss
scheme_type          Integer(10)                 1          //方案类型1.线上补贴2.线下补贴
discount_money       Integer(10)                 0          //优惠总金额 整数，单位取决于discount_order_num字段
single_discount_money Integer(10)                            //单个app或店铺优惠总额度，单位取决于discount_order_num字段
single_order_money   Integer(10)                 0          //单笔订单金额限制
single_order_max_money Integer(10)                 0          //单笔订单最高优惠限额
discount_order_num   Integer(10)                 1          //优惠金额类型1.金额2.单数
user_order_num       Integer(10)                 0          //用户订单数
discount_area        Integer(10)                 1          //优惠区域 省，关联payv2_provinces_city表
discount_type        Integer(10)                 1          //优惠类型1.直减2.折扣
discount_value       Double(13,2)                0.00       //优惠额度,单位由discount_type决定
is_show              Integer(10)                 2          //是否上线1上线2未上线3已下架4已结束
is_delete            Integer(10)                 2          //是否删除1.是2否
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Payv2AppDiscount implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long appId;
	private	Integer merchantType;
	private	String schemeName;
	private	Integer userType;
	private	Date startupStartTime;
	private	Date startupEndTime;
	private	String discountStartTime;
	private	String discountEndTime;
	private	Integer schemeType;
	private	Integer discountMoney;
	private	Integer singleDiscountMoney;
	private	Integer singleOrderMoney;
	private	Integer singleOrderMaxMoney;
	private	Integer discountOrderNum;
	private	Integer userOrderNum;
	private	Integer discountArea;
	private	Integer discountType;
	private	Double discountValue;
	private	Integer isShow;
	private	Integer isDelete;
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
	* app_id  Long(19)  //支付通道id,关联payv2_pay_way表主键    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //支付通道id,关联payv2_pay_way表主键    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* merchant_type  Integer(10)  1  //商户类型1.app2.店铺    
	*/
	public Integer getMerchantType(){
		return merchantType;
	}
	
	/**
	* merchant_type  Integer(10)  1  //商户类型1.app2.店铺    
	*/
	public void setMerchantType(Integer merchantType){
		this.merchantType = merchantType;
	}
	
	/**
	* scheme_name  String(50)  //方案名称    
	*/
	public String getSchemeName(){
		return schemeName;
	}
	
	/**
	* scheme_name  String(50)  //方案名称    
	*/
	public void setSchemeName(String schemeName){
		this.schemeName = schemeName;
	}
	
	/**
	* user_type  Integer(10)  1  //用户类型（1.新用户2.老用户）    
	*/
	public Integer getUserType(){
		return userType;
	}
	
	/**
	* user_type  Integer(10)  1  //用户类型（1.新用户2.老用户）    
	*/
	public void setUserType(Integer userType){
		this.userType = userType;
	}
	
	/**
	* startup_start_time  Date(19)  //启动周期开始时间    
	*/
	public Date getStartupStartTime(){
		return startupStartTime;
	}
	
	/**
	* startup_start_time  Date(19)  //启动周期开始时间    
	*/
	public void setStartupStartTime(Date startupStartTime){
		this.startupStartTime = startupStartTime;
	}
	
	/**
	* startup_end_time  Date(19)  //启动周期结束时间    
	*/
	public Date getStartupEndTime(){
		return startupEndTime;
	}
	
	/**
	* startup_end_time  Date(19)  //启动周期结束时间    
	*/
	public void setStartupEndTime(Date startupEndTime){
		this.startupEndTime = startupEndTime;
	}
	
	/**
	* discount_start_time  String(100)  //优惠时间开始时间hhmmss    
	*/
	public String getDiscountStartTime(){
		return discountStartTime;
	}
	
	/**
	* discount_start_time  String(100)  //优惠时间开始时间hhmmss    
	*/
	public void setDiscountStartTime(String discountStartTime){
		this.discountStartTime = discountStartTime;
	}
	
	/**
	* discount_end_time  String(100)  //优惠时间结束时间hhmmss    
	*/
	public String getDiscountEndTime(){
		return discountEndTime;
	}
	
	/**
	* discount_end_time  String(100)  //优惠时间结束时间hhmmss    
	*/
	public void setDiscountEndTime(String discountEndTime){
		this.discountEndTime = discountEndTime;
	}
	
	/**
	* scheme_type  Integer(10)  1  //方案类型1.线上补贴2.线下补贴    
	*/
	public Integer getSchemeType(){
		return schemeType;
	}
	
	/**
	* scheme_type  Integer(10)  1  //方案类型1.线上补贴2.线下补贴    
	*/
	public void setSchemeType(Integer schemeType){
		this.schemeType = schemeType;
	}
	
	/**
	* discount_money  Integer(10)  0  //优惠总金额  整数，单位取决于discount_order_num字段    
	*/
	public Integer getDiscountMoney(){
		return discountMoney;
	}
	
	/**
	* discount_money  Integer(10)  0  //优惠总金额  整数，单位取决于discount_order_num字段    
	*/
	public void setDiscountMoney(Integer discountMoney){
		this.discountMoney = discountMoney;
	}
	
	/**
	* single_discount_money  Integer(10)  //单个app或店铺优惠总额度，单位取决于discount_order_num字段    
	*/
	public Integer getSingleDiscountMoney(){
		return singleDiscountMoney;
	}
	
	/**
	* single_discount_money  Integer(10)  //单个app或店铺优惠总额度，单位取决于discount_order_num字段    
	*/
	public void setSingleDiscountMoney(Integer singleDiscountMoney){
		this.singleDiscountMoney = singleDiscountMoney;
	}
	
	/**
	* single_order_money  Integer(10)  0  //单笔订单金额限制    
	*/
	public Integer getSingleOrderMoney(){
		return singleOrderMoney;
	}
	
	/**
	* single_order_money  Integer(10)  0  //单笔订单金额限制    
	*/
	public void setSingleOrderMoney(Integer singleOrderMoney){
		this.singleOrderMoney = singleOrderMoney;
	}
	
	/**
	* single_order_max_money  Integer(10)  0  //单笔订单最高优惠限额    
	*/
	public Integer getSingleOrderMaxMoney(){
		return singleOrderMaxMoney;
	}
	
	/**
	* single_order_max_money  Integer(10)  0  //单笔订单最高优惠限额    
	*/
	public void setSingleOrderMaxMoney(Integer singleOrderMaxMoney){
		this.singleOrderMaxMoney = singleOrderMaxMoney;
	}
	
	/**
	* discount_order_num  Integer(10)  1  //优惠金额类型1.金额2.单数    
	*/
	public Integer getDiscountOrderNum(){
		return discountOrderNum;
	}
	
	/**
	* discount_order_num  Integer(10)  1  //优惠金额类型1.金额2.单数    
	*/
	public void setDiscountOrderNum(Integer discountOrderNum){
		this.discountOrderNum = discountOrderNum;
	}
	
	/**
	* user_order_num  Integer(10)  0  //用户订单数    
	*/
	public Integer getUserOrderNum(){
		return userOrderNum;
	}
	
	/**
	* user_order_num  Integer(10)  0  //用户订单数    
	*/
	public void setUserOrderNum(Integer userOrderNum){
		this.userOrderNum = userOrderNum;
	}
	
	/**
	* discount_area  Integer(10)  1  //优惠区域  省，关联payv2_provinces_city表    
	*/
	public Integer getDiscountArea(){
		return discountArea;
	}
	
	/**
	* discount_area  Integer(10)  1  //优惠区域  省，关联payv2_provinces_city表    
	*/
	public void setDiscountArea(Integer discountArea){
		this.discountArea = discountArea;
	}
	
	/**
	* discount_type  Integer(10)  1  //优惠类型1.直减2.折扣    
	*/
	public Integer getDiscountType(){
		return discountType;
	}
	
	/**
	* discount_type  Integer(10)  1  //优惠类型1.直减2.折扣    
	*/
	public void setDiscountType(Integer discountType){
		this.discountType = discountType;
	}
	
	/**
	* discount_value  Double(13,2)  0.00  //优惠额度,单位由discount_type决定    
	*/
	public Double getDiscountValue(){
		return discountValue;
	}
	
	/**
	* discount_value  Double(13,2)  0.00  //优惠额度,单位由discount_type决定    
	*/
	public void setDiscountValue(Double discountValue){
		this.discountValue = discountValue;
	}
	
	/**
	* is_show  Integer(10)  2  //是否上线1上线2未上线3已下架4已结束    
	*/
	public Integer getIsShow(){
		return isShow;
	}
	
	/**
	* is_show  Integer(10)  2  //是否上线1上线2未上线3已下架4已结束    
	*/
	public void setIsShow(Integer isShow){
		this.isShow = isShow;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除1.是2否    
	*/
	public Integer getIsDelete(){
		return isDelete;
	}
	
	/**
	* is_delete  Integer(10)  2  //是否删除1.是2否    
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
	
}