package com.system.server;

import java.text.SimpleDateFormat;
import java.util.List;
import com.system.dao.MailContentDao;
import com.system.dao.MailLogDao;
import com.system.model.MailContentModel;
import com.system.model.MailLogModel;
import com.system.util.StringUtil;

public class MailLogServer {
	public boolean existed(MailContentModel mail) {
		String md5 = mail.getMd5();
		if (StringUtil.isNullOrEmpty(md5)) {
			md5 = String.format("%s%s%s", mail.getSubject(), mail.getBody(),
					new SimpleDateFormat("yyyyMMdd").format(mail.getSendDate()));
			mail.setMd5(md5);
		}
		return existed(md5);
	}

	public boolean existed(String md5) {
		return new MailContentDao().existed(md5);
	}
		
	public int insert(MailLogModel m){
		int id= new  MailLogDao().insert(m);
		m.setId(id);
		return id;
	}

}
