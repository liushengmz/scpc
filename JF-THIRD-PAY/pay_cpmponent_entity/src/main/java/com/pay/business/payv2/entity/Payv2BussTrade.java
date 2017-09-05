package com.pay.business.payv2.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
TABLE:.payv2_buss_trade         
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
parent_id            Long(19)                               //行业父节点
trade_name           String(20)                             //行业类型名称
*/
public class Payv2BussTrade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long parentId;
	private	String tradeName;
	private List<Payv2BussTrade> list;

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
	* parent_id  Long(19)  //行业父节点    
	*/
	public Long getParentId(){
		return parentId;
	}
	
	/**
	* parent_id  Long(19)  //行业父节点    
	*/
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}
	
	/**
	* trade_name  String(20)  //行业类型名称    
	*/
	public String getTradeName(){
		return tradeName;
	}
	
	/**
	* trade_name  String(20)  //行业类型名称    
	*/
	public void setTradeName(String tradeName){
		this.tradeName = tradeName;
	}

	public List<Payv2BussTrade> getList() {
		return list;
	}

	public void setList(List<Payv2BussTrade> list) {
		this.list = list;
	}
	
}