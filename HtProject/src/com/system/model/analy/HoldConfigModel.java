package com.system.model.analy;

public class HoldConfigModel 
{
	private int id;
	private String feeDate;
	private int cpId;
	private float totalAmount;
	private int holdType;
	private int holdPersent;
	private float synAmount;
	private float actureSynAmount;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(String feeDate) {
		this.feeDate = feeDate;
	}
	public int getCpId() {
		return cpId;
	}
	public void setCpId(int cpId) {
		this.cpId = cpId;
	}
	public float getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getHoldType() {
		return holdType;
	}
	public void setHoldType(int holdType) {
		this.holdType = holdType;
	}
	public int getHoldPersent() {
		return holdPersent;
	}
	public void setHoldPersent(int holdPersent) {
		this.holdPersent = holdPersent;
	}
	public float getSynAmount() {
		return synAmount;
	}
	public void setSynAmount(float synAmount) {
		this.synAmount = synAmount;
	}
	public float getActureSynAmount() {
		return actureSynAmount;
	}
	public void setActureSynAmount(float actureSynAmount) {
		this.actureSynAmount = actureSynAmount;
	}
	
	
	
}
