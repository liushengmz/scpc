package com.pay.business.merchant.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.pay.business.payway.entity.Payv2PayWayRate;

/**
TABLE:.payv2_channel_rate       
--------------------------------------------------------
id                   Long(19)           NOTNULL             //
channel_way_id       Long(19)                               //关联payv2_channel_way表
rate_id              Long(19)                               //通道id,关联payv2_pay_way_rate表
pay_way_rate         Double(16,2)                0.00       //通道费
create_time          Date(19)                               //创建时间
*/
public class Payv2ChannelRate implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private	Long id;
	private	Long channelWayId;
	private	Long rateId;
	private	Double payWayRate;
	private	Date createTime;
	
	private Long payWayId;
	private List<Payv2PayWayRate> rateList;
	
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
	* channel_way_id  Long(19)  //关联payv2_channel_way表    
	*/
	public Long getChannelWayId(){
		return channelWayId;
	}
	
	/**
	* channel_way_id  Long(19)  //关联payv2_channel_way表    
	*/
	public void setChannelWayId(Long channelWayId){
		this.channelWayId = channelWayId;
	}
	
	/**
	* rate_id  Long(19)  //通道id,关联payv2_pay_way_rate表    
	*/
	public Long getRateId(){
		return rateId;
	}
	
	/**
	* rate_id  Long(19)  //通道id,关联payv2_pay_way_rate表    
	*/
	public void setRateId(Long rateId){
		this.rateId = rateId;
	}
	
	/**
	* pay_way_rate  Double(16,2)  0.00  //通道费    
	*/
	public Double getPayWayRate(){
		return payWayRate;
	}
	
	/**
	* pay_way_rate  Double(16,2)  0.00  //通道费    
	*/
	public void setPayWayRate(Double payWayRate){
		this.payWayRate = payWayRate;
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

	public Long getPayWayId() {
		return payWayId;
	}

	public void setPayWayId(Long payWayId) {
		this.payWayId = payWayId;
	}

	public List<Payv2PayWayRate> getRateList() {
		return rateList;
	}

	public void setRateList(List<Payv2PayWayRate> rateList) {
		this.rateList = rateList;
	}
	
}