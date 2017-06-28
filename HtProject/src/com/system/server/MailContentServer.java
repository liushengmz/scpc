package com.system.server;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.system.dao.MailContentDao;
import com.system.dao.MailLogDao;
import com.system.model.MailContentModel;
import com.system.model.MailLogModel;
import com.system.util.StringUtil;

public class MailContentServer {

	public int insert(MailContentModel m) {
		int id = new MailContentDao().insert(m);
		m.setId(id);
		return id;
	}

	public boolean isExisted(MailContentModel m) {
		MailContentDao dao = new MailContentDao();
		String md5 = m.getMd5();
		if (!StringUtil.isNullOrEmpty(md5)) {
			return dao.existed(md5);
		}
		
		 String str= m.getSubject()+m.getBody()+new SimpleDateFormat("yyyy-MM-dd").format(m.getSendDate());
		 md5=StringUtil.getMd5String(str, 32);
		 m.setMd5(md5);
		 return dao.existed(md5);
		 
	}

}
