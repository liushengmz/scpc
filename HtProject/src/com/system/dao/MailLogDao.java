package com.system.dao;

import java.util.HashMap;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.model.MailLogModel;

public class MailLogDao {

  
	public int insert(MailLogModel m) {
		String sql = "INSERT INTO `daily_log`.`tbl_mail_log` (`mail_id`, `cp_id`, `mail_address`) VALUES (?,?,?)";

		Map<Integer, Object> ptrs = new HashMap<Integer, Object>();
		ptrs.put(1, m.getMailId());
		ptrs.put(2, m.getCpId());
		ptrs.put(3, m.getMailAddress());

		return new JdbcControl().insertWithGenKey(sql, ptrs);
	}

}
