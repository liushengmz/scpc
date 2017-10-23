package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.sys_config_sms           
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
user_id              Long(19)                               //执行操作的用户id
sms_type             Integer(10)        NOTNULL  2          //类型 (1注册 2绑定手机 3找回支付密码 4提现 5切换账户 6设置密码)
phone_number         String(20)         NOTNULL             //接收短信的手机号
sms_vercode          String(20)                             //该条短信的验证码
sms_contents         String(255)                            //短信的内容
sms_code             String(100)                            //短信的唯一标识符
is_verification      Integer(10)                 0          //验证码是否已经验证 （0-未验证；1-已经验证；2-过期）
sms_effectiveTime    Integer(10)                 0          //短信有效时间（一般验证码验证使用，默认单位 秒）
create_time          Date(19)           NOTNULL  0000-00-00 00:00:00 //创建时间
last_update_time     Date(19)                               //最后一次修改时间
*/
public class SysConfigSms implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long userId;
	private	Integer smsType;
	private	String phoneNumber;
	private	String smsVercode;
	private	String smsContents;
	private	String smsCode;
	private	Integer isVerification;
	private	Integer smsEffectiveTime;
	private	Date createTime;
	private	Date lastUpdateTime;
	public static final String REDIS_ERROR_SMS_KEY = "SysConfigSms_error_sms_%s_%s";
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
	* user_id  Long(19)  //执行操作的用户id    
	*/
	public Long getUserId(){
		return userId;
	}
	
	/**
	* user_id  Long(19)  //执行操作的用户id    
	*/
	public void setUserId(Long userId){
		this.userId = userId;
	}
	
	/**
	* sms_type  Integer(10)  NOTNULL  2  //类型  (1注册  2绑定手机  3找回支付密码  4提现  5切换账户  6设置密码)    
	*/
	public Integer getSmsType(){
		return smsType;
	}
	
	/**
	* sms_type  Integer(10)  NOTNULL  2  //类型  (1注册  2绑定手机  3找回支付密码  4提现  5切换账户  6设置密码)    
	*/
	public void setSmsType(Integer smsType){
		this.smsType = smsType;
	}
	
	/**
	* phone_number  String(20)  NOTNULL  //接收短信的手机号    
	*/
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	/**
	* phone_number  String(20)  NOTNULL  //接收短信的手机号    
	*/
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	/**
	* sms_vercode  String(20)  //该条短信的验证码    
	*/
	public String getSmsVercode(){
		return smsVercode;
	}
	
	/**
	* sms_vercode  String(20)  //该条短信的验证码    
	*/
	public void setSmsVercode(String smsVercode){
		this.smsVercode = smsVercode;
	}
	
	/**
	* sms_contents  String(255)  //短信的内容    
	*/
	public String getSmsContents(){
		return smsContents;
	}
	
	/**
	* sms_contents  String(255)  //短信的内容    
	*/
	public void setSmsContents(String smsContents){
		this.smsContents = smsContents;
	}
	
	/**
	* sms_code  String(100)  //短信的唯一标识符    
	*/
	public String getSmsCode(){
		return smsCode;
	}
	
	/**
	* sms_code  String(100)  //短信的唯一标识符    
	*/
	public void setSmsCode(String smsCode){
		this.smsCode = smsCode;
	}
	
	/**
	* is_verification  Integer(10)  0  //验证码是否已经验证  （0-未验证；1-已经验证；2-过期）    
	*/
	public Integer getIsVerification(){
		return isVerification;
	}
	
	/**
	* is_verification  Integer(10)  0  //验证码是否已经验证  （0-未验证；1-已经验证；2-过期）    
	*/
	public void setIsVerification(Integer isVerification){
		this.isVerification = isVerification;
	}
	
	/**
	* sms_effectiveTime  Integer(10)  0  //短信有效时间（一般验证码验证使用，默认单位  秒）    
	*/
	public Integer getSmsEffectiveTime(){
		return smsEffectiveTime;
	}
	
	/**
	* sms_effectiveTime  Integer(10)  0  //短信有效时间（一般验证码验证使用，默认单位  秒）    
	*/
	public void setSmsEffectiveTime(Integer smsEffectiveTime){
		this.smsEffectiveTime = smsEffectiveTime;
	}
	
	/**
	* create_time  Date(19)  NOTNULL  0000-00-00  00:00:00  //创建时间    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(19)  NOTNULL  0000-00-00  00:00:00  //创建时间    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	/**
	* last_update_time  Date(19)  //最后一次修改时间    
	*/
	public Date getLastUpdateTime(){
		return lastUpdateTime;
	}
	
	/**
	* last_update_time  Date(19)  //最后一次修改时间    
	*/
	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}
	
}