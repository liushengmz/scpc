package com.pay.business.payway.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;

/**
TABLE:.payv2_pay_way            
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
way_type             Integer(10)                 1          //通道类型,1钱包,2第三方支付
dic_id               Long(19)                               //接入支付的类型，关联字典表sys_config_dictionary表
way_name             String(50)                             //通道名称
way_company_name     String(50)                             //所属合作机构或钱包所属银行
merchant_code        String(64)                             //合作身份id/商户号
discout_region       String(255)                            //优惠支付地区，存json数组，如[{"pro":1,"city":2},{"pro":3,"city":4}]
is_trust_bank        Integer(10)                 1          //是否为资金托管银行1.是2.否
way_icon             String(255)                            //icon图标地址
way_slogan           String(50)                             //推广语
income_type          Integer(10)                 1          //收单机构（1.本行2.他行）
way_id               Long(19)                               //收单通道id
income_num           String(50)                             //收单账号
income_line          String(255)                            //线下第三方收单，存json数组，如[{"wayId":1,"userName":"莫凡","userNum":"1654553785@qq.com"},{"wayId":2,"userName":"_fjhkasg","userNum":"adlasdsfgsdg"}]
way_contacts         String(50)                             //联系人
way_phone            String(50)                             //联系方式
way_apk_url          String(255)                            //安装apk地址
way_apk_md5          String(255)                            //apk的md5值
way_apk_package      String(50)                             //apk包名
way_apk_size         Integer(10)                            //apk文件大小
way_ios_url          String(255)                            //iosSDK地址
way_rate             Double(5,2)                 0.00       //支付费率，单位千分号
way_arrival_type     Integer(10)                 1          //到账类型1.T+日期（工作日）2.实时到账3.T+日期
way_arrival_value    Integer(10)                 0          //到账时间
way_transition_type  Integer(10)                            //过渡页类型,1默认模版,2自定义
way_transition_effect String(65535)                          //过渡效果，多个图片地址逗号隔开
way_transition_time  Integer(10)                 1          //图片切换的统一间隔时间
way_loding_type      Integer(10)                            //加载样式1.进度条2.加载图案
progress_groove_color String(10)                             //进度条凹槽色值
progress_color       String(10)                             //进度条色值
loading_pic_type     String(2)                              //图案加载类型
show_words           String(100)                            //显示文字(如:加载中 , 请稍候...)
show_words_color     String(10)                             //文字色值
show_words_shadow_color String(10)                             //文字阴影色值
ending_show          Integer(10)                            //消失动画1.淡出2.从顶部滑动3.从左侧滑动4.从右侧滑动5.从底部滑动6.呈碎片散开
status               Integer(10)                 1          //通道状态,1启用,2不启用
is_delete            Integer(10)                 2          //是否删除,1是,2否
create_time          Date(19)                               //
update_time          Date(19)                               //
*/
public class Payv2PayWay implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Integer wayType;
	private	Long dicId;
	private	String wayName;
	private	String wayCompanyName;
	private	String merchantCode;
	private	String discoutRegion;
	private	Integer isTrustBank;
	private	String wayIcon;
	private	String waySlogan;
	private	Integer incomeType;
	private	Long wayId;
	private	String incomeNum;
	private	String incomeLine;
	private	String wayContacts;
	private	String wayPhone;
	private	String wayApkUrl;
	private	String wayApkMd5;
	private	String wayApkPackage;
	private	Integer wayApkSize;
	private	String wayIosUrl;
	private	Double wayRate;
	private	Integer wayArrivalType;
	private	Integer wayArrivalValue;
	private	Integer wayTransitionType;
	private	String wayTransitionEffect;
	private	Integer wayTransitionTime;
	private	Integer wayLodingType;
	private	String progressGrooveColor;
	private	String progressColor;
	private	String loadingPicType;
	private	String showWords;
	private	String showWordsColor;
	private	String showWordsShadowColor;
	private	Integer endingShow;
	private	Integer status;
	private	Integer isDelete;
	private	Date createTime;
	private	Date updateTime;

	private Double lastPayMoney;
	private Double discountMoney;
	private JSONArray jsonArray; //json字符

	private Double oneMaxMoney;//单笔限额,0表示无限制
	private Double maxMoney;//每天限额,0表示无限制
	private Long rateId;
	private Integer isShow = 1;	//是否显示  1是,2置灰
	private Double rateOneMaxMoney;//通道单笔限额,0表示无限制
	private Double rateMaxMoney;//通道当天总限额,0表示无限制
	
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
	* way_type  Integer(10)  1  //通道类型,1钱包,2第三方支付    
	*/
	public Integer getWayType(){
		return wayType;
	}
	
	/**
	* way_type  Integer(10)  1  //通道类型,1钱包,2第三方支付    
	*/
	public void setWayType(Integer wayType){
		this.wayType = wayType;
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
	* way_name  String(50)  //通道名称    
	*/
	public String getWayName(){
		return wayName;
	}
	
	/**
	* way_name  String(50)  //通道名称    
	*/
	public void setWayName(String wayName){
		this.wayName = wayName;
	}
	
	/**
	* way_company_name  String(50)  //所属合作机构或钱包所属银行    
	*/
	public String getWayCompanyName(){
		return wayCompanyName;
	}
	
	/**
	* way_company_name  String(50)  //所属合作机构或钱包所属银行    
	*/
	public void setWayCompanyName(String wayCompanyName){
		this.wayCompanyName = wayCompanyName;
	}
	
	/**
	* merchant_code  String(64)  //合作身份id/商户号    
	*/
	public String getMerchantCode(){
		return merchantCode;
	}
	
	/**
	* merchant_code  String(64)  //合作身份id/商户号    
	*/
	public void setMerchantCode(String merchantCode){
		this.merchantCode = merchantCode;
	}
	
	/**
	* discout_region  String(255)  //优惠支付地区，存json数组，如[{"pro":1,"city":2},{"pro":3,"city":4}]    
	*/
	public String getDiscoutRegion(){
		return discoutRegion;
	}
	
	/**
	* discout_region  String(255)  //优惠支付地区，存json数组，如[{"pro":1,"city":2},{"pro":3,"city":4}]    
	*/
	public void setDiscoutRegion(String discoutRegion){
		this.discoutRegion = discoutRegion;
	}
	
	/**
	* is_trust_bank  Integer(10)  1  //是否为资金托管银行1.是2.否    
	*/
	public Integer getIsTrustBank(){
		return isTrustBank;
	}
	
	/**
	* is_trust_bank  Integer(10)  1  //是否为资金托管银行1.是2.否    
	*/
	public void setIsTrustBank(Integer isTrustBank){
		this.isTrustBank = isTrustBank;
	}
	
	/**
	* way_icon  String(255)  //icon图标地址    
	*/
	public String getWayIcon(){
		return wayIcon;
	}
	
	/**
	* way_icon  String(255)  //icon图标地址    
	*/
	public void setWayIcon(String wayIcon){
		this.wayIcon = wayIcon;
	}
	
	/**
	* way_slogan  String(50)  //推广语    
	*/
	public String getWaySlogan(){
		return waySlogan;
	}
	
	/**
	* way_slogan  String(50)  //推广语    
	*/
	public void setWaySlogan(String waySlogan){
		this.waySlogan = waySlogan;
	}
	
	/**
	* income_type  Integer(10)  1  //收单机构（1.本行2.他行）    
	*/
	public Integer getIncomeType(){
		return incomeType;
	}
	
	/**
	* income_type  Integer(10)  1  //收单机构（1.本行2.他行）    
	*/
	public void setIncomeType(Integer incomeType){
		this.incomeType = incomeType;
	}
	
	/**
	* way_id  Long(19)  //收单通道id    
	*/
	public Long getWayId(){
		return wayId;
	}
	
	/**
	* way_id  Long(19)  //收单通道id    
	*/
	public void setWayId(Long wayId){
		this.wayId = wayId;
	}
	
	/**
	* income_num  String(50)  //收单账号    
	*/
	public String getIncomeNum(){
		return incomeNum;
	}
	
	/**
	* income_num  String(50)  //收单账号    
	*/
	public void setIncomeNum(String incomeNum){
		this.incomeNum = incomeNum;
	}
	
	/**
	* income_line  String(255)  //线下第三方收单，存json数组，如[{"wayId":1,"userName":"莫凡","userNum":"1654553785@qq.com"},{"wayId":2,"userName":"_fjhkasg","userNum":"adlasdsfgsdg"}]    
	*/
	public String getIncomeLine(){
		return incomeLine;
	}
	
	/**
	* income_line  String(255)  //线下第三方收单，存json数组，如[{"wayId":1,"userName":"莫凡","userNum":"1654553785@qq.com"},{"wayId":2,"userName":"_fjhkasg","userNum":"adlasdsfgsdg"}]    
	*/
	public void setIncomeLine(String incomeLine){
		this.incomeLine = incomeLine;
	}
	
	/**
	* way_contacts  String(50)  //联系人    
	*/
	public String getWayContacts(){
		return wayContacts;
	}
	
	/**
	* way_contacts  String(50)  //联系人    
	*/
	public void setWayContacts(String wayContacts){
		this.wayContacts = wayContacts;
	}
	
	/**
	* way_phone  String(50)  //联系方式    
	*/
	public String getWayPhone(){
		return wayPhone;
	}
	
	/**
	* way_phone  String(50)  //联系方式    
	*/
	public void setWayPhone(String wayPhone){
		this.wayPhone = wayPhone;
	}
	
	/**
	* way_apk_url  String(255)  //安装apk地址    
	*/
	public String getWayApkUrl(){
		return wayApkUrl;
	}
	
	/**
	* way_apk_url  String(255)  //安装apk地址    
	*/
	public void setWayApkUrl(String wayApkUrl){
		this.wayApkUrl = wayApkUrl;
	}
	
	/**
	* way_apk_md5  String(255)  //apk的md5值    
	*/
	public String getWayApkMd5(){
		return wayApkMd5;
	}
	
	/**
	* way_apk_md5  String(255)  //apk的md5值    
	*/
	public void setWayApkMd5(String wayApkMd5){
		this.wayApkMd5 = wayApkMd5;
	}
	
	/**
	* way_apk_package  String(50)  //apk包名    
	*/
	public String getWayApkPackage(){
		return wayApkPackage;
	}
	
	/**
	* way_apk_package  String(50)  //apk包名    
	*/
	public void setWayApkPackage(String wayApkPackage){
		this.wayApkPackage = wayApkPackage;
	}
	
	/**
	* way_apk_size  Integer(10)  //apk文件大小    
	*/
	public Integer getWayApkSize(){
		return wayApkSize;
	}
	
	/**
	* way_apk_size  Integer(10)  //apk文件大小    
	*/
	public void setWayApkSize(Integer wayApkSize){
		this.wayApkSize = wayApkSize;
	}
	
	/**
	* way_ios_url  String(255)  //iosSDK地址    
	*/
	public String getWayIosUrl(){
		return wayIosUrl;
	}
	
	/**
	* way_ios_url  String(255)  //iosSDK地址    
	*/
	public void setWayIosUrl(String wayIosUrl){
		this.wayIosUrl = wayIosUrl;
	}
	
	/**
	* way_rate  Double(5,2)  0.00  //支付费率，单位千分号    
	*/
	public Double getWayRate(){
		return wayRate;
	}
	
	/**
	* way_rate  Double(5,2)  0.00  //支付费率，单位千分号    
	*/
	public void setWayRate(Double wayRate){
		this.wayRate = wayRate;
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
	* way_transition_type  Integer(10)  //过渡页类型,1默认模版,2自定义    
	*/
	public Integer getWayTransitionType(){
		return wayTransitionType;
	}
	
	/**
	* way_transition_type  Integer(10)  //过渡页类型,1默认模版,2自定义    
	*/
	public void setWayTransitionType(Integer wayTransitionType){
		this.wayTransitionType = wayTransitionType;
	}
	
	/**
	* way_transition_effect  String(65535)  //过渡效果，多个图片地址逗号隔开    
	*/
	public String getWayTransitionEffect(){
		return wayTransitionEffect;
	}
	
	/**
	* way_transition_effect  String(65535)  //过渡效果，多个图片地址逗号隔开    
	*/
	public void setWayTransitionEffect(String wayTransitionEffect){
		this.wayTransitionEffect = wayTransitionEffect;
	}
	
	/**
	* way_transition_time  Integer(10)  1  //图片切换的统一间隔时间    
	*/
	public Integer getWayTransitionTime(){
		return wayTransitionTime;
	}
	
	/**
	* way_transition_time  Integer(10)  1  //图片切换的统一间隔时间    
	*/
	public void setWayTransitionTime(Integer wayTransitionTime){
		this.wayTransitionTime = wayTransitionTime;
	}
	
	/**
	* way_loding_type  Integer(10)  //加载样式1.进度条2.加载图案    
	*/
	public Integer getWayLodingType(){
		return wayLodingType;
	}
	
	/**
	* way_loding_type  Integer(10)  //加载样式1.进度条2.加载图案    
	*/
	public void setWayLodingType(Integer wayLodingType){
		this.wayLodingType = wayLodingType;
	}
	
	/**
	* progress_groove_color  String(10)  //进度条凹槽色值    
	*/
	public String getProgressGrooveColor(){
		return progressGrooveColor;
	}
	
	/**
	* progress_groove_color  String(10)  //进度条凹槽色值    
	*/
	public void setProgressGrooveColor(String progressGrooveColor){
		this.progressGrooveColor = progressGrooveColor;
	}
	
	/**
	* progress_color  String(10)  //进度条色值    
	*/
	public String getProgressColor(){
		return progressColor;
	}
	
	/**
	* progress_color  String(10)  //进度条色值    
	*/
	public void setProgressColor(String progressColor){
		this.progressColor = progressColor;
	}
	
	/**
	* loading_pic_type  String(2)  //图案加载类型    
	*/
	public String getLoadingPicType(){
		return loadingPicType;
	}
	
	/**
	* loading_pic_type  String(2)  //图案加载类型    
	*/
	public void setLoadingPicType(String loadingPicType){
		this.loadingPicType = loadingPicType;
	}
	
	/**
	* show_words  String(100)  //显示文字(如:加载中  ,  请稍候...)    
	*/
	public String getShowWords(){
		return showWords;
	}
	
	/**
	* show_words  String(100)  //显示文字(如:加载中  ,  请稍候...)    
	*/
	public void setShowWords(String showWords){
		this.showWords = showWords;
	}
	
	/**
	* show_words_color  String(10)  //文字色值    
	*/
	public String getShowWordsColor(){
		return showWordsColor;
	}
	
	/**
	* show_words_color  String(10)  //文字色值    
	*/
	public void setShowWordsColor(String showWordsColor){
		this.showWordsColor = showWordsColor;
	}
	
	/**
	* show_words_shadow_color  String(10)  //文字阴影色值    
	*/
	public String getShowWordsShadowColor(){
		return showWordsShadowColor;
	}
	
	/**
	* show_words_shadow_color  String(10)  //文字阴影色值    
	*/
	public void setShowWordsShadowColor(String showWordsShadowColor){
		this.showWordsShadowColor = showWordsShadowColor;
	}
	
	/**
	* ending_show  Integer(10)  //消失动画1.淡出2.从顶部滑动3.从左侧滑动4.从右侧滑动5.从底部滑动6.呈碎片散开    
	*/
	public Integer getEndingShow(){
		return endingShow;
	}
	
	/**
	* ending_show  Integer(10)  //消失动画1.淡出2.从顶部滑动3.从左侧滑动4.从右侧滑动5.从底部滑动6.呈碎片散开    
	*/
	public void setEndingShow(Integer endingShow){
		this.endingShow = endingShow;
	}
	
	/**
	* status  Integer(10)  1  //通道状态,1启用,2不启用    
	*/
	public Integer getStatus(){
		return status;
	}
	
	/**
	* status  Integer(10)  1  //通道状态,1启用,2不启用    
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

	public Double getLastPayMoney() {
		return lastPayMoney;
	}

	public void setLastPayMoney(Double lastPayMoney) {
		this.lastPayMoney = lastPayMoney;
	}

	public Double getDiscountMoney() {
		return discountMoney;
	}

	public void setDiscountMoney(Double discountMoney) {
		this.discountMoney = discountMoney;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
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

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
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

}