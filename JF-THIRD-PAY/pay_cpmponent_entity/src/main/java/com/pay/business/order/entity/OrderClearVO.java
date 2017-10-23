package com.pay.business.order.entity;

import java.io.Serializable;
import java.util.Date;

public class OrderClearVO implements Serializable {
	private static final long serialVersionUID = 1136609867545336207L;
	
	private Integer id;
	
	private String orderNum;
	
	private Double downmoney;
	
	/**
	 * 支付状态：1支付成功,2支付失败,3未支付,4超时,5.扣款成功回调失败
	 */
	private String downstatus;
	
	private Date downtime;
	
	private Double upmoney;
	
	/**
	 * 上游支付状态：上游通道支付状态1成功2失败（理论都是成功）||上游通道退款状态1成功2失败（理论都是成功）
	 */
	private String upstatus;
	
	private Date uptime;
	
	/**
	 * 类型1.交易2.退款
	 */
	private Integer type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Double getDownmoney() {
		return downmoney;
	}

	public void setDownmoney(Double downmoney) {
		this.downmoney = downmoney;
	}

	public String getDownstatus() {
		if(new Integer(1).equals(type)){
			if("1".equals(downstatus)){
				return "支付成功";
			}
			if("2".equals(downstatus)){
				return "支付失败";
			}
			if("3".equals(downstatus)){
				return "未支付";
			}
			if("4".equals(downstatus)){
				return "超时";
			}
			if("5".equals(downstatus)){
				return "扣款成功回调失败";
			}
		}else if (new Integer(2).equals(type)) {
			if("1".equals(downstatus)){
				return "退款成功";
			}
			if("2".equals(downstatus)){
				return "退款失败";
			}
		}
		
		return downstatus;
	}

	public void setDownstatus(String downstatus) {
		this.downstatus = downstatus;
	}

	public Date getDowntime() {
		return downtime;
	}

	public void setDowntime(Date downtime) {
		this.downtime = downtime;
	}

	public Double getUpmoney() {
		return upmoney;
	}

	public void setUpmoney(Double upmoney) {
		this.upmoney = upmoney;
	}

	public String getUpstatus() {
		if(new Integer(1).equals(type)){
			if("1".equals(upstatus)){
				return "支付成功";
			}
			if("2".equals(upstatus)){
				return "支付失败";
			}
		}
		if(new Integer(2).equals(type)){
			if("1".equals(upstatus)){
				return "退款成功";
			}
			if("2".equals(upstatus)){
				return "退款失败";
			}
		}
		return "未对账";
	}

	public void setUpstatus(String upstatus) {
		this.upstatus = upstatus;
	}

	public Date getUptime() {
		return uptime;
	}

	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
