package com.pay.business.util.pinganbank.pay;


public class PANOrderVo {
	private String ord_mct_id;	//true		20	商户流水号
	private String ord_type;	//true		1	订单类型（1交易，2辙单【退款】）
	private String add_time;	//true	datetime		下单时间
	private String trade_amount;//	true	int	20	实际交易金额（以分为单位，没有小数点）
	private String trade_no;//	true	string	50	收单机构返回的交易号（某些收单机构没有交易号）
	private String trade_pay_time	;//订单付款成功时间（支付方式返回的原始数据，可能会小于下单时间）
	private String original_amount;//	true	int	20	原始金额（以分为单位，没有小数点）
	private String out_no;//true	string	20	开发者流水号
	private String status;//	ture	string	1	订单状态（1交易成功，2待支付，9待输入密码，4已取消）
	
	public String getOrd_mct_id() {
		return ord_mct_id;
	}
	public void setOrd_mct_id(String ord_mct_id) {
		this.ord_mct_id = ord_mct_id;
	}
	public String getOrd_type() {
		return ord_type;
	}
	public void setOrd_type(String ord_type) {
		this.ord_type = ord_type;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getTrade_amount() {
		return trade_amount;
	}
	public void setTrade_amount(String trade_amount) {
		this.trade_amount = trade_amount;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getTrade_pay_time() {
		return trade_pay_time;
	}
	public void setTrade_pay_time(String trade_pay_time) {
		this.trade_pay_time = trade_pay_time;
	}
	public String getOriginal_amount() {
		return original_amount;
	}
	public void setOriginal_amount(String original_amount) {
		this.original_amount = original_amount;
	}
	public String getOut_no() {
		return out_no;
	}
	public void setOut_no(String out_no) {
		this.out_no = out_no;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
