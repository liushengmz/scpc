<%@page import="java.io.Console"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.util.PageUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), 1);

	if (id == 0)
		id = 2;
	List<CpModel> cps = new CpServer().loadCpBySptone(id);

	List<ProvinceModel> prvs = new ProvinceServer().loadProvince();
	SpTroneModel sTrone = new SpTroneServer().loadSpTroneById(id);
	// List<ProvinceModel> open = new ArrayList<ProvinceModel>();
	String[] prvIds = sTrone.getProvinces().split(",");
	StringBuilder mail = new StringBuilder();
	mail.append("业务名称：" + sTrone.getSpTroneName());
	mail.append("\n开通省份：");
	Boolean iFound = false;
	for (String s : prvIds) {

		int pid = StringUtil.getInteger(s, 32);
		if (pid == 32)
			continue;
		for (ProvinceModel prv : prvs) {
			if (prv.getId() == pid) {
				mail.append(prv.getName());
				mail.append(",");
				prvs.remove(prv);
				iFound = true;
				break;
			}
		}
	}
	if (iFound)
		mail.setLength(mail.length() - 1);
	iFound = false;
	mail.append("\n屏蔽省份：");
	for (ProvinceModel prv : prvs) {
		mail.append(prv.getName());
		mail.append(",");
		iFound = true;
	}
	if (iFound)
		mail.setLength(mail.length() - 1);
	mail.append("\n");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>SP省份信息邮件通知</title>
<link href="../css/base.css" rel="stylesheet" />
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript">
	function frm_onsubmit(sender, e) {
		$.ajax({
			"type": "POST",
			"dataType" : "text",
			"url" : "1"+sender.action,
			"data" : $('#frm').serialize(),
			"success" : function(e) {
				alert(e);
			},
			"error":function(e){
				alert("错误！http code"+e.status);
			}
			
		
		});
	}
</script>
</head>
<body>
	<form action="troneprovincenoticesaction.jsp?id=<%=id%>" id="frm"
		method="post" onsubmit="frm_onsubmit(this,{});return false;">
		<h1 class="mainTitle">SP省份信息邮件通知</h1>

		<fieldset>
			<legend>已经接渠道</legend>
			<%
				for (CpModel m : cps) {
			%>
			<label><input type="checkbox" id="cpid" name="cpid"
				value="<%=m.getId()%>"
				<%=((StringUtil.isNullOrEmpty(m.getMail()) || m.getMail().indexOf("@") == -1)
						? "disabled='disabled'" : "")%> /><%=m.getShortName()%>(<%=m.getMail()%>)</label>
			<%
				}
			%>
		</fieldset>

		<fieldset>
			<legend>邮件内容</legend>
			<input name="subject" style="width: 90%"
				value="业务省份更新：《<%=sTrone.getSpTroneName()%>》" />
			<textarea name="body" rows="10" style="width: 90%"><%=PageUtil.htmlEncode(mail.toString())%></textarea>
		</fieldset>
		<input type="submit" value="发送邮件" />
	</form>
</body>
</html>