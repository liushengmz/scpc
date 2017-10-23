package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_shop_qrcode    
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
shop_id              Long(19)                               //店铺id
alipay_qrcode        String(255)                            //支付宝二维码链接
wechat_mch_id        String(50)                             //微信商户号
create_time          Date(10)                               //
*/
public class Payv2PayShopQrcode implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long shopId;
	private	String alipayQrcode;
	private	String wechatMchId;
	private	Date createTime;

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
	* shop_id  Long(19)  //店铺id    
	*/
	public Long getShopId(){
		return shopId;
	}
	
	/**
	* shop_id  Long(19)  //店铺id    
	*/
	public void setShopId(Long shopId){
		this.shopId = shopId;
	}
	
	/**
	* alipay_qrcode  String(255)  //支付宝二维码链接    
	*/
	public String getAlipayQrcode(){
		return alipayQrcode;
	}
	
	/**
	* alipay_qrcode  String(255)  //支付宝二维码链接    
	*/
	public void setAlipayQrcode(String alipayQrcode){
		this.alipayQrcode = alipayQrcode;
	}
	
	/**
	* create_time  Date(10)  //    
	*/
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	* create_time  Date(10)  //    
	*/
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getWechatMchId() {
		return wechatMchId;
	}

	public void setWechatMchId(String wechatMchId) {
		this.wechatMchId = wechatMchId;
	}
	
}