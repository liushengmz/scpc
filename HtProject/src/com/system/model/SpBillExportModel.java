package com.system.model;

public class SpBillExportModel
{
	private int billId;   //账单Id
	private int detailId; //详细账单Id
	private String billMonth; //账单月份
	private String jsName;    //结算类型
	private String startDate; //起始时间
	private String endDate;   //结束时间
	private String nickName;  //商务负责人
	private String spFullNam; //SP全称
	private String productName; //业务线
	private String spTroneName; //业务名称
	private String amount;     //信息费
	private String rate; 	//结算率
	private String spTroneBillAmount; //帐单信息费
	private String reduceAmount; //核减金额
	private int reduceType;  //核减类型(0是信息费，1是结算款)
	private String actureAmount; //实际应收
	private String preBilling;  //票帐金额
	private String billingDate; //出账单日期
	private String kaipiaoAmount; //开票金额
	private String applyKaipiaoDate; //申请开票日期
	private String kaipiaoDate; //开票日期
	private String payTime; //到账日期
	private String actureBilling; //到帐金额
	private int status; //审核状态
	private String statusName;
	
	//后面ANDY加上去的,2016.10.27
	private String reduceDataAmount; //核减信息费
	private String reduceMoneyAmount; //核减结算款
	
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
	public String getSpFullNam() {
		return spFullNam;
	}
	public void setSpFullNam(String spFullNam) {
		this.spFullNam = spFullNam;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getSpTroneBillAmount() {
		return spTroneBillAmount;
	}
	public void setSpTroneBillAmount(String spTroneBillAmount) {
		this.spTroneBillAmount = spTroneBillAmount;
	}
	public String getReduceAmount() {
		return reduceAmount;
	}
	public void setReduceAmount(String reduceAmount) {
		this.reduceAmount = reduceAmount;
	}
	public int getReduceType() {
		return reduceType;
	}
	public void setReduceType(int reduceType) {
		this.reduceType = reduceType;
	}
	public String getActureAmount() {
		return actureAmount;
	}
	public void setActureAmount(String actureAmount) {
		this.actureAmount = actureAmount;
	}
	public String getPreBilling() {
		return preBilling;
	}
	public void setPreBilling(String preBilling) {
		this.preBilling = preBilling;
	}
	public String getBillingDate() {
		return billingDate;
	}
	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}
	public String getKaipiaoAmount() {
		return kaipiaoAmount;
	}
	public void setKaipiaoAmount(String kaipiaoAmount) {
		this.kaipiaoAmount = kaipiaoAmount;
	}
	public String getApplyKaipiaoDate() {
		return applyKaipiaoDate;
	}
	public void setApplyKaipiaoDate(String applyKaipiaoDate) {
		this.applyKaipiaoDate = applyKaipiaoDate;
	}
	public String getKaipiaoDate() {
		return kaipiaoDate;
	}
	public void setKaipiaoDate(String kaipiaoDate) {
		this.kaipiaoDate = kaipiaoDate;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getActureBilling() {
		return actureBilling;
	}
	public void setActureBilling(String actureBilling) {
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
	public String getReduceDataAmount()
	{
		return reduceDataAmount;
	}
	public void setReduceDataAmount(String reduceDataAmount)
	{
		this.reduceDataAmount = reduceDataAmount;
	}
	public String getReduceMoneyAmount()
	{
		return reduceMoneyAmount;
	}
	public void setReduceMoneyAmount(String reduceMoneyAmount)
	{
		this.reduceMoneyAmount = reduceMoneyAmount;
	}
	
	
	
}
