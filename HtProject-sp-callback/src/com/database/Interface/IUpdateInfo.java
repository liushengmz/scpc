package com.database.Interface;

/**
 * 用于收集，需要更新字段信息
 * 
 * @author Shotgun
 */
public interface IUpdateInfo {

	/**
	 * 返回需要更新的字段值名 需要跟数据库字段名一致
	 * 
	 * @return
	 */
	String[] GetUpateFields();

	/**
	 * 取值
	 * @param filds
	 * @return
	 */
	Object GetValueByName(String filds);

	/**
	 * 返回标识字段名
	 */
	String IdentifyField();

	/**
	 * 数据保存成功后，调用方法
	 * @param IdentifyValue 当使用Insert时，会传入标识值
	 */
	void SetUpdated(Object IdentifyValue);

	
	/**
	 * 数据库真实表名
	 * @return
	 */
	String TableName();

	/**
	 * 数据库库名
	 * @return
	 */
	String Schema();
}
