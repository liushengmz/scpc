package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.MailContentModel;
import com.system.util.SqlUtil;

public class MailContentDao {

	public boolean existed(String md5) {
		String sql = "select id from daily_log.tbl_mail_content where md5='" + md5 + "' limit 1";
		//System.out.println(sql);
		return (Boolean) new JdbcControl().query(sql, new QueryCallBack() {
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException {
				return rs.next();
			}
		});

	}

	public int insert(MailContentModel m) {
		String sql = "INSERT INTO `daily_log`.`tbl_mail_content` (`subject`, `body`, `send_date`, `md5`) VALUES (?,?,?,?)";

		Map<Integer, Object> ptrs =  new HashMap<Integer, Object>();
		ptrs.put(1, m.getSubject());
		ptrs.put(2, m.getBody());
		ptrs.put(3, m.getSendDate());
		ptrs.put(4, m.getMd5());

		return new JdbcControl().insertWithGenKey(sql, ptrs);
	}

}
