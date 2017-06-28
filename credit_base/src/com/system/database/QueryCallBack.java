package com.system.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryCallBack 
{
	Object onCallBack(ResultSet rs) throws SQLException ;
}
