package com.database.Logical;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.database.Interface.IResultSetCallback;
import com.database.Interface.IUpdateInfo;
import com.shotgun.Tools.ConfigManager;
import com.shotgun.Tools.Funcs;

public class MysqlDatabase implements com.database.Interface.IDatabase, Closeable {
	private static Logger logger = Logger.getLogger("sql");
	// String conStr =
	// "jdbc:mysql://192.168.1.99/daily_config?user=root&password=root&useUnicode=true&characterEncoding=UTF8";
	java.sql.Connection _conn;
	/** 错误回调 */
	// ISqlOnError _errproc;
	String _lastSql;

	private static BasicDataSource _basicDataSource;

	protected BasicDataSource getDataSource() {
		if (_basicDataSource != null)
			return _basicDataSource;

		_basicDataSource = new BasicDataSource();
		_basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		String url = ConfigManager.getConfigData("dBaseConnectionString");
		_basicDataSource.setUrl(url);

		// _basicDataSource.setUsername("root");
		// _basicDataSource.setPassword("root");
		_basicDataSource.setInitialSize(1);
		_basicDataSource.setMaxActive(32);
		_basicDataSource.setMinIdle(4);
		_basicDataSource.setRemoveAbandoned(true);
		_basicDataSource.setRemoveAbandonedTimeout(1 * 60 * 60); // 1小时
		_basicDataSource.setMaxWait(2000);
		return _basicDataSource;

	}

	private Connection conn() {
		if (_conn != null)
			return _conn;
		try {
			_conn = getDataSource().getConnection();
			// System.out.println("conn created!");

		} catch (SQLException e) {
			OnSqlError(e);
		}
		return _conn;
	}

	@Override
	public int ExecuteNonQuery(String sql) {
		PreparedStatement m = null;
		try {
			_lastSql = sql;
			m = this.conn().prepareStatement(sql);
			return m.executeUpdate();
		} catch (SQLException e) {
			OnSqlError(e);
			throw new DBRuntimeException(e, _lastSql);
		} finally {
			free(m);
		}
	}

	@Override
	public Object executeScalar(String sql) {
		PreparedStatement m = null;
		ResultSet rs = null;
		try {
			_lastSql = sql;
			m = this.conn().prepareStatement(sql);
			rs = m.executeQuery();
			if (!rs.next())
				return null;
			return rs.getObject(1);
		} catch (SQLException e) {
			this.OnSqlError(e);
			throw new DBRuntimeException(e, _lastSql);
		} finally {
			free(rs, m);
		}
	}

	@Override
	public boolean saveData(IUpdateInfo data) {
		_lastSql = null;
		String[] uFields = data.GetUpateFields();
		if (uFields == null || uFields.length == 0)
			return true;
		String id = data.IdentifyField();
		Object idValue = data.GetValueByName(id);
		Boolean isNew = idValue == null || idValue.equals(0);

		if (isNew)
			_lastSql = getInsertSql(data);
		else
			_lastSql = getUpdateSql(data);
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			stm = this.conn().prepareStatement(_lastSql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < uFields.length; i++) {
				stm.setObject(i + 1, data.GetValueByName(uFields[i]));
			}
			if (!isNew)
				stm.setObject(uFields.length + 1, idValue);

			int count = stm.executeUpdate();
			if (count == 0)
				return false;
			if (!isNew) // for update
			{
				data.SetUpdated(null);
				return true;
			}
			rs = stm.getGeneratedKeys();
			if (rs.next())
				data.SetUpdated(rs.getObject(1));

		} catch (SQLException e) {
			OnSqlError(e, stm);
			throw new DBRuntimeException(e);
		} finally {
			free(rs, stm);
		}
		return true;
	}

	private String getUpdateSql(IUpdateInfo data) {
		String[] fs = data.GetUpateFields();
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		if (!Funcs.isNullOrEmpty(data.Schema()))
			sb.append("`" + data.Schema() + "`.");
		sb.append("`" + data.TableName() + "` set `");

		for (String f : fs) {
			sb.append(f);
			sb.append("`=?,`");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" where `");
		sb.append(data.IdentifyField());
		sb.append("`=?");
		return sb.toString();
	}

	private String getInsertSql(IUpdateInfo data) {
		String[] fs = data.GetUpateFields();
		StringBuilder sb = new StringBuilder();
		sb.append("Insert into ");
		if (data.Schema() != null)
			sb.append("`" + data.Schema() + "`.");
		sb.append("`" + data.TableName() + "` (`");

		for (String f : fs) {
			sb.append(f);
			sb.append("`,`");
		}
		sb.setLength(sb.length() - 2);
		sb.append(") values(");
		for (int i = 0; i < fs.length; i++) {
			sb.append("?,");
		}
		sb.delete(sb.length() - 1, sb.length());
		sb.append(")");

		return sb.toString();
	}

	@Override
	public void close() {
		// System.out.print("db close");
		if (_conn != null) {
			try {
				_conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// System.out.print("ed");
		}
		_conn = null;
	}

	protected void free(AutoCloseable... obj) {
		for (AutoCloseable ac : obj) {
			if (ac == null)
				continue;
			try {
				ac.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// System.out.print(ac.toString());
			// System.out.println(" - closed ");
		}
	}

	/** 数据执行出错 */
	protected void OnSqlError(SQLException ex) {
		OnSqlError(ex, null);
	}

	private void OnSqlError(SQLException e, PreparedStatement stm) {
		StackTraceElement[] st = Thread.currentThread().getStackTrace();
		StackTraceElement[] stex = e.getStackTrace();
		StringBuilder sb = new StringBuilder();
		sb.append(e.getMessage());
		sb.append(" - ");
		sb.append(_lastSql);
		int cc = st.length, ce = stex.length - cc;
		// ArrayList<StackTraceElement> msgs= new ArrayList<>();
		for (int i = 1; i < cc; i++) {
			// String cMsg = st[i].toString();
			String eMsg = stex[ce + i].toString();
			// if (eMsg.equals(cMsg)) {
			sb.append("\r\n\t");
			sb.append(eMsg);

			// }
		}
		// sb.append(stex[ce].toString());
		logger.error(sb.toString());

		// return;
		// }
		// String ptrs="";
		/*
		 * try { ParameterMetaData pmd = stm.getParameterMetaData(); int
		 * c=pmd.getParameterCount(); for(int i=0;i<c;i++){ //Object obj=pmd. }
		 * } catch (SQLException e1) { e1.printStackTrace(); }
		 */
	}

	@Override
	public String sqlEncode(String value) {
		if (value == null || value.length() == 0)
			return "";
		return value.replace("'", "''");
	}

	@Override
	public String fieldEncode(String value) {
		if (value == null || value.length() == 0)
			return "";
		return String.format("`{0}`", value);
	}

	public String LastSql() {
		return _lastSql;
	}

	@Override
	public int executeResultSet(String sql, IResultSetCallback func) {
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			_lastSql = sql;
			stm = this.conn().prepareStatement(sql);
			rs = stm.executeQuery();
			return func.OnReslut(rs);
		} catch (SQLException ex) {
			OnSqlError(ex);
			throw new DBRuntimeException(ex, sql);
		} finally {
			free(rs, stm);
		}
	}

	public boolean sqlToModel(LightDataModel model, String sql) {

		if (model == null)
			return false;
		final LightDataModel m = model;
		return 1 == executeResultSet(sql, new IResultSetCallback() {
			@Override
			public int OnReslut(ResultSet rs) throws SQLException {
				if (!rs.next())
					return 0;
				ResultSetMetaData rsmd = rs.getMetaData();
				int c = rsmd.getColumnCount();
				m.SetDataFilling(true);
				for (int i = 1; i <= c; i++) {
					m.SetValue(rsmd.getColumnName(i), rs.getObject(i));
				}

				m.SetDataFilling(false);
				return 1;
			}
		});

	}

	public <T extends LightDataModel> ArrayList<T> sqlToModels(T model, String sql) {

		if (model == null)
			return null;

		@SuppressWarnings("unchecked")
		final Class<T> cls = (Class<T>) model.getClass();
		final ArrayList<T> list = new ArrayList<T>();

		executeResultSet(sql, new IResultSetCallback() {
			@Override
			public int OnReslut(ResultSet rs) throws SQLException {
				ResultSetMetaData rsmd = rs.getMetaData();
				int c = rsmd.getColumnCount();
				String[] Fields = new String[c];
				for (int i = 0; i < c; i++) {
					Fields[i] = rsmd.getColumnName(i + 1);
				}

				while (rs.next()) {
					T m = null;
					try {
						m = cls.newInstance();
					} catch (Exception e) {
						return -1;
					}
					m.SetDataFilling(true);
					for (int i = 0; i < c; i++) {
						m.SetValue(Fields[i], rs.getObject(i + 1));
					}
					m.SetDataFilling(false);
					list.add(m);
				}
				return list.size();
			}
		});
		return list;

	}

}
