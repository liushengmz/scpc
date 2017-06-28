package com.database.Interface;

import java.util.ArrayList;

import com.database.Logical.LightDataModel;

public interface IDatabase {

	/** 执行SQL语句，返回语句影响的条数 */
	int ExecuteNonQuery(String sql);

	/** 执行SQL语句，返回首行首列的数据。 */
	Object executeScalar(String sql);

	/** 快速数据保存方法 */
	boolean saveData(IUpdateInfo DataRow);

	/** 数据安全编码 */
	String sqlEncode(String value);

	/** 字段/表名 安全编码 */
	String fieldEncode(String string);

	/** 关闭数据库 */
	void close();

	int executeResultSet(String sql, IResultSetCallback func);

	boolean sqlToModel(LightDataModel model, String sql);

	/**
	 * 
	 * @param model
	 * @param sql
	 * @return 总是返回实例List对像
	 */
	<T extends LightDataModel> ArrayList<T> sqlToModels(T model, String sql);

}
