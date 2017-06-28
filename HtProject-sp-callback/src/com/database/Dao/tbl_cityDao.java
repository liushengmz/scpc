package com.database.Dao;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_cityItem;
import com.shotgun.Tools.StaticCacheT;

public class tbl_cityDao {
	static StaticCacheT<tbl_cityItem, Integer> cache;

	static {
		cache = new StaticCacheT<tbl_cityItem, Integer>(new tbl_cityItem(), tbl_cityItem.Fields.id);
		cache.setExpired(24 * 60);
	}

	public static tbl_cityItem queryById(IDatabase dBase, Integer cityId) {
		tbl_cityItem m = cache.getDataByIdx(cityId);
		if (m != null)
			return m;
		m = new tbl_cityItem();
		if (dBase.sqlToModel(m, String.format("select * from `%s`  where id =%d", m.TableName(), cityId))) {
			cache.insertItem(m);
			return m;
		}
		return null;
	}

}
