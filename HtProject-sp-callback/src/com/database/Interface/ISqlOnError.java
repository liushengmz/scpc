package com.database.Interface;


import java.sql.SQLException;

/** SQL相关错误回调 */
public interface ISqlOnError {
	void OnSqlError(SQLException ex, String Sql);
}
