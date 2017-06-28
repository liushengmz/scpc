package com.database.Interface;

public interface IModelCreator <T extends com.database.Logical.LightDataModel>{
	T CreateOne();
}
