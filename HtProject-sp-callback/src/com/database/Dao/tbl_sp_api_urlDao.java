package com.database.Dao;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_sp_api_urlItem;
import com.database.LightModel.tbl_troneItem;
import com.shotgun.Tools.Funcs;
import com.shotgun.Tools.IStaticCacheFind;
import com.shotgun.Tools.StaticCacheT;

public class tbl_sp_api_urlDao {
	static StaticCacheT<tbl_sp_api_urlItem, Integer> cache;

	static {
		cache = new StaticCacheT<>(new tbl_sp_api_urlItem(), tbl_troneItem.Fields.id);
	}

	public static tbl_sp_api_urlItem queryById(IDatabase dBase, int id) {
		tbl_sp_api_urlItem m = cache.getDataByIdx(id);
		if (m != null)
			return m;
		String sql = String.format("select * from tbl_sp_api_url where id=%d", id);
		m = new tbl_sp_api_urlItem();
		if (!dBase.sqlToModel(m, sql))
			return null;
		cache.insertItem(m);
		return m;
	}

	public static tbl_sp_api_urlItem queryByVirtualpage(IDatabase dBase, String virtualPage) {
		// Iterable<tbl_sp_api_urlItem> data = cache.GetCacheData(false);
		if (Funcs.isNullOrEmpty(virtualPage))
			return null;
		tbl_sp_api_urlItem m = cache.FindFirst(new IStaticCacheFind<tbl_sp_api_urlItem>() {
			@Override
			public boolean onFind(tbl_sp_api_urlItem item) {
				return virtualPage.equalsIgnoreCase(item.get_virtual_page());
			}
		});
		if (m != null)
			return m;
		m = new tbl_sp_api_urlItem();
		String sql = "select * from tbl_sp_api_url where virtual_page='" + dBase.sqlEncode(virtualPage) + "'";
		if (!dBase.sqlToModel(m, sql))
			return null;
		cache.insertItem(m);
		return m;
	}

}
