
package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import com.system.database.JdbcControlLvLog;
import com.system.database.QueryCallBack;
import com.system.model.LvUserModel;
import com.system.util.StringUtil;

public class LvUserDao
{
	public LvUserModel getUserByImei(String imei)
	{

		String tabName = getTabelName(imei);
		String sql = "select * from little_video_log." + tabName
				+ " where imei=" + StringUtil.sqlEncode(imei, true);

		QueryCallBack qcb = new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if (!rs.next())
					return null;
				LvUserModel m = new LvUserModel();
				m.setAndroidLevel(StringUtil
						.getString(rs.getString("Android_Level"), ""));
				m.setAndroidVersion(StringUtil
						.getString(rs.getString("Android_Version"), ""));
				m.setCity(rs.getInt("City"));
				m.setCreateDate(rs.getDate("create_Date"));
				m.setId(rs.getInt("id"));
				m.setImei(StringUtil.getString(rs.getString("Imei"), ""));
				m.setImsi(StringUtil.getString(rs.getString("Imsi"), ""));
				m.setLevel(rs.getInt("Level"));
				m.setMac(StringUtil.getString(rs.getString("mac"), ""));
				m.setModel(StringUtil.getString(rs.getString("Model"), ""));
				m.setName(StringUtil.getString(rs.getString("Name"), ""));
				m.setProvince(rs.getInt("Province"));
				m.setPwd(StringUtil.getString(rs.getString("pwd"), ""));
				m.setAndroidVersion(StringUtil
						.getString(rs.getString("Android_Version"), ""));
				m.setAndroidVersion(StringUtil
						.getString(rs.getString("Android_Version"), ""));

				m.setAppkey(StringUtil.getString(rs.getString("appkey"), ""));
				m.setChannel(StringUtil.getString(rs.getString("channel"), ""));

				return m;
			}
		};

		JdbcControlLvLog db = new JdbcControlLvLog();

		return (LvUserModel) db.query(sql, qcb);
	}

	private String getTabelName(String imei)
	{
		int idx = 0;
		char[] chars = imei.toCharArray();
		for (int i = chars.length - 1; i >= 0; i--)
		{
			char c = chars[i];
			idx = c - '0';
			if (idx >= 0 && idx <= 9)
			{
				return "tbl_user_" + Integer.toString(idx);
			}
		}

		return "tbl_user_0";
	}

	public void Insert(LvUserModel user)
	{
		String insSql = "insert into little_video_log."
				+ getTabelName(user.getImei())
				+ "(imei,imsi,mac,android_Version,android_Level,model,level,city,province,name,pwd,appkey,channel)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		int i = 0;
		map.put(++i, user.getImei());
		map.put(++i, user.getImsi());
		map.put(++i, user.getMac());
		map.put(++i, user.getAndroidVersion());
		map.put(++i, user.getAndroidLevel());
		map.put(++i, user.getModel());
		map.put(++i, user.getLevel());
		map.put(++i, user.getCity());
		map.put(++i, user.getProvince());
		map.put(++i, user.getName());
		map.put(++i, user.getPwd());
		map.put(++i, user.getAppkey());
		map.put(++i, user.getChannel());

		JdbcControlLvLog jdbc = new JdbcControlLvLog();
		user.setId(jdbc.insertWithGenKey(insSql, map));

	}

	public void UpdateImsi(LvUserModel user)
	{
		String upSql = "update little_video_log." + getTabelName(user.getImei())
				+ " set imsi=?, city=?,province=? where id=?";
		HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		int i = 0;
		map.put(++i, user.getImsi());
		map.put(++i, user.getCity());
		map.put(++i, user.getProvince());
		map.put(++i, user.getId());

		JdbcControlLvLog jdbc = new JdbcControlLvLog();
		jdbc.execute(upSql, map);
		return;
	}

	public void UpdateLevel(String imei, int level, boolean iForce)
	{
		String upSql = "update little_video_log." + getTabelName(imei)
				+ " set Level=? where imei=? ";
		if (!iForce)
			upSql += " and  Level<?";
		HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		int i = 0;
		map.put(++i, level);
		map.put(++i, imei);

		if (!iForce)
			map.put(++i, level);

		JdbcControlLvLog jdbc = new JdbcControlLvLog();
		jdbc.execute(upSql, map);
		return;
	}
}
