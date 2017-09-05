package com.pay.business.sysconfig.entity;

import java.io.Serializable;



/**
TABLE:.sys_config_dictionary    
--------------------------------------------------------
id                   Integer(10)        NOTNULL             //
dict_pvalue          Integer(10)        NOTNULL             //父级id
dict_name            String(255)        NOTNULL             //字典名称
dict_value           String(255)        NOTNULL             //字典值
curr_type            Integer(10)        NOTNULL  1          //0失效 1正常
*/
public class SysConfigDictionary implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2146878709354545574L;
	private	Integer id;
	private	Integer dictPvalue;
	private	String dictName;
	private	String dictValue;
	private	Integer currType;

	/**
	* id  Integer(10)  NOTNULL  //    
	*/
	public Integer getId(){
		return id;
	}
	
	/**
	* id  Integer(10)  NOTNULL  //    
	*/
	public void setId(Integer id){
		this.id = id;
	}
	
	/**
	* dict_pvalue  Integer(10)  NOTNULL  //父级id    
	*/
	public Integer getDictPvalue(){
		return dictPvalue;
	}
	
	/**
	* dict_pvalue  Integer(10)  NOTNULL  //父级id    
	*/
	public void setDictPvalue(Integer dictPvalue){
		this.dictPvalue = dictPvalue;
	}
	
	/**
	* dict_name  String(255)  NOTNULL  //字典名称    
	*/
	public String getDictName(){
		return dictName;
	}
	
	/**
	* dict_name  String(255)  NOTNULL  //字典名称    
	*/
	public void setDictName(String dictName){
		this.dictName = dictName;
	}
	
	/**
	* dict_value  String(255)  NOTNULL  //字典值    
	*/
	public String getDictValue(){
		return dictValue;
	}
	
	/**
	* dict_value  String(255)  NOTNULL  //字典值    
	*/
	public void setDictValue(String dictValue){
		this.dictValue = dictValue;
	}
	
	/**
	* curr_type  Integer(10)  NOTNULL  0  //1失效  0正常    
	*/
	public Integer getCurrType(){
		return currType;
	}
	
	/**
	* curr_type  Integer(10)  NOTNULL 01  //1失效  0正常    
	*/
	public void setCurrType(Integer currType){
		this.currType = currType;
	}
	
}