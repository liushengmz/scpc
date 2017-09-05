package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
TABLE:.payv2_buss_app_support_pay_way  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //关联payv2_buss_company_app的主键
pay_way_id           Long(19)                               //关联payv2_buss_support_pay_way的主键
pay_way_rate         Double(5,2)                            //支付通道费,千分比
sort_num             Integer(10)                            //排列序号
status               Integer(10)                 1          //状态,1启用,2不启用
is_delete            Integer(10)                 2          //是否删除,1是,2否
create_time          Date(19)                               //
update_time          Date(19)                               //
*/
public class Payv2BussAppSupportPayWay implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long appId;
	private	Integer merchantType;
	private	Long payWayId;
	private List<Long> payWayIds; 
	private	Double payWayRate;
	private	Integer sortNum;
	private	Integer status;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;
	private String wayIcon;
	private	String appName;
	
	private	String wayName;
	
	private String payWayUserName;//收款人名称
	private String payWayUserNum;//收款人预留收款账号

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
	* app_id  Long(19)  //关联payv2_buss_company_app的主键    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //关联payv2_buss_company_app的主键    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* pay_way_id  Long(19)  //关联payv2_buss_support_pay_way的主键    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //关联payv2_buss_support_pay_way的主键    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* pay_way_rate  Double(5,2)  //支付通道费,千分比    
	*/
	public Double getPayWayRate(){
		return payWayRate;
	}
	
	/**
	* pay_way_rate  Double(5,2)  //支付通道费,千分比    
	*/
	public void setPayWayRate(Double payWayRate){
		this.payWayRate = payWayRate;
	}
	
	/**
	* sort_num  Integer(10)  //排列序号    
	*/
	public Integer getSortNum(){
		return sortNum;
	}
	
	/**
	* sort_num  Integer(10)  //排列序号    
	*/
	public void setSortNum(Integer sortNum){
		this.sortNum = sortNum;
	}
	
	/**
	* status  Integer(10)  1  //状态,1启用,2不启用    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  1  //状态,1启用,2不启用    
	*/
	public void setStatus(Integer status){
		this.status = status;
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
	

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getWayName() {
		return wayName;
	}

	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}

	public String getPayWayUserName() {
		return payWayUserName;
	}

	public void setPayWayUserName(String payWayUserName) {
		this.payWayUserName = payWayUserName;
	}

	public String getPayWayUserNum() {
		return payWayUserNum;
	}

	public void setPayWayUserNum(String payWayUserNum) {
		this.payWayUserNum = payWayUserNum;
	}

	public List<Long> getPayWayIds() {
		return payWayIds;
	}

	public void setPayWayIds(List<Long> payWayIds) {
		this.payWayIds = payWayIds;
	}

	public String getWayIcon() {
		return wayIcon;
	}

	public void setWayIcon(String wayIcon) {
		this.wayIcon = wayIcon;
	}

}