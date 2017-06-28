package com.database.Dao;

import com.database.LightModel.tbl_sp_trone_apiItem;
import com.shotgun.Tools.StaticCacheT;

public class tbl_sp_trone_apiDao {
	static StaticCacheT<tbl_sp_trone_apiItem, Integer> cache;
	static {
		cache=new StaticCacheT<>( new tbl_sp_trone_apiItem(),tbl_sp_trone_apiItem.Fields.id);
	}
	
	
	
}
