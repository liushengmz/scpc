
package com.system.server;

import com.system.dao.LvUserDao;
import com.system.model.LvUserModel;

public class LvUserServer
{
	public LvUserModel getUserByImsi(String imei)
	{
		LvUserDao dao = new LvUserDao();
		return dao.getUserByImei(imei);
	}

	public void InsertUser(LvUserModel u)
	{
		new LvUserDao().Insert(u);
	}

	public void UpdateImsi(LvUserModel u)
	{
		new LvUserDao().UpdateImsi(u);
	}

	public void UpdateLevel(LvUserModel u)
	{
		new LvUserDao().UpdateLevel(u.getImei(), u.getLevel(), true);
	}

	/**
	 * 更新用户等级，降级操作需要用强制更新
	 * 
	 * @param imei
	 *            用户IMEI
	 * @param level
	 *            用户等级
	 * @param iForce
	 *            强制更新
	 */
	public void UpdateLevel(String imei, int level, Boolean iForce)
	{
		new LvUserDao().UpdateLevel(imei, level, iForce);
	}

}
