package com.shotgun.Tools;

public interface IStaticCacheFind<T extends com.database.Logical.LightDataModel> {
	boolean onFind(T item);
}
