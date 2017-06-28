package com.system.model.analy;

public class MrSummerModel 
{
	private String feeDate;
	private int spId;
	private int cpId;
	private int troneId;
	private int troneOrderId;
	private String mcc;
	private int provinceId;
	private int cityId;
	private int dataRows;
	
	public String getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(String feeDate) {
		this.feeDate = feeDate;
	}
	public int getSpId() {
		return spId;
	}
	public void setSpId(int spId) {
		this.spId = spId;
	}
	public int getCpId() {
		return cpId;
	}
	public void setCpId(int cpId) {
		this.cpId = cpId;
	}
	public int getTroneId() {
		return troneId;
	}
	public void setTroneId(int troneId) {
		this.troneId = troneId;
	}
	public int getTroneOrderId() {
		return troneOrderId;
	}
	public void setTroneOrderId(int troneOrderId) {
		this.troneOrderId = troneOrderId;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getDataRows() {
		return dataRows;
	}
	public void setDataRows(int dataRows) {
		this.dataRows = dataRows;
	}
	
	@Override
	public String toString() {		
		return feeDate + "-" + spId + "-" +cpId + "-" + troneId + "-" + troneOrderId + "-" + mcc + "-" + provinceId + "-" + cityId + "-" + dataRows;
	}
	
}
