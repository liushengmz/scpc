package com.shotgun.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import com.database.Interface.IDatabase;
import com.database.Logical.DBRuntimeException;
import com.database.Logical.MysqlDatabase;
import com.database.Logical.ReturnObj;

public class StaticCacheT<T extends com.database.Logical.LightDataModel, IDX> extends StaticCache {

	private int _expried;
	private String _idField;
	private String _tabName;
	private String _indexField;
	private HashMap<IDX, T> _data;
	private T instanceT;

	/** 数据失效时间 */
	private long _exprieTime;
	private Static_Cache_Status _status;

	public StaticCacheT(T m, String IdxField) {
		setExpired(15);
		_status = Static_Cache_Status.Idel;
		this._idField = m.IdentifyField();
		this._tabName = m.TableName();
		if (Funcs.isNullOrEmpty(IdxField))
			this._indexField = this._idField;
		else
			this._indexField = IdxField;
		this.instanceT = m;
	}

	@Override
	public void clearCache() {
		_data = null;
		_status = Static_Cache_Status.Idel;
		super.remove(this);
		WriteLog(false, 0, 0);
	}

	/** 数据缓存有效期时长(分钟) */
	public int getExpired() {
		return _expried;
	}

	/** 数据缓存有效期时长(分钟) */
	public void setExpired(int value) {
		_expried = value;
	}

	IDatabase createDatabase() {
		return new MysqlDatabase();
	}

	/**
	 * 取得当前缓存状态 ，不会激发加载或过期清除
	 * 
	 * @return
	 */
	public Static_Cache_Status getStatus() {
		return _status;
	}

	/**
	 * 取得当前缓存状态
	 * 
	 * @param iStart
	 *            如果处于未加载状态，是否启动加载
	 * @return
	 */
	public Static_Cache_Status getStatus(boolean iStart) {
		if (iStart && _status == Static_Cache_Status.Idel) {
			loadFreshData();
		}
		return _status;
	}

	private void loadFreshData() {
		synchronized (this) {
			if (_status != Static_Cache_Status.Idel)
				return;
			_status = Static_Cache_Status.Loading;
		}
		Runnable fun = new Runnable() {
			@Override
			public void run() {
				// System.out.println("thread start");
				loadData();
			}
		};

		new Thread(fun).start();
		// System.out.println("thread created");

	}

	@SuppressWarnings("unchecked")
	void loadData() {
		int maxId = 0;
		HashMap<IDX, T> data = _data;
		if (data == null)
			_data = data = new HashMap<IDX, T>();
		else if (data.size() > 0) {
			for (Entry<IDX, T> t : data.entrySet()) {
				ReturnObj obj = t.getValue().GetValue(_idField);
				if (!obj.IsSucces)
					continue;
				int value = (Integer) obj.Value;
				if (value > maxId)
					maxId = value;
			}
		}
		long st = System.currentTimeMillis();
		super.add(this);
		IDatabase dBase = createDatabase();
		try {
			ArrayList<T> list;
			do {
				String sql = String.format("select * from `%s` where `%s`>%d order by `%s` asc limit 1000",
						this._tabName, this._idField, maxId, this._idField);
				// System.out.println(sql);
				list = dBase.sqlToModels(instanceT, sql);
				for (T m : list) {
					data.put((IDX) m.GetValueByName(_indexField), m);
					int v = (Integer) m.GetValueByName(_idField);
					if (v > maxId)
						maxId = v;
				}
			} while (list != null && list.size() == 1000);

			this._exprieTime = System.currentTimeMillis() + this.getExpired() * 60 * 1000;
			_exprieTime += (_exprieTime % 10) * 30 * 1000;// 人为5分钟随机误差

			this._status = Static_Cache_Status.AllLoad;

		} catch (DBRuntimeException ex) {
			// WriteLog(ex.Message);
			_status = Static_Cache_Status.Idel;
			this._exprieTime = System.currentTimeMillis() + this.getExpired() * 60 * 1000;
		} finally {
			if (dBase != null)
				dBase.close();
		}
		st = System.currentTimeMillis() - st;
		WriteLog(true, st, data.size());

	}

	/** 获取未过期数据，如果数据已经过期则返回null,并且激发重新加载 */
	private HashMap<IDX, T> getUnexpriedData() {
		if (_data == null) {
			loadFreshData();
			return null;
		}
		if (_status != Static_Cache_Status.AllLoad)
			return null;
		if (System.currentTimeMillis() > this._exprieTime) {
			clearCache();
			loadFreshData();
			return null;
		}
		return _data;

	}

	public Iterable<T> GetCacheData(boolean iFull) {
		HashMap<IDX, T> tData = getUnexpriedData();
		if (tData == null)
			return null;
		if (iFull && _status != Static_Cache_Status.AllLoad)
			return null;
		return tData.values();
	}

	/** 根据主索引，快速查找数据 （非完整数据） */
	public T getDataByIdx(IDX idx) {
		if (_data == null)
			return null;
		HashMap<IDX, T> tData = getUnexpriedData();
		if (tData == null || !tData.containsKey(idx))
			return null;
		return tData.get(idx);
	}

	/**
	 * 从外部插入单条数据，通常发生在，数据缓存成功之后，新增的数据<br/>
	 * 注意Fields要全部读取的，否则在二次取出使用时可能出现问题
	 * 
	 * @param data
	 *            需要插入的数据
	 */
	@SuppressWarnings("unchecked")
	public void insertItem(T data) {
		if (data == null)
			return;
		HashMap<IDX, T> dt = getUnexpriedData();
		if (dt == null || _status != Static_Cache_Status.AllLoad)
			return;
		dt.put((IDX) data.GetValueByName(this._indexField), data);
		WriteLog(true, 0, 1);
	}

	/**
	 * 查找首条符合条件的数据
	 * 
	 * @param func
	 *            条件匹配函数
	 * @return 如果无符合记录，返回null
	 */
	public T FindFirst(IStaticCacheFind<T> func) {
		Iterable<T> data = GetCacheData(false);
		if (data == null)
			return null;
		for (T m : data) {
			if (func.onFind(m))
				return m;
		}
		return null;
	}

	/**
	 * 查找所有符合条件的数据
	 * 
	 * @param func
	 *            条件匹配函数
	 * @return 如果无符合记录，返回null
	 */
	public ArrayList<T> FindAll(IStaticCacheFind<T> func) {
		Iterable<T> data = GetCacheData(false);
		if (data == null)
			return null;
		ArrayList<T> ret = new ArrayList<T>();
		for (T m : data) {
			if (func.onFind(m)) {
				ret.add(m);
			}
		}
		if (ret.size() == 0)
			return null;
		return ret;
	}

	void WriteLog(boolean iAdd, long elapsedMs, int count) {
		String msg = String.format("%s cache, count %d, elapsed %dms", (iAdd ? "add" : "remove"), count, elapsedMs);
		WriteLog(msg);
	}

	void WriteLog(String msg) {
		System.out.format("%s %s\n", _tabName, msg);
	}
}
