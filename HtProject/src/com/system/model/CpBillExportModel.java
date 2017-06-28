package com.system.model;

public class CpBillExportModel
{
	
	private int billId;   //账单Id
	private int detailId; //详细账单Id
	private String billMonth; //账单月份
	private String jsName;    //结算类型
	private String startDate; //起始时间
	private String endDate;   //结束时间
	private String nickName;  //商务负责人
	private String cpFullNam; //CP全称
	private String productName; //业务线
	private String spTroneName; //业务名称
	private float amount;     //信息费
	private float rate; 	//结算率
	private float spTroneBillAmount; //帐单信息费
	private float reduceAmount; //核减金额
	private int reduceType;  //核减类型(0是信息费，1是结算款)
	private float actureAmount; //实际应收
	private float preBilling;  //帐单金额
	private String billingDate; //帐单日期
	private float kaipiaoAmount; //收票金额
	private String getbillDate; //收票日期
	private String applyPayBillDate; //申请付款日期
	private String payTime; //付款日期
	private float actureBilling; //付款金额
	private int status; //审核状态
	private String statusName;
	public int getBillId() {
		return billId;
	}
	public void setBillId(int billId) {
		this.billId = billId;
	}
	public int getDetailId() {
		return detailId;
	}
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getJsName() {
		return jsName;
	}
	public void setJsName(String jsName) {
		this.jsName = jsName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCpFullNam() {
		return cpFullNam;
	}
	public void setCpFullNam(String cpFullNam) {
		this.cpFullNam = cpFullNam;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSpTroneName() {
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName) {
		this.spTroneName = spTroneName;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getSpTroneBillAmount() {
		return spTroneBillAmount;
	}
	public void setSpTroneBillAmount(float spTroneBillAmount) {
		this.spTroneBillAmount = spTroneBillAmount;
	}
	public float getReduceAmount() {
		return reduceAmount;
	}
	public void setReduceAmount(float reduceAmount) {
		this.reduceAmount = reduceAmount;
	}
	public int getReduceType() {
		return reduceType;
	}
	public void setReduceType(int reduceType) {
		this.reduceType = reduceType;
	}
	public float getActureAmount() {
		return actureAmount;
	}
	public void setActureAmount(float actureAmount) {
		this.actureAmount = actureAmount;
	}
	public float getPreBilling() {
		return preBilling;
	}
	public void setPreBilling(float preBilling) {
		this.preBilling = preBilling;
	}
	public String getBillingDate() {
		return billingDate;
	}
	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}
	public float getKaipiaoAmount() {
		return kaipiaoAmount;
	}
	public void setKaipiaoAmount(float kaipiaoAmount) {
		this.kaipiaoAmount = kaipiaoAmount;
	}
	public String getGetbillDate() {
		return getbillDate;
	}
	public void setGetbillDate(String getbillDate) {
		this.getbillDate = getbillDate;
	}
	public String getApplyPayBillDate() {
		return applyPayBillDate;
	}
	public void setApplyPayBillDate(String applyPayBillDate) {
		this.applyPayBillDate = applyPayBillDate;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public float getActureBilling() {
		return actureBilling;
	}
	public void setActureBilling(float actureBilling) {
		this.actureBilling = actureBilling;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
	
	
	
}
