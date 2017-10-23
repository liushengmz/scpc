package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;

/**
TABLE:.payv2_app_type           
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
type_name            String(20)                             //名称
*/
public class Payv2AppType implements Serializable {
	private static final long serialVersionUID = 1L;
	private	Long id;
	private	String typeName;

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
	* type_name  String(20)  //名称    
	*/
	public String getTypeName(){
		return typeName;
	}
	
	/**
	* type_name  String(20)  //名称    
	*/
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}
	
}