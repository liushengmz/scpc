
package com.system.model;

import com.system.util.StringUtil;

/**
 * 存储进REDIS的对象
 * @author Andy.Chen
 *
 */
public class RedisCpSingleOrderModel
{

	public static final String	MAP_KEY_TEMP_TABLE_ID	= "TEMP_TABLE_ID";
	public static final String	MAP_KEY_MONTH_NAME		= "MONTH_NAME";
	public static final String	MAP_KEY_MONTH_TABLE_ID	= "MONTH_TABLE_ID";
	public static final String	MAP_KEY_CP_ID			= "CP_ID";
	public static final String	MAP_KEY_CLIENT_ORDER_ID	= "CP_ORDER_ID";
	public static final String	MAP_KEY_FLOW_SIZE		= "FLOW_SIZE";
	public static final String	MAP_KEY_OPERATOR		= "OPERATOR";
	public static final String	MAP_KEY_MOBILE			= "MOBILE";
	public static final String	MAP_KEY_RANG			= "RANG";
	public static final String	MAP_KEY_TIME_TYPE		= "TIME_TYPE";
	public static final String	MAP_KEY_CP_TRONE_ID		= "CP_TRONE_ID";
	public static final String	MAP_KEY_TRONE_ID		= "TRONE_ID";
	public static final String	MAP_KEY_CP_RATIO		= "CP_RATIO";
	public static final String	MAP_KEY_SP_RATIO		= "SP_RATIO";
	public static final String	MAP_KEY_SP_TRONE_ID		= "SP_TRONE_ID";
	public static final String	MAP_KEY_SP_ID			= "SP_ID";
	public static final String  MAP_KEY_SP_API_ID		= "SP_API_ID";
	public static final String	MAP_KEY_SEND_SMS		= "SEND_SMS";
	public static final String	MAP_KEY_PRICE			= "PRICE";
	public static final String	MAP_KEY_SERVER_ORDER_ID	= "SP_ORDER_ID";
	public static final String	MAP_KEY_BASE_PRICE_ID	= "BASE_PRICE_ID";
	public static final String	MAP_KEY_SP_STATUS		= "SP_STATUS";
	public static final String	MAP_KEY_SP_ERROR_MSG	= "SP_ERROR_MSG";
	public static final String	MAP_KEY_STATUS			= "STATUS";
	public static final String 	MAP_KEY_CREATE_DATE		= "CREATE_DATE";
	public static final String	MAP_KEY_NOTIFY_URL		= "NOTIFY_URL";
	public static final String	MAP_KEY_CREATE_MILS		= "CREATE_MILS";
	public static final String	MAP_KEY_NOTIFY_STATUS	= "NOTIFY_STATUS";
	public static final String	MAP_KEY_NOTIFY_TIMES	= "NOTIFY_TIMES";
	public static final String	MAP_KEY_LAST_NOTIFY_MILS	= "LAST_NOTIFY_MILS";

	private int					tempTableId;
	private String				monthName;
	private int					monthTableId;
	private int					cpId;
	private String				clientOrderId;
	private int					flowSize;
	private int					operator;
	private String				mobile;
	private int					rang;
	private int					timeType;
	private int					cpTroneId;
	private int					troneId;
	private int					spTroneId;
	private int					spId;
	private int 				spApiId;
	private int					sendSms;
	private int					price;
	private int					cpRatio;
	private int					spRatio;
	private String				serverOrderId;
	private int					basePriceId;
	private String				spStatus;
	private String				spErrorMsg;
	private int					status;
	private String				createDate;
	private long 				createMils;
	private String				notifyUrl;
	private int					notifyStatus;
	private int					notifyTimes;
	private int					lastNotifyMils;
	

	public int getTempTableId()
	{
		return tempTableId;
	}

	public String getMonthName()
	{
		return monthName;
	}

	public int getMonthTableId()
	{
		return monthTableId;
	}

	public int getCpId()
	{
		return cpId;
	}

	public String getClientOrderId()
	{
		return clientOrderId;
	}

	public int getFlowSize()
	{
		return flowSize;
	}

	public int getOperator()
	{
		return operator;
	}

	public String getMobile()
	{
		return mobile;
	}

	public int getRang()
	{
		return rang;
	}

	public int getTimeType()
	{
		return timeType;
	}

	public int getCpTroneId()
	{
		return cpTroneId;
	}

	public int getTroneId()
	{
		return troneId;
	}

	public int getSpTroneId()
	{
		return spTroneId;
	}

	public int getSpId()
	{
		return spId;
	}

	public int getSendSms()
	{
		return sendSms;
	}

	public int getPrice()
	{
		return price;
	}

	public int getCpRatio()
	{
		return cpRatio;
	}

	public int getSpRatio()
	{
		return spRatio;
	}

	public String getServerOrderId()
	{
		return serverOrderId;
	}

	public int getBasePriceId()
	{
		return basePriceId;
	}

	public String getSpStatus()
	{
		return spStatus;
	}

	public String getSpErrorMsg()
	{
		return spErrorMsg;
	}

	public void setTempTableId(int tempTableId)
	{
		this.tempTableId = tempTableId;
	}

	public void setMonthName(String monthName)
	{
		this.monthName = monthName;
	}

	public void setMonthTableId(int monthTableId)
	{
		this.monthTableId = monthTableId;
	}

	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}

	public void setClientOrderId(String clientOrderId)
	{
		this.clientOrderId = clientOrderId;
	}

	public void setFlowSize(int flowSize)
	{
		this.flowSize = flowSize;
	}

	public void setOperator(int operator)
	{
		this.operator = operator;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public void setRang(int rang)
	{
		this.rang = rang;
	}

	public void setTimeType(int timeType)
	{
		this.timeType = timeType;
	}

	public void setCpTroneId(int cpTroneId)
	{
		this.cpTroneId = cpTroneId;
	}

	public void setTroneId(int troneId)
	{
		this.troneId = troneId;
	}

	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}

	public void setSpId(int spId)
	{
		this.spId = spId;
	}

	public int getSpApiId()
	{
		return spApiId;
	}

	public void setSpApiId(int spApiId)
	{
		this.spApiId = spApiId;
	}

	public void setSendSms(int sendSms)
	{
		this.sendSms = sendSms;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public void setCpRatio(int cpRatio)
	{
		this.cpRatio = cpRatio;
	}

	public void setSpRatio(int spRatio)
	{
		this.spRatio = spRatio;
	}

	public void setServerOrderId(String serverOrderId)
	{
		this.serverOrderId = serverOrderId;
	}

	public void setBasePriceId(int basePriceId)
	{
		this.basePriceId = basePriceId;
	}

	public void setSpStatus(String spStatus)
	{
		this.spStatus = spStatus;
	}

	public void setSpErrorMsg(String spErrorMsg)
	{
		this.spErrorMsg = spErrorMsg;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public long getCreateMils()
	{
		return createMils;
	}

	public void setCreateMils(long createMils)
	{
		this.createMils = createMils;
	}

	public String getNotifyUrl()
	{
		return notifyUrl;
	}

	public int getNotifyStatus()
	{
		return notifyStatus;
	}

	public int getNotifyTimes()
	{
		return notifyTimes;
	}

	public void setNotifyUrl(String notifyUrl)
	{
		this.notifyUrl = notifyUrl;
	}

	public void setNotifyStatus(int notifyStatus)
	{
		this.notifyStatus = notifyStatus;
	}

	public void setNotifyTimes(int notifyTimes)
	{
		this.notifyTimes = notifyTimes;
	}

	public int getLastNotifyMils()
	{
		return lastNotifyMils;
	}

	public void setLastNotifyMils(int lastNotifyMils)
	{
		this.lastNotifyMils = lastNotifyMils;
	}

	// 原始 KEY用 CP_SINGLE_CPID_CLIENT_ORDER_ID
	public static void main(String[] args)
	{
		String clientOrder = 1 + "--" + System.currentTimeMillis();
		System.out.println(clientOrder);
		System.out.println(StringUtil.getMd5String(clientOrder, 16));
	}
}
