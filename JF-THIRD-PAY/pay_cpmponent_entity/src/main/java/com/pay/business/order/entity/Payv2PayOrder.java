package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_order          
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
order_num            String(64)                             //支付集订单号
channel_id           Long(19)                               //渠道id，关联payv2_channel表
order_name           String(500)                            //订单名称
goods_id             Long(19)                               //商品id,关联payv2_pay_goods表
order_describe       String(65535)                          //订单描述
merchant_order_num   String(64)                             //商户订单号
company_id           Long(19)                               //商户id,关联payv2_buss_company表
user_id              Long(19)                               //用户id，关联payv2_pay_user
order_type           Integer(10)                 1          //订单类型1.app（线上）2.店铺（线下）
app_id               Long(19)                               //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
group_id             Long(19)                               //应用分组id，关联payv2_pay_order_group表
pay_way_id           Long(19)                               //支付渠道，关联payv2_pay_way
rate_id              Long(19)                               //支付通道路由id
rate_type            Integer(10)                 1          //支付类型1.app支付2.web3.扫码
pay_way_user_name    String(20)                             //收款账号
pay_user_name        String(20)                             //支付账号
bank_transaction     String(50)                             //银行流水号
pay_money            Double(9,2)                            //订单金额
pay_discount_money   Double(9,2)                            //折扣后的金额,也就是用户最终需要支付的金额
pay_way_money        Double(9,2)                            //通道费（以分为单位，小于分不收费）
discount_id          Long(19)                               //优惠方案id，关联payv2_app_discount表
discount_money       Double(9,2)                            //优惠金额
pay_status           Integer(10)                            //支付状态,1支付成功,2支付失败,3未支付,4超时,5.扣款成功回调失败
liquidation_time     Date(19)                               //清算时间
create_time          Date(19)                               //创建订单时间
pay_time             Date(19)                               //订单支付时间
return_url           String(255)                            //返回页面url
notify_url           String(255)                            //回调地址url（服务器）
callback_time        Date(19)                               //订单回调时间
update_time          Date(19)                               //修改时间
*/
public class Payv2PayOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	String orderNum;
	private	Long channelId;
	private	String orderName;
	private	String remark;
	private	Long goodsId;
	private	String orderDescribe;
	private	String merchantOrderNum;
	private	Long companyId;
	private	Long userId;
	private String userName;
	private	Integer orderType;
	private	Long appId;
	private	Long groupId;
	private	Long payWayId;
	private	Long rateId;
	private	Integer rateType;
	private	String payWayUserName;
	private	String payUserName;
	private	String bankTransaction;
	private	Double payMoney;
	private	Double payDiscountMoney;
	private	Double payWayMoney;
	private	Double bussWayRate;
	private	Double costRate;
	private	Double costRateMoney;
	private	Long discountId;
	private	Double discountMoney;
	private	Integer payStatus;
	private	Date liquidationTime;
	private	Date createTime;
	private	Date payTime;
	private	String returnUrl;
	private	String notifyUrl;
	private	Date callbackTime;
	private	Date updateTime;

	private	String companyName;
	private	String appName;
	private	String wayName;
	private String rateName;
	private	String goodsName;
	private String channelName;
	private String payStatusName;
	
	private double refundMoney;
	
	private Integer orderCount;
	
	private	String groupValue;
	
	private String payTimeBegin;
	private String payTimeEnd;
	private Double returnMoney;//退款金额
	private Double actuallyPayMoney;//实收金额
	private Double arrivalMoney;//到账金额
	private Double payWayRate;//通道费‰
	
	private String alipayOrderType;//对账单业务类型
	private String payType;//支付类型
	
	//支付通道参数
	private String rateKey1;
	private String rateKey2;
	private String rateKey3;
	private String rateKey4;
	private String rateKey5;
	private String rateKey6;
	//微信appid
	private String gzhAppId;
	//微信秘钥
	private String gzhKey;
	//预留字段
	private String gzhStr;
	private Integer dic_id;
	private String rateCompanyName;
	
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

	public Integer getDic_id() {
		return dic_id;
	}

	public void setDic_id(Integer dic_id) {
		this.dic_id = dic_id;
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

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
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
	* order_num  String(64)  //支付集订单号    
	*/
	public String getOrderNum(){
		return orderNum;
	}
	
	/**
	* order_num  String(64)  //支付集订单号    
	*/
	public void setOrderNum(String orderNum){
		this.orderNum = orderNum;
	}
	
	/**
	* channel_id  Long(19)  //渠道id，关联payv2_channel表    
	*/
	public Long getChannelId(){
		return channelId;
	}
	
	/**
	* channel_id  Long(19)  //渠道id，关联payv2_channel表    
	*/
	public void setChannelId(Long channelId){
		this.channelId = channelId;
	}
	
	/**
	* order_name  String(500)  //订单名称    
	*/
	public String getOrderName(){
		return orderName;
	}
	
	/**
	* order_name  String(500)  //订单名称    
	*/
	public void setOrderName(String orderName){
		this.orderName = orderName;
	}
	
	/**
	* order_describe  String(65535)  //订单描述    
	*/
	public String getOrderDescribe(){
		return orderDescribe;
	}
	
	/**
	* order_describe  String(65535)  //订单描述    
	*/
	public void setOrderDescribe(String orderDescribe){
		this.orderDescribe = orderDescribe;
	}
	
	/**
	* merchant_order_num  String(64)  //商户订单号    
	*/
	public String getMerchantOrderNum(){
		return merchantOrderNum;
	}
	
	/**
	* merchant_order_num  String(64)  //商户订单号    
	*/
	public void setMerchantOrderNum(String merchantOrderNum){
		this.merchantOrderNum = merchantOrderNum;
	}
	
	/**
	* company_id  Long(19)  //商户id,关联payv2_buss_company表    
	*/
	public Long getCompanyId(){
		return companyId;
	}
	
	/**
	* company_id  Long(19)  //商户id,关联payv2_buss_company表    
	*/
	public void setCompanyId(Long companyId){
		this.companyId = companyId;
	}
	
	/**
	* user_id  Long(19)  //用户id，关联payv2_pay_user    
	*/
	public Long getUserId(){
		return userId;
	}
	
	/**
	* user_id  Long(19)  //用户id，关联payv2_pay_user    
	*/
	public void setUserId(Long userId){
		this.userId = userId;
	}
	
	/**
	* order_type  Integer(10)  1  //订单类型1.app（线上）2.店铺（线下）    
	*/
	public Integer getOrderType(){
		return orderType;
	}
	
	/**
	* order_type  Integer(10)  1  //订单类型1.app（线上）2.店铺（线下）    
	*/
	public void setOrderType(Integer orderType){
		this.orderType = orderType;
	}
	
	/**
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表    
	*/
	public Long getAppId(){
		return appId;
	}
	
	/**
	* app_id  Long(19)  //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表    
	*/
	public void setAppId(Long appId){
		this.appId = appId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付渠道，关联payv2_pay_way    
	*/
	public Long getPayWayId(){
		return payWayId;
	}
	
	/**
	* pay_way_id  Long(19)  //支付渠道，关联payv2_pay_way    
	*/
	public void setPayWayId(Long payWayId){
		this.payWayId = payWayId;
	}
	
	/**
	* pay_way_user_name  String(20)  //收款账号    
	*/
	public String getPayWayUserName(){
		return payWayUserName;
	}
	
	/**
	* pay_way_user_name  String(20)  //收款账号    
	*/
	public void setPayWayUserName(String payWayUserName){
		this.payWayUserName = payWayUserName;
	}
	
	/**
	* pay_user_name  String(20)  //支付账号    
	*/
	public String getPayUserName(){
		return payUserName;
	}
	
	/**
	* pay_user_name  String(20)  //支付账号    
	*/
	public void setPayUserName(String payUserName){
		this.payUserName = payUserName;
	}
	
	/**
	* bank_transaction  String(50)  //银行流水号    
	*/
	public String getBankTransaction(){
		return bankTransaction;
	}
	
	/**
	* bank_transaction  String(50)  //银行流水号    
	*/
	public void setBankTransaction(String bankTransaction){
		this.bankTransaction = bankTransaction;
	}
	
	/**
	* pay_money  Double(9,2)  //订单金额    
	*/
	public Double getPayMoney(){
		return payMoney;
	}
	
	/**
	* pay_money  Double(9,2)  //订单金额    
	*/
	public void setPayMoney(Double payMoney){
		this.payMoney = payMoney;
	}
	
	/**
	* pay_discount_money  Double(9,2)  //折扣后的金额,也就是用户最终需要支付的金额    
	*/
	public Double getPayDiscountMoney(){
		return payDiscountMoney;
	}
	
	/**
	* pay_discount_money  Double(9,2)  //折扣后的金额,也就是用户最终需要支付的金额    
	*/
	public void setPayDiscountMoney(Double payDiscountMoney){
		this.payDiscountMoney = payDiscountMoney;
	}
	
	/**
	* discount_id  Long(19)  //优惠方案id，关联payv2_app_discount表    
	*/
	public Long getDiscountId(){
		return discountId;
	}
	
	/**
	* discount_id  Long(19)  //优惠方案id，关联payv2_app_discount表    
	*/
	public void setDiscountId(Long discountId){
		this.discountId = discountId;
	}
	
	/**
	* discount_money  Double(9,2)  //优惠金额    
	*/
	public Double getDiscountMoney(){
		return discountMoney;
	}
	
	/**
	* discount_money  Double(9,2)  //优惠金额    
	*/
	public void setDiscountMoney(Double discountMoney){
		this.discountMoney = discountMoney;
	}
	
	/**
	* pay_status  Integer(10)  //支付状态,1支付成功,2支付失败,3未支付,4超时,5.扣款成功回调失败    
	*/
	public Integer getPayStatus(){
		return payStatus;
	}
	
	/**
	* pay_status  Integer(10)  //支付状态,1支付成功,2支付失败,3未支付,4超时,5.扣款成功回调失败    
	*/
	public void setPayStatus(Integer payStatus){
		this.payStatus = payStatus;
	}
	
	/**
	* liquidation_time  Date(19)  //清算时间    
	*/
	public Date getLiquidationTime(){
		return liquidationTime;
	}
	
	/**
	* liquidation_time  Date(19)  //清算时间    
	*/
	public void setLiquidationTime(Date liquidationTime){
		this.liquidationTime = liquidationTime;
	}
	
	/**
	* create_time  Date(19)  //创建订单时间    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  //创建订单时间    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	/**
	* pay_time  Date(19)  //订单支付时间    
	*/
	public Date getPayTime(){
		return payTime;
	}
	
	/**
	* pay_time  Date(19)  //订单支付时间    
	*/
	public void setPayTime(Date payTime){
		this.payTime = payTime;
	}
	
	/**
	* return_url  String(255)  //返回页面url    
	*/
	public String getReturnUrl(){
		return returnUrl;
	}
	
	/**
	* return_url  String(255)  //返回页面url    
	*/
	public void setReturnUrl(String returnUrl){
		this.returnUrl = returnUrl;
	}
	
	/**
	* notify_url  String(255)  //回调地址url（服务器）    
	*/
	public String getNotifyUrl(){
		return notifyUrl;
	}
	
	/**
	* notify_url  String(255)  //回调地址url（服务器）    
	*/
	public void setNotifyUrl(String notifyUrl){
		this.notifyUrl = notifyUrl;
	}
	
	/**
	* callback_time  Date(19)  //订单回调时间    
	*/
	public Date getCallbackTime(){
		return callbackTime;
	}
	
	/**
	* callback_time  Date(19)  //订单回调时间    
	*/
	public void setCallbackTime(Date callbackTime){
		this.callbackTime = callbackTime;
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

	public double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupValue() {
		return groupValue;
	}

	public void setGroupValue(String groupValue) {
		this.groupValue = groupValue;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getPayTimeEnd() {
		return payTimeEnd;
	}

	public void setPayTimeEnd(String payTimeEnd) {
		this.payTimeEnd = payTimeEnd;
	}

	public String getPayTimeBegin() {
		return payTimeBegin;
	}

	public void setPayTimeBegin(String payTimeBegin) {
		this.payTimeBegin = payTimeBegin;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPayStatusName() {
		return payStatusName;
	}

	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}

	public Double getPayWayMoney() {
		return payWayMoney;
	}

	public void setPayWayMoney(Double payWayMoney) {
		this.payWayMoney = payWayMoney;
	}

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}
	public Double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}

	public Double getActuallyPayMoney() {
		return actuallyPayMoney;
	}

	public void setActuallyPayMoney(Double actuallyPayMoney) {
		this.actuallyPayMoney = actuallyPayMoney;
	}

	public Integer getRateType() {
		return rateType;
	}

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}

	public Double getArrivalMoney() {
		return arrivalMoney;
	}

	public void setArrivalMoney(Double arrivalMoney) {
		this.arrivalMoney = arrivalMoney;
	}

	public Double getPayWayRate() {
		return payWayRate;
	}

	public void setPayWayRate(Double payWayRate) {
		this.payWayRate = payWayRate;
	}

	public String getAlipayOrderType() {
		return alipayOrderType;
	}

	public void setAlipayOrderType(String alipayOrderType) {
		this.alipayOrderType = alipayOrderType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getBussWayRate() {
		return bussWayRate;
	}

	public void setBussWayRate(Double bussWayRate) {
		this.bussWayRate = bussWayRate;
	}

	public Double getCostRate() {
		return costRate;
	}

	public void setCostRate(Double costRate) {
		this.costRate = costRate;
	}

	public Double getCostRateMoney() {
		return costRateMoney;
	}

	public void setCostRateMoney(Double costRateMoney) {
		this.costRateMoney = costRateMoney;
	}

	public String getRateCompanyName() {
		return rateCompanyName;
	}

	public void setRateCompanyName(String rateCompanyName) {
		this.rateCompanyName = rateCompanyName;
	}
}