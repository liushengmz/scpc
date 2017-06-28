package com.database.Interface;

import java.sql.ResultSet;
import java.sql.SQLException;
/**查询回调*/
public interface IResultSetCallback {
	int OnReslut(ResultSet rs) throws SQLException;
}
