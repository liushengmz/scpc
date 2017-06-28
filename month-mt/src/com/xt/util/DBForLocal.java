package com.xt.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.common.util.ConnectionService;

public class DBForLocal {
	private static Logger logger = Logger.getLogger(DBForLocal.class);

	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public DBForLocal() {
	}

	public PreparedStatement iniPreparedStatement(String sqlStr) throws SQLException {
		if (connection == null) {
			connection = ConnectionService.getInstance().getConnectionForLocal();
		}
		preparedStatement = connection.prepareStatement(sqlStr);
		return preparedStatement;
	}

	public int executeUpdate(String sqlStr) throws SQLException {
		if (connection == null) {
			connection = ConnectionService.getInstance().getConnectionForLocal();
		}
		preparedStatement = connection.prepareStatement(sqlStr);
		return preparedStatement.executeUpdate();
	}

	public ResultSet executeQuery(String sqlStr) throws SQLException {
		if (connection == null) {
			connection = ConnectionService.getInstance().getConnectionForLocal();
		}
		preparedStatement = connection.prepareStatement(sqlStr);
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}

	public void close() {
		if (this.resultSet != null) {
			try {
				this.resultSet.close();
			} catch (SQLException localSQLException1) {
				this.logger.error("ResultSet close", localSQLException1);
			}
			this.resultSet = null;
		}
		if (this.preparedStatement != null) {
			try {
				this.preparedStatement.close();
			} catch (SQLException localSQLException2) {
				this.logger.error("Statement close", localSQLException2);
			}
			this.preparedStatement = null;
		}
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException localSQLException3) {
				this.logger.error("Connection close", localSQLException3);
			}
			this.connection = null;
		}
	}
}