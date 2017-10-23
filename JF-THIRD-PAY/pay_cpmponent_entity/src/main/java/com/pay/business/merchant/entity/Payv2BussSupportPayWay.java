package com.pay.business.merchant.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.pay.business.payway.entity.Payv2PayWayRate;

/**
TABLE:.payv2_buss_support_pay_way  
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
parent_id            Long(19)                               //商户的ID
pay_type             Integer(10)                 1          //支持的支付类型,1钱包支付,2第三方支付如微信支付宝
pay_way_id           Long(19)                               //支付通道ID,关联支付通道表
rate_id              Long(19)                               //支付通通道路由id，关联payv2_pay_way_rate表
pay_way_user_name    String(20)                             //支付通道帐户名
pay_way_user_num     String(50)                             //支付通道收款帐号
pay_way_rate         Double(5,2)                            //支付通道费,千分比
pay_way_status       Integer(10)                 1          //通道状态,1启用,2不启用
is_delete            Integer(10)                 2          //是否删除,1是,2否
create_time          Date(19)                               //
update_time          Date(19)                               //
*/
public class Payv2BussSupportPayWay implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long parentId;
	private	Integer payType;
	private	Long payWayId;
	private	Long rateId;
	private	String payWayUserName;
	private	String payWayUserNum;
	private	Double payWayRate;
	private	Integer sortNum;
	private	Double oneMinMoney;
	private	Double oneMaxMoney;
	private	Double maxMoney;
	private	Integer payWayStatus;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;
	private	Long receivablesId;

	private	String companyName;//商户名字
	private	String wayIcon;
	private	String wayName;//支付通道名字
	private Integer weekType;//结算周期
	private Integer wayArrivalValue;//结算周期值
	private List<Payv2PayWayRate> rateList; //通道路由
	private	String dictName;//字典
	private Double rateOneMaxMoney;
	private Double rateMaxMoney;
	private String rateKey1;//第三方支付参数1
	private String rateKey2;//第三方支付参数2
	private String rateKey3;//第三方支付参数3
	private String rateKey4;//第三方支付参数4
	private String rateKey5;//第三方支付参数5
	private String rateKey6;//第三方支付参数6
	
	private String gzhAppId;//公众号app_id
	private String gzhKey;//公众号key
	
	private Double costRate;
	private String rateName;
	private String payWayName;
	
	
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
	* parent_id  Long(19)  //商户的ID    
	*/
	public Long getParentId(){
		return parentId;
	}
	
	/**
	* parent_id  Long(19)  //商户的ID    
	*/
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}
	
	/**
	* pay_type  Integer(10)  1  //支持的支付类型,1钱包支付,2第三方支付如微信支付宝    
	*/
	public Integer getPayType(){
		return payType;
	}
	
	/**
	* pay_type  Integer(10)  1  //支持的支付类型,1钱包支付,2第三方支付如微信支付宝    
	*/
	public void setPayType(Integer payType){
		this.payType = payType;
	}
	
	/**
	* pay_way_id  Long(19)  //支付通道ID,关联支付通道表    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付通道ID,关联支付通道表    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* pay_way_user_name  String(20)  //支付通道帐户名    
	*/
	public String getPayWayUserName(){
		return payWayUserName;
	}
	
	/**
	* pay_way_user_name  String(20)  //支付通道帐户名    
	*/
	public void setPayWayUserName(String payWayUserName){
		this.payWayUserName = payWayUserName;
	}
	
	/**
	* pay_way_user_num  String(50)  //支付通道收款帐号    
	*/
	public String getPayWayUserNum(){
		return payWayUserNum;
	}
	
	/**
	* pay_way_user_num  String(50)  //支付通道收款帐号    
	*/
	public void setPayWayUserNum(String payWayUserNum){
		this.payWayUserNum = payWayUserNum;
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
	* pay_way_status  Integer(10)  1  //通道状态,1启用,2不启用    
	*/
	public Integer getPayWayStatus(){
		return payWayStatus;
	}
	
	/**
	* pay_way_status  Integer(10)  1  //通道状态,1启用,2不启用    
	*/
	public void setPayWayStatus(Integer payWayStatus){
		this.payWayStatus = payWayStatus;
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

	public String getWayName() {
		return wayName;
	}

	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	
	public Long getReceivablesId() {
		return receivablesId;
	}

	public void setReceivablesId(Long receivablesId) {
		this.receivablesId = receivablesId;
	}

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}

	public List<Payv2PayWayRate> getRateList() {
		return rateList;
	}

	public void setRateList(List<Payv2PayWayRate> rateList) {
		this.rateList = rateList;
	}

	public Integer getWeekType() {
		return weekType;
	}

	public void setWeekType(Integer weekType) {
		this.weekType = weekType;
	}

	public String getWayIcon() {
		return wayIcon;
	}

	public void setWayIcon(String wayIcon) {
		this.wayIcon = wayIcon;
	}

	public Integer getWayArrivalValue() {
		return wayArrivalValue;
	}

	public void setWayArrivalValue(Integer wayArrivalValue) {
		this.wayArrivalValue = wayArrivalValue;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Double getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(Double maxMoney) {
		this.maxMoney = maxMoney;
	}

	public Double getOneMaxMoney() {
		return oneMaxMoney;
	}

	public void setOneMaxMoney(Double oneMaxMoney) {
		this.oneMaxMoney = oneMaxMoney;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public Double getRateOneMaxMoney() {
		return rateOneMaxMoney;
	}

	public void setRateOneMaxMoney(Double rateOneMaxMoney) {
		this.rateOneMaxMoney = rateOneMaxMoney;
	}

	public Double getRateMaxMoney() {
		return rateMaxMoney;
	}

	public void setRateMaxMoney(Double rateMaxMoney) {
		this.rateMaxMoney = rateMaxMoney;
	}

	public String getRateKey1() {
		return rateKey1;
	}

	public void setRateKey1(String rateKey1) {
		this.rateKey1 = rateKey1;
	}

	public String getRateKey2() {
		return rateKey2;
	}

	public void setRateKey2(String rateKey2) {
		this.rateKey2 = rateKey2;
	}

	public String getRateKey3() {
		return rateKey3;
	}

	public void setRateKey3(String rateKey3) {
		this.rateKey3 = rateKey3;
	}

	public String getRateKey4() {
		return rateKey4;
	}

	public void setRateKey4(String rateKey4) {
		this.rateKey4 = rateKey4;
	}

	public String getRateKey5() {
		return rateKey5;
	}

	public void setRateKey5(String rateKey5) {
		this.rateKey5 = rateKey5;
	}

	public String getRateKey6() {
		return rateKey6;
	}

	public void setRateKey6(String rateKey6) {
		this.rateKey6 = rateKey6;
	}

	public String getGzhAppId() {
		return gzhAppId;
	}

	public void setGzhAppId(String gzhAppId) {
		this.gzhAppId = gzhAppId;
	}

	public String getGzhKey() {
		return gzhKey;
	}

	public void setGzhKey(String gzhKey) {
		this.gzhKey = gzhKey;
	}

	public Double getOneMinMoney() {
		return oneMinMoney;
	}

	public void setOneMinMoney(Double oneMinMoney) {
		this.oneMinMoney = oneMinMoney;
	}

	public Double getCostRate() {
		return costRate;
	}

	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}
}