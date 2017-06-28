package com.database.Logical;

import java.sql.SQLException;

public class DBRuntimeException extends RuntimeException {
	/** 不知什么gui */
	private static final long serialVersionUID = 1L;

	public DBRuntimeException(SQLException e) {
		super(e);
	}

	public DBRuntimeException(SQLException e, String _lastSql) {
		// TODO Auto-generated constructor stub
	}
}
