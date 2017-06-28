
package com.system.handler;

import java.util.List;

import com.system.api.AppInitRequestModel;
import com.system.api.AppInitResponseModel;
import com.system.api.BaseRequest;
import com.system.api.LevelItem;
import com.system.api.baseResponse;
import com.system.cache.LocateCache;
import com.system.cache.LvLevelCache;
import com.system.model.LvLevelModel;
import com.system.model.LvUserModel;
import com.system.model.ProvinceModel;
import com.system.server.LvUserServer;
import com.system.util.ImsiUtil;
import com.system.util.StringUtil;

public class AppInitHttpHandler extends BaseFilter
{

	@Override
	protected baseResponse ProcessReuqest(String s)
	{
		AppInitRequestModel m = BaseRequest.ParseJson(s,
				AppInitRequestModel.class);

		AppInitResponseModel result = new AppInitResponseModel();

		if (StringUtil.isNullOrEmpty(m.getImei()))
		{
			result.setStatus(com.system.constant.Constant.ERROR_MISS_PARAMETER);
			return result;
		}

		LvUserModel user = getUserInfo(m);

		result.setLevel(user.getLevel());
		result.setName(user.getName());
		result.setPassword(user.getPwd());
		// System.out.println("appkey:"+m.getAppkey());
		String appkey = m.getAppkey();
		if (StringUtil.isNullOrEmpty(appkey)) // 兼容已经发包的APP
			appkey = "57d963881bc74500925d9b2afd728f79";

		List<LvLevelModel> levels = LvLevelCache.getLevelByAppkey(appkey);
		LevelItem[] lItems = new LevelItem[levels.size()];
		for (int i = 0; i < lItems.length; i++)
		{
			lItems[i] = new LevelItem(levels.get(i));
		}
		result.setLevels(lItems);

		return result;
	}

	private LvUserModel getUserInfo(AppInitRequestModel m)
	{
		LvUserServer s = new LvUserServer();
		LvUserModel u = s.getUserByImsi(m.getImei());
		if (u != null)
		{
			// System.out.println("old user:"+m.getImei());
			if (!StringUtil.isNullOrEmpty(m.getImsi())
					&& u.getImsi() != m.getImsi())
			{
				u.setImsi(m.getImsi());
				FillAreaInfo(u);
				s.UpdateImsi(u);
			}
			return u;
		}
		// System.out.println("new user:"+m.getImei());

		u = new LvUserModel();
		u.setAndroidLevel(m.getAndroidLevel());
		u.setAndroidVersion(m.getAndroidVersion());

		u.setImei(m.getImei());
		u.setImsi(m.getImsi());
		u.setLevel(0);
		u.setMac(m.getMac());
		u.setModel(m.getModel());
		u.setAppkey(m.getAppkey());
		u.setChannel(m.getChannel());

		FillRandName(u);
		FillAreaInfo(u);

		s.InsertUser(u);
		return u;
	}

	private void FillRandName(LvUserModel u)
	{
		long ts = System.currentTimeMillis();
		if (u.getImei().length() < 8)
			u.setName(u.getImei());
		else
			u.setName(Long.toString(100000 + (ts & 0xFFffFF)));
		u.setPwd(Long.toHexString((ts & 0xFFffFF) | 0x100000));

	}

	private void FillAreaInfo(LvUserModel u)
	{
		String phone = ImsiUtil.ImsiToPhone(u.getImsi());
		u.setCity(416);
		u.setProvince(32);

		if (StringUtil.isNullOrEmpty(phone))
			return;

		int city = LocateCache.getCityIdByPhone(phone);
		if (city == -1)
			return;
		ProvinceModel provinceModel = LocateCache.getProvinceByCityId(city);
		u.setCity(city);
		System.out.println(phone);
		u.setProvince(provinceModel.getId());
	}

}
