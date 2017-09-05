package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_provinces_city     
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
parent_id            Long(19)                    0          //上级ID
name                 String(20)                             //省市名称
*/
public class Payv2ProvincesCity implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	Long parentId;
	private	String name;

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
	* parent_id  Long(19)  0  //上级ID    
	*/
	public Long getParentId(){
		return parentId;
	}
	
	/**
	* parent_id  Long(19)  0  //上级ID    
	*/
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}
	
	/**
	* name  String(20)  //省市名称    
	*/
	public String getName(){
		return name;
	}
	
	/**
	* name  String(20)  //省市名称    
	*/
	public void setName(String name){
		this.name = name;
	}
	
}