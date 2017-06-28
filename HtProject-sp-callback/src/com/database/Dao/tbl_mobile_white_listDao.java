package com.database.Dao;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_mobile_white_listItem;
import com.shotgun.Tools.StaticCache.Static_Cache_Status;
import com.shotgun.Tools.Funcs;
import com.shotgun.Tools.StaticCacheT;

public class tbl_mobile_white_listDao {
	private static StaticCacheT<tbl_mobile_white_listItem, String> cache;

	static {
		cache = new StaticCacheT<>(new tbl_mobile_white_listItem(), tbl_mobile_white_listItem.Fields.mobile);
	}

	/**
	 * 是否同步名单用户
	 * 
	 * @param dBase
	 *            为空时，缓存数据未加载时，不进行检查
	 * @param mobile
	 * @return
	 */
	public static boolean isWhite(IDatabase dBase, String mobile) {
		if(Funcs.isNullOrEmpty(mobile))
			return false;
		if (cache.getStatus(true) == Static_Cache_Status.AllLoad) {
			return cache.getDataByIdx(mobile) != null;
		}
		if (dBase == null)
			return false;
		String sql = "select id from tbl_mobile_white_list where mobile='" + dBase.sqlEncode(mobile) + "'";
		return dBase.executeScalar(sql) != null;
	}
}
