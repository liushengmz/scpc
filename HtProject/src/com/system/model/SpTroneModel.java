package com.system.model;

public class SpTroneModel
{
	private int id;
	private int spId;
	private String spName;
	private String commerceUserName;
	private String spTroneName;
	private int operator;
	private String operatorName;
	private int jsTypes;
	private float jieSuanLv;
	private String provinces;
	private String provinceList;
	private int troneApiId;
	private String troneApiName;
	private int troneType;
	private int status;
	
	private float dayLimit;
	private float monthLimit;
	private float userDayLimit;
	private float userMonthLimit;
	private int serviceCodeId;
	private String servoceCodeName;
	
	//增加is_on_api字段，shield_start_hour屏蔽开始时间，shield_end_hour屏蔽结束时间
	private int apiStatus;//is_on_api字段
	private String shieldStart;//屏蔽开始时间
	private String shieldEnd;  //屏蔽结束时间
	private String remark;
	
	private int upDataType;
	private int limiteType;
	private String upDataName;
	//商务ID
	private int commerceUserId;
	
	//是否是导量数据，0为否，1为是
	private int isUnHoldData;
	
	//当走API的时候，并且判断手机号的地区时，可以选择是否强制拒绝还是交由上游去处理
	private int isForceHold;
	
	//增加是否监控数据
	private int isWatchData;
	
	public int getTroneType()
	{
		return troneType;
	}
	public void setTroneType(int troneType)
	{
		this.troneType = troneType;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getSpId()
	{
		return spId;
	}
	public void setSpId(int spId)
	{
		this.spId = spId;
	}
	public String getSpName()
	{
		return spName;
	}
	public void setSpName(String spName)
	{
		this.spName = spName;
	}
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public int getOperator()
	{
		return operator;
	}
	public void setOperator(int operator)
	{
		this.operator = operator;
	}
	public String getOperatorName()
	{
		return operatorName;
	}
	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}
	public float getJieSuanLv()
	{
		return jieSuanLv;
	}
	public void setJieSuanLv(float jieSuanLv)
	{
		this.jieSuanLv = jieSuanLv;
	}
	public String getProvinces()
	{
		return provinces;
	}
	public void setProvinces(String provinces)
	{
		this.provinces = provinces;
	}
	public int getTroneApiId()
	{
		return troneApiId;
	}
	public void setTroneApiId(int troneApiId)
	{
		this.troneApiId = troneApiId;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getTroneApiName()
	{
		return troneApiName;
	}
	public void setTroneApiName(String troneApiName)
	{
		this.troneApiName = troneApiName;
	}
	public String getCommerceUserName()
	{
		return commerceUserName;
	}
	public void setCommerceUserName(String commerceUserName)
	{
		this.commerceUserName = commerceUserName;
	}
	public float getDayLimit()
	{
		return dayLimit;
	}
	public void setDayLimit(float dayLimit)
	{
		this.dayLimit = dayLimit;
	}
	public float getMonthLimit()
	{
		return monthLimit;
	}
	public void setMonthLimit(float monthLimit)
	{
		this.monthLimit = monthLimit;
	}
	public float getUserDayLimit()
	{
		return userDayLimit;
	}
	public void setUserDayLimit(float userDayLimit)
	{
		this.userDayLimit = userDayLimit;
	}
	public float getUserMonthLimit()
	{
		return userMonthLimit;
	}
	public void setUserMonthLimit(float userMonthLimit)
	{
		this.userMonthLimit = userMonthLimit;
	}
	public int getServiceCodeId()
	{
		return serviceCodeId;
	}
	public void setServiceCodeId(int serviceCodeId)
	{
		this.serviceCodeId = serviceCodeId;
	}
	public String getServoceCodeName()
	{
		return servoceCodeName;
	}
	public void setServoceCodeName(String servoceCodeName)
	{
		this.servoceCodeName = servoceCodeName;
	}
	public int getJsTypes()
	{
		return jsTypes;
	}
	public void setJsTypes(int jsTypes)
	{
		this.jsTypes = jsTypes;
	}
	public int getApiStatus() {
		return apiStatus;
	}
	public void setApiStatus(int apiStatus) {
		this.apiStatus = apiStatus;
	}
	public String getShieldStart() {
		return shieldStart;
	}
	public void setShieldStart(String shieldStart) {
		this.shieldStart = shieldStart;
	}
	public String getShieldEnd() {
		return shieldEnd;
	}
	public void setShieldEnd(String shieldEnd) {
		this.shieldEnd = shieldEnd;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getUpDataType() {
		return upDataType;
	}
	public void setUpDataType(int upDataType) {
		this.upDataType = upDataType;
	}
	public int getLimiteType() {
		return limiteType;
	}
	public void setLimiteType(int limiteType) {
		this.limiteType = limiteType;
	}
	public String getUpDataName() {
		return upDataName;
	}
	public void setUpDataName(String upDataName) {
		this.upDataName = upDataName;
	}
	public int getCommerceUserId() {
		return commerceUserId;
	}
	public void setCommerceUserId(int commerceUserId) {
		this.commerceUserId = commerceUserId;
	}
	public int getIsUnHoldData()
	{
		return isUnHoldData;
	}
	public void setIsUnHoldData(int isUnHoldData)
	{
		this.isUnHoldData = isUnHoldData;
	}
	public int getIsForceHold()
	{
		return isForceHold;
	}
	public void setIsForceHold(int isForceHold)
	{
		this.isForceHold = isForceHold;
	}
	public String getProvinceList()
	{
		return provinceList;
	}
	public void setProvinceList(String provinceList)
	{
		this.provinceList = provinceList;
	}
	public int getIsWatchData()
	{
		return isWatchData;
	}
	public void setIsWatchData(int isWatchData)
	{
		this.isWatchData = isWatchData;
	}
	
	
}
