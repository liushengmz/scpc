<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.model.UserModel"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	UserModel user = (UserModel)session.getAttribute("user");
	if(!RightServer.existUserRight(user))
	{
		out.clear();
		out.print("<script>window.location.href='login.jsp'</script>");
		return;
	}
	String sysTitle = ConfigManager.getConfigData("SYSTEM_TITLE","运营管理平台ss");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%= sysTitle %></title>
</head>
<frameset rows="80,*" cols="*" frameborder="no" border="0"
	framespacing="0">
	<frame src="head.jsp" name="head" scrolling="No" noresize="noresize">
	<frameset cols="170,*" rows="*" id="attachucp" frameborder="no"
		border="0" framespacing="0">
		<frame src="left.jsp" name="leftFrame" scrolling="auto"
			noresize="noresize" id="leftFrame">
		<frameset rows="33,*" cols="*" frameborder="no" border="0" framespacing="0">
		<frame src="righttop.jsp" scrolling="auto" name="rightTop" id="rightTop">
		<frame src="welcome.html" scrolling="auto" name="right" id="rightFrame">
		</frameset>
	</frameset>
</frameset>
</html>
