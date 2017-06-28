package com.database.Dao;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_cityItem;
import com.database.LightModel.tbl_phone_locateItem;
import com.shotgun.Tools.StaticCacheT;

public class tbl_phone_locateDao {
 	static StaticCacheT<tbl_phone_locateItem, String> cache;

	static {
		cache = new StaticCacheT<tbl_phone_locateItem, String>(new tbl_phone_locateItem(),
				tbl_phone_locateItem.Fields.phone);
		cache.setExpired(24 * 60);
	}

	public static tbl_cityItem queryCityInfoByPhone(IDatabase dBase, String phone) {
		tbl_phone_locateItem m = queryByPhone(dBase, phone);
		if (m == null)
			return null;
		return tbl_cityDao.queryById(dBase, m.get_city_id());
	}

	public static tbl_phone_locateItem queryByPhone(IDatabase dBase, String phone) {
		tbl_phone_locateItem m = cache.getDataByIdx(phone);
		if (m != null)
			return m;
		m = new tbl_phone_locateItem();
		if (dBase.sqlToModel(m,
				String.format("select * from `%s`  where phone ='%s'", m.TableName(), dBase.sqlEncode(phone)))) {
			cache.insertItem(m);
			return m;
		}
		return null;
	}
}
