package com.system.sdk.model;

public class SdkDataSummerModel
{
	private String title;//标题
	private int userRows;//激活用户
	private int activityRows;//活跃用户
	private int troneRequestRows;//通道请求数
	private int troneEffectRequestRows;//有效通道请求数
	private float effectAmount;//理论计费金额
	private int troneOrderRows;//指令成功数
	private int msgRows;//短息成功数
	private int sucRows;//计费成功数
	private float amount;//计费金额
	
	public int getUserRows()
	{
		return userRows;
	}
	public void setUserRows(int userRows)
	{
		this.userRows = userRows;
	}
	public int getActivityRows()
	{
		return activityRows;
	}
	public void setActivityRows(int activityRows)
	{
		this.activityRows = activityRows;
	}
	public int getTroneRequestRows()
	{
		return troneRequestRows;
	}
	public void setTroneRequestRows(int troneRequestRows)
	{
		this.troneRequestRows = troneRequestRows;
	}
	public int getTroneEffectRequestRows()
	{
		return troneEffectRequestRows;
	}
	public void setTroneEffectRequestRows(int troneEffectRequestRows)
	{
		this.troneEffectRequestRows = troneEffectRequestRows;
	}
	public int getTroneOrderRows()
	{
		return troneOrderRows;
	}
	public void setTroneOrderRows(int troneOrderRows)
	{
		this.troneOrderRows = troneOrderRows;
	}
	public int getMsgRows()
	{
		return msgRows;
	}
	public void setMsgRows(int msgRows)
	{
		this.msgRows = msgRows;
	}
	public int getSucRows()
	{
		return sucRows;
	}
	public void setSucRows(int sucRows)
	{
		this.sucRows = sucRows;
	}
	public float getAmount()
	{
		return amount;
	}
	public void setAmount(float amount)
	{
		this.amount = amount;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public float getEffectAmount() {
		return effectAmount;
	}
	public void setEffectAmount(float effectAmount) {
		this.effectAmount = effectAmount;
	}
	
	
}
