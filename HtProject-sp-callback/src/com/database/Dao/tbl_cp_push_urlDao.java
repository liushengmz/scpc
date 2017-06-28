package com.database.Dao;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_cp_push_urlItem;
import com.shotgun.Tools.StaticCacheT;

public class tbl_cp_push_urlDao {
	static StaticCacheT<tbl_cp_push_urlItem, Integer> cache;

	static {
		cache = new StaticCacheT<>(new tbl_cp_push_urlItem(), tbl_cp_push_urlItem.Fields.id);
	}

	public static tbl_cp_push_urlItem queryById(IDatabase dBase, Integer id) {
		tbl_cp_push_urlItem m = cache.getDataByIdx(id);
		if (m != null)
			return m;
		m = new tbl_cp_push_urlItem();
		if (dBase.sqlToModel(m, String.format("select * from `%s`  where id =%d", m.TableName(), id))) {
			cache.insertItem(m);
			return m;
		}
		return null;
	}
}
