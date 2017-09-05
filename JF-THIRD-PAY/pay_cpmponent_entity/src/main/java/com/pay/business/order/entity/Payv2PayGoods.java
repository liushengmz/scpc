package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_pay_goods          
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
app_id               Long(19)                               //应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
type                 Integer(10)                            //类型1.线上2线下
goods_name           String(255)                            //商品名称
create_time          Date(19)                               //创建时间
*/
public class Payv2PayGoods implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long appId;
	private	Integer type;
	private	String goodsName;
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
	* type  Integer(10)  //类型1.线上2线下    
	*/
	public Integer getType(){
		return type;
	}
	
	/**
	* type  Integer(10)  //类型1.线上2线下    
	*/
	public void setType(Integer type){
		this.type = type;
	}
	
	/**
	* goods_name  String(255)  //商品名称    
	*/
	public String getGoodsName(){
		return goodsName;
	}
	
	/**
	* goods_name  String(255)  //商品名称    
	*/
	public void setGoodsName(String goodsName){
		this.goodsName = goodsName;
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
	
}