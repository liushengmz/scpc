package com.database.Dao;

import java.util.ArrayList;

import com.database.Interface.IDatabase;
import com.database.LightModel.tbl_trone_orderItem;
import com.shotgun.Tools.IStaticCacheFind;
import com.shotgun.Tools.StaticCache.Static_Cache_Status;
import com.shotgun.Tools.StaticCacheT;

public class tbl_trone_orderDao {
	static StaticCacheT<tbl_trone_orderItem, Integer> cache;
	//ArrayList<tbl_trone_orderItem> beUpdated = null;

	static {
		cache = new StaticCacheT<>(new tbl_trone_orderItem(), tbl_trone_orderItem.Fields.id);
	}

	public static tbl_trone_orderItem queryById(IDatabase dBase, int id) {
		tbl_trone_orderItem m = cache.getDataByIdx(id);
		if (m != null)
			return m;
		String sql = String.format("select * from tbl_sp_api_url where id=%d", id);
		m = new tbl_trone_orderItem();
		if (!dBase.sqlToModel(m, sql))
			return null;
		cache.insertItem(m);
		return m;
	}

	/**
	 * 查找缓存中的可以用CP业务
	 * 
	 * @param dBase
	 * @param trondId
	 *            SP通道号
	 * @return
	 */
	public static ArrayList<tbl_trone_orderItem> queryByTroneId(IDatabase dBase, int trondId) {
		ArrayList<tbl_trone_orderItem> list = null;
		if (cache.getStatus(true) == Static_Cache_Status.AllLoad) {
			list = cache.FindAll(new IStaticCacheFind<tbl_trone_orderItem>() {
				@Override
				public boolean onFind(tbl_trone_orderItem item) {
					return item.get_trone_id() == trondId && !item.get_disable();
				}
			});
			if (list != null) {
				System.out.format("tbl_trone_order find all  troneId:%d, match:%d\n", trondId, list.size());
				return list;
			}
		}
		System.out.println("tbl_trone_order not match direct sql");

		String sql = "select * from tbl_trone_order where disable=0 and trone_id=" + Integer.toString(trondId);
		list = dBase.sqlToModels(new tbl_trone_orderItem(), sql);
		return list;
	}

	/** 更新数据缓存，清除当前缓存，重新加载 */
	public static void refreshCache() {
		cache.clearCache();
		cache.getDataByIdx(0);
		System.out.println("tbl_trone_order clear cache!");
	}

//	/**
//	 * 延时更新到数据库
//	 * 
//	 * @param item
//	 */
//	public static void updateDelay(tbl_trone_orderItem item) {
//
//	}
}
