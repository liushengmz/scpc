package com.pay.business.payway.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_way_rate       
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
pay_way_name         String(50)                             //支付通道名称（由支付平台和主键生成）
rate_name            String(50)                             //通道名称（自动生成）
pay_way_id           Long(19)                               //所属渠道id
pay_type             Integer(10)                 1          //支付类型1.app支付2.web3.扫码
pay_view_type        Integer(10)                 1          //1.支付宝，2.微信 。。。。新增叠加（此字段主要针对客户端无界面支付策略筛选）
dic_id               Long(19)                               //接入支付的类型，关联字典表sys_config_dictionary表
company_name         String(50)                             //所属机构名称
company_alias        String(50)                             //商户简称
contacts_name        String(50)                             //联系人名称
contacts_phone       String(20)                             //联系人电话
account_bank         String(50)                             //开户银行
account_bank_son     String(50)                             //开户支行
account_type         Integer(10)                 1          //收款账号类型（1对公2对私）
account_name         String(50)                             //开户名称
account_card         String(50)                             //开户账号
pay_way_rate         Double(9,2)                 0.00       //通道费‰
way_arrival_type     Integer(10)                 1          //到账类型1.T+日期（工作日）2.实时到账3.T+日期
way_arrival_value    Integer(10)                 0          //到账时间
one_max_money        Double(16,2)                0.00       //单笔限额0表示无限制
max_money            Double(16,2)                0.00       //每日限额0表示无限制
rate_key1            String(100)                            //第三方通道字段1,如支付宝app_id
rate_key2            String(4000)                           //第三方通道字段2,如支付宝商户私钥
rate_key3            String(800)                            //第三方通道字段3,如支付宝公钥
rate_key4            String(100)                            //通道字段4
rate_key5            String(65535)                          //通道字段5
rate_key6            String(65535)                          //通道字段6
gzh_app_id           String(100)                            //公众号app_id
gzh_key              String(100)                            //公众号key
gzh_str              String(100)                            //公众号预留字段
key_remark           String(65535)                          //通道备注
auto_record          Integer(10)                 1          //是否自动对账1是2否
is_delete            Integer(10)                 2          //是否删除1.是2.否
status               Integer(10)                 1          //状态1.开启2.关闭
create_user_by       Long(19)                               //创建者
update_user_by       Long(19)                               //修改人
create_time          Date(19)                               //创建时间
update_time          Date(19)                               //修改时间
*/
public class Payv2PayWayRate implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	String payWayName;
	private	String rateName;
	private	Long payWayId;
	private	Integer payType;
	private	Integer payViewType;
	private	Long dicId;
	private	String companyName;
	private	String companyAlias;
	private	String contactsName;
	private	String contactsPhone;
	private	String accountBank;
	private	String accountBankSon;
	private	Integer accountType;
	private	String accountName;
	private	String accountCard;
	private	Double payWayRate;
	private	Integer wayArrivalType;
	private	Integer wayArrivalValue;
	private	Double oneMaxMoney;
	private	Double maxMoney;
	private	String rateKey1;
	private	String rateKey2;
	private	String rateKey3;
	private	String rateKey4;
	private	String rateKey5;
	private	String rateKey6;
	private	String gzhAppId;
	private	String gzhKey;
	private	String gzhStr;
	private	String keyRemark;
	private	Integer autoRecord;
	private	Integer isDelete;
	private	Integer status;
	private	Long createUserBy;
	private	Long updateUserBy;
	private	Date createTime;
	private	Date updateTime;
	
	private Double companyRate;			//商户的费率
	private String dictName;			//字典支付通道
	
	

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
	* pay_way_name  String(50)  //支付通道名称    
	*/
	public String getPayWayName(){
		return payWayName;
	}
	
	/**
	* pay_way_name  String(50)  //支付通道名称    
	*/
	public void setPayWayName(String payWayName){
		this.payWayName = payWayName;
	}
	
	/**
	* pay_way_id  Long(19)  //所属渠道id    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //所属渠道id    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* pay_type  Integer(10)  1  //支付类型1.web2.扫码3.app支付    
	*/
	public Integer getPayType(){
		return payType;
	}
	
	/**
	* pay_type  Integer(10)  1  //支付类型1.web2.扫码3.app支付    
	*/
	public void setPayType(Integer payType){
		this.payType = payType;
	}
	
	/**
	* dic_id  Long(19)  //接入支付的类型，关联字典表sys_config_dictionary表    
	*/
	public Long getDicId(){
		return dicId;
	}
	
	/**
	* dic_id  Long(19)  //接入支付的类型，关联字典表sys_config_dictionary表    
	*/
	public void setDicId(Long dicId){
		this.dicId = dicId;
	}
	
	/**
	* company_name  String(50)  //所属机构名称    
	*/
	public String getCompanyName(){
		return companyName;
	}
	
	/**
	* company_name  String(50)  //所属机构名称    
	*/
	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}
	
	/**
	* contacts_name  String(50)  //联系人名称    
	*/
	public String getContactsName(){
		return contactsName;
	}
	
	/**
	* contacts_name  String(50)  //联系人名称    
	*/
	public void setContactsName(String contactsName){
		this.contactsName = contactsName;
	}
	
	/**
	* contacts_phone  String(20)  //联系人电话    
	*/
	public String getContactsPhone(){
		return contactsPhone;
	}
	
	/**
	* contacts_phone  String(20)  //联系人电话    
	*/
	public void setContactsPhone(String contactsPhone){
		this.contactsPhone = contactsPhone;
	}
	
	/**
	* pay_way_rate  Double(9,2)  0.00  //通道费‰    
	*/
	public Double getPayWayRate(){
		return payWayRate;
	}
	
	/**
	* pay_way_rate  Double(9,2)  0.00  //通道费‰    
	*/
	public void setPayWayRate(Double payWayRate){
		this.payWayRate = payWayRate;
	}
	
	/**
	* way_arrival_type  Integer(10)  1  //到账类型1.T+日期（工作日）2.实时到账3.T+日期    
	*/
	public Integer getWayArrivalType(){
		return wayArrivalType;
	}
	
	/**
	* way_arrival_type  Integer(10)  1  //到账类型1.T+日期（工作日）2.实时到账3.T+日期    
	*/
	public void setWayArrivalType(Integer wayArrivalType){
		this.wayArrivalType = wayArrivalType;
	}
	
	/**
	* way_arrival_value  Integer(10)  0  //到账时间    
	*/
	public Integer getWayArrivalValue(){
		return wayArrivalValue;
	}
	
	/**
	* way_arrival_value  Integer(10)  0  //到账时间    
	*/
	public void setWayArrivalValue(Integer wayArrivalValue){
		this.wayArrivalValue = wayArrivalValue;
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
	* status  Integer(10)  1  //状态1.开启2.关闭    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  1  //状态1.开启2.关闭    
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

	public Integer getPayViewType() {
		return payViewType;
	}

	public void setPayViewType(Integer payViewType) {
		this.payViewType = payViewType;
	}

	public Double getCompanyRate() {
		return companyRate;
	}

	public void setCompanyRate(Double companyRate) {
		this.companyRate = companyRate;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public Double getOneMaxMoney() {
		return oneMaxMoney;
	}

	public void setOneMaxMoney(Double oneMaxMoney) {
		this.oneMaxMoney = oneMaxMoney;
	}

	public Double getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(Double maxMoney) {
		this.maxMoney = maxMoney;
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

	public String getGzhStr() {
		return gzhStr;
	}

	public void setGzhStr(String gzhStr) {
		this.gzhStr = gzhStr;
	}

	public String getKeyRemark() {
		return keyRemark;
	}

	public void setKeyRemark(String keyRemark) {
		this.keyRemark = keyRemark;
	}

	public Integer getAutoRecord() {
		return autoRecord;
	}

	public void setAutoRecord(Integer autoRecord) {
		this.autoRecord = autoRecord;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	public String getCompanyAlias() {
		return companyAlias;
	}

	public void setCompanyAlias(String companyAlias) {
		this.companyAlias = companyAlias;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getAccountBankSon() {
		return accountBankSon;
	}

	public void setAccountBankSon(String accountBankSon) {
		this.accountBankSon = accountBankSon;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
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

	public Long getCreateUserBy() {
		return createUserBy;
	}

	public void setCreateUserBy(Long createUserBy) {
		this.createUserBy = createUserBy;
	}

	public Long getUpdateUserBy() {
		return updateUserBy;
	}

	public void setUpdateUserBy(Long updateUserBy) {
		this.updateUserBy = updateUserBy;
	}
	
}