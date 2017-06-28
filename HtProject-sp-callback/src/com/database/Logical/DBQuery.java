package com.database.Logical;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.database.Interface.IDatabase;
import com.database.Interface.IResultSetCallback;

public class DBQuery<T extends com.database.Logical.LightDataModel> {
	int _pageSzie, _currentPage;
	IDatabase _dBase;
	T _model;
	private String[] _fields;

	public DBQuery(T model) {
		_model = model;
	}

	public DBQuery(T model, IDatabase db) {
		this._model = model;
		SetDBase(db);
	}

	public IDatabase GetDBase() {
		return _dBase;
	}

	public void SetDBase(IDatabase db) {
		_dBase = db;
	}

		
	/** 获取需要查询的字段 */
	public String[] GetFields() {
		return this._fields;
	}

	/** 设置需要查询的字段 */
	public void SetFields(String[] value) {
		this._fields = value;
	}

	public ArrayList<T> GetDataList(T model) {
		final ArrayList<T> list = new ArrayList<T>();
		@SuppressWarnings("unchecked")
		final Class<T> cls = (Class<T>) model.getClass();
		
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		FillFields(sb);
		sb.append(" from ");
		sb.append(this._model.TableName());
		sb.append(" limit 1");

		this.GetDBase().executeResultSet(sb.toString(), new IResultSetCallback() {
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

	private void FillFields(StringBuffer sb) {
		 if(this._fields==null || this._fields.length==0){
			 sb.append(" * ");
			 return ;
		 }
		 for(int i=0;i<_fields.length;i++){
			 sb.append(this._dBase.fieldEncode(_fields[i]) );
			 sb.append(",");
		 }
		 sb.setLength(sb.length()-1);
		 sb.append(" ");
	}

	public T GetModelByFilter() {
		return null;
	}

 
	
	
}
