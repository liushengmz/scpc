package com.database.Dao;

import java.util.ArrayList;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_troneItem;
import com.shotgun.Tools.IStaticCacheFind;
import com.shotgun.Tools.StaticCacheT;

public class tbl_troneDao {
	static StaticCacheT<tbl_troneItem, Integer> cache;

	static {
		cache = new StaticCacheT<tbl_troneItem, Integer>(new tbl_troneItem(), tbl_troneItem.Fields.id);
	}

	public static tbl_troneItem queryById(IDatabase dBase, Integer troneId) {
		tbl_troneItem m = cache.getDataByIdx(troneId);
		if (m != null)
			return m;
		m = new tbl_troneItem();
		if (dBase.sqlToModel(m,
				String.format("select * from `%s`  where `%s` =%d", m.TableName(), m.IdentifyField(), troneId))) {
			cache.insertItem(m);
			return m;
		}
		return null;
	}

	public static ArrayList<tbl_troneItem> queryByPort(IDatabase dBase, int sp_api_url_id, String port) {

		ArrayList<tbl_troneItem> ms = cache.FindAll(new  IStaticCacheFind<tbl_troneItem>() {
			@Override
			public boolean onFind(tbl_troneItem m) {
				return m.get_status() == 1 && m.get_sp_api_url_id() == sp_api_url_id && port.equalsIgnoreCase(m.get_trone_num());
			}
		});
			 
		if (ms!=null)
			return ms;

		String sql = String.format(
				"select *  from `%s` where trone_num ='%s' and sp_api_url_id=%d and status=1",
				tbl_troneItem.tableName, dBase.sqlEncode(port), sp_api_url_id);

		ms = dBase.sqlToModels(new tbl_troneItem(), sql);
		if (ms != null && ms.size() != 0) {
			for (tbl_troneItem m : ms) {
				cache.insertItem(m);
			}
		}
		return ms;
	}

}
