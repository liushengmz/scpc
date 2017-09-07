package com.system.server;

import com.system.dao.CpTroneDao;

public class CpServer
{
	/**
	 * 获取CP对应的充值余额
	 * @param cpId
	 * @return
	 */
	public static int getCpRemainingSum(int cpId)
	{
		return 0;
	}
	
	/**
	 * 增加自动
	 * @param cpId
	 * @param troneId
	 * @param ratio
	 * @return
	 */
	public static int addCpTrone(int cpId,int troneId,int ratio)
	{
		return new CpTroneDao().addCpTrone(cpId, troneId, ratio);
	}
}
