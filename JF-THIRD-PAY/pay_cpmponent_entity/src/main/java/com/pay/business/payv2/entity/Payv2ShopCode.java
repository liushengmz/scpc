package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_shop_code          
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
shop_id              Long(19)                               //门店id,关联payv2_buss_company_shop表
code_name            String(50)                             //收款码名称
terminal_code        String(50)                             //终端号
tags_msg             String(50)                             //标签信息
shop_two_code_url    String(255)                            //二维码地址
create_time          Date(19)                               //创建时间
*/
public class Payv2ShopCode implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long shopId;
	private	String codeName;
	private	String terminalCode;
	private	String tagsMsg;
	private	String shopTwoCodeUrl;
	private	Date createTime;
	
	private String terminalType;

	private String shopName;//店铺名称
	
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
	* shop_id  Long(19)  //门店id,关联payv2_buss_company_shop表    
	*/
	public Long getShopId(){
		return shopId;
	}
	
	/**
	* shop_id  Long(19)  //门店id,关联payv2_buss_company_shop表    
	*/
	public void setShopId(Long shopId){
		this.shopId = shopId;
	}
	
	/**
	* code_name  String(50)  //收款码名称    
	*/
	public String getCodeName(){
		return codeName;
	}
	
	/**
	* code_name  String(50)  //收款码名称    
	*/
	public void setCodeName(String codeName){
		this.codeName = codeName;
	}
	
	/**
	* terminal_code  String(50)  //终端号    
	*/
	public String getTerminalCode(){
		return terminalCode;
	}
	
	/**
	* terminal_code  String(50)  //终端号    
	*/
	public void setTerminalCode(String terminalCode){
		this.terminalCode = terminalCode;
	}
	
	/**
	* tags_msg  String(50)  //标签信息    
	*/
	public String getTagsMsg(){
		return tagsMsg;
	}
	
	/**
	* tags_msg  String(50)  //标签信息    
	*/
	public void setTagsMsg(String tagsMsg){
		this.tagsMsg = tagsMsg;
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

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
}