<%@page import="com.system.model.MailLogModel"%>
<%@page import="com.system.server.MailLogServer"%>
<%@page import="com.system.model.MailContentModel"%>
<%@page import="com.system.server.MailContentServer"%>
<%@page import="com.system.dao.MailContentDao"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.util.MailUtil"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.util.*"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="java.util.regex.Pattern"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	out.clear();
	String ptr = request.getParameter("cpid");
	if (StringUtil.isNullOrEmpty(ptr)) {
		out.write("参数错误！没有勾选CP");
		return;
	}
	String[] ars = request.getParameter("cpid").split(",");
	int[] ids = new int[ars.length];
	for (int x = 0; x < ids.length; x++) {
		int i = StringUtil.getInteger(ars[x], 0);
		ids[x] = i;
	}

	List<CpModel> cps = new CpServer().loadCpByIds(ids);

	String subject = request.getParameter("subject");
	String body = request.getParameter("body");

	MailContentModel m = new MailContentModel();
	m.setSubject(subject);
	m.setBody(body);
	m.setSendDate(new Date());

	MailContentServer mcs = new MailContentServer();
	if (mcs.isExisted(m)) {
		out.print("该邮件已经存在！");
		return;
	}

	List<String> addr = new ArrayList<String>();
	String[] adrs = null;
	for (CpModel cp : cps) {
		if (!cp.getMail().contains(",")) {
			addr.add(cp.getMail());
			continue;
		}
		adrs = cp.getMail().split(",");
		for (String str : adrs) {
			if (StringUtil.isNullOrEmpty(str))
				addr.add(str.trim());
		}

	}
	adrs = addr.toArray(new String[0]);

	MailLogServer mls = new MailLogServer();

	mcs.insert(m);//记录邮件内容
	for (CpModel cp : cps) { //插入发送日志
		MailLogModel mlm = new MailLogModel();
		mlm.setCpId(cp.getId());
		mlm.setMailAddress(cp.getMail());
		mlm.setMailId(m.getId());
		mls.insert(mlm);
	}

	//out.print("send to "+ adrs.length);
	MailUtil.SendTextMail(subject, body, adrs);
	//out.print("done！");
%>
邮件发送成功
